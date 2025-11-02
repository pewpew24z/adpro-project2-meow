package se233.project2;

import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se233.project2.model.GameCharacter;
import se233.project2.model.Keys;
import se233.project2.model.Platform;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GameCharacter movements
 * Tests character movement, jumping, and collision detection
 */
public class GameCharacterMovementTest {
    private GameCharacter character;
    private Keys keys;
    private List<Platform> platforms;

    @BeforeEach
    public void setUp() {
        // Initialize character at position (100, 100)
        character = new GameCharacter(100, 100);
        keys = new Keys();
        platforms = new ArrayList<>();

        // Add a ground platform
        platforms.add(new Platform(0, 668, 1280, 52));
    }

    @Test
    public void testInitialPosition() {
        assertEquals(100, character.getX(), "Initial X position should be 100");
        assertEquals(100, character.getY(), "Initial Y position should be 100");
    }

    @Test
    public void testMoveRight() {
        // Simulate pressing D key
        keys.update(KeyCode.D, true);

        int initialX = character.getX();
        character.update(keys, platforms);

        assertTrue(character.getX() > initialX, "Character should move right when D is pressed");
        assertTrue(character.isFacingRight(), "Character should face right when moving right");
    }

    @Test
    public void testMoveLeft() {
        // Simulate pressing A key
        keys.update(KeyCode.A, true);

        int initialX = character.getX();
        character.update(keys, platforms);

        assertTrue(character.getX() < initialX, "Character should move left when A is pressed");
        assertFalse(character.isFacingRight(), "Character should face left when moving left");
    }

    @Test
    public void testJump() {
        // First, let character fall to ground
        for (int i = 0; i < 100; i++) {
            character.update(keys, platforms);
        }

        // Now test jump
        keys.update(KeyCode.W, true);
        int groundY = character.getY();

        character.update(keys, platforms);
        keys.update(KeyCode.W, false);

        // After a few frames, character should be above ground
        for (int i = 0; i < 5; i++) {
            character.update(keys, platforms);
        }

        assertTrue(character.getY() < groundY, "Character should jump up when W is pressed");
    }

    @Test
    public void testGravity() {
        // Character should fall when not on ground
        int initialY = character.getY();

        for (int i = 0; i < 10; i++) {
            character.update(keys, platforms);
        }

        assertTrue(character.getY() > initialY, "Character should fall due to gravity");
    }

    @Test
    public void testDirectionChange() {
        // Start facing right (default)
        assertTrue(character.isFacingRight(), "Character should start facing right");

        // Move left
        keys.update(KeyCode.A, true);
        character.update(keys, platforms);

        assertFalse(character.isFacingRight(), "Character should face left after moving left");

        // Move right
        keys.update(KeyCode.A, false);
        keys.update(KeyCode.D, true);
        character.update(keys, platforms);

        assertTrue(character.isFacingRight(), "Character should face right after moving right");
    }

    @Test
    public void testBoundaryCheck() {
        // Test left boundary
        character = new GameCharacter(0, 100);
        keys.update(KeyCode.A, true);
        character.update(keys, platforms);

        assertTrue(character.getX() >= 0, "Character should not move past left boundary");

        // Test right boundary
        character = new GameCharacter(1230, 100);
        keys.update(KeyCode.A, false);
        keys.update(KeyCode.D, true);

        for (int i = 0; i < 20; i++) {
            character.update(keys, platforms);
        }

        assertTrue(character.getX() <= 1280 - 47, "Character should not move past right boundary");
    }

    @Test
    public void testPlatformCollision() {
        // Add a platform at y=300
        platforms.add(new Platform(50, 300, 200, 50));

        // Position character above platform
        character = new GameCharacter(100, 200);

        // Let character fall
        for (int i = 0; i < 50; i++) {
            character.update(keys, platforms);
        }

        // Character should land on platform (y should be approximately 300 - 36)
        assertTrue(character.getY() <= 300, "Character should land on platform");
    }

    @Test
    public void testCenterPosition() {
        double centerX = character.getCenterX();
        double centerY = character.getCenterY();

        assertTrue(centerX > character.getX(), "Center X should be greater than X");
        assertTrue(centerY > character.getY(), "Center Y should be greater than Y");
    }

    @Test
    public void testCharacterNotDead() {
        assertFalse(character.isDead(), "Character should not be dead initially");
    }

    @Test
    public void testCharacterDeath() {
        character.die();
        assertTrue(character.isDead(), "Character should be dead after calling die()");
    }
}