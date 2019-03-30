package engine.renderEngine.guis.render;

import java.util.List;

import javax.activity.InvalidActivityException;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import engine.renderEngine.Loader;
import engine.renderEngine.MasterRenderSystem;
import engine.renderEngine.guis.GUITexture;
import engine.renderEngine.models.RawModel;
import engine.scene.lensFlare.FlareTexture;
import engine.utils.SFMath;

public class GUIRenderer {
	
	private final RawModel quad;
	private RawModel flareQuad;
	private GUIShader shader;
	
	private FlareModeShader flareShader = null;
	
	public GUIRenderer(boolean supportFlareRendering) {
		float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
		quad = Loader.loadToVAO(positions);
		shader = new GUIShader();
		if (supportFlareRendering) {
			flareShader = new FlareModeShader();
			float[] flarePos = {-0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f};
			flareQuad = Loader.loadToVAO(flarePos);
		}
	}
	
	public void render(List<GUITexture> guis) {
		MasterRenderSystem.disableFaceCulling();
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		for (GUITexture texture : guis) {
			
			shader.loadAlphaFlag(texture.useCustomAlpha);
			shader.loadCustomAlpha(texture.customAlpha);
			
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
		MasterRenderSystem.enableFaceCulling();
	}
	
	public void renderFlareMode(FlareTexture[] textures, float brightness) throws InvalidActivityException {
		
		if (flareShader == null) {
			throw new InvalidActivityException(" ");
		}
		
		flareShader.start();
		GL30.glBindVertexArray(flareQuad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		flareShader.loadBrightness(brightness);
		for (FlareTexture texture : textures) {
			
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
			
			float xScale = texture.getScale();
	        float yScale = xScale * Display.getWidth() / Display.getHeight();
	        Vector2f centerPos = texture.getPosition();
	        flareShader.loadTransformation(new Vector4f(centerPos.x, centerPos.y, xScale, yScale));
	        GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
			
		}
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		flareShader.stop();
	}
	
	public void cleanUp() {
		shader.cleanUp();
		if (flareShader != null) {
			flareShader.cleanUp();
		}
	}

}
