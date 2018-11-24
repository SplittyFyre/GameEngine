package scene;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import renderEngine.DisplayManager;
import scene.entities.Camera;
import scene.entities.Entity;
import scene.entities.Light;
import scene.entities.players.Player;
import scene.entities.projectiles.Bolt;
import scene.terrain.Terrain;

public class Scene {
	
	public Scene(Player player) {
		this.player = player;
	}
	
	private float counter = 0;
	
	private List<Entity> entities = new ArrayList<Entity>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	 
	private float skyR = 0, skyG = 0, skyB = 0;
	private List<Light> lights;
	private Camera camera;
	private Vector4f clipPlane;
	
	private Player player;
	
	public Vector4f getClipPlanePointer() {
		return clipPlane;
	}
	
	public void setClipPlanePointer(Vector4f plane) {
		clipPlane = plane;
	}
	
	public void setEntityList(List<Entity> e) {
		entities = new ArrayList<Entity>(e);
	}
	
	public void setTerrainList(List<Terrain> t) {
		terrains = new ArrayList<Terrain> (t);
	}
	
	public void setLightList(List<Light> l) {
		lights = new ArrayList<Light> (l);
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
	
	public void shootProps() {
		
		counter += DisplayManager.getFrameTime();
		
		if (counter > 0) {
			
			player.getProjectiles().add(Bolt.bluephaser(new Vector3f(-2500, 750, 2900)
					, 0, 20, 0,
					5, 0, 45, 0, 0));
			
			player.getProjectiles().add(Bolt.bluephaser(new Vector3f(-2500, 750, 2900)
					, -5, 20, 0,
					5, 0, 45, 0, 0));

			
			if (counter > 2) {
				counter = -5;
			}
		}
		
	}
	
}
