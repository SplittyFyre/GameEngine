package engine.scene.terrain.render;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import engine.renderEngine.TRFrustumCuller;
import engine.renderEngine.models.TerrainLODModel;
import engine.renderEngine.textures.TRTerrainTexturePack;
import engine.scene.TRScene;
import engine.scene.entities.camera.TRCamera;
import engine.scene.terrain.TRTerrain;
import engine.utils.TRMath;

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
	
	public void render(List<TRTerrain> terrains, TRFrustumCuller frustumCuller, TRScene scene) {
		prepare(scene);
		int culled = 0;
		for (TRTerrain terrain : terrains) {
			
			if (!frustumCuller.isSphereOutside(terrain.getX() + (terrain.size / 2f), terrain.getY(), terrain.getZ() + (terrain.size / 2f), Math.max(terrain.getMaxHeight(), terrain.size_times_sqrt2_div_2))) {
				prepareTerrain(terrain, scene.getCamera());
				loadModelMatrix(terrain);
				GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(),
						GL11.GL_UNSIGNED_INT, 0);
				unbindTexturedModel();
			}
			else {
				culled++;
			}
		}
		shader.stop();
	}
	
	private void prepare(TRScene scene) {
		shader.start();
		shader.loadClipPlane(scene.getClipPlanePointer());
		shader.loadSkyContext(scene.skyCtx);
		shader.loadLights(scene.getLights());
		shader.loadViewMatrix(scene.getCamera());
		shader.loadShineVariables(1, 0);
		shader.loadAmbientLight(scene.getAmbientLight());
		shader.loadLightsInUse(scene.getLights().size());
	}

	private void prepareTerrain(TRTerrain terrain, TRCamera camera) {
		TerrainLODModel rawModel = terrain.getModel();
		
		float distance = TRMath.distance(new Vector2f(terrain.getX(), terrain.getZ()), new Vector2f(camera.getPosition().x, camera.getPosition().z));
		
		if (distance > 20000) {
			rawModel.ensureLODLevel(3);
		}
		else if (distance > 12500) {
			rawModel.ensureLODLevel(2);
		}
		else if (distance > 7500) {
			rawModel.ensureLODLevel(1);
		}
		else {
			rawModel.ensureLODLevel(0);
		}
		
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		bindTextures(terrain);
		shader.loadTiling(terrain.getTexturePack().getTiling());
		TRTerrainTexturePack pack = terrain.getTexturePack();
		shader.loadUseAltitudeVarying(pack.useAsAltitudeBasedTextures);
		shader.loadHeightTextureCaps(pack.cap1, pack.cap2, pack.cap3);
		shader.loadMaxHeight(terrain.getMaxHeight()); // local max height
		shader.loadYOffset(terrain.getY());
	}

	private void bindTextures(TRTerrain terrain) {
		
		TRTerrainTexturePack texturePack = terrain.getTexturePack();	
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

	private void loadModelMatrix(TRTerrain terrain) {
		Matrix4f transformationMatrix = TRMath.createTransformationMatrix(
				new Vector3f(terrain.getX(), terrain.getY(), terrain.getZ()), 0, 0, 0, 1);
		shader.loadTransformationMatrix(transformationMatrix);
	}
	
	public void setProjectionMatrix(Matrix4f matrix) {
		shader.start();
		shader.loadProjectionMatrix(matrix);
		shader.stop();
	}	
	
}
