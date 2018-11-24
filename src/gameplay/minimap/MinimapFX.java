package gameplay.minimap;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import postProcessing.ImageRenderer;
import renderEngine.Loader;
import renderEngine.models.RawModel;

public class MinimapFX {
	
	private final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };
	private ImageRenderer imgrenderer;
	private MinimapShader shader;
	private RawModel quad;
	
	public MinimapFX() {
		imgrenderer = new ImageRenderer(Display.getWidth(), Display.getHeight());
		shader = new MinimapShader();
		quad = Loader.loadToVAO(POSITIONS, 2);
	}
	
	public void processMinimap(int texture) {
		start();
		shader.start();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		imgrenderer.renderQuad();
		shader.stop();
		end();
	}
	
	private void start() {
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	private static void end() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
	
	public int getOutputTexture() {
		return imgrenderer.getOutputTexture();
	}
	
	public void cleanUp() {
		shader.cleanUp();
		imgrenderer.cleanUp();
	}

}
