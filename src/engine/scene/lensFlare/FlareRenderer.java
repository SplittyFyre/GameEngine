package engine.scene.lensFlare;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;

import engine.renderEngine.GLQuery;
import engine.renderEngine.Loader;
import engine.renderEngine.TRDisplayManager;
import engine.renderEngine.models.RawModel;

public class FlareRenderer {
	
	private RawModel flareQuad;
	private FlareModeShader shader;
	
	private final GLQuery occlusionQuery;
	
	private float coverage = 0;
	
	private final float occludeTestWidth, occludeTestHeight;
	
	public FlareRenderer(float occludeTestWidth) {
		shader = new FlareModeShader();
		this.occlusionQuery = new GLQuery(GL15.GL_SAMPLES_PASSED);
		this.occludeTestWidth = occludeTestWidth;
		this.occludeTestHeight = occludeTestWidth * TRDisplayManager.getAspectRatio();
		float[] flarePos = {-0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f};
		flareQuad = Loader.loadToVAO(flarePos);
	}
	
	private void performOcclusionTest(Vector2f sunScreenPos) {
		
		if (occlusionQuery.isResultReady()) {
			int visibleSamples = occlusionQuery.getResult();
			System.out.println(visibleSamples);
			this.coverage = visibleSamples / 12544.f;
		}
		
		if (!occlusionQuery.isInUse) {
			//GL11.glColorMask(false, false, false, false);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			//GL11.glDepthFunc(GL11.GL_LESS);
			//GL11.glDepthMask(false);
			occlusionQuery.start();
			shader.loadTransformation(sunScreenPos.x, sunScreenPos.y, occludeTestWidth, occludeTestHeight);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
			occlusionQuery.end();
			//GL11.glColorMask(true, true, true, true);
			//GL11.glDepthMask(true);

		}
	}
	
	public void renderLensFlare(Vector2f sunScreenPos, FlareTexture[] textures, float brightness) {
		
		shader.start();
		
		shader.loadBrightness(brightness * this.coverage);
		
		GL30.glBindVertexArray(flareQuad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
				

		performOcclusionTest(sunScreenPos);
		
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		
		for (FlareTexture texture : textures) {
			
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
			
			float xScale = texture.getScale();
	        float yScale = xScale * Display.getWidth() / Display.getHeight();
	        Vector2f centerPos = texture.getPosition();
	        shader.loadTransformation(centerPos.x, centerPos.y, xScale, yScale);
	        GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
			
		}
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	public void cleanUp() {
		occlusionQuery.delete();
		shader.cleanUp();
	}
	
}
