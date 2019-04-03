package engine.renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import engine.postProcessing.Fbo;
import engine.renderEngine.models.TexturedModel;
import engine.scene.TRScene;
import engine.scene.entities.TREntity;
import engine.scene.entities.camera.Camera;
import engine.scene.entities.render.EntityRenderSystem;
import engine.scene.particles.ParticleWatcher;
import engine.scene.skybox.SkyboxRenderSystem;
import engine.scene.terrain.render.TerrainRenderSystem;
import engine.water.dudv.DUDVWaterRenderer;
import engine.water.dudv.DUDVWaterTile;
import engine.water.dudv.WaterFrameBuffers;

public class MasterRenderSystem {
	
	private static final int RENDER_ENTITIES_BIT = 1;
	private static final int RENDER_TERRAIN_BIT = 2;
	private static final int RENDER_SKYBOX_BIT = 4;
	private static final int RENDER_DUDVWATER_BIT = 8;
	private final int renderSupportMask;
	
	private EntityRenderSystem entityRenderer;
	private TerrainRenderSystem terrainRenderer;
	private SkyboxRenderSystem skyboxRenderer;
	private DUDVWaterRenderer waterRenderer;
	private Map<TexturedModel, List<TREntity>> entities = new HashMap<TexturedModel, List<TREntity>>();
	
	private boolean isRFAvailable(int mask) {
		return (renderSupportMask & mask) != 0;
	}
	
	public MasterRenderSystem(int renderSupportMask, Matrix4f projectionMatrix) {
		this.renderSupportMask = renderSupportMask;
		enableFaceCulling();
		if (isRFAvailable(RENDER_ENTITIES_BIT))
			this.entityRenderer = new EntityRenderSystem(projectionMatrix);
		if (isRFAvailable(RENDER_TERRAIN_BIT))
			this.terrainRenderer = new TerrainRenderSystem(projectionMatrix);
		if (isRFAvailable(RENDER_SKYBOX_BIT))
			this.skyboxRenderer = new SkyboxRenderSystem(projectionMatrix);
		if (isRFAvailable(RENDER_DUDVWATER_BIT))
			this.waterRenderer = new DUDVWaterRenderer(projectionMatrix);
	}
	
	private void renderWithoutWater(TRScene scene) {
		for (TREntity entity : scene.getEntities())
			processEntity(entity);
		prepare();
		
		if (Keyboard.isKeyDown(Keyboard.KEY_F3))
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		
		skyboxRenderer.render(scene);
		entityRenderer.render(entities, scene);
		terrainRenderer.render(scene.getTerrains(), scene);
		//FINISH***********************************************************
		entities.clear();
	}
	
	public void renderMainPass(TRScene scene, Fbo fbo) {
		
		Camera camera = scene.getCamera();
		
		if (scene.getWaters().size() != 0) {
			WaterFrameBuffers buffers = waterRenderer.getFBOs();
			DUDVWaterTile water = scene.getWaters().get(0);

			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
			scene.setClipPlanePointer(new Vector4f(0, -1, 0, 15));
			buffers.bindReflectionFrameBuffer();
			float distance = 2 * (camera.getPosition().y - water.getHeight());
			camera.getPosition().y -= distance;
			camera.invertPitch();
			scene.setClipPlanePointer(new Vector4f(0, 1, 0, -water.getHeight() + 0.07f));
			renderWithoutWater(scene);
			camera.getPosition().y += distance;
			camera.invertPitch();
			buffers.bindRefractionFrameBuffer();
			scene.setClipPlanePointer(new Vector4f(0, -1, 0, water.getHeight() + 0.07f));
			renderWithoutWater(scene);
			buffers.unbindCurrentFrameBuffer();
			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
		}	
		
		if (fbo != null)
			fbo.bindFrameBuffer();
		
		renderWithoutWater(scene);
		
		if (scene.getWaters().size() != 0) {
			waterRenderer.render(scene.getWaters(), camera, scene.getLights().get(0));
		}
		
		ParticleWatcher.renderParticles(camera);
		
		if (fbo != null)
			fbo.unbindFrameBuffer();
	}

	public void cleanUp() {
		entityRenderer.getShader().cleanUp();
		terrainRenderer.getShader().cleanUp();
		skyboxRenderer.getShader().cleanUp();
		waterRenderer.cleanUp();
	}
	
	private static void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0, 0, 0, 1);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
	}
	
	public void processEntity(TREntity entity) {
		TexturedModel entityModel = entity.getModel();
		List<TREntity> batch = entities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		}
		else {
			List<TREntity> newBatch = new ArrayList<TREntity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);		
		}
		
		entity.getBoundingBox().minX = entity.getPosition().x + entity.getStaticBoundingBox().minX;
		entity.getBoundingBox().minY = entity.getPosition().y + entity.getStaticBoundingBox().minY + entity.bbyoffset;
		entity.getBoundingBox().minZ = entity.getPosition().z + entity.getStaticBoundingBox().minZ;
		entity.getBoundingBox().maxX = entity.getPosition().x + entity.getStaticBoundingBox().maxX;
		entity.getBoundingBox().maxY = entity.getPosition().y + entity.getStaticBoundingBox().maxY + entity.bbyoffset;
		entity.getBoundingBox().maxZ = entity.getPosition().z + entity.getStaticBoundingBox().maxZ;
		
		Vector3f vec = entity.getScale();
		
		if (vec.x > 1) {
			float modX = (vec.x - 1) * (entity.getBoundingBox().maxX - entity.getBoundingBox().minX) / 2;
			entity.getBoundingBox().minX -= modX;
			entity.getBoundingBox().maxX += modX;
		}
		else if (vec.x < 1) {
			float modX = (1 - vec.x) * (entity.getBoundingBox().maxX - entity.getBoundingBox().minX) / 2;
			entity.getBoundingBox().minX -= modX;
			entity.getBoundingBox().maxX += modX;
		}
		
		if (vec.y > 1) {
			float modY = (vec.y - 1) * (entity.getBoundingBox().maxY - entity.getBoundingBox().minY) / 2;
			entity.getBoundingBox().minY -= modY;
			entity.getBoundingBox().maxY += modY;
		}
		else if (vec.y < 1) {
			float modY = (1 - vec.y) * (entity.getBoundingBox().maxY - entity.getBoundingBox().minY) / 2;
			entity.getBoundingBox().minY -= modY;
			entity.getBoundingBox().maxY += modY;
		}
		
		if (vec.z > 1) {
			float modZ = (vec.z - 1) * (entity.getBoundingBox().maxZ - entity.getBoundingBox().minZ) / 2;
			entity.getBoundingBox().minZ -= modZ;
			entity.getBoundingBox().maxZ += modZ;
		}
		else if (vec.z < 1) {
			float modZ = (1 - vec.z) * (entity.getBoundingBox().maxZ - entity.getBoundingBox().minZ) / 2;
			entity.getBoundingBox().minZ -= modZ;
			entity.getBoundingBox().maxZ += modZ;
		}
		
	}
	
	public void setProjectionMatrix(Matrix4f matrix) {
		entityRenderer.setProjectionMatrix(matrix);
		terrainRenderer.setProjectionMatrix(matrix);
	}
	
	public static void enableFaceCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableFaceCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

}
