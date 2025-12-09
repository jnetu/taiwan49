package graphics;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.stb.STBEasyFont.stb_easy_font_print;
import static org.lwjgl.system.MemoryStack.stackPush;

import java.nio.ByteBuffer;

import org.lwjgl.system.MemoryStack;

public class TextRenderer {
	public static void drawText(String text, float x, float y, float scale, float r, float g, float b) {
		try (MemoryStack stack = stackPush()) {

			// default buffer - 270
			ByteBuffer charBuffer = stack.malloc(text.length() * 270);
			// texts quads
			int quads = stb_easy_font_print(0, 0, text, null, charBuffer);

			glEnableClientState(GL_VERTEX_ARRAY);
			glVertexPointer(2, GL_FLOAT, 16, charBuffer);

			glPushMatrix();
			// Posiciona e escala o texto
			glTranslatef(x, y, 0);
			glScalef(scale, scale, 1f);

			glColor3f(r, g, b);

			// final draw
			glDrawArrays(GL_QUADS, 0, quads * 4);
			glPopMatrix();

			glDisableClientState(GL_VERTEX_ARRAY);
		}
	}
}
