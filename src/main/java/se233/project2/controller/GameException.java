package se233.project2.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GameException - ALL IN ONE CLASS!
 * - Exception types (enum)
 * - Factory methods (for throwing)
 * - Handler methods (for catching)
 *
 * Usage:
 *   throw GameException.resourceLoadError(path);
 *
 *   catch (GameException e) {
 *       GameException.handle(e);
 *   }
 */
public class GameException extends Exception {
    private static final Logger logger = LoggerFactory.getLogger(GameException.class);

    /**
     * Exception Types
     */
    public enum ExceptionType {
        RESOURCE_LOAD_ERROR("Resource loading failed"),
        GAME_STATE_ERROR("Invalid game state"),
        SPRITE_ERROR("Sprite operation failed"),
        SOUND_ERROR("Sound operation failed"),
        COLLISION_ERROR("Collision detection failed"),
        FILE_IO_ERROR("File I/O operation failed"),
        GENERAL_ERROR("General game error");

        private final String description;

        ExceptionType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    private final ExceptionType type;
    private final String resourcePath;

    // ==================== Constructor ====================

    private GameException(ExceptionType type, String message, Throwable cause, String resourcePath) {
        super(message, cause);
        this.type = type;
        this.resourcePath = resourcePath;
    }

    // ==================== Factory Methods (for throwing) ====================

    public static GameException resourceLoadError(String resourcePath) {
        return new GameException(
                ExceptionType.RESOURCE_LOAD_ERROR,
                "Failed to load resource: " + resourcePath,
                null,
                resourcePath
        );
    }

    public static GameException resourceLoadError(String resourcePath, Throwable cause) {
        return new GameException(
                ExceptionType.RESOURCE_LOAD_ERROR,
                "Failed to load resource: " + resourcePath,
                cause,
                resourcePath
        );
    }

    public static GameException gameStateError(String message) {
        return new GameException(ExceptionType.GAME_STATE_ERROR, message, null, null);
    }

    public static GameException gameStateError(String message, Throwable cause) {
        return new GameException(ExceptionType.GAME_STATE_ERROR, message, cause, null);
    }

    public static GameException spriteError(String message) {
        return new GameException(ExceptionType.SPRITE_ERROR, message, null, null);
    }

    public static GameException spriteError(String message, Throwable cause) {
        return new GameException(ExceptionType.SPRITE_ERROR, message, cause, null);
    }

    public static GameException soundError(String message) {
        return new GameException(ExceptionType.SOUND_ERROR, message, null, null);
    }

    public static GameException soundError(String message, Throwable cause) {
        return new GameException(ExceptionType.SOUND_ERROR, message, cause, null);
    }

    public static GameException collisionError(String message) {
        return new GameException(ExceptionType.COLLISION_ERROR, message, null, null);
    }

    public static GameException fileIOError(String message, Throwable cause) {
        return new GameException(ExceptionType.FILE_IO_ERROR, message, cause, null);
    }

    public static GameException generalError(String message) {
        return new GameException(ExceptionType.GENERAL_ERROR, message, null, null);
    }

    public static GameException generalError(String message, Throwable cause) {
        return new GameException(ExceptionType.GENERAL_ERROR, message, cause, null);
    }

    // ==================== Handler Methods (for catching) ====================

    /**
     * Main handler - call this in catch block
     * Usage: catch (GameException e) { GameException.handle(e); }
     */
    public static void handle(GameException e) {
        logger.error("Game Exception: {}", e.toString(), e);

        switch (e.type) {
            case RESOURCE_LOAD_ERROR:
                handleResourceLoad(e);
                break;
            case GAME_STATE_ERROR:
                handleGameState(e);
                break;
            case SPRITE_ERROR:
                handleSprite(e);
                break;
            case SOUND_ERROR:
                handleSound(e);
                break;
            case COLLISION_ERROR:
                handleCollision(e);
                break;
            case FILE_IO_ERROR:
                handleFileIO(e);
                break;
            case GENERAL_ERROR:
            default:
                handleGeneral(e);
                break;
        }
    }

    /**
     * Handle unexpected non-GameException errors
     */
    public static void handleUnexpected(Exception e) {
        logger.error("Unexpected exception: {}", e.getClass().getName(), e);
        logger.error("Message: {}", e.getMessage());
    }

    /**
     * Quick handler for any throwable
     */
    public static void handleAny(Throwable t) {
        if (t instanceof GameException) {
            handle((GameException) t);
        } else if (t instanceof Exception) {
            handleUnexpected((Exception) t);
        } else {
            logger.error("Unexpected throwable: {}", t.getClass().getName(), t);
        }
    }

    // ==================== Private Handler Methods ====================

    private static void handleResourceLoad(GameException e) {
        logger.warn("Resource load failed: {}", e.resourcePath);
        logger.info("Using fallback resource");
    }

    private static void handleGameState(GameException e) {
        logger.error("Game state error: {}", e.getMessage());
        logger.info("Attempting to recover game state");
    }

    private static void handleSprite(GameException e) {
        logger.warn("Sprite error: {}", e.getMessage());
        logger.info("Using fallback sprite");
    }

    private static void handleSound(GameException e) {
        logger.warn("Sound error: {}", e.getMessage());
        logger.info("Continuing without sound");
    }

    private static void handleCollision(GameException e) {
        logger.error("Collision error: {}", e.getMessage());
        logger.info("Skipping collision for this frame");
    }

    private static void handleFileIO(GameException e) {
        logger.error("File I/O error: {}", e.getMessage());
        if (e.hasCause()) {
            logger.error("Caused by: {}", e.getCause().getMessage());
        }
    }

    private static void handleGeneral(GameException e) {
        logger.error("General error: {}", e.getMessage());
        if (e.hasCause()) {
            logger.error("Caused by: {}", e.getCause().getMessage());
        }
    }

    // ==================== Logging Helpers ====================

    /**
     * Log exception without full handling
     */
    public static void log(GameException e) {
        logger.warn("Exception logged: {}", e.toString());
    }

    /**
     * Log detailed exception info
     */
    public static void logDetails(GameException e) {
        logger.info("=== Exception Details ===");
        logger.info("Type: {}", e.type);
        logger.info("Description: {}", e.type.getDescription());
        logger.info("Message: {}", e.getMessage());
        if (e.resourcePath != null) {
            logger.info("Resource: {}", e.resourcePath);
        }
        if (e.hasCause()) {
            logger.info("Cause: {}", e.getCause().getClass().getSimpleName());
        }
        logger.info("========================");
    }

    // ==================== Getters & Utility ====================

    public ExceptionType getType() {
        return type;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public String getErrorCode() {
        return type.name();
    }

    public String getTypeDescription() {
        return type.getDescription();
    }

    public boolean isResourceLoadError() {
        return type == ExceptionType.RESOURCE_LOAD_ERROR;
    }

    public boolean isGameStateError() {
        return type == ExceptionType.GAME_STATE_ERROR;
    }

    public boolean isSpriteError() {
        return type == ExceptionType.SPRITE_ERROR;
    }

    public boolean isSoundError() {
        return type == ExceptionType.SOUND_ERROR;
    }

    public boolean hasCause() {
        return getCause() != null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(type.name()).append("] ");
        sb.append(getMessage());

        if (resourcePath != null) {
            sb.append(" | Resource: ").append(resourcePath);
        }

        if (hasCause()) {
            sb.append(" | Caused by: ").append(getCause().getMessage());
        }

        return sb.toString();
    }
}