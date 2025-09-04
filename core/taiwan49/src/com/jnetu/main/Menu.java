package com.jnetu.main;

import java.awt.Color;
import java.awt.Graphics;

public class Menu {
	public String[] options = { "ingles", "chines", "japones", "exit" };
	public int curOption = 0;
	public int languageChoice = 0;
	public int maxOptions = options.length - 1;
	public boolean down = false;
	public boolean up = false;
	public boolean enter = false;

	public boolean pause = false;

	public void tick() {
		if (up) {
			up = false;
			curOption--;
			if (curOption < 0) {
				curOption = maxOptions;
			}
		}
		if (down) {
			down = false;
			curOption++;
			if (curOption > maxOptions) {
				curOption = 0;
			}
		}

		if (enter) {
			enter = false;

			if (options[curOption] == "exit") {
				System.exit(1);
			}

			pause = false;
			Game.state = "CUTSCENE";

		}

		if (options[curOption] == "ingles") {
			languageChoice = 0;

		} else if (options[curOption] == "chines") {
			languageChoice = 1;
		} else if (options[curOption] == "japones") {
			languageChoice = 2;
		} else if (options[curOption] == "exit") {
			languageChoice = 0;
		}

	}

	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
		g.setColor(Color.WHITE);

		g.drawString("jnetu开发者", (int) (Game.WIDTH * 0.5) - 80, (int) (Game.HEIGHT * 0.2));
		g.setFont(Game.font.deriveFont(19f));
		g.drawString("这个游戏是一项独立的工作", (int) 40, (int) (Game.HEIGHT * 0.35));

		g.drawString("动作按钮是空格键", (int) 80, (int) (Game.HEIGHT * 0.45));
		if (pause) {
			if (languageChoice == 0) {
				g.drawString("Continue", 110, 150);
			} else if (languageChoice == 1) {
				g.drawString("继续", 110, 150);
			} else if (languageChoice == 2) {
				g.drawString("続ける\" (つづける", 110, 150);
			}
		} else {
			g.drawString("English", 110, 150);

		}

		g.drawString("中文", 121, 175);
		g.drawString("日本语", 111, 200);

		if (languageChoice == 0) {
			g.drawString("Exit", 123, 225);
		} else if (languageChoice == 1) {
			g.drawString("退出", 120, 225);
		} else if (languageChoice == 2) {
			g.drawString("出口", 120, 225);
		}

		if (options[curOption] == "ingles") {
			g.drawString(">", 80, 150);

		} else if (options[curOption] == "chines") {
			g.drawString(">", 80, 175);

		} else if (options[curOption] == "japones") {
			g.drawString(">", 80, 200);

		} else if (options[curOption] == "exit") {
			g.drawString(">", 80, 225);

		}
	}
}
