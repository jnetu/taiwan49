package util;

import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryStack.stackPush;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.io.IOException;

import main.Game;
import main.Texture;

public class WindowUtil {
	public static void glfwCenterWindow(long window) {
		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);

			glfwGetWindowSize(window, pWidth, pHeight);
			org.lwjgl.glfw.GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			if (vidmode != null) {
				glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2,
						(vidmode.height() - pHeight.get(0)) / 2);
			}
		}
	}

	public static void setVsync(boolean enabled) {
		if (enabled) {
			glfwSwapInterval(1); // V-Sync ON
		} else {
			glfwSwapInterval(0); // ALL FPS
		}
		if (Game.DEBUG)
			System.out.println("V-Sync: " + (enabled ? "Ligado" : "Desligado"));
	}

	public static void resize(int windowWidth, int windowHeight, int WIDTH, int HEIGHT) {
		float targetAspect = (float) WIDTH / (float) HEIGHT;
		float windowAspect = (float) windowWidth / (float) windowHeight;

		int viewWidth, viewHeight;
		int viewX, viewY;

		if (windowAspect >= targetAspect) {
			// too wide
			viewHeight = windowHeight;
			viewWidth = (int) (windowHeight * targetAspect);
			viewY = 0;
			viewX = (windowWidth - viewWidth) / 2; // Centraliza horizontalmente
		} else {
			// too high
			viewWidth = windowWidth;
			viewHeight = (int) (windowWidth / targetAspect);
			viewX = 0;
			viewY = (windowHeight - viewHeight) / 2; // Centraliza verticalmente
		}
		glViewport(viewX, viewY, viewWidth, viewHeight);
	}

	// value<0 - decrease
	// value>0 - increase
	public static void modifyTargetFPS(int value) {

		Game.targetFPS = Game.targetFPS + value;
		if (Game.targetFPS < 0) {
			Game.targetFPS = 0;
		}
	}

	public static void setWindowIcon(long window, String path) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer comp = stack.mallocInt(1);

			ByteBuffer imageBuffer;
			try {
				imageBuffer = Texture.ioResourceToByteBuffer(path, 4096);
			} catch (IOException e) {
				System.err.println("Ícone não encontrado no caminho: " + path);
				e.printStackTrace();
				return;
			}

			ByteBuffer rawImage = STBImage.stbi_load_from_memory(imageBuffer, w, h, comp, 4);
			if (rawImage == null) {
				System.err.println("Falha ao decodificar ícone via STB: " + STBImage.stbi_failure_reason());
				return;
			}

			GLFWImage.Buffer iconBuffer = GLFWImage.malloc(1, stack);

			iconBuffer.position(0);
			iconBuffer.width(w.get(0));
			iconBuffer.height(h.get(0));
			iconBuffer.pixels(rawImage);

			// Define o icone
			GLFW.glfwSetWindowIcon(window, iconBuffer);
			STBImage.stbi_image_free(rawImage);
		}
	}
}
