package entities;

import graphics.TextureRenderer;
import main.Game;
import main.Texture;

public class Bullet extends Entity {

    public int dy;
    public double speed;
    protected Texture sheet;
    protected float[] uv;

    public Bullet(int x, int y, int width, int height, Texture sheet, float[] uv, double speed, int dy) {
        super(x, y, width, height, sheet);
        this.sheet = sheet;
        this.uv    = uv;
        this.speed = speed;
        this.dy    = dy;
        maskx = 0; masky = 0; maskw = 0; maskh = 0;
    }

    @Override
    public void tick() {
        y += (int) (dy * speed);
        if (y < 0) Game.bullets.remove(this);
    }

    @Override
    public void render() {
        int scale = Game.SCALE;
        TextureRenderer.drawTextureRegion(sheet,
                x * scale, y * scale,
                width * scale, height * scale,
                uv);
    }
}
