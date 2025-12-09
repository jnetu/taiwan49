package graphics;

import static org.lwjgl.opengl.GL11.*;

import main.Texture;

public class TextureRenderer {

	public static void drawTexture(Texture texture, float x, float y, float width, float height) {
        glColor3f(1.0f, 1.0f, 1.0f); // Reset de cor para branco
        
        texture.bind();
        
        glBegin(GL_QUADS);
            // Topo Esquerdo
            glTexCoord2f(0, 0); 
            glVertex2f(x, y);
            
            // Topo Direito
            glTexCoord2f(1, 0); 
            glVertex2f(x + width, y);
            
            // Baixo Direito
            glTexCoord2f(1, 1); 
            glVertex2f(x + width, y + height);
            
            // Baixo Esquerdo
            glTexCoord2f(0, 1); 
            glVertex2f(x, y + height);
        glEnd();
        
        texture.unbind();
    }
	
	public static void fillRect(float x, float y, float width, float height, float r, float g, float b) {
        
		/*
		 * red square glColor3f(1.0f, 0.0f, 0.0f); glBegin(GL_QUADS); glVertex2f(50,
		 * 50); glVertex2f(150, 50); glVertex2f(150, 150); glVertex2f(50, 150); glEnd();
		 */
		
		glDisable(GL_TEXTURE_2D); // Garante que não vai tentar mapear textura
        glColor3f(r, g, b);
        
        glBegin(GL_QUADS);
            glVertex2f(x, y);
            glVertex2f(x + width, y);
            glVertex2f(x + width, y + height);
            glVertex2f(x, y + height);
        glEnd();
        
        glEnable(GL_TEXTURE_2D); // Reabilita para as próximas chamadas
    }
}
