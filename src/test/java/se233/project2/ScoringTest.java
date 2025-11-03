package se233.project2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se233.project2.model.ScoreModel;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Scoring system
 * ✅ แก้ไขให้ทดสอบ ScoreModel แทน Score (ไม่ต้องพึ่ง JavaFX)
 */
public class ScoringTest {
    private ScoreModel score;

    @BeforeEach
    public void setUp() {
        score = new ScoreModel();
    }

    @Test
    public void testInitialScore() {
        assertEquals(0, score.getScore(), "Initial score should be 0");
    }

    @Test
    public void testAddScore() {
        score.addScore(10);
        assertEquals(10, score.getScore(), "Score should be 10 after adding 10 points");
    }

    @Test
    public void testAddMultipleScores() {
        score.addScore(5);
        score.addScore(10);
        score.addScore(15);

        assertEquals(30, score.getScore(), "Score should be 30 after adding 5, 10, and 15 points");
    }

    @Test
    public void testRegularEnemyScore() {
        // RegularEnemy gives 1 point
        score.addScore(1);
        assertEquals(1, score.getScore(), "Score should be 1 after defeating RegularEnemy");
    }

    @Test
    public void testSecondTierEnemyScore() {
        // SecondTierEnemy gives 1 point
        score.addScore(1);
        assertEquals(1, score.getScore(), "Score should be 1 after defeating SecondTierEnemy");
    }

    @Test
    public void testSmallBossScore() {
        // SmallBoss gives 2 points
        score.addScore(2);
        assertEquals(2, score.getScore(), "Score should be 2 after defeating SmallBoss");
    }

    @Test
    public void testWallBossScore() {
        // WallBoss gives 3 points
        score.addScore(3);
        assertEquals(3, score.getScore(), "Score should be 3 after defeating WallBoss");
    }

    @Test
    public void testJavaBossScore() {
        // JavaBoss gives 3 points
        score.addScore(3);
        assertEquals(3, score.getScore(), "Score should be 3 after defeating JavaBoss");
    }

    @Test
    public void testBoss3Score() {
        // Boss3 gives 5 points
        score.addScore(5);
        assertEquals(5, score.getScore(), "Score should be 5 after defeating Boss3");
    }

    @Test
    public void testResetScore() {
        score.addScore(100);
        assertEquals(100, score.getScore(), "Score should be 100");

        score.resetScore();
        assertEquals(0, score.getScore(), "Score should be 0 after reset");
    }

    @Test
    public void testSetScore() {
        score.setScore(50);
        assertEquals(50, score.getScore(), "Score should be 50 after setScore(50)");

        score.setScore(100);
        assertEquals(100, score.getScore(), "Score should be 100 after setScore(100)");
    }

    @Test
    public void testMultipleEnemyKills() {
        // Kill 3 regular enemies (1 point each)
        for (int i = 0; i < 3; i++) {
            score.addScore(1);
        }

        assertEquals(3, score.getScore(), "Score should be 3 after killing 3 regular enemies");
    }

    @Test
    public void testMixedEnemyKills() {
        // RegularEnemy: 1 point
        score.addScore(1);

        // SecondTierEnemy: 1 point
        score.addScore(1);

        // SmallBoss: 2 points
        score.addScore(2);

        // WallBoss: 3 points
        score.addScore(3);

        // JavaBoss: 3 points
        score.addScore(3);

        // Boss3: 5 points
        score.addScore(5);

        // Total: 1 + 1 + 2 + 3 + 3 + 5 = 15
        assertEquals(15, score.getScore(), "Score should be 15 after defeating all enemy types");
    }

    @Test
    public void testStage1Completion() {
        // Stage 1: 3 RegularEnemies + 1 WallBoss
        // 3 * 1 + 1 * 3 = 6 points
        for (int i = 0; i < 3; i++) {
            score.addScore(1); // RegularEnemy
        }
        score.addScore(3); // WallBoss

        assertEquals(6, score.getScore(), "Score should be 6 after completing Stage 1");
    }

