package main;

public class Spritesheet {

    private Texture texture;
    private int sheetWidth;
    private int sheetHeight;

    public Spritesheet(String path) {
        texture = new Texture(path);
        sheetWidth = texture.getWidth();
        sheetHeight = texture.getHeight();
    }

    /**
     * Retorna as coordenadas UV [uMin, vMin, uMax, vMax] de um frame do spritesheet.
     * Os parâmetros x, y, width, height são em pixels, igual ao getSubimage() antigo.
     */
    public float[] getUV(int x, int y, int width, int height) {
        float uMin = (float) x / sheetWidth;
        float vMin = (float) y / sheetHeight;
        float uMax = (float) (x + width) / sheetWidth;
        float vMax = (float) (y + height) / sheetHeight;
        return new float[]{uMin, vMin, uMax, vMax};
    }

    public Texture getTexture() {
        return texture;
    }
}
