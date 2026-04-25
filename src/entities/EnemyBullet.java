package entities;

import graphics.TextureRenderer;
import main.Game;
import main.Texture;

public class EnemyBullet extends Bullet {

	public EnemyBullet(int x, int y, int width, int height, Texture sheet, float[] uv, double speed, int dy) {
		super(x, y, width, height, sheet, uv, speed, dy);
		maskx = 5;
		masky = 0;
		maskw = 0;
		maskh = 0;
	}

    @Override
    public void tick() {
        y += (int) (dy * speed);
        if (y > Game.HEIGHT) Game.enemyBullets.remove(this);
    }
    // render() herdado do Bullet funciona direto
    
    public void render() {
        int scale = Game.SCALE;
        TextureRenderer.drawTextureRegion(sheet,
                x * scale, y * scale,
                width * scale, height * scale,
                uv);
    }
}
