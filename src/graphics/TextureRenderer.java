package graphics;

import static org.lwjgl.opengl.GL11.*;

import main.Texture;

public class TextureRenderer {

    public static void drawTexture(Texture texture, float x, float y, float width, float height) {
        drawTextureRegion(texture, x, y, width, height, 0, 0, 1, 1);
    }

    /**
     * Desenha uma região do spritesheet definida pelas coordenadas UV.
     * uv = { uMin, vMin, uMax, vMax }
     */
    public static void drawTextureRegion(Texture texture, float x, float y, float width, float height, float[] uv) {
        drawTextureRegion(texture, x, y, width, height, uv[0], uv[1], uv[2], uv[3]);
    }

    public static void drawTextureRegion(Texture texture, float x, float y, float width, float height,
                                          float uMin, float vMin, float uMax, float vMax) {
        glColor3f(1.0f, 1.0f, 1.0f);
        texture.bind();

        glBegin(GL_QUADS);
            glTexCoord2f(uMin, vMin); glVertex2f(x,         y);
            glTexCoord2f(uMax, vMin); glVertex2f(x + width, y);
            glTexCoord2f(uMax, vMax); glVertex2f(x + width, y + height);
            glTexCoord2f(uMin, vMax); glVertex2f(x,         y + height);
        glEnd();

        texture.unbind();
    }

    public static void fillRect(float x, float y, float width, float height, float r, float g, float b) {
        glDisable(GL_TEXTURE_2D);
        glColor3f(r, g, b);

        glBegin(GL_QUADS);
            glVertex2f(x,         y);
            glVertex2f(x + width, y);
            glVertex2f(x + width, y + height);
            glVertex2f(x,         y + height);
        glEnd();

        glEnable(GL_TEXTURE_2D);
    }

    /** Retângulo com alpha — útil para fade overlay. */
    public static void fillRectAlpha(float x, float y, float width, float height, float r, float g, float b, float a) {
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glColor4f(r, g, b, a);

        glBegin(GL_QUADS);
            glVertex2f(x,         y);
            glVertex2f(x + width, y);
            glVertex2f(x + width, y + height);
            glVertex2f(x,         y + height);
        glEnd();

        glEnable(GL_TEXTURE_2D);
    }

    public static void drawRect(float x, float y, float width, float height, float r, float g, float b) {
        glDisable(GL_TEXTURE_2D);
        glColor3f(r, g, b);

        glBegin(GL_LINE_LOOP);
            glVertex2f(x,         y);
            glVertex2f(x + width, y);
            glVertex2f(x + width, y + height);
            glVertex2f(x,         y + height);
        glEnd();

        glEnable(GL_TEXTURE_2D);
    }
}
