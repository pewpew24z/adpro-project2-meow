package se233.project2.model.boss;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import se233.project2.model.item.Bullet;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Boss3 (Last Boss) - บอสตัวสุดท้ายของ Stage 3
 * - ยิงกระสุน 5 ทิศทาง (ไม่เกิน 180°) สุ่ม x2 หรือ x3
 * - ยิง weapon พุ่งตรงไปที่ผู้เล่น
 * - Weapon animation 10 frames (custom width)
 */
public class Boss3 extends Pane {
    private ImageView bossImageView;
    private Circle fallbackCircle;
    private CustomWeaponSprite weaponEffect;

    private Image bossImage;
    private Image weaponSprite;
    private Image bulletSprite;

    private double x, y;
    private double width = 500;
    private double height = 500;
    private int health;
    private int maxHealth;
    private boolean alive = true;
    private Random random = new Random();

    // Animation (เฉพาะ weapon effect)
    private int weaponAnimationTick = 0;
    private final int ANIMATION_SPEED = 6;

    // Shooting
    private List<AnimatedBullet> bullets;
    private List<WeaponProjectile> weapons;  // ✨ เพิ่ม weapon projectiles
    private long lastShoot = 0;
    private long lastWeaponShoot = 0;  // ✨ แยก cooldown
    private long shootInterval = 1_200_000_000; // 1.2 วินาที (bullet)
    private long weaponInterval = 2_000_000_000L; // 2 วินาที (weapon)

    // Player tracking
    private double playerX = 0;
    private double playerY = 0;

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
        this.weapons = new ArrayList<>();

