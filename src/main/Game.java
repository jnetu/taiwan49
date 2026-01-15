package main;

import org.lwjgl.opengl.GL;

import graphics.TextRenderer;
import graphics.TextureRenderer;
import util.WindowUtil;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Game {

	private long window;
	private Texture playerTexture;

	// window configs
	public static final int WIDTH = 300;
	public static final int HEIGHT = 256;
	final static int SCALE = 3;
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

		WindowUtil.setWindowIcon(window, "/window/icon.png");
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
		// tick here!
	}

	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		// render here!

		// background
		TextureRenderer.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE, 0.2f, 0.2f, 0.2f);

		// desenha o 'plauer' em x 100 y 100 e o tamanho * escala
		float x = 100;
		float y = 100;
		float w = playerTexture.getWidth() * SCALE;
		float h = playerTexture.getHeight() * SCALE;
		TextureRenderer.drawTexture(playerTexture, x, y, w, h);

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
