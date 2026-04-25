package main;

import static org.lwjgl.glfw.GLFW.*;

import util.WindowUtil;

public class Input {

	private static long lastVsyncTime = 0;
	private static long lastFpsTime = 0;
	private static long lastMenuTime = 0;
	private static long lastEnterTime = 0;
	private static long lastSkipTime = 0;

	// Debounce mínimo para teclas de navegação (ms)
	private static final long DEBOUNCE = 180;

	public static void TickInput(Game game) {
		long window = game.getWindow();
		long now = System.currentTimeMillis();

		// ── Sair ─────────────────────────────────────────────────────────────
		if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS) {
			glfwSetWindowShouldClose(window, true);
		}

		// ── Menu ─────────────────────────────────────────────────────────────
		if (Game.state.equals("MENU") && Game.menu != null) {
			if (now - lastMenuTime > DEBOUNCE) {
				if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS || glfwGetKey(window, GLFW_KEY_UP) == GLFW_PRESS) {
					Game.menu.up = true;
					lastMenuTime = now;
				}
				if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS || glfwGetKey(window, GLFW_KEY_DOWN) == GLFW_PRESS) {
					Game.menu.down = true;
					lastMenuTime = now;
				}
			}
			if (now - lastEnterTime > DEBOUNCE) {
				if (glfwGetKey(window, GLFW_KEY_ENTER) == GLFW_PRESS) {
					Game.menu.enter = true;
					lastEnterTime = now;
				}
			}
		}

		// ── Cutscene ─────────────────────────────────────────────────────────
		if (Game.state.equals("CUTSCENE") && Game.cutscene != null) {
			if (now - lastSkipTime > DEBOUNCE) {
				if (glfwGetKey(window, GLFW_KEY_ENTER) == GLFW_PRESS) {
					Game.cutscene.pressedSkip = true;
					lastSkipTime = now;
				}
			}
		}

		// ── Jogo ─────────────────────────────────────────────────────────────
		if (Game.state.equals("RUN") && Game.player != null) {
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

		// ── V-Sync toggle ────────────────────────────────────────────────────
		if (glfwGetKey(window, GLFW_KEY_V) == GLFW_PRESS && now - lastVsyncTime > 200) {
			boolean vsync = !game.getVsync();
			game.setVsync(vsync);
			WindowUtil.setVsync(vsync);
			game.setDebugInfo();
			lastVsyncTime = now;
		}

		// ── FPS manual ───────────────────────────────────────────────────────
		if (glfwGetKey(window, GLFW_KEY_F1) == GLFW_PRESS && now - lastFpsTime > 100) {
			WindowUtil.modifyTargetFPS(10);
			lastFpsTime = now;
		}
		if (glfwGetKey(window, GLFW_KEY_F2) == GLFW_PRESS && now - lastFpsTime > 100) {
			WindowUtil.modifyTargetFPS(-10);
			lastFpsTime = now;
		}
	}
}
