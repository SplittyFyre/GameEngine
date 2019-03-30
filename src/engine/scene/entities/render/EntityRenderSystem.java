package engine.scene.entities.render;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import engine.renderEngine.MasterRenderSystem;
import engine.renderEngine.models.RawModel;
import engine.renderEngine.models.TexturedModel;
import engine.renderEngine.textures.ModelTexture;
import engine.scene.TRScene;
import engine.scene.entities.TREntity;
import engine.utils.SFMath;

public class EntityRenderSystem {
	
	private EntityShader shader;
	
	private int instanceVBO;
	
	public EntityShader getShader() {
		return shader;
	}
	
	public EntityRenderSystem(Matrix4f projectionMatrix) {
		this.shader = new EntityShader();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(Map<TexturedModel, List<TREntity>> entities, TRScene scene) { 
		prepare(scene);
		for (TexturedModel model : entities.keySet()) {
			prepareTexturedModel(model);
			List<TREntity> batch = entities.get(model);
			
			/*
			 * Here, we have all of the entities, each with the same model, why not render instanced here?
			 * */
			
			for (TREntity entity : batch) {
				prepareInstance(entity);

				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(),
						GL11.GL_UNSIGNED_INT, 0);
				GL11.glDisable(GL11.GL_BLEND);
			}
			unbindTexturedModel();
		}
		shader.stop();
	}
	
	private void prepare(TRScene scene) {
		shader.start();
		shader.loadClipPlane(scene.getClipPlanePointer());
		shader.loadSkyContext(scene.skyCtx);
		shader.loadLights(scene.getLights());
		shader.loadViewMatrix(scene.getCamera());
		shader.loadAmbientLight(scene.getAmbientLight());
		shader.loadLightsInUse(scene.getLights().size());
		
		shader.loadCellShadingStatus(scene.useCellShading, scene.numCellLevels);
		
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

	private void prepareInstance(TREntity entity) {
		
		Matrix4f transformationMatrix;
		
		if (entity.useCustomRotationAxis && entity.customRotationAxis != null) {
			/*if (entity.ignoreRY) {
				transformationMatrix = SFMath.createTransformationMatrix(new Vector3f(entity.getPosition()), entity.customRotationAxis,
						entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale(), true);
			}
			else {
				transformationMatrix = SFMath.createTransformationMatrix(new Vector3f(entity.getPosition()), entity.customRotationAxis,
						entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());	
			}*/
			transformationMatrix = SFMath.createTransformationMatrix(new Vector3f(entity.getPosition()), entity.customRotationAxis,
					entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		}
		else {
			transformationMatrix = SFMath.createTransformationMatrix(entity.getPosition(),
					entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		}
		
		if (entity.useParentTransform) {
			Matrix4f.mul(SFMath.createTransformationMatrix(entity.parentTransform.getPosition(),
					entity.parentTransform.getRotX(), entity.parentTransform.getRotY(), entity.parentTransform.getRotZ(), entity.parentTransform.getScale()),
					transformationMatrix, transformationMatrix);
		}
		
		shader.loadTransformationMatrix(transformationMatrix);
		shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
	
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
