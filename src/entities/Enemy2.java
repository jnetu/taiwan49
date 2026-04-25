package entities;

import java.awt.Rectangle;

import main.Game;
import main.Texture;

public class Enemy2 extends Enemy {

    private double degree = 0;
    private double amplitude = 4.2;

    public Enemy2(int x, int y, int width, int height, Texture texture) {
        super(x, y, width, height, texture);
        loadSprites("/enemy2.png"); // substitui o sheet carregado pelo Enemy base
        speed = 1.0;
        maskx = -4; masky = 0; maskw = 0; maskh = 0;
        damagemaskx = 12; damagemasky = 10; damagemaskw = -24; damagemaskh = -16;
    }

    @Override
    public void tick() {
        if (!died) {
            y += (int) speed;
            x = (int) (x + Math.sin(degree * Math.PI / 180) * amplitude);
            degree += 5;

            if (x > Game.WIDTH - this.getWidth() * 2) x = Game.WIDTH - this.getWidth() * 2;
            if (x < this.getWidth())                  x = this.getWidth();

            if (verifyBulletCollision()) {
                died = true;
                Game.score += 30;
            }
        }

        if (this.getY() > Game.HEIGHT) {
            Game.enemies.remove(this);
            return;
        }

        tickAnimation();
        tickDeath();
    }
}
