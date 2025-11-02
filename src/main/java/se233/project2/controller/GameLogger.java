package se233.project2.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GameLogger - Centralized logging for character movements, actions, and scoring
 * Uses different log levels for different types of events:
 * - TRACE: Detailed movement data
 * - DEBUG: Character actions
 * - INFO: Scoring events
 * - WARN: Game state warnings
 * - ERROR: Critical errors
 */
public class GameLogger {
    private static final Logger logger = LoggerFactory.getLogger(GameLogger.class);
    private static GameLogger instance;

    private GameLogger() {}

    public static GameLogger getInstance() {
        if (instance == null) {
            instance = new GameLogger();
        }
        return instance;
    }

    // ==================== Character Movement Logging (TRACE) ====================

    public void logMovement(String characterType, double x, double y, double velocityX, double velocityY) {
        logger.trace("{} movement - Position: ({}, {}), Velocity: ({}, {})",
                characterType, x, y, velocityX, velocityY);
    }

    public void logJump(String characterType, double x, double y) {
        logger.trace("{} jumped at position ({}, {})", characterType, x, y);
    }

    public void logLanding(String characterType, double x, double y) {
        logger.trace("{} landed at position ({}, {})", characterType, x, y);
    }

    public void logDirectionChange(String characterType, String direction) {
        logger.trace("{} changed direction to {}", characterType, direction);
    }

    // ==================== Character Actions Logging (DEBUG) ====================

    public void logShoot(String characterType, double x, double y, String direction) {
        logger.debug("{} shot bullet at ({}, {}) towards {}",
                characterType, x, y, direction);
    }

    public void logSpecialAttack(String characterType, double x, double y) {
        logger.debug("{} used special attack at ({}, {})", characterType, x, y);
    }

    public void logProne(String characterType) {
        logger.debug("{} entered prone position", characterType);
    }

    public void logStandUp(String characterType) {
        logger.debug("{} stood up from prone", characterType);
    }

    public void logDeath(String characterType, double x, double y) {
        logger.debug("{} died at position ({}, {})", characterType, x, y);
    }

    public void logEnemySpawn(String enemyType, double x, double y) {
        logger.debug("{} spawned at ({}, {})", enemyType, x, y);
    }

    public void logBossSpawn(String bossType, double x, double y) {
        logger.debug("{} spawned at ({}, {})", bossType, x, y);
    }

    // ==================== Scoring Logging (INFO) ====================

    public void logScore(String entityType, int pointsAwarded, int totalScore) {
        logger.info("Score awarded: {} defeated for {} points. Total score: {}",
                entityType, pointsAwarded, totalScore);
    }

    public void logEnemyDefeated(String enemyType, int pointsAwarded, int totalScore) {
        logger.info("{} defeated! +{} points (Total: {})",
                enemyType, pointsAwarded, totalScore);
    }

    public void logBossDefeated(String bossType, int pointsAwarded, int totalScore) {
        logger.info("BOSS DEFEATED: {} eliminated for {} points! Total score: {}",
                bossType, pointsAwarded, totalScore);
    }

    public void logStageComplete(int stage, int totalScore) {
        logger.info("Stage {} completed! Total score: {}", stage, totalScore);
    }

    public void logGameComplete(int finalScore) {
        logger.info("Game completed! Final score: {}", finalScore);
    }

    // ==================== Game State Logging (INFO/WARN) ====================

    public void logStageStart(int stage) {
        logger.info("Starting Stage {}", stage);
    }

    public void logPlayerHit(int livesRemaining) {
        logger.warn("Player hit! Lives remaining: {}", livesRemaining);
    }

    public void logGameOver(int finalScore) {
        logger.warn("Game Over! Final score: {}", finalScore);
    }

    public void logBossPhaseChange(String bossType, String phase) {
        logger.info("{} entered {} phase", bossType, phase);
    }

    // ==================== Collision Logging (DEBUG) ====================

    public void logBulletHit(String bulletOwner, String target, double x, double y) {
        logger.debug("{} bullet hit {} at ({}, {})", bulletOwner, target, x, y);
    }

    public void logPlayerCollision(String obstacleType, double x, double y) {
        logger.debug("Player collided with {} at ({}, {})", obstacleType, x, y);
    }

    // ==================== Error Logging (ERROR) ====================

    public void logError(String errorType, String message) {
        logger.error("{}: {}", errorType, message);
    }

    public void logError(String errorType, String message, Exception e) {
        logger.error("{}: {}", errorType, message, e);
    }

    public void logResourceLoadError(String resourcePath) {
        logger.error("Failed to load resource: {}", resourcePath);
    }

    public void logGameStateError(String message) {
        logger.error("Game state error: {}", message);
    }

    // ==================== Performance Logging (DEBUG) ====================

    public void logFrameTime(long frameTime) {
        if (frameTime > 20_000_000) { // > 20ms (below 50 FPS)
            logger.debug("Frame time: {} ms (performance warning)", frameTime / 1_000_000);
        }
    }

    public void logMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1_048_576; // MB
        logger.debug("Memory usage: {} MB", usedMemory);
    }
}