package engine.scene.terrain.render;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import engine.renderEngine.MasterRenderSystem;
import engine.renderEngine.models.RawModel;
import engine.renderEngine.textures.TerrainTexturePack;
import engine.scene.ICScene;
import engine.scene.terrain.Terrain;
import engine.utils.SFMath;

public class TerrainRenderSystem {
	
	private TerrainShader shader;
	
	public TerrainShader getShader() {
		return shader;
	}
	
	public TerrainRenderSystem(Matrix4f projectionMatrix) {
		this.shader = new TerrainShader();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.connectTextureUnits();
		shader.stop();
	}
	
	public void render(List<Terrain> terrains, ICScene scene) {
		prepare(scene);
		for (Terrain terrain : terrains) {
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			shader.loadBase(terrain.base);
			shader.loadHeight(terrain.getY());
			if (terrain.base) {
				MasterRenderSystem.enableFaceCulling(); 
				GL11.glCullFace(GL11.GL_FRONT);
			}
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(),
					GL11.GL_UNSIGNED_INT, 0);
			unbindTexturedModel();
			MasterRenderSystem.disableFaceCulling();
		}
		shader.stop();
	}
	
	private void prepare(ICScene scene) {
		shader.start();
		shader.loadClipPlane(scene.getClipPlanePointer());
		shader.loadSkyContext(scene.skyCtx);
		shader.loadLights(scene.getLights());
		shader.loadViewMatrix(scene.getCamera());
		shader.loadShineVariables(1, 0);
	}

	private void prepareTerrain(Terrain terrain) {
		RawModel rawModel = terrain.getModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		bindTextures(terrain);
		shader.loadTiling(terrain.getTexturePack().getTiling());
	}

	private void bindTextures(Terrain terrain) {
		
		TerrainTexturePack texturePack = terrain.getTexturePack();	
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBackgroundTexture());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getrTexture());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getgTexture());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getbTexture());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMap());
		
	}
	
	private void unbindTexturedModel() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	private void loadModelMatrix(Terrain terrain) {
		Matrix4f transformationMatrix = SFMath.createTransformationMatrix(
				new Vector3f(terrain.getX(), terrain.getY(), terrain.getZ()), 0, 0, 0, 1);
		shader.loadTransformationMatrix(transformationMatrix);
	}
	
	public void setProjectionMatrix(Matrix4f matrix) {
		shader.start();
		shader.loadProjectionMatrix(matrix);
		shader.stop();
	}	
	
}
