package entities;

import java.awt.Rectangle;

import main.Game;
import main.Spritesheet;
import main.Texture;

public class Enemy3 extends Enemy {

    private float[] bulletUV;
    private Texture bulletSheet;

    private long lastShootTime;
    private boolean canShoot = false, wannaShoot = false;
    private final long intervaloShoot = 1000;

    public Enemy3(int x, int y, int width, int height, Texture texture) {
        super(x, y, width, height, texture);
        loadSprites("/enemy3.png");
        speed = 1.0;
        maskx = 0; masky = 0; maskw = 0; maskh = 0;
        damagemaskx = 12; damagemasky = 10; damagemaskw = -24; damagemaskh = -16;

        Spritesheet bSS = new Spritesheet("/bullet.png");
        bulletSheet = bSS.getTexture();
        bulletUV    = bSS.getUV(16, 0, 16, 16);
    }

    @Override
    public void tick() {
        if (!died) {
            // perseguição ao player
            if (x < Game.player.getX() - 10 && !haveCollision(x + speed, y)) {
                x += (int) speed;
            } else if (x > Game.player.getX() - 8 && !haveCollision(x - speed, y)) {
                x -= (int) speed;
            } else {
                wannaShoot = true;
            }

            if (y < Game.player.getY() - this.getHeight() - 20 && !haveCollision(x, y + speed)) {
                y += (int) speed;
            } else if (y > Game.player.getY()) {
                y += (int) speed;
            }

            // tiro
            if (wannaShoot && canShoot) {
                canShoot = false;
                wannaShoot = false;
                Game.enemyBullets.add(new EnemyBullet(
                        this.getX() + this.width / 2 - 5, this.getY(),
                        Game.SCALE * 5, Game.SCALE * 5, bulletSheet, bulletUV, 5, 1));
                lastShootTime = System.currentTimeMillis();
            } else if (!canShoot) {
                if (System.currentTimeMillis() - lastShootTime >= intervaloShoot) canShoot = true;
                wannaShoot = false;
            }

            if (verifyBulletCollision()) {
                died = true;
                Game.score += 50;
            }
        }

        if (x > Game.WIDTH - this.getWidth() * 2) x = Game.WIDTH - this.getWidth() * 2;
        if (x < this.getWidth())                  x = this.getWidth();
        if (y > Game.HEIGHT) {
            Game.enemies.remove(this);
            return;
        }

        tickAnimation();
        tickDeath();
    }

    private boolean haveCollision(double dx, double dy) {
        Rectangle cur = new Rectangle((int) dx + maskx, (int) dy + masky, width + maskw, height + maskh);
        for (Enemy en : Game.enemies) {
            if (en == this || !(en instanceof Enemy3)) continue;
            Rectangle target = new Rectangle(en.getX() + maskx, en.getY() + masky, width + maskw, height + maskh);
            if (target.intersects(cur)) return true;
        }
        return false;
    }
}
