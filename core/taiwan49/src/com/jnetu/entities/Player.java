package com.jnetu.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.jnetu.main.Spritesheet;
import com.jnetu.main.Game;

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

	private BufferedImage[] runningPlayer;
	private BufferedImage[] runningPlayerLeft;
	private BufferedImage[] runningUp;
	private BufferedImage[] runningDown;
	private BufferedImage bulletSprite;
	private int frames = 0, maxFrames = 5;
	private int index = 0, maxIndex = 4;

	private boolean dead = false;

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		runningPlayer = new BufferedImage[5];
		runningPlayerLeft = new BufferedImage[5];
		runningUp = new BufferedImage[2];
		runningDown = new BufferedImage[2];

		Spritesheet spritesheet = new Spritesheet("/player.png");
		runningPlayerLeft[0] = spritesheet.getSprite(0, 62, 32, 34);
		runningPlayerLeft[1] = spritesheet.getSprite(32, 62, 32, 34);
		runningPlayerLeft[2] = spritesheet.getSprite(64, 62, 32, 34);
		runningPlayerLeft[3] = spritesheet.getSprite(64 + 32, 62, 32, 34);
		runningPlayerLeft[4] = spritesheet.getSprite(128, 62, 32, 64);

		runningPlayer[0] = spritesheet.getSprite(96, 14, 32, 34);
		runningPlayer[1] = spritesheet.getSprite(96 + 32, 14, 32, 34);
		runningPlayer[2] = spritesheet.getSprite(96 + 64, 14, 32, 34);
		runningPlayer[3] = spritesheet.getSprite(96 + 64 + 32, 14, 32, 34);
		runningPlayer[4] = spritesheet.getSprite(96 + 128, 14, 32, 64);

		runningDown[0] = spritesheet.getSprite(0, 7, 16, 41);
		runningDown[1] = spritesheet.getSprite(48, 7, 16, 41);

		runningUp[0] = spritesheet.getSprite(16, 7, 16, 41);
		runningUp[1] = spritesheet.getSprite(32, 7, 16, 41);

		spritesheet = new Spritesheet("/bullet.png");
		bulletSprite = spritesheet.getSprite(0, 0, 16, 16);

		moved = false;
		right = false;
		left = false;
		pressedShoot = false;
		canShoot = true;
		curDirection = downDirection;
		speed = 3;
		maskx = 4;
		masky = 8;
		maskw = -20;
		maskh = -10;
	}

	public void tick() {

		if (moved) {
			frames++;
			if (frames > maxFrames) {
				frames = 0;
				index++;
				if (index > maxIndex) {
					index = 0;
				}
			}
		}
		moveLogic();
		shootLogic();

		if (collisionBullet() || collisionEnemy()) {
			dead = true;
		}

		if (dead) {
			Game.cutscene.runDeadScene();
		}

	}

	private boolean collisionBullet() {
		Rectangle curPlayer = new Rectangle(this.getX() + maskx, this.getY() + masky, this.getWidth() + maskw,
				this.getHeight() + maskh);
		for (int i = 0; i < Game.enemyBullets.size(); i++) {
			EnemyBullet en = Game.enemyBullets.get(i);

			Rectangle target = new Rectangle((int) en.x + maskx, (int) en.getY() + masky, en.width + en.maskw,
					en.height + en.maskh);

			if (target.intersects(curPlayer)) {

				return true;

			}

		}
		return false;
	}

	private boolean collisionEnemy() {
		Rectangle curPlayer = new Rectangle(this.getX() + maskx, this.getY() + masky, this.getWidth() + maskw,
				this.getHeight() + maskh);

		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy en = Game.enemies.get(i);
			if (en.died) {
				return false;
			}
			Rectangle targetEnemy = new Rectangle((int) en.x + en.damagemaskx, (int) en.getY() + en.damagemasky, en.width + en.damagemaskw,
					en.height + en.damagemaskh);

			if (targetEnemy.intersects(curPlayer)) {

				return true;

			}

		}
		return false;

	}

	public void render(Graphics g) {

		if (moved) {

			if (curDirection == rightDirection) {
				g.drawImage(runningPlayer[index], x, y, null);
			}
			if (curDirection == leftDirection) {
				g.drawImage(runningPlayerLeft[index], x, y, null);
			}
			if (curDirection == upDirection) {
				g.drawImage(runningUp[index % 2], x, y, null);
			}
			if (curDirection == downDirection) {
				g.drawImage(runningDown[index % 2], x, y, null);
			}
		} else {
			g.drawImage(runningDown[index % 2], x, y, null);
		}
		g.setColor(new Color(0, 255, 0));
		g.drawRect(this.getX() + maskx, this.getY() + masky, this.getWidth() + maskw, this.getHeight() + maskh);

	}

	private void shootLogic() {
		if (pressedShoot && canShoot) {
			canShoot = false;
			pressedShoot = false;
			Game.bullets.add(new Bullet(this.getX() + this.width / 2 - 5, this.getY(), 5, 5, bulletSprite, 5, -1));
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
		if (this.getX() < 0) {
			this.setX(0);
		}
		if (this.getX() + this.getWidth() > Game.WIDTH) {
			this.setX(Game.WIDTH - this.getWidth());
		}
		if (this.getY() + this.getHeight() > Game.HEIGHT) {
			this.setY(Game.HEIGHT - this.getHeight());
		}
		if (this.getY() < 0) {
			this.setY(0);
		}

	}

}
