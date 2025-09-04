package com.jnetu.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.jnetu.main.Game;


public class Entity {
	
	public int x;
	public int y;
	public int width;
	public int height;
	public int maskx, masky, maskw, maskh;
	private BufferedImage sprite;
	
	public Entity(int d, int e, int width, int height, BufferedImage sprite) {
		this.x = d;
		this.y = e;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		this.maskx = 0;
		this.masky = 0;
		this.maskh = height;
		this.maskw = width;
	}
	public void render(Graphics g) {
		g.drawImage(sprite,this.getX(), this.getY(),null);
	}
	public void tick() {
		
	}
	
	public boolean isCollinding(Entity en1, Entity en2) {
		Rectangle r1 = new Rectangle((en1.x + en1.maskx),(en1.y + en1.masky), en1.width+ en1.maskw, en1.height + en1.maskh);
		Rectangle r2 = new Rectangle((en2.x + en2.maskx),(en2.y + en2.masky), en2.width + en2.maskw, en2.height + en2.maskh);
		return r1.intersects(r2);
	}
	

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
