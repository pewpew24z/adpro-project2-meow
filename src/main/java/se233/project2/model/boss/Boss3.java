package se233.project2.model.boss;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import se233.project2.model.AnimatedSprite;
import se233.project2.model.item.Bullet;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Boss3 (Last Boss) - บอสตัวสุดท้ายของ Stage 3
 * - อยู่นิ่งที่ตำแหน่งขวาสุด
 * - ใช้ภาพธรรมดา (ไม่มี animation) สำหรับตัว boss
 * - ยิงกระสุนเล็งไปที่ player โดยตรง (animated bullet)
 * - กระสุนที่ตกพื้นจะ Boom
 */
public class Boss3 extends Pane {
    private ImageView bossImageView;  // ⭐ เปลี่ยนจาก AnimatedSprite เป็น ImageView ธรรมดา
    private Circle fallbackCircle;
    private AnimatedSprite weaponEffect;

    private Image bossImage;
    private Image weaponSprite;
    private Image bulletSprite;

    private double x, y;
    private double width = 500;   // ⭐ ขยายให้ใหญ่เกือบเต็มจอ
    private double height = 500;  // ⭐ ขยายให้ใหญ่เกือบเต็มจอ
    private int health;
    private int maxHealth;
    private boolean alive = true;

    // Animation (เฉพาะ weapon effect)
    private int weaponAnimationTick = 0;
    private final int ANIMATION_SPEED = 6;

    // Shooting
    private List<AnimatedBullet> bullets;
    private long lastShoot = 0;
    private long shootInterval = 1_200_000_000; // 1.2 วินาที

    // Player tracking
    private double playerX = 0;
    private double playerY = 0;

    // Weapon sprite sheet
    private static final int WEAPON_FRAMES = 3;
    private static final int WEAPON_WIDTH = 32;
    private static final int WEAPON_HEIGHT = 32;

    public Boss3(Image bossImage, Image weaponSprite, Image bulletSprite,
                 double x, double y, int maxHealth) {
        this.x = x;
        this.y = y;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.bossImage = bossImage;
        this.weaponSprite = weaponSprite;
        this.bulletSprite = bulletSprite;
        this.bullets = new ArrayList<>();

        setupSprite();
        this.setTranslateX(x);
        this.setTranslateY(y);
    }

    private void setupSprite() {
        if (bossImage != null) {
            // ⭐ ใช้ ImageView ธรรมดาสำหรับ boss (ไม่มี animation)
            bossImageView = new ImageView(bossImage);
            bossImageView.setFitWidth(width);
            bossImageView.setFitHeight(height);
            bossImageView.setPreserveRatio(true);
            bossImageView.setSmooth(false);
            this.getChildren().add(bossImageView);

            // ⭐ Weapon effect ยังคงเป็น animated sprite
            if (weaponSprite != null) {
                weaponEffect = new AnimatedSprite(
                        weaponSprite,
                        WEAPON_FRAMES,
                        WEAPON_FRAMES,
                        0, 0,
                        WEAPON_WIDTH,
                        WEAPON_HEIGHT,
                        WEAPON_WIDTH,
                        WEAPON_HEIGHT
                );
                weaponEffect.setFitWidth(150);  // ⭐ ขยาย weapon effect
                weaponEffect.setFitHeight(150);
                weaponEffect.setPreserveRatio(true);
                weaponEffect.setSmooth(false);
                weaponEffect.setTranslateX(-120);  // ⭐ ปรับตำแหน่ง weapon
                weaponEffect.setTranslateY(height / 2 - 75);  // ⭐ กลางตัว boss
                weaponEffect.setVisible(false);
                this.getChildren().add(weaponEffect);
            }
        } else {
            // Fallback - แสดงวงกลม
            fallbackCircle = new Circle(width / 2, Color.DARKRED);
            this.getChildren().add(fallbackCircle);
        }
    }

    public void update(long now) {
        if (!alive) return;

        // ⭐ Boss3 stays stationary (no movement)
        // ⭐ Boss sprite ไม่มี animation (ไม่ต้อง update)

        // Update weapon animation (ถ้ากำลังแสดงอยู่)
        if (weaponEffect != null && weaponEffect.isVisible()) {
            updateWeaponAnimation();
        }

        // ⭐ Shooting - aim at player
        if (now - lastShoot > shootInterval) {
            shootAtPlayer();
            lastShoot = now;
        }

        // Update bullets
        List<AnimatedBullet> toRemove = new ArrayList<>();
        for (AnimatedBullet bullet : bullets) {
            bullet.update();
            if (!bullet.isActive()) {
                toRemove.add(bullet);
            }
        }
        bullets.removeAll(toRemove);
    }

    /**
     * Update weapon effect animation only
     */
    private void updateWeaponAnimation() {
        weaponAnimationTick++;
        if (weaponAnimationTick >= ANIMATION_SPEED) {
            weaponAnimationTick = 0;
            weaponEffect.tick();
        }
    }

