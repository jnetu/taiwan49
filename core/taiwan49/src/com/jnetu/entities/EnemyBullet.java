package com.jnetu.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.jnetu.main.Game;

public class EnemyBullet extends Bullet{
	public int dx, dy;
	public double speed;
	private BufferedImage sprite;

	public EnemyBullet(int x, int y, int width, int height, BufferedImage sprite, double speed, int dy) {
		super(x, y, width, height, sprite, speed, dy);
		this.speed = speed;
		this.dy = dy;
		this.sprite = sprite;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		maskx = 5;
		masky = 0;
		maskw = 0;
		maskh = 0;
		speed = 0.1;
	}

	public void tick() {
		
		y += dy * speed;
		if (y > Game.HEIGHT) {
			Game.bullets.remove(this);
		}
	}

	public void render(Graphics g) {
		//g.setColor(Color.WHITE);
		//g.fillRect(x+ maskx, y+ masky, width + maskw, height + maskh);
		g.drawImage(sprite, x, y, null);
	}
}
