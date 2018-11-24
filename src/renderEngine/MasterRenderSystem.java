package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.models.TexturedModel;
import scene.Scene;
import scene.entities.Entity;
import scene.entities.render.EntityRenderSystem;
import scene.skybox.SkyboxRenderSystem;
import scene.terrain.Terrain;
import scene.terrain.render.TerrainRenderSystem;

public class MasterRenderSystem {
	
	private EntityRenderSystem entityRenderer;
	private TerrainRenderSystem terrainRenderer;
	private SkyboxRenderSystem skyboxRenderer;
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	public MasterRenderSystem(Loader loader, Matrix4f projectionMatrix) {
		enableFaceCulling();
		this.entityRenderer = new EntityRenderSystem(projectionMatrix);
		this.terrainRenderer = new TerrainRenderSystem(projectionMatrix);
		this.skyboxRenderer = new SkyboxRenderSystem(projectionMatrix);
	}
	
	public void renderMainPass(Scene scene) {
		float skyR = scene.getSkyR(), skyG = scene.getSkyG(), skyB = scene.getSkyB();
		for (Entity entity : scene.getEntities())
			processEntity(entity);
		for (Terrain terrain : scene.getTerrains())
			terrains.add(terrain);
		prepare();
		skyboxRenderer.render(scene.getCamera(), skyR, skyG, skyB);
		entityRenderer.render(entities, skyR, skyG, skyB, scene.getLights(), scene.getCamera(), scene.getClipPlanePointer());
		disableFaceCulling();
		terrainRenderer.render(terrains, skyR, skyG, skyB, scene.getLights(), scene.getCamera(), scene.getClipPlanePointer());
		//FINISH***********************************************************
		terrains.clear();
		entities.clear();
	}
	
	public void renderMiniMapPass(Scene scene) {
		float skyR = scene.getSkyR(), skyG = scene.getSkyG(), skyB = scene.getSkyB();
		for (Entity entity : scene.getEntities())
			processEntity(entity);
		for (Terrain terrain : scene.getTerrains())
			terrains.add(terrain);
		prepare();
		entityRenderer.render(entities, skyR, skyG, skyB, scene.getLights(), scene.getCamera(), scene.getClipPlanePointer());
		terrainRenderer.render(terrains, skyR, skyG, skyB, scene.getLights(), scene.getCamera(), scene.getClipPlanePointer());
		//FINISH***********************************************************
		terrains.clear();
		entities.clear();
	}
	
	public void cleanUp() {
		entityRenderer.getShaderPointer().cleanUp();
		terrainRenderer.getShaderPointer().cleanUp();
	}
	
	private static void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0, 0, 0, 1);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
	}
	
	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		}
		else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);		
		}
		
		entity.getBoundingBox().minX = entity.getPosition().x + entity.getStaticBoundingBox().minX;
		entity.getBoundingBox().minY = entity.getPosition().y + entity.getStaticBoundingBox().minY;
		entity.getBoundingBox().minZ = entity.getPosition().z + entity.getStaticBoundingBox().minZ;
		entity.getBoundingBox().maxX = entity.getPosition().x + entity.getStaticBoundingBox().maxX;
		entity.getBoundingBox().maxY = entity.getPosition().y + entity.getStaticBoundingBox().maxY;
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
		else
			;
		
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
		else
			;
		
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
		else
			;
		
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
