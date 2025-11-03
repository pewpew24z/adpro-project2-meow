package se233.project2.model;

/**
 * ัดการ logic ของคะแนนแยกจาก UI
 */
public class ScoreModel {
    private int score;

    public ScoreModel() {
        this.score = 0;
    }

    public ScoreModel(int initialScore) {
        this.score = initialScore;
    }

    public void addScore(int points) {
        score += points;
    }

    public void setScore(int newScore) {
        this.score = newScore;
    }

    public void resetScore() {
        score = 0;
    }

    public int getScore() {
        return score;
    }

    public boolean hasScore(int minimumScore) {
        return score >= minimumScore;
    }

    @Override
    public String toString() {
        return "Score: " + score;
    }
}