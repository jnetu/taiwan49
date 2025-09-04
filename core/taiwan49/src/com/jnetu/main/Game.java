package com.jnetu.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Taskbar;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.jnetu.entities.Bullet;
import com.jnetu.entities.Enemy;
import com.jnetu.entities.EnemyBullet;
import com.jnetu.entities.Player;
import com.jnetu.entities.Spawner;

public class Game extends Canvas implements Runnable, KeyListener {
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 300;
	public static final int HEIGHT = 256;
	final static int SCALE = 3;
	public static JFrame frame;

	private Thread thread;

	private BufferedImage image;
	private BufferedImage[] floorImages;
	private BufferedImage bossFloor;

	public static Player player;
	public static Spawner spawner;
	public static Spritesheet spritesheet;
	public static Cutscene cutscene;
	public static List<Bullet> bullets;
	public static List<EnemyBullet> enemyBullets;
	public static List<Enemy> enemies;
	public static Font font;
	public InputStream fontStream = ClassLoader.getSystemClassLoader().getResourceAsStream("Unifontexmono-DYWdE.TTF");
	public static Menu menu;

	public static String state = "MENU";

	public int i = 0;
	public boolean decrease = false;

	public Random random;

	int index = 0;

	public static int score = 0;
	
	public static boolean restarted = false;

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double fps = 60.0;
		double ns = 1000000000 / fps;
		double delta = 0;
		int frames = 0;// see frames per second
		double timer = System.currentTimeMillis(); // see frames per second
		for (;;) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;

			if (delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}

			if (System.currentTimeMillis() - timer >= 1000) { // see frames per second
				System.out.println(frames);// see fps
				frames = 0;
				timer += 1000;
			}

		}
	}

	public Game() {
		addKeyListener(this);
		this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE)); // set canvas dimensions
		frame = new JFrame("Taiwan 49");
		frame.add(this); // add Canvas
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);// null --> center
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // break application on exit
		frame.setVisible(true);

		// SET ICON
		Image icon = null;

		try {
			icon = ImageIO.read(getClass().getResource("/icon.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Taskbar taskbar = Taskbar.getTaskbar();

		taskbar.setIconImage(icon);

		// to windows os
		frame.setIconImage(icon);

		// END SET ICON

		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		player = new Player(WIDTH / 2, HEIGHT - 70, 25, 35, image);
		bullets = new ArrayList<Bullet>();
		enemyBullets = new ArrayList<EnemyBullet>();
		enemies = new ArrayList<Enemy>();

		cutscene = new Cutscene();
		cutscene.runIntro();

		spawner = new Spawner();

		floorImages = new BufferedImage[6];

		Spritesheet sh = new Spritesheet("/floor1.png");
		floorImages[0] = sh.getSprite(0, 0, 256, 256);
		sh = new Spritesheet("/floor2.png");
		floorImages[1] = sh.getSprite(0, 0, 256, 256);
		sh = new Spritesheet("/floor3.png");
		floorImages[2] = sh.getSprite(0, 0, 256, 256);
		sh = new Spritesheet("/floor4.png");
		floorImages[3] = sh.getSprite(0, 0, 256, 256);
		sh = new Spritesheet("/floor5.png");
		floorImages[4] = sh.getSprite(0, 0, 256, 256);
		sh = new Spritesheet("/floor6.png");
		floorImages[5] = sh.getSprite(0, 0, 256, 256);
		sh = new Spritesheet("/bossfloor.png");
		bossFloor = sh.getSprite(0, 0, 256, 256);

		random = new Random();
		index = random.nextInt(6);
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(30f);
		} catch (FontFormatException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		menu = new Menu();

	}

	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}

	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
	}

	public synchronized void stop() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void reset() {
		player = new Player(WIDTH / 2, HEIGHT - 70, 25, 35, image);
		bullets = new ArrayList<Bullet>();
		enemyBullets = new ArrayList<EnemyBullet>();
		enemies = new ArrayList<Enemy>();
		cutscene = new Cutscene();
		restarted = true;
		cutscene.runIntro();
		spawner = new Spawner();
		index = random.nextInt(6);
	}

	public void tick() {

		if (state == "RUN") {
			
			player.tick();
			spawner.tick();

			for (int i = 0; i < bullets.size(); i++) {
				bullets.get(i).tick();
			}
			for (int i = 0; i < enemyBullets.size(); i++) {
				enemyBullets.get(i).tick();
			}
			for (int i = 0; i < enemies.size(); i++) {
				enemies.get(i).tick();
			}
		} else if (state == "CUTSCENE") {
			cutscene.tick();
		} else if (state == "MENU") {
			menu.tick();
		}
	}

	public void render() {
		requestFocus();

		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		// draw on background image
		Graphics g = image.getGraphics();
		g.setFont(font);
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, WIDTH, HEIGHT);

		// draw floor
		g.drawImage(floorImages[index], 0, 0, WIDTH, HEIGHT, null);

		if (state == "RUN") {
			for (int i = 0; i < enemies.size(); i++) {
				enemies.get(i).render(g);
			}
			for (int i = 0; i < bullets.size(); i++) {
				bullets.get(i).render(g);
			}
			for (int i = 0; i < enemyBullets.size(); i++) {
				enemyBullets.get(i).render(g);
			}

			player.render(g);

			g.setColor(Color.BLACK);
			g.fillRect(9, 10, 64, 12);

			g.setColor(Color.GREEN);
			g.setFont(Game.font.deriveFont(13f));
			String formatted = String.format("%09d得分", score);

			g.drawString("" + formatted, 10, 20);

		} else if (state == "CUTSCENE") {
			cutscene.render(g);
		}else if (state == "MENU") {
			menu.render(g);
		}

		// draw background
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);

//		g.setColor(Color.GREEN);
//		g.setFont(Game.font.deriveFont(23f));
//		String formatted = String.format("%09d", score);
//		
//		g.drawString("" + formatted, 30, 50);

		bs.show();

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
			

		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
			
		}
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
			menu.up = true;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
			menu.down = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_X || e.getKeyCode() == KeyEvent.VK_Z) {
			if (state == "RUN") {
				player.pressedShoot = true;
			} else if (state == "CUTSCENE") {
				cutscene.pressedSkip = true;

				if (cutscene.showDead) {
					reset();
				}
			}else if(state == "MENU") {
				 menu.enter = true;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_F) {
			System.out.println("debug cutscene");
			cutscene.runIntro();
			state = "CUTSCENE";
		}
		if (e.getKeyCode() == KeyEvent.VK_G) {
			System.out.println("debug cutscene dead");
			cutscene.runDeadScene();
			state = "CUTSCENE";
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;

		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
		}

	}
}
