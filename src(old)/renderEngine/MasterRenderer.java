package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import shaders.StaticShader;
import shaders.TerrainShader;
import skybox.SkyboxRenderer;
import terrains.Terrain;

public class MasterRenderer {
	
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 1f;
	private static final float FAR_PLANE = 5000;
	
	private static float fovCoefficient = 0.5f;

	private static final float RED = 0.1f;
	private static final float GREEN = 0.1f;
	private static final float BLUE = 0.1f;
	
	private Matrix4f projectionMatrix;
	
	private StaticShader shader = new StaticShader();
	private EntityRenderer renderer;
	
	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader();
	
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	private SkyboxRenderer skyboxRenderer;
	
	public MasterRenderer(Loader loader){
		enableFaceCulling();
		createProjectionMatrix();
		renderer = new EntityRenderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
		
	}
	
	public static void enableFaceCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableFaceCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	public void renderAll(List<Entity> entities, List<Terrain> terrains, List<Light> lights, Camera cam) {
		
		for (Terrain terrain : terrains) 
			processTerrain(terrain);
			
		for (Entity entity : entities) 
			processEntity(entity);
		
		render(lights, cam);	
	}
	
	public void render(List<Light> lights, Camera camera) {
		prepare();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		skyboxRenderer.render(camera, RED, GREEN, BLUE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		shader.start();
		shader.loadSkyColour(RED, GREEN, BLUE);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		terrainShader.start();
		terrainShader.loadSkyColour(RED, GREEN, BLUE);
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		terrains.clear();
		entities.clear();
	}
	
	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
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
		
		if (entity.getScale() > 1) {
			
			float modifierX = ((entity.getScale() - 1) * (entity.getBoundingBox().maxX - entity.getBoundingBox().minX)) / 2;
			float modifierY = ((entity.getScale() - 1) * (entity.getBoundingBox().maxY - entity.getBoundingBox().minY)) / 2;
			float modifierZ = ((entity.getScale() - 1) * (entity.getBoundingBox().maxZ - entity.getBoundingBox().minZ)) / 2;
			
			entity.getBoundingBox().minX -= modifierX;
			entity.getBoundingBox().maxX += modifierX;
			entity.getBoundingBox().minY -= modifierY;
			entity.getBoundingBox().maxY += modifierY;
			entity.getBoundingBox().minZ -= modifierZ;
			entity.getBoundingBox().maxZ += modifierZ;
			
		}
		else if (entity.getScale() < 1) {
			
			float modifierX = ((1 - entity.getScale()) * (entity.getBoundingBox().maxX - entity.getBoundingBox().minX)) / 2;
			float modifierY = ((1 - entity.getScale()) * (entity.getBoundingBox().maxY - entity.getBoundingBox().minY)) / 2;
			float modifierZ = ((1 - entity.getScale()) * (entity.getBoundingBox().maxZ - entity.getBoundingBox().minZ)) / 2;
			
			entity.getBoundingBox().minX -= modifierX;
			entity.getBoundingBox().maxX += modifierX;
			entity.getBoundingBox().minY -= modifierY;
			entity.getBoundingBox().maxY += modifierY;
			entity.getBoundingBox().minZ -= modifierZ;
			entity.getBoundingBox().maxZ += modifierZ;
			
		}
		else
			;
		
	}
	
	public void cleanUp(){
		shader.cleanUp();
		terrainShader.cleanUp();
	}
	
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(RED, GREEN, BLUE, 1);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
	}
	
	private void createProjectionMatrix() {
		
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV * fovCoefficient))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	
	public static float getFovCoefficient() {
		return fovCoefficient;
	}

	public static void setFovCoefficient(float fovCoefficient) {
		MasterRenderer.fovCoefficient = fovCoefficient;
	}

}
