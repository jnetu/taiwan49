// @encoding UTF-8
package graphics;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.system.MemoryStack.stackPush;

/**
 * Renderizador de texto Unicode (Latin, CJK, etc.) via STB TrueType.
 *
 * Aspecto pixel-art: fonte assada em tamanho pequeno (ex. 8px) + GL_NEAREST +
 * scale alto no drawText. Resultado: texto blocado e nítido, sem suavização.
 *
 * Uso:
 *   font = new UnicodeFontRenderer("/NotoSansSC-Regular.ttf", 8f);
 *   font.drawText("台灣 Hello", x, y, 3f, 1f, 1f, 1f);
 */
public class UnicodeFontRenderer {

    private static final int ATLAS_W = 2048;
    private static final int ATLAS_H = 2048;

    // Ranges Unicode assados no atlas — adicione mais se precisar
    private static final int[][] RANGES = {
        { 0x0020, 0x00FF },  // Basic Latin + Latin-1
        { 0x3000, 0x303F },  // CJK Punctuation
        { 0x3040, 0x309F },  // Hiragana
        { 0x30A0, 0x30FF },  // Katakana
        { 0x4E00, 0x9FFF },  // CJK Unified Ideographs
        { 0xFF00, 0xFFEF },  // Fullwidth Forms
    };

    private int   atlasTextureId;
    private float fontSize;

    private final Map<Integer, STBTTPackedchar.Buffer> rangeBuffers = new HashMap<>();

    public UnicodeFontRenderer(String fontPath, float fontSize) {
        this.fontSize = fontSize;
        load(fontPath);
    }

    // ─────────────────────────────────────────────────────────────────────────

    private void load(String path) {
        ByteBuffer fontData = readResource(path);
        ByteBuffer bitmap   = BufferUtils.createByteBuffer(ATLAS_W * ATLAS_H);

        try (STBTTPackContext pc = STBTTPackContext.malloc()) {
            stbtt_PackBegin(pc, bitmap, ATLAS_W, ATLAS_H, 0, 1);
            // SEM oversampling para manter o visual pixel-art
            stbtt_PackSetOversampling(pc, 1, 1);

            for (int[] r : RANGES) {
                int count = r[1] - r[0] + 1;
                STBTTPackedchar.Buffer buf = STBTTPackedchar.malloc(count);
                stbtt_PackFontRange(pc, fontData, 0, fontSize, r[0], buf);
                rangeBuffers.put(r[0], buf);
            }

            stbtt_PackEnd(pc);
        }

        atlasTextureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, atlasTextureId);
        // GL_NEAREST = visual pixelado, sem interpolação
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, ATLAS_W, ATLAS_H, 0, GL_ALPHA, GL_UNSIGNED_BYTE, bitmap);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    // ─────────────────────────────────────────────────────────────────────────

    /**
     * @param text  qualquer String UTF-16 Java (CJK, Latin, símbolos...)
     * @param x, y  posição em pixels de tela (coordenada OpenGL)
     * @param scale fator de escala — use valores altos (3-5) para pixel-art
     * @param r,g,b cor [0..1]
     */
    public void drawText(String text, float x, float y, float scale, float r, float g, float b) {
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, atlasTextureId);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glColor3f(r, g, b);

        try (MemoryStack stack = stackPush()) {
            FloatBuffer xb = stack.floats(0f);
            FloatBuffer yb = stack.floats(0f);
            STBTTAlignedQuad q = STBTTAlignedQuad.malloc(stack);

            glPushMatrix();
            glTranslatef(x, y + fontSize * scale, 0f);
            glScalef(scale, scale, 1f);

            glBegin(GL_QUADS);
            for (int i = 0; i < text.length(); ) {
                int cp = text.codePointAt(i);
                i += Character.charCount(cp);

                if (cp == '\n') {
                    xb.put(0, 0f);
                    yb.put(0, yb.get(0) + fontSize);
                    continue;
                }

                int[] range = findRange(cp);
                if (range == null) continue;

                stbtt_GetPackedQuad(rangeBuffers.get(range[0]),
                        ATLAS_W, ATLAS_H, cp - range[0], xb, yb, q, false);

                glTexCoord2f(q.s0(), q.t0()); glVertex2f(q.x0(), q.y0());
                glTexCoord2f(q.s1(), q.t0()); glVertex2f(q.x1(), q.y0());
                glTexCoord2f(q.s1(), q.t1()); glVertex2f(q.x1(), q.y1());
                glTexCoord2f(q.s0(), q.t1()); glVertex2f(q.x0(), q.y1());
            }
            glEnd();
            glPopMatrix();
        }

        glBindTexture(GL_TEXTURE_2D, 0);
    }

    // ─────────────────────────────────────────────────────────────────────────

    private int[] findRange(int cp) {
        for (int[] r : RANGES) {
            if (cp >= r[0] && cp <= r[1]) return r;
        }
        return null;
    }

    private static ByteBuffer readResource(String path) {
        try (InputStream is = UnicodeFontRenderer.class.getResourceAsStream(path)) {
            if (is == null) throw new RuntimeException("Fonte não encontrada: " + path);
            try (ReadableByteChannel rbc = Channels.newChannel(is)) {
                ByteBuffer buf = BufferUtils.createByteBuffer(1024 * 1024);
                while (true) {
                    int n = rbc.read(buf);
                    if (n == -1) break;
                    if (buf.remaining() == 0) {
                        ByteBuffer b2 = BufferUtils.createByteBuffer(buf.capacity() * 2);
                        buf.flip(); b2.put(buf); buf = b2;
                    }
                }
                buf.flip();
                return buf;
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler fonte: " + path, e);
        }
    }

    public void destroy() {
        glDeleteTextures(atlasTextureId);
        rangeBuffers.values().forEach(STBTTPackedchar.Buffer::free);
        rangeBuffers.clear();
    }
}
