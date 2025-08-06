package net.jneto.taiwan49;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen {
	
	public final boolean OLED = false;
	

    private final Main game;

    // --- NOVOS OBJETOS PARA CÂMERA E VIEWPORT ---
    private OrthographicCamera camera;
    private Viewport viewport;
    
    // --- TAMANHO FIXO DO NOSSO MUNDO VIRTUAL ---
    private static final float WORLD_WIDTH = 800;
    private static final float WORLD_HEIGHT = 600;

    // Ferramentas de desenho
    private SpriteBatch batch;
    private BitmapFont font;
    private GlyphLayout layout;
    
    
 // --- NOSSO NOVO FUNDO VERDE ---
    private Texture greenBackground;
    
    
    
 // --- FERRAMENTA DE DESENHO DE FORMAS ---
    private ShapeRenderer shapeRenderer;

    /**
     * O construtor agora aceita a instância do jogo.
     * @param game A instância principal da classe Main.
     */
    public FirstScreen(final Main game) {
        this.game = game;

        // 1. Cria a câmera e o viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        
        // 2. Inicializa as ferramentas de desenho
        batch = new SpriteBatch();
        font = new BitmapFont(); 
        font.setColor(Color.WHITE);
        layout = new GlyphLayout();
        
     // --- CRIA A TEXTURA DO FUNDO VERDE ---
        // 1. Cria um Pixmap (mapa de pixels na memória RAM) de 1x1.
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        // 2. Define a cor do pixmap como o nosso verde.
        pixmap.setColor(0, 0.6f, 0.2f, 1);
        pixmap.fill(); // Preenche o pixmap com a cor definida.
        // 3. Cria uma Textura (que vai para a memória da GPU) a partir do pixmap.
        greenBackground = new Texture(pixmap);
        // 4. Libera o pixmap da memória RAM, pois não precisamos mais dele.
        pixmap.dispose();
        
        
        
        // --- iniciar o shaperenderer
        shapeRenderer = new ShapeRenderer();
        
        
        
        
        //FONTE PERSONALIZADA ---
     // 1. Aponta para o nosso arquivo .ttf na pasta assets.
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Roboto-Regular.ttf"));
        
        // 2. Cria um objeto de parâmetros para configurar a fonte.
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 40; // Define o tamanho da fonte em pixels. Quanto maior, mais nítida.
        parameter.color = Color.YELLOW; // Vamos deixá-la amarela.
        parameter.borderColor = Color.BLACK; // Cor da borda
        parameter.borderWidth = 1; // Largura da borda
        
        // --- A MÁGICA PARA DEIXAR SUAVE (NÃO PIXELADA) ---
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        // --------------------------------------------------

        // 3. Gera o objeto BitmapFont e o atribui à nossa variável.
        font = generator.generateFont(parameter);
        
        // 4. Libera o gerador da memória, pois não precisamos mais dele.
        generator.dispose();
        
    }

    @Override
    public void render(float delta) {
        // Limpa a tela
    	
    	if(OLED) {
    		ScreenUtils.clear(0, 0, 0, 1);
    	}else {
    		ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1);
    	}
        
        
        
        
        
        
     // --- DESENHANDO AS FORMAS (COM SHAPERENDERER) ---
        // Começamos o desenho de formas
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled); // Queremos formas preenchidas
        
        // 3. Desenha o fundo verde
        shapeRenderer.setColor(0, 0.6f, 0.2f, 1);
        shapeRenderer.rect(0, 0, WORLD_WIDTH, WORLD_HEIGHT); // Desenha um retângulo
        
        // 4. Desenha um pequeno retângulo vermelho para exemplificar
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(0, 0, 800, 10); // Posição (x,y) e Tamanho (largura, altura)

        // Terminamos o desenho de formas
        shapeRenderer.end();
        
        
        
        
        
        
        
        // 3. ATUALIZA A CÂMERA E APLICA O VIEWPORT AO BATCH
        // Isso diz ao SpriteBatch para desenhar no sistema de coordenadas do nosso mundo, não em pixels.
        batch.setProjectionMatrix(camera.combined);
        
        
        
     
        
        

        // Inicia o lote de desenho
        batch.begin();
        //batch.draw(greenBackground, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        // Agora, usamos as coordenadas do nosso MUNDO VIRTUAL para posicionar o texto
        String meuTexto = "Modo OLED desligado!";
        if(OLED) {
        	meuTexto = "Modo OLED ligado!";
    	}
        
        layout.setText(font, meuTexto);
        float textWidth = layout.width;
        
        
        
        font.getData().setScale(1.0f);
        // O texto será desenhado no centro do MUNDO de 800x600 unidades.
        font.draw(batch, meuTexto,
                  (WORLD_WIDTH - textWidth) / 2, // Centro X do mundo
                  WORLD_HEIGHT / 2);             // Centro Y do mundo
        
        // Finaliza o lote
        batch.end();
        
     
        
     
    }

    @Override
    public void resize(int width, int height) {
        // 4. ATUALIZA O VIEWPORT QUANDO A JANELA MUDA DE TAMANHO
        // Esta é a parte mais importante! Ele recalcula as barras pretas e o posicionamento.
        // O 'true' no final centraliza a câmera.
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        // Libera os recursos
        batch.dispose();
        font.dispose();
    }
    
    // --- O resto dos métodos pode permanecer vazio ---

    @Override
    public void show() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }
}