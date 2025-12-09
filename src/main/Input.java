package main;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F1;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F2;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_V;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

import util.WindowUtil;

public class Input {

	private static long lastVsyncTime = 0;
	private static long lastFpsTime = 0;

	public static void TickInput(Game game) {
		long window = game.getWindow();
		long now = System.currentTimeMillis();

		// EXIT
		if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS) {
			glfwSetWindowShouldClose(window, true);
		}

		// VSYNC
		if (glfwGetKey(window, GLFW_KEY_V) == GLFW_PRESS) {
			if (now - lastVsyncTime > 200) {

				boolean vsync = !game.getVsync();
				game.setVsync(vsync);
				WindowUtil.setVsync(vsync);
				game.setDebugInfo();
				lastVsyncTime = now;
			}

		}

		if (glfwGetKey(window, GLFW_KEY_F1) == GLFW_PRESS) {
			if (now - lastFpsTime > 100) {
				WindowUtil.modifyTargetFPS(10);
			}
		}
		if (glfwGetKey(window, GLFW_KEY_F2) == GLFW_PRESS) {
			if (now - lastFpsTime > 100) {
				WindowUtil.modifyTargetFPS(-10);
			}
		}
	}
}
