package main;

import org.lwjgl.opengl.GL;

import entities.Bullet;
import entities.Enemy;
import entities.EnemyBullet;
import entities.Player;
import entities.Spawner;
import graphics.TextRenderer;
import graphics.TextureRenderer;
import graphics.UnicodeFontRenderer;
import util.WindowUtil;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {

	private long window;
	private Texture playerTexture;

	public static Player player;
	public static Spawner spawner;
	public static Menu menu;
	public static Cutscene cutscene;
	public static UnicodeFontRenderer font;

	// window configs
	public static final int WIDTH = 300;
	public static final int HEIGHT = 256;
	public final static int SCALE = 3;
	private static final String TITLE = "Taiwan 49";

	// personal configs
	public boolean vsync = true;
	public static double targetFPS = 60;
	public static double last_targetFPS = 60;
	public final static double TARGET_TPS = 60.0;

	// debug
	public static boolean DEBUG = true;
	private int showFPS = 0;
	private int showTPS = 0;
	private String debugInfo = "FPS: 0 | TPS: 0 | V-Sync: ON";

	// entity lists
	public static List<Bullet> bullets;
	public static List<EnemyBullet> enemyBullets;
	public static List<Enemy> enemies;

	// game state
	public static String state = "MENU";
	public static int score = 0;
	public static boolean restarted = false;

	public Random random;

	// ─────────────────────────────────────────────────────────────────────────

	public static void main(String[] args) {
		new Game().start();
	}

	public void start() {
		if (DEBUG)
			System.out.println("Iniciando LWJGL " + org.lwjgl.Version.getVersion());
		init();
		run();
		dispose();
	}

	public void init() {
		org.lwjgl.glfw.GLFWErrorCallback.createPrint(System.err).set();

		if (!glfwInit())
			throw new IllegalStateException("Falha ao inicializar GLFW");

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

		window = glfwCreateWindow(WIDTH * SCALE, HEIGHT * SCALE, TITLE, NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Falha ao criar janela GLFW");

		glfwSetFramebufferSizeCallback(window, (win, w, h) -> WindowUtil.resize(w, h, WIDTH, HEIGHT));
		WindowUtil.glfwCenterWindow(window);
		glfwMakeContextCurrent(window);
		WindowUtil.setVsync(vsync);
		glfwShowWindow(window);

		GL.createCapabilities();

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_TEXTURE_2D);

		// ── Textures ──────────────────────────────────────────────────────────
		playerTexture = new Texture("/player.png");

		// ── Font Unicode (coloque a fonte em res/fonts/NotoSansSC-Regular.ttf) ─
		font = new UnicodeFontRenderer("/NotoSansSC-Regular.ttf", 16f);

		// ── Entity lists ──────────────────────────────────────────────────────
		bullets = new ArrayList<>();
		enemyBullets = new ArrayList<>();
		enemies = new ArrayList<>();

		// ── Instances ─────────────────────────────────────────────────────────
		menu = new Menu(font);
		cutscene = new Cutscene(font);
		player = new Player(WIDTH / 2 - 8, HEIGHT / 2 - 20, 16, 41, playerTexture);
		spawner = new Spawner();

		// ── Camera pixel-perfect ──────────────────────────────────────────────
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, WIDTH * SCALE, HEIGHT * SCALE, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);

		glClearColor(0f, 0f, 0f, 1f);
	}

	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1_000_000_000.0 / TARGET_TPS;
		double delta = 0;

		long lastRenderTime = System.nanoTime();
		int frames = 0, ticks = 0;
		long timer = System.currentTimeMillis();

		while (!glfwWindowShouldClose(window)) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;

			while (delta >= 1) {
				tick();
				ticks++;
				delta--;
				if (delta > 100) {
					delta = 0;
					break;
				}
			}

			if (vsync) {
				render();
				frames++;
			} else if (targetFPS > 0) {
				double nsPerFrame = 1_000_000_000.0 / targetFPS;
				long nextRender = lastRenderTime + (long) nsPerFrame;
				long now_t = System.nanoTime();
				if (now_t < nextRender) {
					while (now_t < nextRender) {
						long rem = nextRender - now_t;
						if (rem > 1_500_000) {
							try {
								Thread.sleep(1);
							} catch (InterruptedException ignored) {
							}
						} else {
							Thread.onSpinWait();
						}
						now_t = System.nanoTime();
					}
				}
				render();
				frames++;
				lastRenderTime = now_t;
			} else {
				render();
				frames++;
			}

			glfwPollEvents();

			if (System.currentTimeMillis() - timer >= 1000) {
				showFPS = frames;
				showTPS = ticks;
				debugInfo = String.format("FPS: %d | TPS: %d | V-Sync: %s", showFPS, showTPS, vsync ? "ON" : "OFF");
				if (DEBUG)
					System.out.println(debugInfo);
				frames = 0;
				ticks = 0;
				timer += 1000;
			}
		}
	}

	public void tick() {
		Input.TickInput(this);

		switch (state) {
		case "RUN" -> {
			if (player != null)
				player.tick();
			spawner.tick();
			for (int i = enemies.size() - 1; i >= 0; i--)
				enemies.get(i).tick();
			for (int i = bullets.size() - 1; i >= 0; i--)
				bullets.get(i).tick();
			for (int i = enemyBullets.size() - 1; i >= 0; i--)
				enemyBullets.get(i).tick();
		}
		case "MENU" -> menu.tick();
		case "CUTSCENE" -> cutscene.tick();
		}
	}

	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		switch (state) {
		case "RUN" -> {
			TextureRenderer.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE, 0.2f, 0.2f, 0.2f);
			player.render();
			for (int i = 0; i < enemies.size(); i++)
				enemies.get(i).render();
			for (int i = 0; i < bullets.size(); i++)
				bullets.get(i).render();
			for (int i = 0; i < enemyBullets.size(); i++)
				enemyBullets.get(i).render();

			// HUD debug (Latin puro — TextRenderer ainda funciona aqui)
			if (DEBUG) {
				TextRenderer.drawText(debugInfo, 10, 10, 1.7f, 1f, 1f, 1f);
				TextRenderer.drawText("'V' - V-Sync Toggle", 10, 30, 1.4f, 0.8f, 0.8f, 0.8f);
			}
		}
		case "MENU" -> menu.render();
		case "CUTSCENE" -> cutscene.render();
		}

		glfwSwapBuffers(window);
	}

	private void dispose() {
		font.destroy();
		org.lwjgl.glfw.Callbacks.glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	public long getWindow() {
		return window;
	}

	public boolean getVsync() {
		return vsync;
	}

	public int getShowFPS() {
		return showFPS;
	}

	public int getShowTPS() {
		return showTPS;
	}

	public void setVsync(boolean v) {
		this.vsync = v;
	}

	public void setDebugInfo() {
		debugInfo = String.format("FPS: %d | TPS: %d | V-Sync: %s", showFPS, showTPS, vsync ? "ON" : "OFF");
	}
}
