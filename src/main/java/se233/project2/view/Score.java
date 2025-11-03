package se233.project2.view;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import se233.project2.model.ScoreModel;

/**
 * Score - UI Component สำหรับแสดงคะแนน
 * ใช้ ScoreModel จัดการ logic ภายใน
 */
public class Score extends Label {
    private ScoreModel scoreModel;

    public Score(int x, int y) {
        this.scoreModel = new ScoreModel();
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        this.setTextFill(Color.WHITE);
        updateDisplay();
    }

    /**
     * เพิ่มคะแนน
     */
    public void addScore(int points) {
        scoreModel.addScore(points);
        updateDisplay();
    }

    /**
     * ตั้งค่าคะแนนโดยตรง (สำหรับ restore ระหว่าง stage)
     */
    public void setScore(int newScore) {
        scoreModel.setScore(newScore);
        updateDisplay();
    }

    /**
     * รีเซ็ตคะแนน
     */
    public void resetScore() {
        scoreModel.resetScore();
        updateDisplay();
    }

    /**
     * อัพเดตการแสดงผล
     */
    private void updateDisplay() {
        this.setText("SCORE: " + scoreModel.getScore());
    }

    /**
     * ดึงค่าคะแนนปัจจุบัน
     */
    public int getScore() {
        return scoreModel.getScore();
    }

    /**
     * ดึง ScoreModel (สำหรับการทดสอบหรือ logic อื่นๆ)
     */
    public ScoreModel getModel() {
        return scoreModel;
    }
}