        setupSprite();
        this.setTranslateX(x);
        this.setTranslateY(y);
    }

    private void setupSprite() {
        if (bossImage != null) {
            bossImageView = new ImageView(bossImage);
            bossImageView.setFitWidth(width);
            bossImageView.setFitHeight(height);
            bossImageView.setPreserveRatio(true);
            bossImageView.setSmooth(false);
            this.getChildren().add(bossImageView);

            // ✨ Weapon effect - custom animation sprite
            if (weaponSprite != null) {
                weaponEffect = new CustomWeaponSprite(weaponSprite);
                weaponEffect.setFitWidth(100);
                weaponEffect.setFitHeight(120);
                weaponEffect.setPreserveRatio(false);
                weaponEffect.setSmooth(false);
                weaponEffect.setTranslateX(180);  // ใกล้ตำแหน่งยิง
                weaponEffect.setTranslateY(100);
                weaponEffect.setVisible(false);
                this.getChildren().add(weaponEffect);
            }
        } else {
            fallbackCircle = new Circle(width / 2, Color.DARKRED);
            this.getChildren().add(fallbackCircle);
        }
    }

    public void update(long now) {
        if (!alive) return;

        // Update weapon animation
        if (weaponEffect != null && weaponEffect.isVisible()) {
            updateWeaponAnimation();
        }

        // ✨ Bullet shooting (5 ทิศทาง, สุ่ม x2 หรือ x3)
        if (now - lastShoot > shootInterval) {
            shootBulletPattern();
            lastShoot = now;
        }

        // ✨ Weapon shooting (พุ่งตรงไปที่ผู้เล่น)
        if (now - lastWeaponShoot > weaponInterval) {
            shootWeaponAtPlayer();
            lastWeaponShoot = now;
        }

        // Update bullets
        List<AnimatedBullet> bulletsToRemove = new ArrayList<>();
        for (AnimatedBullet bullet : bullets) {
            bullet.update();
            if (!bullet.isActive()) {
                bulletsToRemove.add(bullet);
            }
        }
        bullets.removeAll(bulletsToRemove);

        // ✨ Update weapons
        List<WeaponProjectile> weaponsToRemove = new ArrayList<>();
        for (WeaponProjectile weapon : weapons) {
            weapon.update();
            if (!weapon.isActive()) {
                weaponsToRemove.add(weapon);
            }
        }
        weapons.removeAll(weaponsToRemove);
    }

    private void updateWeaponAnimation() {
        weaponAnimationTick++;
        if (weaponAnimationTick >= ANIMATION_SPEED) {
            weaponAnimationTick = 0;
            weaponEffect.tick();
        }
    }

    /**
     * ✨ ยิงกระสุน 5 ทิศทาง (ไม่เกิน 180°) สุ่ม x2 หรือ x3
     */
    private void shootBulletPattern() {
        double bulletX = x + 220;
        double bulletY = y + 150;
        double baseSpeed = 7;

        // ✨ สุ่มจำนวนรอบ: 2 หรือ 3
        int rounds = random.nextBoolean() ? 2 : 3;

        for (int round = 0; round < rounds; round++) {
            //0-359 degree
            for (int i = 0; i < 5; i++) {
                double angle = Math.toRadians(random.nextInt(360)); // 0–359 องศา
                double vx = baseSpeed * Math.cos(angle);
                double vy = baseSpeed * Math.sin(angle);

                AnimatedBullet bullet = new AnimatedBullet(
                        bulletSprite,
                        bulletX,
                        bulletY,
                        vx,
                        vy
                );
                bullets.add(bullet);
            }

        }

        System.out.println("🔥 Boss3 fired " + rounds + " rounds of 5-direction bullets!");
    }

    /**
     * ✨ ยิง weapon พุ่งตรงไปที่ผู้เล่น จากพิกัด (220, 130)
     */
    private void shootWeaponAtPlayer() {
        // Show weapon effect
        if (weaponEffect != null) {
            weaponEffect.setVisible(true);
            weaponEffect.reset();
        }

        // ✨ ยิงจากพิกัด x=220, y=130
        double weaponX = x + 220;
        double weaponY = y + 130;

        // ✨ คำนวณทิศทางไปหาผู้เล่น
        double dx = playerX - weaponX;
        double dy = playerY - weaponY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > 0) {
            double speed = 8;
            double vx = (dx / distance) * speed;
            double vy = (dy / distance) * speed;

            WeaponProjectile weapon = new WeaponProjectile(
                    weaponSprite,
                    weaponX,
                    weaponY,
                    vx,
                    vy
            );
            weapons.add(weapon);
        }

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

        System.out.println("⚔️ Boss3 fired weapon towards player!");
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
        // ✨ รวม bullets และ weapons เข้าด้วยกัน
        List<Bullet> allProjectiles = new ArrayList<>();
        allProjectiles.addAll(bullets);
        allProjectiles.addAll(weapons);
        return allProjectiles;
    }

    public void removeBullet(Bullet bullet) {
        bullets.remove(bullet);
        weapons.remove(bullet);
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getBossWidth() { return width; }
    public double getBossHeight() { return height; }
    public int getHealth() { return health; }

    /**
     * ✨ Custom Weapon Sprite - 10 frames with variable widths
     */
    private class CustomWeaponSprite extends ImageView {
        private Image spriteSheet;
        private int currentFrame = 0;
        private final int TOTAL_FRAMES = 10;

        // ✨ Frame data: [x, y, width, height]
        private final int[][] frames = {
                {0, 0, 16, 22},      // Frame 1
                {15, 0, 14, 22},     // Frame 2
                {31, 0, 18, 22},     // Frame 3
                {50, 0, 18, 22},     // Frame 4
                {69, 0, 18, 22},     // Frame 5
                {90, 0, 18, 22},     // Frame 6
                {108, 0, 18, 22},    // Frame 7
                {127, 0, 18, 22},    // Frame 8
                {147, 0, 18, 22},    // Frame 9
                {167, 0, 18, 22}     // Frame 10
        };

        public CustomWeaponSprite(Image sprite) {
            this.spriteSheet = sprite;
            this.setImage(sprite);
            updateViewport();
        }

        public void tick() {
            currentFrame = (currentFrame + 1) % TOTAL_FRAMES;
            updateViewport();
        }

        public void reset() {
            currentFrame = 0;
            updateViewport();
        }

        private void updateViewport() {
            int[] frame = frames[currentFrame];
            this.setViewport(new Rectangle2D(frame[0], frame[1], frame[2], frame[3]));
        }
    }

    /**
     * AnimatedBullet - กระสุนธรรมดา (4 frames)
     */
    private class AnimatedBullet extends Bullet {
        private CustomBulletSprite bulletSprite;
        private int animTick = 0;
        private final int ANIM_SPEED = 4;
        private static final double PLATFORM_Y = 585;

        public AnimatedBullet(Image sprite, double x, double y, double vx, double vy) {
            super(sprite, x, y, vx, vy, false);

            if (sprite != null) {
                this.getChildren().clear();
                bulletSprite = new CustomBulletSprite(sprite);
                bulletSprite.setFitWidth(60);
                bulletSprite.setFitHeight(54);
                bulletSprite.setPreserveRatio(true);
                bulletSprite.setSmooth(false);
                this.getChildren().add(bulletSprite);
            }
        }

        @Override
        public void update() {
            if (this.getY() >= PLATFORM_Y) {
                this.setVerticalSpeed(0);
                this.deactivate();
                return;
            }

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

    /**
     * ✨ WeaponProjectile - weapon ที่พุ่งตรงไปหาผู้เล่น
     */
    private class WeaponProjectile extends Bullet {
        private CustomBulletSprite weaponSprite;
        private int animTick = 0;
        private final int ANIM_SPEED = 4;
        private static final double PLATFORM_Y = 585;

        public WeaponProjectile(Image sprite, double x, double y, double vx, double vy) {
            super(sprite, x, y, vx, vy, false);

            if (sprite != null) {
                this.getChildren().clear();
                weaponSprite = new CustomBulletSprite(sprite);
                weaponSprite.setFitWidth(60);
                weaponSprite.setFitHeight(54);
                weaponSprite.setPreserveRatio(true);
                weaponSprite.setSmooth(false);
                this.getChildren().add(weaponSprite);
            }
        }

        @Override
        public void update() {
            if (this.getY() >= PLATFORM_Y) {
                this.setVerticalSpeed(0);
                this.deactivate();
                return;
            }

            super.update();

            if (weaponSprite != null) {
                animTick++;
                if (animTick >= ANIM_SPEED) {
                    animTick = 0;
                    weaponSprite.tick();
                }
            }
        }
    }

    /**
     * Custom Bullet Sprite - 4 frames
     */
    private class CustomBulletSprite extends ImageView {
        private Image spriteSheet;
        private int currentFrame = 0;
        private final int TOTAL_FRAMES = 4;

        public CustomBulletSprite(Image sprite) {
            this.spriteSheet = sprite;
            this.setImage(sprite);
            updateViewport();
        }

        public void tick() {
            currentFrame = (currentFrame + 1) % TOTAL_FRAMES;
            updateViewport();
        }

        private void updateViewport() {
            int x = currentFrame * 40;
            this.setViewport(new Rectangle2D(x, 0, 40, 36));
        }
    }
}