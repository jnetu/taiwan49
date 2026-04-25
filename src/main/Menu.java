// @encoding UTF-8
package main;

import graphics.TextureRenderer;
import graphics.UnicodeFontRenderer;

public class Menu {

	public String[] options = { "ingles", "chines", "japones", "exit" };
	public int curOption = 0;
	public int languageChoice = 0;
	public int maxOptions = options.length - 1;

	public boolean down = false;
	public boolean up = false;
	public boolean enter = false;
	public boolean pause = false;

	private UnicodeFontRenderer font;

	// escala pixelart: fonte assada em 8px, renderizada em 3x = 24px blocados
	private static final float SCALE = 3f;

	public Menu(UnicodeFontRenderer font) {
		this.font = font;
	}

	// ─────────────────────────────────────────────────────────────────────────

	public void tick() {
		if (up) {
			up = false;
			curOption--;
			if (curOption < 0)
				curOption = maxOptions;
		}
		if (down) {
			down = false;
			curOption++;
			if (curOption > maxOptions)
				curOption = 0;
		}

		if (enter) {
			enter = false;
			if (options[curOption].equals("exit"))
				System.exit(0);

			// define idioma
			languageChoice = switch (options[curOption]) {
			case "ingles" -> 0;
			case "chines" -> 1;
			case "japones" -> 2;
			default -> 0;
			};

			pause = false;
			Game.cutscene.runIntro(); // ← inicia a cutscene corretamente
			Game.state = "CUTSCENE";
		}
	}

	// ─────────────────────────────────────────────────────────────────────────

	public void render() {
		int W = Game.WIDTH * Game.SCALE;
		int H = Game.HEIGHT * Game.SCALE;

		TextureRenderer.fillRect(0, 0, W, H, 0f, 0f, 0f);

		// ── Crédito do dev ────────────────────────────────────────────────────
		// "jnetu 开发者" = "jnetu developer"
		font.drawText("jnetu  开发者", 20, 30, SCALE, 0.5f, 0.5f, 0.5f);

		// ── Título ────────────────────────────────────────────────────────────
		font.drawText("Taiwan 49", W / 2f - 120, H * 0.22f, SCALE * 2f, 1f, 0.85f, 0.2f);

		// ── Opções ────────────────────────────────────────────────────────────
		String[] labels = pause ? new String[] { "Continue", "继续", "続ける", "Exit" }
				: new String[] { "English", "中文", "日本語", "Exit" };

		float optX = W / 2f - 40;
		float optY = H * 0.4f;
		float step = H * 0.05f;

		for (int i = 0; i < labels.length; i++) {
			float fy = optY + i * step;
			boolean selected = (i == curOption);
			float bright = selected ? 1f : 0.45f;
			float gr = selected ? 1f : 0.45f;
			float bl = selected ? 0f : 0.45f;

			if (selected)
				font.drawText(">", optX - 24, fy, SCALE, 1f, 1f, 0f);
			font.drawText(labels[i], optX, fy, SCALE, bright, gr, bl);
		}
		// instruções
		font.drawText("W/S  ENTER", W - 160, H - 40, SCALE * 0.7f, 0.3f, 0.3f, 0.3f);
	}
}
