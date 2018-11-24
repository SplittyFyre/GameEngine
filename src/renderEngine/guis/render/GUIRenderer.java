package renderEngine.guis.render;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import renderEngine.Loader;
import renderEngine.models.RawModel;
import renderEngine.textures.GUITexture;
import utils.SFMath;

public class GUIRenderer {
	
	private final RawModel quad;
	private GUIShader shader;
	
	public GUIRenderer() {
		float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
		quad = Loader.loadToVAO(positions);
		shader = new GUIShader();
	}
	
	public void render(List<GUITexture> guis) {
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		for (GUITexture texture : guis) {
			
			shader.loadAlphaFlag(texture.flagAlpha);
			shader.loadCustomAlpha(texture.custAlpha);
			
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
			Matrix4f matrix = SFMath.createTransformationMatrix(texture.getPosition(), texture.getScale(), texture.getRotation(), texture.isFlipped());
			shader.loadTransformation(matrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}

}
