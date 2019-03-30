package engine.scene;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector4f;

import engine.scene.contexts.SkyContext;
import engine.scene.entities.Light;
import engine.scene.entities.TREntity;
import engine.scene.entities.camera.Camera;
import engine.scene.skybox.TRSkybox;
import engine.scene.terrain.TRTerrain;
import engine.water.dudv.DUDVWaterTile;

public class TRScene {
	
	public static final int MAX_LIGHTS = 4;
			
	protected List<TREntity> entities = new ArrayList<TREntity>();
	protected List<TRTerrain> terrains = new ArrayList<TRTerrain>();
	protected List<Light> lights = new ArrayList<Light>();
	protected List<DUDVWaterTile> waters = new ArrayList<DUDVWaterTile>();
	 
	protected Camera camera;
	protected Vector4f clipPlane = new Vector4f();
	
	public boolean useCellShading = false;
	public float numCellLevels = 4.f;
	
	public SkyContext skyCtx;
	
	private TRSkybox skybox;
	
	public TRSkybox getSkybox() {
		return skybox;
	}
	public void setSkybox(TRSkybox skybox) {
		this.skybox = skybox;
	}
	
	private float ambientLight = 0.5f;
	public float getAmbientLight() {
		return ambientLight;
	}
	public void setAmbientLight(float ambientLight) {
		this.ambientLight = ambientLight;
	}
	
	
	public TRScene() {
		this.skyCtx = new SkyContext(0.000075f, 5.f, 0, 0, 0);
	}
	public TRScene(SkyContext skyCtx) {
		this.skyCtx = skyCtx;
	}
	
		
	public Vector4f getClipPlanePointer() {
		return clipPlane;
	}
	
	public void setClipPlanePointer(Vector4f plane) {
		clipPlane = plane;
	}
	
	public void setEntityList(List<TREntity> e) {
		entities = (e);
	}
	
	public void setTerrainList(List<TRTerrain> t) {
		terrains = (t);
	}
	
	public void setLightList(List<Light> l) {
		lights = (l);
	}
	
	public void addLight(Light light) {
		if (lights.size() > MAX_LIGHTS) {
			throw new RuntimeException("caution: more than " + MAX_LIGHTS + " lights in scene");
		}
		else {
			lights.add(light);
		}
	}
	
	public void setCamera(Camera c) {
		this.camera = c;
	}
	
	public List<TREntity> getEntities() {
		return entities;
	}
	
	public List<TRTerrain> getTerrains() {
		return terrains;
	}
	
	public List<Light> getLights() {
		return lights;
	}
	
	public List<DUDVWaterTile> getWaters() {
		return waters;
	}
	
	public Camera getCamera() {
		return camera;
	}

	public float getSkyR() {
		return this.skyCtx.skyR;
	}

	public float getSkyG() {
		return this.skyCtx.skyG;
	}

	public float getSkyB() {
		return this.skyCtx.skyB;
	}
	
}
