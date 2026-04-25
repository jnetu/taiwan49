package entities;

import graphics.TextureRenderer;
import main.Texture;

public class Entity {

    public int x;
    public int y;
    public int width;
    public int height;
    public int maskx, masky, maskw, maskh;
    private Texture texture;

    public Entity(int x, int y, int width, int height, Texture texture) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.texture = texture;
        this.maskx = 0;
        this.masky = 0;
        this.maskh = height;
        this.maskw = width;
    }

    public void render() {
        TextureRenderer.drawTexture(texture, x, y, width, height);
    }

    public void tick() {
    }

    public boolean isCollinding(Entity en1, Entity en2) {
        java.awt.Rectangle r1 = new java.awt.Rectangle(
                (en1.x + en1.maskx), (en1.y + en1.masky),
                en1.width + en1.maskw, en1.height + en1.maskh);
        java.awt.Rectangle r2 = new java.awt.Rectangle(
                (en2.x + en2.maskx), (en2.y + en2.masky),
                en2.width + en2.maskw, en2.height + en2.maskh);
        return r1.intersects(r2);
    }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }
    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public Texture getTexture() { return texture; }
}

