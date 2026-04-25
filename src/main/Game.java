package main;

import org.lwjgl.opengl.GL;

import entities.Bullet;
import entities.Enemy;
import entities.EnemyBullet;
import entities.Player;
import graphics.TextRenderer;
import graphics.TextureRenderer;
import util.WindowUtil;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entities.Spawner;

public class Game {

	private long window;
	private Texture playerTexture;
	public static Player player;
	public static Spawner spawner;

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

	// debug configs
	public static boolean DEBUG = true;
	private int showFPS = 0;
	private int showTPS = 0;
	private String debugInfo = "FPS: 0 | TPS: 0 | V-Sync: ON";
	
	
	public static List<Bullet> bullets;
	public static List<EnemyBullet> enemyBullets;
	public static List<Enemy> enemies;
	
	
	public static String state = "MENU";

	public int i = 0;
	public boolean decrease = false;

	public Random random;

	int index = 0;

	public static int score = 0;
	
	public static boolean restarted = false;
	

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
		// error out
		org.lwjgl.glfw.GLFWErrorCallback.createPrint(System.err).set();

		// start GLFW
		if (!glfwInit()) {
			throw new IllegalStateException("Falha ao inicializar GLFW");
		}

		// window configs
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

		window = glfwCreateWindow(WIDTH * SCALE, HEIGHT * SCALE, TITLE, NULL, NULL);
		if (window == NULL) {
			throw new RuntimeException("Falha ao criar a janela GLFW");
		}

		glfwSetFramebufferSizeCallback(window, (win, newWidth, newHeight) -> {
			WindowUtil.resize(newWidth, newHeight, WIDTH, HEIGHT);
		});

		WindowUtil.glfwCenterWindow(window);

		glfwMakeContextCurrent(window);
		WindowUtil.setVsync(vsync);
		glfwShowWindow(window);

		GL.createCapabilities();

		// PNG LOAD
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_TEXTURE_2D);
		playerTexture = new Texture("/player.png");

		// END PNG LOAD
		
		
		
		//ENTITY INSTANCES
		bullets = new ArrayList<Bullet>();
		enemyBullets = new ArrayList<EnemyBullet>();
		enemies = new ArrayList<Enemy>();
		//
		
		
		//WINDOWS ICON!
		//WindowUtil.setWindowIcon(window, "/window/icon.png");

		// instancia o player no centro da tela
		player = new Player(WIDTH / 2 - 8, HEIGHT / 2 - 20, 16, 41, playerTexture);
		spawner = new Spawner();

		// pixel perfect camera
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, WIDTH * SCALE, HEIGHT * SCALE, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		// END pixel perfect camera

		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	}

	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000 / TARGET_TPS;
		double delta = 0;

		// manual fps
		long lastRenderTime = System.nanoTime();

		int frames = 0;
		int ticks = 0;
		long timer = System.currentTimeMillis();

		while (!glfwWindowShouldClose(window)) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;

			// boolean tickOn = false;
			while (delta >= 1) {
				tick();
				ticks++;
				delta--;
				// tickOn = true; // only on tick

				// hard reset - too many ticks & low render on poor pc :(
				if (delta > 100) {
					delta = 0;
					break;
				}
			}

			if (vsync) {
				render();
				frames++;
			} else if (targetFPS > 0) {

				double nsPerFrame = 1000000000.0 / targetFPS;
				// nextRenderTime - exactly next frame will be
				long nextRenderTime = lastRenderTime + (long) nsPerFrame;
				long now_t = System.nanoTime();

				// CPU Saver !
				if (now_t < nextRenderTime) {

					while (now_t < nextRenderTime) {
						long remainingTime = nextRenderTime - now_t;

						if (remainingTime > 1500000) {
							try {
								Thread.sleep(1);
							} catch (InterruptedException e) {
								e.printStackTrace();
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
				// Modo Ilimitado (targetFPS <= 0)
				render();
				frames++;
			}

			glfwPollEvents();

			if (System.currentTimeMillis() - timer >= 1000) {

				// draw visual infos
				showFPS = frames;
				showTPS = ticks;
				debugInfo = String.format("FPS: %d | TPS: %d | V-Sync: %s", showFPS, showTPS, (vsync ? "ON" : "OFF"));

				if (DEBUG)
					System.out.println("FPS: " + showFPS + " | TPS: " + showTPS);
				frames = 0;
				ticks = 0;
				timer += 1000;
			}

		}
	}

	public void tick() {
		Input.TickInput(this);
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

	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		// render here!

		// background
		TextureRenderer.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE, 0.2f, 0.2f, 0.2f);

		// render do player
		player.render();

		// enemies
		for (int i = 0; i < enemies.size(); i++)
			enemies.get(i).render();
		// bullets do player
		for (int i = 0; i < bullets.size(); i++)
			bullets.get(i).render();
		// bullets dos inimigos
		for (int i = 0; i < enemyBullets.size(); i++)
			enemyBullets.get(i).render();
		TextRenderer.drawText(debugInfo, 10, 10, 1.7f, 1f, 1f, 1f);
		TextRenderer.drawText("'V' - V-Sync Toggle", 10, 30, 1.4f, 0.8f, 0.8f, 0.8f);
		if (!vsync) {
			TextRenderer.drawText("'F1/F2' - increase/decrease FPS(set 0 to ulimited fps) - " + targetFPS, 10, 50, 1.4f,
					0.8f, 0.8f, 0.8f);
		}

		glfwSwapBuffers(window);
	}

	// clean - exit
	private void dispose() {
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

	public void setDebugInfo() {
		debugInfo = String.format("FPS: %d | TPS: %d | V-Sync: %s", showFPS, showTPS, (vsync ? "ON" : "OFF"));
	}

	public void setVsync(boolean vsync) {
		this.vsync = vsync;
	}

}
