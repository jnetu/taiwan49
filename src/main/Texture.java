package main;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {

	private int id;
	private int width;
	private int height;

	public Texture(String resourcePath) {
		load(resourcePath);
	}

	private void load(String resourcePath) {

		ByteBuffer imageBuffer;

		try {
			// LER DO JAR: Transforma o arquivo dentro do JAR em ByteBuffer
			imageBuffer = ioResourceToByteBuffer(resourcePath, 1024);
		} catch (IOException e) {
			throw new RuntimeException("Erro IO ao ler textura: " + resourcePath, e);
		}
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer channels = stack.mallocInt(1);

			// load image
			ByteBuffer image = stbi_load_from_memory(imageBuffer, w, h, channels, 4);
			
			if (image == null) {
				throw new RuntimeException(
						"Falha ao carregar textura: " + resourcePath + " Motivo: " + stbi_failure_reason());
			}

			this.width = w.get(0);
			this.height = h.get(0);

			id = glGenTextures();

			glBindTexture(GL_TEXTURE_2D, id);

			// GL_NEAREST
			// GL_LINEAR
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);

			// free ram
			stbi_image_free(image);
		}
	}
	
	// Método Auxiliar: Pega o arquivo do Classpath (JAR) e joga na Memória Nativa
    private ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer;
        
        // Pega o arquivo de dentro do JAR (começando com /)
        try (InputStream source = Texture.class.getResourceAsStream(resource)) {
            if (source == null) {
                throw new IOException("Arquivo não encontrado no classpath: " + resource);
            }
            
            try (ReadableByteChannel rbc = Channels.newChannel(source)) {
                buffer = BufferUtils.createByteBuffer(bufferSize);

                while (true) {
                    int bytes = rbc.read(buffer);
                    if (bytes == -1) break;
                    if (buffer.remaining() == 0) {
                        buffer = resizeBuffer(buffer, buffer.capacity() * 2);
                    }
                }
            }
        }
        buffer.flip();
        return buffer;
    }
    private ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, id);
	}

	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}