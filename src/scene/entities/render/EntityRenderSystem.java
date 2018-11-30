package scene.entities.render;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import renderEngine.MasterRenderSystem;
import renderEngine.models.RawModel;
import renderEngine.models.TexturedModel;
import renderEngine.textures.ModelTexture;
import scene.entities.Entity;
import scene.entities.Light;
import scene.entities.camera.Camera;
import utils.SFMath;

public class EntityRenderSystem {
	
	private EntityShader shader;
	
	public EntityShader getShaderPointer() {
		return shader;
	}
	
	public EntityRenderSystem(Matrix4f projectionMatrix) {
		this.shader = new EntityShader();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(Map<TexturedModel, List<Entity>> entities, 
			float skyR, float skyG, float skyB, List<Light> lights, Camera camera, Vector4f clipPlane) { 
		prepare(skyR, skyG, skyB, lights, camera, clipPlane);
		for (TexturedModel model : entities.keySet()) {
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			
			/*
			 * Here, we have all of the entities, each with the same model, why not render instanced here?
			 * Maybe?
			 * Perhaps if there are only 1 or 2 entities here then it would be inefficient
			 * Either: instanced for EVERYTHING, no instanced, either-or, XOR, monostable XD
			 * */
			
			for (Entity entity : batch) {
				prepareInstance(entity);
				if (entity.translucent) {
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glBlendFunc(GL11.GL_ONE_MINUS_SRC_COLOR, GL11.GL_ONE);
					//GL11.glBlendFunc(GL11.GL_ONE_MINUS_SRC_COLOR, GL11.GL_ONE_MINUS_SRC_ALPHA);
				}
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(),
						GL11.GL_UNSIGNED_INT, 0);
				GL11.glDisable(GL11.GL_BLEND);
			}
			unbindTexturedModel();
		}
		shader.stop();
	}
	
	private void prepare(float skyR, float skyG, float skyB, List<Light> lights, Camera camera, Vector4f clipPlane) {
		shader.start();
		shader.loadClipPlane(clipPlane);
		shader.loadSkyColour(skyR, skyG, skyB);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		shader.connectTextureUnits();
	}
	
	private void prepareTexturedModel(TexturedModel model) {
		
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		ModelTexture texture = model.getTexture();
		shader.loadNumberOfRows(texture.getNumRows());
		
		if (texture.isTransparent()) 
			MasterRenderSystem.disableFaceCulling();
		
		shader.loadFakeLight(texture.isUseFakeLighting());
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
		shader.loadUseSpecularMap(texture.hasSpecularMap());
		shader.loadBrightDamper(texture.getBrightDamper());
		if (texture.hasSpecularMap()) {
			GL13.glActiveTexture(GL13.GL_TEXTURE1);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getSpecularMap());
		}
	}

	private void unbindTexturedModel() {
		MasterRenderSystem.enableFaceCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	private void prepareInstance(Entity entity) {
		
		Matrix4f transformationMatrix;
		
		if (entity.customRotationAxis && entity.customOrigin != null) {
			if (entity.ignoreRY) {
				transformationMatrix = SFMath.createTransformationMatrix(new Vector3f(entity.getPosition()), entity.customOrigin,
						entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale(), true);
			}
			else {
				transformationMatrix = SFMath.createTransformationMatrix(new Vector3f(entity.getPosition()), entity.customOrigin,
						entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());	
			}
		}
		else {
			transformationMatrix = SFMath.createTransformationMatrix(entity.getPosition(),
					entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		}
		
		shader.loadTransformationMatrix(transformationMatrix);
		shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
		shader.loadHighlight(entity.getHighlight());
	
		/*Vector4f mins = new Vector4f(entity.getStaticBoundingBox().minX, 
									 entity.getStaticBoundingBox().minY,
									 entity.getStaticBoundingBox().minZ, 1);
		
		Vector4f maxs = new Vector4f(entity.getStaticBoundingBox().maxX, 
				 					 entity.getStaticBoundingBox().maxY,
				 					 entity.getStaticBoundingBox().maxZ, 1);
		
		Matrix4f.transform(transformationMatrix, mins, mins);
		Matrix4f.transform(transformationMatrix, maxs, maxs);
		
		entity.getBoundingBox().minX = mins.x;
		entity.getBoundingBox().minY = mins.y;
		entity.getBoundingBox().minZ = mins.z;
		
		entity.getBoundingBox().maxX = maxs.x;
		entity.getBoundingBox().maxY = maxs.y;
		entity.getBoundingBox().maxZ = maxs.z;*/
		
	}
	
	public void setProjectionMatrix(Matrix4f matrix) {
		shader.start();
		shader.loadProjectionMatrix(matrix);
		shader.stop();
	}

}
