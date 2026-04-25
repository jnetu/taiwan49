// @encoding UTF-8
package main;

import graphics.TextureRenderer;
import graphics.UnicodeFontRenderer;

public class Cutscene {

	public int state = 1;
	public int cutsceneState = 0; // 0=idle 1=intro 2=dead

	private int fadeAlpha = 0;
	private boolean runFadeIn = false;
	private boolean endFadeIn = false;
	private boolean runFadeOff = false;

	public boolean pressedSkip = false;
	public boolean showDead = false;

	private Texture scene1, scene2, scene3, scene4, scene5, deadscene;
	private UnicodeFontRenderer font;

	// escala pixelart igual ao Menu
	private static final float SCALE = 3f;

	public Cutscene(UnicodeFontRenderer font) {
		this.font = font;
		scene1 = new Texture("/scene1.png");
		scene2 = new Texture("/scene2.png");
		scene3 = new Texture("/scene3.png");
		scene4 = new Texture("/scene4.png");
		scene5 = new Texture("/scene5.png");
		deadscene = new Texture("/deadscene.png");
	}

	// ─────────────────────────────────────────────────────────────────────────

	public void tick() {
		fadeLogic();
		if (state > 6)
			Game.state = "RUN";
		if (pressedSkip && !runFadeIn && !runFadeOff && cutsceneState != 2) {
			pressedSkip = false;
			runFadeIn = true;
		}
	}

	public void render() {
		int W = Game.WIDTH * Game.SCALE;
		int H = Game.HEIGHT * Game.SCALE;

		TextureRenderer.fillRect(0, 0, W, H, 0f, 0f, 0f);

		if (cutsceneState == 1)
			renderIntro(W, H);
		else if (cutsceneState == 2)
			renderDead(W, H);

		if (fadeAlpha > 0)
			TextureRenderer.fillRectAlpha(0, 0, W, H, 0f, 0f, 0f, fadeAlpha / 255f);
	}

	// ─────────────────────────────────────────────────────────────────────────

	private void renderIntro(int W, int H) {
		int lang = Game.menu.languageChoice; // 0=EN 1=中文 2=日本語

		// imagem da cena — ocupa metade superior da tela
		float imgW = W * 0.55f;
		float imgH = H * 0.45f;
		float imgX = W * 0.5f - imgW * 0.5f;
		float imgY = H * 0.03f;

		// área de texto logo abaixo da imagem
		float tx = 120f;
		float ty0 = imgY + imgH + 100f;
		float ls = fontSize() * SCALE + 10f; // line-spacing proporcional à fonte

		// ── state 1: tela de título ───────────────────────────────────────────
		if (state == 1) {
			String title = Game.restarted ? (lang == 0 ? "you have found 1 final" : "你找到了 1 个残局") : "Taiwan 49";
			font.drawText(title, W * 0.5f - 60, H * 0.38f, SCALE * 1.5f, 1f, 1f, 1f);
			renderSkipHint(lang, W, H);
			return;
		}

		// ── states 2-6: imagem + texto ────────────────────────────────────────
		Texture img = switch (state) {
		case 2 -> scene1;
		case 3 -> scene2;
		case 4 -> scene3;
		case 5 -> scene4;
		case 6 -> scene5;
		default -> null;
		};
		if (img != null)
			TextureRenderer.drawTexture(img, imgX, imgY, imgW, imgH);

		String[] lines = getLines(lang, state);
		if (lines != null) {
			for (int i = 0; i < lines.length; i++) {
				font.drawText(lines[i], tx, ty0 + i * ls, SCALE, 1f, 1f, 1f);
			}
		}

		renderSkipHint(lang, W, H);
	}

	private void renderDead(int W, int H) {
		if (!showDead)
			return;
		float imgW = W * 0.55f;
		float imgH = H * 0.45f;
		float imgX = W * 0.5f - imgW * 0.5f;
		float imgY = H * 0.03f;
		TextureRenderer.drawTexture(deadscene, imgX, imgY, imgW, imgH);
		font.drawText("you are dead", 12, imgY + imgH + 10, SCALE, 1f, 0.2f, 0.2f);
	}

	private void renderSkipHint(int lang, int W, int H) {
		String hint = lang == 0 ? "ENTER - skip" : "ENTER - 跳过";
		font.drawText(hint, W - 160, H - 40, SCALE * 0.7f, 0.3f, 0.3f, 0.3f);
	}

	// ─────────────────────────────────────────────────────────────────────────
	// Textos — edite as strings diretamente, sem escapes
	// ─────────────────────────────────────────────────────────────────────────

	private String[] getLines(int lang, int state) {
		return switch (state) {
		case 2 -> lang == 0 ? EN_2 : (lang == 1 ? ZH_2 : JA_2);
		case 3 -> lang == 0 ? EN_3 : (lang == 1 ? ZH_3 : JA_3);
		case 4 -> lang == 0 ? EN_4 : (lang == 1 ? ZH_4 : JA_4);
		case 5 -> lang == 0 ? EN_5 : (lang == 1 ? ZH_5 : JA_5);
		case 6 -> lang == 0 ? EN_6 : (lang == 1 ? ZH_6 : JA_6);
		default -> null;
		};
	}

