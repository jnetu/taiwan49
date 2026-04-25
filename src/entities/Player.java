package entities;

import java.awt.Rectangle;

import graphics.TextureRenderer;
import main.Game;
import main.Spritesheet;
import main.Texture;

public class Player extends Entity {

    public int life;
    public int speed;
    public boolean right, left, up, down;
    private boolean moved;
    private int rightDirection = 0, leftDirection = 1, upDirection = 2, downDirection = 3;
    private int curDirection;
    public boolean pressedShoot, canShoot;
    private long lastShootTime;
    private final int intervaloDeAtirar = 240;

    // UVs de cada frame: [uMin, vMin, uMax, vMax]
    private float[][] runningRight;
    private float[][] runningLeft;
    private float[][] runningUp;
    private float[][] runningDown;

    // Textura compartilhada do spritesheet do player
    private Texture playerSheet;

    // Bullet
    private float[] bulletUV;
    private Texture bulletSheet;

    private int frames = 0, maxFrames = 5;
    private int index = 0, maxIndex = 4;

    private boolean dead = false;

    // Dimensões reais dos frames em pixels
    private static final int FRAME_W_SIDE = 32;
    private static final int FRAME_H_SIDE = 34;
    private static final int FRAME_W_VERT = 16;
    private static final int FRAME_H_VERT = 41;

    public Player(int x, int y, int width, int height, Texture texture) {
        super(x, y, width, height, texture);

        Spritesheet spritesheet = new Spritesheet("/player.png");
        playerSheet = spritesheet.getTexture();

        // Direita (5 frames)
        runningRight = new float[5][];
        runningRight[0] = spritesheet.getUV(96,       14, 32, 34);
        runningRight[1] = spritesheet.getUV(96 + 32,  14, 32, 34);
        runningRight[2] = spritesheet.getUV(96 + 64,  14, 32, 34);
        runningRight[3] = spritesheet.getUV(96 + 96,  14, 32, 34);
        runningRight[4] = spritesheet.getUV(96 + 128, 14, 32, 34);

        // Esquerda (5 frames)
        runningLeft = new float[5][];
        runningLeft[0] = spritesheet.getUV(0,   62, 32, 34);
        runningLeft[1] = spritesheet.getUV(32,  62, 32, 34);
        runningLeft[2] = spritesheet.getUV(64,  62, 32, 34);
        runningLeft[3] = spritesheet.getUV(96,  62, 32, 34);
        runningLeft[4] = spritesheet.getUV(128, 62, 32, 34);

        // Baixo (2 frames)
        runningDown = new float[2][];
        runningDown[0] = spritesheet.getUV(0,  7, 16, 41);
        runningDown[1] = spritesheet.getUV(48, 7, 16, 41);

        // Cima (2 frames)
        runningUp = new float[2][];
        runningUp[0] = spritesheet.getUV(16, 7, 16, 41);
        runningUp[1] = spritesheet.getUV(32, 7, 16, 41);

        // Bullet
        Spritesheet bSheet = new Spritesheet("/bullet.png");
        bulletSheet = bSheet.getTexture();
        bulletUV = bSheet.getUV(0, 0, 16, 16);

        moved = false;
        pressedShoot = false;
        canShoot = true;
        curDirection = downDirection;
        speed = 3;
        maskx = 4;
        masky = 8;
        maskw = -20;
        maskh = -10;
    }

    @Override
    public void tick() {
        if (moved) {
            frames++;
            if (frames > maxFrames) {
                frames = 0;
                index++;
                if (index > maxIndex) index = 0;
            }
        }
        moveLogic();
        shootLogic();

        if (collisionBullet() || collisionEnemy()) {
            dead = true;
        }

        if (dead) {
            // Game.cutscene.runDeadScene();
        }
    }

    @Override
    public void render() {
        int scale = Game.SCALE;
        float[] uv;
        float w, h;

        if (moved) {
            switch (curDirection) {
                case 0: // right
                    uv = runningRight[index];
                    w = FRAME_W_SIDE * scale;
                    h = FRAME_H_SIDE * scale;
                    break;
                case 1: // left
                    uv = runningLeft[index];
                    w = FRAME_W_SIDE * scale;
                    h = FRAME_H_SIDE * scale;
                    break;
                case 2: // up
                    uv = runningUp[index % 2];
                    w = FRAME_W_VERT * scale;
                    h = FRAME_H_VERT * scale;
                    break;
                default: // down
                    uv = runningDown[index % 2];
                    w = FRAME_W_VERT * scale;
                    h = FRAME_H_VERT * scale;
                    break;
            }
        } else {
            // parado: frame idle (down)
            uv = runningDown[index % 2];
            w = FRAME_W_VERT * scale;
            h = FRAME_H_VERT * scale;
        }

        TextureRenderer.drawTextureRegion(playerSheet, x * scale, y * scale, w, h, uv);

        // hitbox debug (verde)
        if (Game.DEBUG) {
            TextureRenderer.drawRect(
                (x + maskx) * scale,
                (y + masky) * scale,
                (width  + maskw) * scale,
                (height + maskh) * scale,
                0f, 1f, 0f
            );
        }
    }

    // -------------------------------------------------------------------------

    private void shootLogic() {
        if (pressedShoot && canShoot) {
            canShoot = false;
            pressedShoot = false;
            Game.bullets.add(new Bullet(
                    this.getX() + this.width / 2 - 5, this.getY(),
                    16, 16, bulletSheet, bulletUV, 5, -1));
            lastShootTime = System.currentTimeMillis();
        } else if (!canShoot) {
            if (System.currentTimeMillis() - lastShootTime >= intervaloDeAtirar) {
                canShoot = true;
            }
            pressedShoot = false;
        }
    }

	private void moveLogic() {
		moved = false;
		if (right) {
			moved = true;
			curDirection = rightDirection;
			x += speed;
		}
		if (left) {
			moved = true;
			curDirection = leftDirection;
			x -= speed;
		}
		if (up) {
			moved = true;
			curDirection = upDirection;
			y -= speed;
		}
		if (down) {
			moved = true;
			curDirection = downDirection;
			y += speed;
		}

		if (x < 0)
			x = 0;
		if (x + width > Game.WIDTH)
			x = Game.WIDTH - width;
		if (y + height > Game.HEIGHT)
			y = Game.HEIGHT - height;
		if (y < 0)
			y = 0;
	}

    private boolean collisionBullet() {
        Rectangle curPlayer = new Rectangle(x + maskx, y + masky, width + maskw, height + maskh);
        for (EnemyBullet en : Game.enemyBullets) {
            Rectangle target = new Rectangle(en.x + en.maskx, en.y + en.masky,
                                             en.width + en.maskw, en.height + en.maskh);
            if (target.intersects(curPlayer)) return true;
        }
        return false;
    }

    private boolean collisionEnemy() {
        Rectangle curPlayer = new Rectangle(x + maskx, y + masky, width + maskw, height + maskh);
        for (Enemy en : Game.enemies) {
            if (en.died) continue;
            Rectangle targetEnemy = new Rectangle(en.x + en.damagemaskx, en.y + en.damagemasky,
                                                   en.width + en.damagemaskw, en.height + en.damagemaskh);
            if (targetEnemy.intersects(curPlayer)) return true;
        }
        return false;
    }

    public boolean isDead() { return dead; }
}
