package engine.scene.skybox;

import javax.management.RuntimeErrorException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import engine.scene.TRScene;
import engine.scene.contexts.SkyContext;

public class SkyboxRenderSystem {
	
	private SkyboxShader shader;
		
	public SkyboxRenderSystem(Matrix4f projMatrix) {
		shader = new SkyboxShader();
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projMatrix);
		shader.stop();
	}
	
	public void render(TRScene scene) {
		SkyContext ctx = scene.skyCtx;
		
		TRSkybox skybox = scene.getSkybox();
		if (skybox.getTexture1() == null) {
			throw new RuntimeErrorException(null, "skybox texture 1 cannot be null");
		}
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_SRC_ALPHA);
		shader.start();
		//GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		skybox.rotate();
		shader.loadViewMatrix(scene.getCamera(), skybox.rotation);
		shader.loadFogColour(ctx.skyR, ctx.skyG, ctx.skyB);
		shader.loadFadeLimits(skybox.lowerFadeBound, skybox.upperFadeBound);
		shader.loadBloomEffect(skybox.getBloomEffect());
		GL30.glBindVertexArray(skybox.getCube().getVaoID());
		GL20.glEnableVertexAttribArray(0);
		
		bindTextures(skybox);
		
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, skybox.getCube().getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
		//GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	// GL_TEXTURE(%d) are consecutive
	private void bindTextures(TRSkybox skybox) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, skybox.getTexture1().getTexture());
		
		boolean b = true;
		if (skybox.getTexture2() != null) {
			GL13.glActiveTexture(GL13.GL_TEXTURE1);
			GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, skybox.getTexture2().getTexture());
			b = false;
		}
		shader.loadHasOnlyOneTexture(b); 
		shader.loadBlendFactor(skybox.getBlend());
	}
	
	public SkyboxShader getShader() {
		return shader;
	}
	
}