    @Test
    public void testStage2Completion() {
        // Stage 2: 5 SecondTierEnemies + 1 JavaBoss
        // 5 * 1 + 1 * 3 = 8 points
        for (int i = 0; i < 5; i++) {
            score.addScore(1); // SecondTierEnemy
        }
        score.addScore(3); // JavaBoss

        assertEquals(8, score.getScore(), "Score should be 8 after completing Stage 2");
    }

    @Test
    public void testStage3Completion() {
        // Stage 3: 3 SmallBosses + 1 Boss3
        // 3 * 2 + 1 * 5 = 11 points
        for (int i = 0; i < 3; i++) {
            score.addScore(2); // SmallBoss
        }
        score.addScore(5); // Boss3

        assertEquals(11, score.getScore(), "Score should be 11 after completing Stage 3");
    }

    @Test
    public void testFullGameCompletion() {
        // Stage 1
        for (int i = 0; i < 3; i++) {
            score.addScore(1); // RegularEnemy
        }
        score.addScore(3); // WallBoss

        // Stage 2
        for (int i = 0; i < 5; i++) {
            score.addScore(1); // SecondTierEnemy
        }
        score.addScore(3); // JavaBoss

        // Stage 3
        for (int i = 0; i < 3; i++) {
            score.addScore(2); // SmallBoss
        }
        score.addScore(5); // Boss3

        // Total: 6 + 8 + 11 = 25 points
        assertEquals(25, score.getScore(), "Score should be 25 after completing all 3 stages");
    }

    @Test
    public void testScorePersistenceBetweenStages() {
        // Simulate stage transitions
        // Stage 1 completion
        score.setScore(6);
        assertEquals(6, score.getScore());

        // Stage 2 completion (add to existing)
        score.addScore(8);
        assertEquals(14, score.getScore());

        // Stage 3 completion (add to existing)
        score.addScore(11);
        assertEquals(25, score.getScore(), "Score should persist and accumulate across stages");
    }

    @Test
    public void testNegativeScoreHandling() {
        // Try to add negative score
        score.addScore(-10);

        // This test verifies current behavior
        // Score allows negative values (implementation choice)
        assertEquals(-10, score.getScore(), "Negative score is added as per current implementation");
    }

    @Test
    public void testLargeScore() {
        // Test with large score values
        score.addScore(1000);
        score.addScore(5000);

        assertEquals(6000, score.getScore(), "Score should handle large values correctly");
    }

    @Test
    public void testScoreIncrement() {
        int previousScore = score.getScore();

        score.addScore(1);

        assertTrue(score.getScore() > previousScore, "Score should increment after adding points");
    }

    @Test
    public void testMultipleResets() {
        score.addScore(50);
        score.resetScore();

        assertEquals(0, score.getScore(), "Score should be 0 after first reset");

        score.addScore(100);
        assertEquals(100, score.getScore(), "Score should be 100 after adding");

        score.resetScore();
        assertEquals(0, score.getScore(), "Score should be 0 after second reset");
    }

    @Test
    public void testSetScoreOverwrite() {
        // Test that setScore overwrites existing score
        score.addScore(50);
        assertEquals(50, score.getScore());

        score.setScore(100); // Should overwrite, not add
        assertEquals(100, score.getScore(), "setScore should overwrite existing score");
    }

    @Test
    public void testSetScoreThenAdd() {
        // Test combining setScore and addScore
        score.setScore(10);
        assertEquals(10, score.getScore());

        score.addScore(5);
        assertEquals(15, score.getScore(), "addScore should work correctly after setScore");
    }

    @Test
    public void testHasScore() {
        // Test new hasScore method
        score.setScore(50);

        assertTrue(score.hasScore(50), "Should have score >= 50");
        assertTrue(score.hasScore(30), "Should have score >= 30");
        assertFalse(score.hasScore(51), "Should not have score >= 51");
    }

    @Test
    public void testToString() {
        score.setScore(42);
        assertEquals("Score: 42", score.toString(), "toString should format correctly");
    }
}