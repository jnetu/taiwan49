package com.jnetu.entities;


import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.jnetu.main.Game;
import com.jnetu.main.Spritesheet;

public class Enemy extends Entity {
	public double speed;

	private BufferedImage[] enemy;
	private BufferedImage[] deadEnemy;
	private int frames = 0, maxFrames = 5;
	private int index = 0, maxIndex = 1;
	public boolean died = false, readToRemove = false;
	private int diedFrames = 0, maxDiedFrames = 20;
	private int diedIndex = 0, maxDiedIndex = 1;

	private long timeDead;
	private final int timeToDespawn = 1500;

	public int damagemaskx, damagemasky, damagemaskw, damagemaskh;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		speed = 1.2;
		enemy = new BufferedImage[2];
		deadEnemy = new BufferedImage[2];
		Spritesheet spritesheet = new Spritesheet("/enemy1.png");
		Spritesheet spriteExplosion = new Spritesheet("/deadexplosion.png");
		Spritesheet spriteDead = new Spritesheet("/deadlastframe.png");
		enemy[0] = spritesheet.getSprite(0, 0, 32, 48);
		enemy[1] = spritesheet.getSprite(32, 0, 32, 48);
		deadEnemy[0] = spriteExplosion.getSprite(0, 0, 95, 118);
		deadEnemy[1] = spriteDead.getSprite(0, 0, 256, 256);
		this.width = 32;
		this.height = 48;
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

		if (!died) {

			y += speed;
			if (verifyBulletColision()) {
				died = true;
				Game.score += 10;
			}
		}

		if (this.getY() > Game.HEIGHT) {
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
//		g.setColor(new Color(255, 0, 0));
//
//		g.drawRect(x + maskx, y + masky, width + maskw, height + maskh);
//		
//		g.setColor(Color.BLUE);
//		g.drawRect(getX() + damagemaskx, getY() + damagemasky, 
//				width + damagemaskw, height + damagemaskh);
//		

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

}