    private void shootAtPlayer() {
        // Show weapon effect
        if (weaponEffect != null) {
            weaponEffect.setVisible(true);
            weaponEffect.reset();
        }

        // ⭐ ยิงจากหน้าท้อง (stomach area of sprite)
        // Sprite coordinates: x=83, y=63, width=23, height=22
        double bulletX = x + 83 + 11.5;  // x + 83 + (width/2) for center of stomach
        double bulletY = y + 63 + 11;    // y + 63 + (height/2) for center of stomach

        // ⭐ ยิงกระสุน 3 ลูกพร้อมกัน: ตรงลง, เอียงซ้าย, เอียงขวา
        double baseSpeed = 7;

        // ลูกที่ 1: ตรงลง (vy > 0)
        double vx1 = 0;
        double vy1 = baseSpeed;
        AnimatedBullet bullet1 = new AnimatedBullet(bulletSprite, bulletX, bulletY, vx1, vy1);
        bullets.add(bullet1);

        // ลูกที่ 2: เอียงซ้าย (vx < 0, vy > 0)
        double angle2 = Math.toRadians(45); // 45 องศา
        double vx2 = -baseSpeed * Math.sin(angle2);
        double vy2 = baseSpeed * Math.cos(angle2);
        AnimatedBullet bullet2 = new AnimatedBullet(bulletSprite, bulletX, bulletY, vx2, vy2);
        bullets.add(bullet2);

        // ลูกที่ 3: เอียงขวา (vx > 0, vy > 0)
        double angle3 = Math.toRadians(45); // 45 องศา
        double vx3 = baseSpeed * Math.sin(angle3);
        double vy3 = baseSpeed * Math.cos(angle3);
        AnimatedBullet bullet3 = new AnimatedBullet(bulletSprite, bulletX, bulletY, vx3, vy3);
        bullets.add(bullet3);

        // Hide weapon effect after delay
        new Thread(() -> {
            try {
                Thread.sleep(300);
                if (weaponEffect != null) {
                    weaponEffect.setVisible(false);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void setPlayerPosition(double playerX, double playerY) {
        this.playerX = playerX;
        this.playerY = playerY;
    }

    public void takeDamage(int damage) {
        if (!alive) return;

        health -= damage;
        if (health <= 0) {
            health = 0;
            alive = false;
            this.setVisible(false);
        }
    }

    public boolean checkBulletCollision(Bullet bullet) {
        if (!alive || !bullet.isPlayerBullet()) return false;

        double bx = bullet.getCenterX();
        double by = bullet.getCenterY();

        boolean hit = bx >= x && bx <= x + width &&
                by >= y && by <= y + height;

        if (hit) {
            takeDamage(1);
            bullet.deactivate();
            return true;
        }
        return false;
    }

    public boolean checkPlayerCollision(double playerX, double playerY,
                                        double playerWidth, double playerHeight) {
        if (!alive) return false;

        return x < playerX + playerWidth &&
                x + width > playerX &&
                y < playerY + playerHeight &&
                y + height > playerY;
    }

    // Getters
    public boolean isAlive() { return alive; }
    public List<Bullet> getBullets() {
        // Convert to List<Bullet> for compatibility
        return new ArrayList<>(bullets);
    }
    public void removeBullet(Bullet bullet) { bullets.remove(bullet); }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getBossWidth() { return width; }
    public double getBossHeight() { return height; }
    public int getHealth() { return health; }

    /**
     * AnimatedBullet - Inner class สำหรับกระสุนที่มี animation (4 frames)
     * boss3_bullet.png: 4 frames แนวนอน
     */
    private class AnimatedBullet extends Bullet {
        private AnimatedSprite bulletSprite;
        private int animTick = 0;
        private final int ANIM_SPEED = 4;

        public AnimatedBullet(Image sprite, double x, double y, double vx, double vy) {
            super(sprite, x, y, vx, vy, false);

            if (sprite != null) {
                this.getChildren().clear();
                // ⭐ boss3_bullet.png: 4 frames, width=40, height=36
                // frame 1: x=0, frame 2: x=40, frame 3: x=80, frame 4: x=120
                bulletSprite = new AnimatedSprite(
                        sprite,
                        4,      // totalColumns = 4 frames
                        4,      // frameCount = 4 frames
                        0,      // offsetX = 0
                        0,      // offsetY = 0
                        40,     // width = 40 (ตามที่ระบุ)
                        36,     // height = 36 (ตามที่ระบุ)
                        40,     // spacingX = 40 (เฟรมถัดไปอยู่ที่ +40)
                        36      // spacingY = 36
                );
                bulletSprite.setFitWidth(60);  // ⭐ ขยาย bullet
                bulletSprite.setFitHeight(54);  // ⭐ รักษา aspect ratio (60/40 * 36 = 54)
                bulletSprite.setPreserveRatio(true);
                bulletSprite.setSmooth(false);
                this.getChildren().add(bulletSprite);
            }
        }

        @Override
        public void update() {
            super.update();

            if (bulletSprite != null) {
                animTick++;
                if (animTick >= ANIM_SPEED) {
                    animTick = 0;
                    bulletSprite.tick();
                }
            }
        }
    }
}