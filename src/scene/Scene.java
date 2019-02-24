package scene;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector4f;

import scene.entities.Entity;
import scene.entities.Light;
import scene.entities.camera.Camera;
import scene.terrain.Terrain;
import water.WaterTile;

public class Scene {
	
	private List<Entity> entities = new ArrayList<Entity>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	private List<Light> lights = new ArrayList<Light>();
	private List<WaterTile> waters = new ArrayList<WaterTile>();
	 
	private float skyR = 0, skyG = 0, skyB = 0;
	private Camera camera;
	private Vector4f clipPlane = new Vector4f();
		
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
		return skyR;
	}

	public float getSkyG() {
		return skyG;
	}

	public float getSkyB() {
		return skyB;
	}
	
}
