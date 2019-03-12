package engine.scene;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector4f;

import engine.scene.contexts.SkyContext;
import engine.scene.entities.Entity;
import engine.scene.entities.Light;
import engine.scene.entities.camera.Camera;
import engine.scene.terrain.Terrain;
import engine.water.WaterTile;

public class ICScene {
			
	protected List<Entity> entities = new ArrayList<Entity>();
	protected List<Terrain> terrains = new ArrayList<Terrain>();
	protected List<Light> lights = new ArrayList<Light>();
	protected List<WaterTile> waters = new ArrayList<WaterTile>();
	 
	protected Camera camera;
	protected Vector4f clipPlane = new Vector4f();
	
	public boolean useCellShading = false;
	public float numCellLevels = 4.f;
	
	public SkyContext skyCtx;
	
	
	
	public ICScene() {
		this.skyCtx = new SkyContext(0.000075f, 5.f, 0, 0, 0);
	}
	public ICScene(SkyContext skyCtx) {
		this.skyCtx = skyCtx;
	}
	
		
	public Vector4f getClipPlanePointer() {
		return clipPlane;
	}
	
	public void setClipPlanePointer(Vector4f plane) {
		clipPlane = plane;
	}
	
	public void setEntityList(List<Entity> e) {
		entities = (e);
	}
	
	public void setTerrainList(List<Terrain> t) {
		terrains = (t);
	}
	
	public void setLightList(List<Light> l) {
		lights = (l);
	}
	
	public void setCamera(Camera c) {
		this.camera = c;
	}
	
	public List<Entity> getEntities() {
		return entities;
	}
	
	public List<Terrain> getTerrains() {
		return terrains;
	}
	
	public List<Light> getLights() {
		return lights;
	}
	
	public List<WaterTile> getWaters() {
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
