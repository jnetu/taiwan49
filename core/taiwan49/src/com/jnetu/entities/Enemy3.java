package com.jnetu.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.jnetu.main.Game;
import com.jnetu.main.Spritesheet;

public class Enemy3 extends Enemy {
	private BufferedImage[] enemy;
	private BufferedImage[] deadEnemy;
	private BufferedImage enemyBullet;

	private int frames = 0, maxFrames = 5;
	private int index = 0, maxIndex = 1;

	private boolean died = false, readToRemove = false;
	private int diedFrames = 0, maxDiedFrames = 20;
	private int diedIndex = 0, maxDiedIndex = 1;

	private long timeDead;
	private final int timeToDespawn = 1500;

	private long lastShootTime;
	private boolean canShoot = false, wannaShoot = false;
	private long intervaloShoot = 1000;
	
	public int damagemaskx,damagemasky,damagemaskw,damagemaskh;

	public Enemy3(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		speed = 1.0;
		enemy = new BufferedImage[2];
		deadEnemy = new BufferedImage[2];
		Spritesheet spritesheet = new Spritesheet("/enemy3.png");
		Spritesheet spriteExplosion = new Spritesheet("/deadexplosion.png");
		Spritesheet spriteDead = new Spritesheet("/deadlastframe.png");
		Spritesheet spriteBullet = new Spritesheet("/bullet.png");
		enemy[0] = spritesheet.getSprite(0, 0, 32, 48);
		enemy[1] = spritesheet.getSprite(32, 0, 32, 48);
		deadEnemy[0] = spriteExplosion.getSprite(0, 0, 95, 118);
		deadEnemy[1] = spriteDead.getSprite(0, 0, 256, 256);

		enemyBullet = spriteBullet.getSprite(16, 0, 16, 16);
//		maskx = -4;
//		masky = 0;
//		maskw = 20;
//		maskh = 32;
		maskx = -4;
		masky = 0;
		maskw = 0;
		maskh = 0;
		damagemaskx = 12;
		damagemasky = 10;
		damagemaskw = -24;
		damagemaskh = -16;

	}

	public void tick() {
		maskx = 0;
		masky = 0;
		maskw = 0;
		maskh = 0;
		if (!died) {
			if (x < Game.player.getX() - 10 && !haveCollision(x + speed, y)) {
				x += speed;
			} else if (x > Game.player.getX() - 8 && !haveCollision(x - speed, y)) {
				x -= speed;
			} else {
				wannaShoot = true;
			}

			if (y < Game.player.getY() - this.getHeight() - 20 && !haveCollision(x, y + speed)) {
				y += speed;
			} else if (y > Game.player.getY()) {
				y += speed;

			}
			if (wannaShoot && canShoot) {
				canShoot = false;
				wannaShoot = false;
				Game.enemyBullets
						.add(new EnemyBullet(this.getX() + this.width / 2 - 5, this.getY(), 5, 5, enemyBullet, 5, 1));
				lastShootTime = System.currentTimeMillis();
			} else if (!canShoot) {
				if (System.currentTimeMillis() - lastShootTime >= intervaloShoot) {
					canShoot = true;
				}
				wannaShoot = false;
			}

			if (verifyBulletColision()) {
				died = true;
				Game.score+=50;
			}
		}

		if (x > Game.WIDTH - this.getWidth() * 2) {
			x = Game.WIDTH - this.getWidth() * 2;
		}
		if (x < 0 + this.getWidth()) {
			x = 0 + this.getWidth();
		}
		if(y > Game.HEIGHT) {
			Game.enemies.remove(this);
		}

		frames++;
		if (frames > maxFrames) {
			frames = 0;
			index++;
			if (index > maxIndex) {
				index = 0;
			}
		}

		if (died && !readToRemove) {
			timeDead = System.currentTimeMillis();
			readToRemove = true;
		} else if (readToRemove) {
			if (System.currentTimeMillis() - timeDead >= timeToDespawn) {
				Game.enemies.remove(this);
			}
		}

		if (died) {
			diedFrames++;
			if (diedFrames > maxDiedFrames) {
				diedIndex++;
				if (diedIndex > maxDiedIndex) {
					diedIndex = maxDiedIndex;
				}
			}
		}

	}

	public void render(Graphics g) {
		g.setColor(new Color(255, 0, 0));

		g.drawRect(x + maskx, y + masky, width + maskw, height + maskh);

		if (!died) {
			g.drawImage(enemy[index], x, y, null);
		} else {
			g.drawImage(deadEnemy[diedIndex], x, y, 48, 48, null);
		}
	}

	private boolean verifyBulletColision() {
		for (int i = 0; i < Game.bullets.size(); i++) {
			Bullet b = Game.bullets.get(i);
			Rectangle r1 = new Rectangle(b.getX() + b.maskx, b.getY() + b.masky, b.getWidth() + b.maskw,
					b.getHeight() + b.maskh);
			Rectangle r2 = new Rectangle(x + maskx, y + masky, width + maskw, height + maskh);
			if (r1.intersects(r2)) {
				Game.bullets.remove(i);
				return true;
			}

		}
		return false;
	}

	public boolean haveCollision(double d, double e) {
		Rectangle curEnemy = new Rectangle((int) d + maskx, (int) e + masky,width + maskw,height + maskh);
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy en = Game.enemies.get(i);
			if (en == this) {

				continue;
			}
			if (en instanceof Enemy3) {
				Rectangle targetEnemy = new Rectangle( en.getX() + maskx, en.getY() + masky, width + maskw, height + maskh);
				if (targetEnemy.intersects(curEnemy)) {

					return true;

				}
			}

		}
		return false;
	}

}