	// ── English ───────────────────────────────────────────────────────────────
	private static final String[] EN_2 = { "Under the scorching sun of the fields", "of rice in Thailand, Chai worked",
			"tirelessly, cultivating the land that", "sustained his community for generations.", };
	private static final String[] EN_3 = { "Suddenly, the serene atmosphere was",
			"interrupted by the distant sound of engines", "and the echoing of marching boots.",
			"Chai spotted a column of soldiers led by", "none other than Chiang Kai-shek.", };
	private static final String[] EN_4 = { "Chiang informed Chai of the communist", "China invasion plans for Taiwan.",
			"He handed Chai a cold Coca-Cola:", "'You'll need this. The battle is near,",
			"and we need every courageous man.'", };
	private static final String[] EN_5 = { "While Chai absorbed the gravity of the",
			"situation, across the Taiwan Strait,", "communist leaders were secretly building",
			"a powerful war machine to subjugate Taiwan.", };
	private static final String[] EN_6 = { "Determined to protect his homeland,",
			"Chai led his community in resistance.", "Coca-Cola in hand as a symbol of hope,",
			"he bravely faced the invading army.", };

	// ── 中文 ──────────────────────────────────────────────────────────────────
	private static final String[] ZH_2 = { "在泰国稻田的灼热阳光下，", "柴辛勤劳地工作着，", "培育着支撑社区几代人的土地。", "微风轻轻吹过，柴充满了决心。", };
	private static final String[] ZH_3 = { "突然间，宁静被远处的引擎声打破。", "柴抬起头，发现一支士兵队伍正在逼近，", "而领头的正是著名的国民党领袖", "蒋介石。", };
	private static final String[] ZH_4 = { "蒋介石告诉柴有关共产中国", "入侵台湾的计划。", "他递给柴一听冰凉的可口可乐，", "说：「战斗即将到来，我们需要你。」", };
	private static final String[] ZH_5 = { "柴思考着局势，而在台湾海峡另一侧，", "共产主义领导人正秘密研发", "一种强大的战争机器，", "承诺将征服台湾。", };
	private static final String[] ZH_6 = { "决心保卫祖国和自由，", "柴带领社区抵抗进攻。", "手持可口可乐作为希望的象征，", "他勇敢地面对入侵的军队。", };

	// ── 日本語 ────────────────────────────────────────────────────────────────
	private static final String[] JA_2 = { "タイの稲田の灼熱の太陽の下で、", "チャイは何世代にもわたって", "共同体を支えてきた土地を", "疲れを知らず耕していた。", };
	private static final String[] JA_3 = { "突然、静寂がエンジン音と", "行軍の足音で破られた。", "チャイは兵士の列を目にした。", "先頭にいたのは蒋介石だった。", };
	private static final String[] JA_4 = { "蒋介石は台湾への侵攻計画を伝えた。", "彼はチャイに冷たいコカ・コーラを手渡し、", "「戦いはもうすぐだ。",
			"君のような勇敢な男が必要だ。」", };
	private static final String[] JA_5 = { "チャイが状況の深刻さを考える中、", "台湾海峡の反対側では、", "共産主義の指導者たちが", "強力な兵器を秘密裏に開発していた。", };
	private static final String[] JA_6 = { "故郷と自由を守る決意を固め、", "チャイは抵抗運動を率いた。", "希望の象徴としてコカ・コーラを手に、", "彼は勇敢に侵略軍と対峙した。", };

	// ─────────────────────────────────────────────────────────────────────────

	private float fontSize() {
		// retorna o tamanho base da fonte usada no UnicodeFontRenderer
		return 8f;
	}

	private void fadeLogic() {
		if (runFadeIn) {
			fadeAlpha += 5;
			if (fadeAlpha >= 255) {
				fadeAlpha = 255;
				runFadeIn = false;
				endFadeIn = true;
			}
		}
		if (endFadeIn) {
			endFadeIn = false;
			runFadeOff = true;
			state++;
			if (cutsceneState == 2)
				showDead = true;
		}
		if (runFadeOff) {
			fadeAlpha -= 5;
			if (fadeAlpha <= 0) {
				fadeAlpha = 0;
				runFadeOff = false;
			}
		}
	}

	// ── API ───────────────────────────────────────────────────────────────────

	public void runIntro() {
		cutsceneState = 1;
		state = 1;
		runFadeIn = false;
		runFadeOff = false;
		fadeAlpha = 0;
	}

	public void runFade() {
		endFadeIn = true;
	}

	public void runDeadScene() {
		cutsceneState = 2;
		state = 1;
		runFadeIn = true;
		runFadeOff = false;
		Game.state = "CUTSCENE";
	}
}
