package com.jnetu.main;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Cutscene {

	public int i = 0;
	public boolean runFadeIn;
	public boolean endRunFadeIn;

	public int state = 1;

	public int cutsceneState = 0; // 0 - not run

	public boolean runFadeOff;
	public boolean endRunFadeOff;

	public boolean pressedSkip = false;

	public BufferedImage scene1;
	public BufferedImage scene2;
	public BufferedImage scene3;
	public BufferedImage scene4;
	public BufferedImage scene5;
	public BufferedImage deadscene;

	public boolean showDead = false;

	Cutscene() {
		Spritesheet sh = new Spritesheet("/scene1.png");
		scene1 = sh.getSprite(0, 0, 256, 256);
		sh = new Spritesheet("/scene2.png");
		scene2 = sh.getSprite(0, 0, 256, 172);
		sh = new Spritesheet("/scene3.png");
		scene3 = sh.getSprite(0, 0, 256, 256);
		sh = new Spritesheet("/scene4.png");
		scene4 = sh.getSprite(0, 0, 256, 256);
		sh = new Spritesheet("/scene5.png");
		scene5 = sh.getSprite(0, 0, 256, 256);

		sh = new Spritesheet("/deadscene.png");
		deadscene = sh.getSprite(0, 0, 256, 256);

	}

	public void tick() {

		fadeLogic();

		if (state > 6) {
			Game.state = "RUN";
		}

		if (pressedSkip && !runFadeIn && !runFadeOff && cutsceneState != 2) {
			pressedSkip = false;
			runFadeIn = true;

		}

	}

	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
		if (cutsceneState == 1) {// 1 = intro

			if (Game.menu.languageChoice == 0) {
				if (state == 1) {
					if (Game.restarted) {
						g2.setColor(Color.black);
						g2.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
						g2.setColor(Color.white);
						g2.setFont(Game.font.deriveFont(12f));
						g2.drawString("you have found 1 final", 64, 80 + 128 + 32);
					} else {
						g2.setColor(Color.black);
						g2.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
						g2.setColor(Color.white);
						g2.setFont(Game.font.deriveFont(20f));
						g2.drawString("Taiwan 49", 150 - 45, 128 - 32);
					}

				}
				if (state == 2) {
					g2.setColor(Color.white);
					g2.drawImage(scene1, Game.WIDTH / 2 - 64 - 16, 30, 128 + 32, 128, null);
					g2.setFont(Game.font.deriveFont(12f));
					g2.drawString("Under the scorching sun of the fields", 4, 10 + 128 + 32);
					g2.drawString("of rice in Thailand, Chai worked", 4, 20 + 128 + 32);
					g2.drawString("tirelessly, cultivating the land that", 4, 30 + 128 + 32);
					g2.drawString("sustained his community for generations.", 4, 40 + 128 + 32);
					g2.drawString("The wind blew gently through the", 4, 50 + 128 + 32);
					g2.drawString("rows of plantations, while Chai", 4, 60 + 128 + 32);
					g2.drawString("dedicated himself to his work with", 4, 70 + 128 + 32);
					g2.drawString("determination.", 4, 80 + 128 + 32);
				}
				if (state == 3) {
					g2.setColor(Color.white);
					g2.drawImage(scene2, Game.WIDTH / 2 - 64 - 16, 30, 128 + 32, 128, null);
					g2.setFont(Game.font.deriveFont(12f));
					g2.drawString("Suddenly, the serene atmosphere of the ", 4, 10 + 128 + 32);
					g2.drawString("fields was interrupted by the distant sound ", 4, 20 + 128 + 32);
					g2.drawString("of engines and the echoing of marching boots.", 4, 30 + 128 + 32);
					g2.drawString("Chai raised his eyes and spotted a", 4, 40 + 128 + 32);
					g2.drawString("column of soldiers approaching,", 4, 50 + 128 + 32);
					g2.drawString("led by none other than Chiang Kai-shek,", 4, 60 + 128 + 32);
					g2.drawString("the renowned nationalist", 4, 70 + 128 + 32);
					g2.drawString("Chinese leader. Tension hung in the air as", 4, 80 + 128 + 32);
					g2.drawString("Chiang Kai-shek, with a grave demeanor,", 4, 90 + 128 + 32);
				}
				if (state == 4) {
					g2.setColor(Color.white);
					g2.drawImage(scene3, Game.WIDTH / 2 - 64 - 16, 30, 128 + 32, 128, null);
					g2.setFont(Game.font.deriveFont(12f));
					g2.drawString("", 4, 10 + 128 + 32);
					g2.drawString("informed Chai about the plans for invasion", 4, 10 + 128 + 32);
					g2.drawString("by the communist China in Taiwan. In a ", 4, 20 + 128 + 32);
					g2.drawString("gesture of solidarity and encouragement,", 4, 30 + 128 + 32);
					g2.drawString("he handed Chai a cold Coca-Cola,", 4, 40 + 128 + 32);
					g2.drawString("saying: 'You'll need this, my", 4, 50 + 128 + 32);
					g2.drawString("friend. The battle is near, and we need", 4, 60 + 128 + 32);
					g2.drawString("every courageous man like you.'", 4, 70 + 128 + 32);
				}
				if (state == 5) {
					g2.setColor(Color.white);
					g2.drawImage(scene4, Game.WIDTH / 2 - 64 - 16, 30, 128 + 32, 128, null);
					g2.setFont(Game.font.deriveFont(12f));
					g2.drawString("While Chai absorbed the gravity of the", 4, 10 + 128 + 32);
					g2.drawString("situation, on the other side of the", 4, 20 + 128 + 32);
					g2.drawString("Taiwan Strait, in mainland Taiwan,", 4, 30 + 128 + 32);
					g2.drawString("the leaders communists were busy with", 4, 40 + 128 + 32);
					g2.drawString("a sinister plan: They were secretly", 4, 50 + 128 + 32);
					g2.drawString("developing a powerful weapon, a war machine", 4, 60 + 128 + 32);
					g2.drawString("that promised to subjugate Taiwan", 4, 70 + 128 + 32);
					g2.drawString("and expand their domain over the region.", 4, 80 + 128 + 32);
				}
				if (state == 6) {
					g2.setColor(Color.white);
					g2.drawImage(scene5, Game.WIDTH / 2 - 64 - 16, 30, 128 + 32, 128, null);
					g2.setFont(Game.font.deriveFont(12f));
					g2.drawString("Determined to protect his homeland and", 4, 10 + 128 + 32);
					g2.drawString("his freedom, Chai led his community", 4, 20 + 128 + 32);
					g2.drawString("in resistance against the impending", 4, 30 + 128 + 32);
					g2.drawString("communist attack. With Coca-Cola in hand as", 4, 40 + 128 + 32);
					g2.drawString("a symbol of energy and hope, he", 4, 50 + 128 + 32);
					g2.drawString("bravely faced the invading army,", 4, 60 + 128 + 32);
					g2.drawString("ready to defend his homeland against", 4, 70 + 128 + 32);
					g2.drawString("whatever adversity came his way.", 4, 80 + 128 + 32);
				}

			}
			if (Game.menu.languageChoice == 1) {
				if (state == 1) {
					if (Game.restarted) {
						g2.setColor(Color.black);
						g2.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
						g2.setColor(Color.white);
						g2.setFont(Game.font.deriveFont(12f));
						g2.drawString("你找到了 1 个残局", 64, 80 + 128 + 32);
					} else {
						g2.setColor(Color.black);
						g2.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
						g2.setColor(Color.white);
						g2.setFont(Game.font.deriveFont(12f));
						g2.drawString("show logo", 64, 80 + 128 + 32);
					}

				}
				if (state == 2) {
					g2.setColor(Color.white);
					g2.drawImage(scene1, Game.WIDTH / 2 - 64 - 16, 30, 128 + 32, 128, null);
					g2.setFont(Game.font.deriveFont(12f));
					g2.drawString("在泰国稻田的灼热阳光下，柴辛勤劳地工作着", 4, 15 + 128 + 32);
					g2.drawString("培育着支撑他社区几代人的土地。", 4, 30 + 128 + 32);
					g2.drawString("微风轻轻吹过种植园的每一行，而柴则专注于", 4, 45 + 128 + 32);
					g2.drawString("他的工作，充满了决心。", 4, 60 + 128 + 32);
				}
				if (state == 3) {
					g2.setColor(Color.white);
					g2.drawImage(scene2, Game.WIDTH / 2 - 64 - 16, 30, 128 + 32, 128, null);
					g2.setFont(Game.font.deriveFont(12f));
					g2.drawString("突然间，田野间宁静的气氛被远处的引擎声", 4, 15 + 128 + 32);
					g2.drawString("和行军步伐的回声打破。柴抬起头，发现一", 4, 30 + 128 + 32);
					g2.drawString("支士兵队伍正在逼近，而领头的竟是著名的", 4, 45 + 128 + 32);
					g2.drawString("国民党领袖蒋介石。紧张笼罩在空气中，", 4, 60 + 128 + 32);
					g2.drawString("而蒋介石以严肃的态度开始了他的讲话。", 4, 75 + 128 + 32);
				}
				if (state == 4) {
					g2.setColor(Color.white);
					g2.drawImage(scene3, Game.WIDTH / 2 - 64 - 16, 30, 128 + 32, 128, null);
					g2.setFont(Game.font.deriveFont(12f));
					g2.drawString("蒋介石面色凝重地告诉柴有关共产中国入侵台湾的计划。", 4, 15 + 128 + 32);
					g2.drawString("作为团结和鼓励的表示，他递给柴一听冰凉的可口可乐，", 4, 30 + 128 + 32);
					g2.drawString("说道：“你会需要这个的，我的朋友。战斗即将到来，", 4, 45 + 128 + 32);
					g2.drawString("我们需要像你这样勇敢的人。”", 4, 60 + 128 + 32);
				}
				if (state == 5) {
					g2.setColor(Color.white);
					g2.drawImage(scene4, Game.WIDTH / 2 - 64 - 16, 30, 128 + 32, 128, null);
					g2.setFont(Game.font.deriveFont(12f));
					g2.drawString("柴思考着局势的严重性，而在台湾海峡的另一侧，", 4, 15 + 128 + 32);
					g2.drawString("在台湾大陆，共产主义领导人正忙于一个阴险的计划：", 4, 30 + 128 + 32);
					g2.drawString("他们正在秘密研发一种强大的武器，一种战争机器，", 4, 45 + 128 + 32);
					g2.drawString("承诺将征服台湾，并扩展他们在该地区的领域。", 4, 60 + 128 + 32);
				}
				if (state == 6) {
					g2.setColor(Color.white);
					g2.drawImage(scene5, Game.WIDTH / 2 - 64 - 16, 30, 128 + 32, 128, null);
					g2.setFont(Game.font.deriveFont(12f));
					g2.drawString("决心保卫祖国和自由,柴带领着他的社区抵抗即将来临的", 4, 15 + 128 + 32);
					g2.drawString("共产主义进攻。手持可口可乐，作为能量和希望的象征，", 4, 30 + 128 + 32);
					g2.drawString("他勇敢地面对入侵的军队，准备抵挡任何来自敌人的", 4, 45 + 128 + 32);
					g2.drawString("挑战。", 4, 60 + 128 + 32);
				}
			}
			if (Game.menu.languageChoice == 2) {
				if (state == 1) {
					if (Game.restarted) {
						g2.setColor(Color.black);
						g2.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
						g2.setColor(Color.white);
						g2.setFont(Game.font.deriveFont(12f));
						g2.drawString("你找到了 1 个残局", 64, 80 + 128 + 32);
					} else {
						g2.setColor(Color.black);
						g2.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
						g2.setColor(Color.white);
						g2.setFont(Game.font.deriveFont(12f));
						g2.drawString("show logo", 64, 80 + 128 + 32);
					}

				}
				if (state == 2) {
					g2.setColor(Color.white);
					g2.drawImage(scene1, Game.WIDTH / 2 - 64 - 16, 30, 128 + 32, 128, null);
					g2.setFont(Game.font.deriveFont(12f));
					g2.drawString("在泰国稻田的灼热阳光下，柴辛勤劳地工作着", 4, 15 + 128 + 32);
					g2.drawString("培育着支撑他社区几代人的土地。", 4, 30 + 128 + 32);
					g2.drawString("微风轻轻吹过种植园的每一行，而柴则专注于", 4, 45 + 128 + 32);
					g2.drawString("他的工作，充满了决心。", 4, 60 + 128 + 32);
				}
				if (state == 3) {
					g2.setColor(Color.white);
					g2.drawImage(scene2, Game.WIDTH / 2 - 64 - 16, 30, 128 + 32, 128, null);
					g2.setFont(Game.font.deriveFont(12f));
					g2.drawString("突然间，田野间宁静的气氛被远处的引擎声", 4, 15 + 128 + 32);
					g2.drawString("和行军步伐的回声打破。柴抬起头，发现一", 4, 30 + 128 + 32);
					g2.drawString("支士兵队伍正在逼近，而领头的竟是著名的", 4, 45 + 128 + 32);
					g2.drawString("国民党领袖蒋介石。紧张笼罩在空气中，", 4, 60 + 128 + 32);
					g2.drawString("而蒋介石以严肃的态度开始了他的讲话。", 4, 75 + 128 + 32);
				}
				if (state == 4) {
					g2.setColor(Color.white);
					g2.drawImage(scene3, Game.WIDTH / 2 - 64 - 16, 30, 128 + 32, 128, null);
					g2.setFont(Game.font.deriveFont(12f));
					g2.drawString("蒋介石面色凝重地告诉柴有关共产中国入侵台湾的计划。", 4, 15 + 128 + 32);
					g2.drawString("作为团结和鼓励的表示，他递给柴一听冰凉的可口可乐，", 4, 30 + 128 + 32);
					g2.drawString("说道：“你会需要这个的，我的朋友。战斗即将到来，", 4, 45 + 128 + 32);
					g2.drawString("我们需要像你这样勇敢的人。”", 4, 60 + 128 + 32);
				}
				if (state == 5) {
					g2.setColor(Color.white);
					g2.drawImage(scene4, Game.WIDTH / 2 - 64 - 16, 30, 128 + 32, 128, null);
					g2.setFont(Game.font.deriveFont(12f));
					g2.drawString("柴思考着局势的严重性，而在台湾海峡的另一侧，", 4, 15 + 128 + 32);
					g2.drawString("在台湾大陆，共产主义领导人正忙于一个阴险的计划：", 4, 30 + 128 + 32);
					g2.drawString("他们正在秘密研发一种强大的武器，一种战争机器，", 4, 45 + 128 + 32);
					g2.drawString("承诺将征服台湾，并扩展他们在该地区的领域。", 4, 60 + 128 + 32);
				}
				if (state == 6) {
					g2.setColor(Color.white);
					g2.drawImage(scene5, Game.WIDTH / 2 - 64 - 16, 30, 128 + 32, 128, null);
					g2.setFont(Game.font.deriveFont(12f));
					g2.drawString("决心保卫祖国和自由,柴带领着他的社区抵抗即将来临的", 4, 15 + 128 + 32);
					g2.drawString("共产主义进攻。手持可口可乐，作为能量和希望的象征，", 4, 30 + 128 + 32);
					g2.drawString("他勇敢地面对入侵的军队，准备抵挡任何来自敌人的", 4, 45 + 128 + 32);
					g2.drawString("挑战。", 4, 60 + 128 + 32);
				}
			}
		}
		

		if (cutsceneState == 2) { // dead
			if (showDead) {

				g2.setColor(Color.white);
				g2.drawImage(deadscene, Game.WIDTH / 2 - 64 - 16, 30, 128 + 32, 128, null);
				g2.setFont(Game.font.deriveFont(12f));
				g2.drawString("you are dead", 4, 10 + 128 + 32);
			}
		}
		loadFadeIn(g2);

	}

	private void loadFadeIn(Graphics g2) {
		if (runFadeIn) {
			g2.setColor(new Color(0, 0, 0, i));
			g2.fillRect(0, 0, 1000, 1000);
		} else if (endRunFadeIn) {
			g2.setColor(new Color(0, 0, 0, i));
			g2.fillRect(0, 0, 1000, 1000);
		}
		if (runFadeOff) {
			g2.setColor(new Color(0, 0, 0, i));
			g2.fillRect(0, 0, 1000, 1000);
		}

	}

	private void fadeLogic() {
		if (runFadeIn) {
			i += 5;
			if (i >= 255) {
				runFadeIn = false;
				endRunFadeIn = true;
			}
		}
		if (endRunFadeIn) {
			runFadeOff = true;
			endRunFadeIn = false;

			state++;

			if (cutsceneState == 2) {
				showDead = true;
			}

		}
		if (runFadeOff) {
			runFadeIn = false;
			if (i <= 0) {
				runFadeOff = false;
				// END
			} else {
				i -= 5;
			}

		}
	}

	public void runIntro() {
		cutsceneState = 1;
		state = 1;
		runFadeIn = false;
		runFadeOff = false;
	}

	public void runFade() {
		endRunFadeIn = true;
	}

	public void runDeadScene() {
		cutsceneState = 2;
		state = 1;
		runFadeIn = true;
		runFadeOff = false;
		Game.state = "CUTSCENE";
	}

}
