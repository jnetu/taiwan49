package main;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F1;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F2;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_V;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
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

		// PLAYER MOVEMENT
		if (Game.player != null) {
			Game.player.right = glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS
					|| glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS;
			Game.player.left = glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS
					|| glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS;
			Game.player.up = glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS
					|| glfwGetKey(window, GLFW_KEY_UP) == GLFW_PRESS;
			Game.player.down = glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS
					|| glfwGetKey(window, GLFW_KEY_DOWN) == GLFW_PRESS;

			if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS) {
				Game.player.pressedShoot = true;
			}
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
