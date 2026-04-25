package entities;

import java.awt.Rectangle;

import graphics.TextureRenderer;
import main.Game;
import main.Spritesheet;
import main.Texture;

public class Enemy extends Entity {

    public double speed;

    // frames de animação vivo
    protected float[][] enemyUV;
    protected Texture enemySheet;

    // frames de morte
    protected float[] explosionUV;
    protected Texture explosionSheet;
    protected float[] deadLastUV;
    protected Texture deadLastSheet;

    protected int frames = 0, maxFrames = 5;
    protected int index = 0, maxIndex = 1;

    public boolean died = false, readToRemove = false;
    protected int diedFrames = 0, maxDiedFrames = 20;
    protected int diedIndex = 0, maxDiedIndex = 1;

    protected long timeDead;
    protected final int timeToDespawn = 1500;

    public int damagemaskx, damagemasky, damagemaskw, damagemaskh;

    // dimensões dos frames em pixels
    protected static final int FRAME_W = 32;
    protected static final int FRAME_H = 48;
    protected static final int DEAD_W  = 95;
    protected static final int DEAD_H  = 118;
    protected static final int LAST_W  = 256;
    protected static final int LAST_H  = 256;

    public Enemy(int x, int y, int width, int height, Texture texture) {
        super(x, y, width, height, texture);
        speed = 1.2;

        loadSprites("/enemy1.png");

        this.width  = FRAME_W;
        this.height = FRAME_H;
        maskx = -4; masky = 0; maskw = 0; maskh = 0;
        damagemaskx = 12; damagemasky = 10; damagemaskw = -24; damagemaskh = -16;
    }

    /** Carrega o spritesheet do inimigo vivo + sheets de morte compartilhados. */
    protected void loadSprites(String enemyPath) {
        Spritesheet ss = new Spritesheet(enemyPath);
        enemySheet = ss.getTexture();
        enemyUV = new float[2][];
        enemyUV[0] = ss.getUV(0,  0, FRAME_W, FRAME_H);
        enemyUV[1] = ss.getUV(32, 0, FRAME_W, FRAME_H);

        Spritesheet exSS = new Spritesheet("/deadexplosion.png");
        explosionSheet = exSS.getTexture();
        explosionUV    = exSS.getUV(0, 0, DEAD_W, DEAD_H);

        Spritesheet dlSS = new Spritesheet("/deadlastframe.png");
        deadLastSheet = dlSS.getTexture();
        deadLastUV    = dlSS.getUV(0, 0, LAST_W, LAST_H);
    }

    @Override
    public void tick() {
        if (!died) {
            y += (int) speed;
            if (verifyBulletCollision()) {
                died = true;
                Game.score += 10;
            }
        }

        if (this.getY() > Game.HEIGHT) {
            Game.enemies.remove(this);
            return;
        }

        tickAnimation();
        tickDeath();
    }

    protected void tickAnimation() {
        frames++;
        if (frames > maxFrames) {
            frames = 0;
            index++;
            if (index > maxIndex) index = 0;
        }
    }

    protected void tickDeath() {
        if (died && !readToRemove) {
            timeDead = System.currentTimeMillis();
            readToRemove = true;
        } else if (readToRemove) {
            if (System.currentTimeMillis() - timeDead >= timeToDespawn) {
                Game.enemies.remove(this);
                return;
            }
        }

        if (died) {
            diedFrames++;
            if (diedFrames > maxDiedFrames) {
                diedIndex++;
                if (diedIndex > maxDiedIndex) diedIndex = maxDiedIndex;
            }
        }
    }

    @Override
    public void render() {
        int scale = Game.SCALE;
        float rx = x * scale;
        float ry = y * scale;

        if (!died) {
            TextureRenderer.drawTextureRegion(enemySheet,
                    rx, ry, FRAME_W * scale, FRAME_H * scale,
                    enemyUV[index]);
        } else {
            if (diedIndex == 0) {
                TextureRenderer.drawTextureRegion(explosionSheet,
                        rx, ry, 48 * scale, 48 * scale,
                        explosionUV);
            } else {
                TextureRenderer.drawTextureRegion(deadLastSheet,
                        rx, ry, 48 * scale, 48 * scale,
                        deadLastUV);
            }
        }

        if (Game.DEBUG) {
            TextureRenderer.drawRect(
                    (x + maskx) * scale, (y + masky) * scale,
                    (width + maskw) * scale, (height + maskh) * scale,
                    1f, 0f, 0f);
            TextureRenderer.drawRect(
                    (x + damagemaskx) * scale, (y + damagemasky) * scale,
                    (width + damagemaskw) * scale, (height + damagemaskh) * scale,
                    0f, 0f, 1f);
        }
    }

    protected boolean verifyBulletCollision() {
        for (int i = 0; i < Game.bullets.size(); i++) {
            Bullet b = Game.bullets.get(i);
            Rectangle r1 = new Rectangle(b.getX() + b.maskx, b.getY() + b.masky,
                                          b.getWidth() + b.maskw, b.getHeight() + b.maskh);
            Rectangle r2 = new Rectangle(x + maskx, y + masky, width + maskw, height + maskh);
            if (r1.intersects(r2)) {
                Game.bullets.remove(i);
                return true;
            }
        }
        return false;
    }
}
