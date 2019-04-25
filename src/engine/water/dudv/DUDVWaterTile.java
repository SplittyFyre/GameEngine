package engine.water.dudv;

import org.lwjgl.util.vector.Vector3f;

import engine.renderEngine.Loader;
import engine.renderEngine.TRDisplayManager;

public class DUDVWaterTile {
	
	private int dudvMapTexture, normalMapTexture;
	
	public int getDudvMapTexture() {
		return dudvMapTexture;
	}

	public int getNormalMapTexture() {
		return normalMapTexture;
	}
	

	public float size;   
	
	private float height;
	private float counter = 0;
	private float x, z;
	
	private float waveSpeed = 0.03f;
	private float waveIntensity = 0.01f;
	private float shineDamper = 15.f, reflectivity = 0.5f;
	
	private float tiling = 50;
	
	private Vector3f colourOffset = new Vector3f(0, 0, 0);
	
	private float movedFactor = 0;
	
	public float updateMovedFactor() {
		movedFactor += waveSpeed * TRDisplayManager.getFrameDeltaTime();
		movedFactor %= 1;
		return movedFactor;
	}
	
	
	public float getTiling() {
		return tiling;
	}

	public DUDVWaterTile setTiling(float tiling) {
		this.tiling = tiling;
		return this;
	}
	
	
	public float getWaveIntensity() {
		return waveIntensity;
	}

	public DUDVWaterTile setWaveIntensity(float waveIntensity) {
		this.waveIntensity = waveIntensity;
		return this;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public DUDVWaterTile setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
		return this;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public DUDVWaterTile setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
		return this;
	}
	
	public float getWaveSpeed() {
		return waveSpeed;
	}
	
	public DUDVWaterTile setWaveSpeed(float waveSpeed) {
		this.waveSpeed = waveSpeed;
		return this;
	}



	public DUDVWaterTile(String dudvTexture, String normalTexture, float centerX, float centerZ, float height, float size, float waveSpeed)   {
		this.x = centerX;
		this.z = centerZ;
		this.height = height;
		this.size = size;
		this.waveSpeed = waveSpeed;
		
		this.dudvMapTexture = Loader.loadTexture(dudvTexture);
		this.normalMapTexture = Loader.loadTexture(normalTexture);
	}
	
	public DUDVWaterTile(String dudvTexture, String normalTexture, float centerX, float centerZ, float height, float size, float waveSpeed, Vector3f colourOffset)   {
		this.x = centerX;
		this.z = centerZ;
		this.height = height;
		this.size = size;
		this.waveSpeed = waveSpeed;
		this.colourOffset = colourOffset;
		
		this.dudvMapTexture = Loader.loadTexture(dudvTexture);
		this.normalMapTexture = Loader.loadTexture(normalTexture);
	}
	
	public DUDVWaterTile(String dudvTexture, String normalTexture, float centerX, float centerZ, float height, float size)   {
		this.x = centerX;
		this.z = centerZ;
		this.height = height;
		this.size = size;
		
		this.dudvMapTexture = Loader.loadTexture(dudvTexture);
		this.normalMapTexture = Loader.loadTexture(normalTexture);
	}
	
	public DUDVWaterTile(String dudvTexture, String normalTexture, float centerX, float centerZ, float height, float size, Vector3f colourOffset)   {
		this.x = centerX;
		this.z = centerZ;
		this.height = height;
		this.size = size;
		this.colourOffset = colourOffset;
		
		this.dudvMapTexture = Loader.loadTexture(dudvTexture);
		this.normalMapTexture = Loader.loadTexture(normalTexture);
	}
	
	public Vector3f getColourOffset() {
		return colourOffset;
	}

	public void setColourOffset(Vector3f colourOffset) {
		this.colourOffset = colourOffset;
	}

	public void update() {
		/*if (counter <= 1000) {
			height += 0.005f;
			counter++;
		}
		else if (counter <= 2000) {
			height -= 0.005f;
			counter++;
		}
		else {
			counter = 0;
		}*/
	}

	public float getHeight() {
		return height;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}
	
	public void addVec(Vector3f vec) {
		this.x += vec.x;
		this.height += vec.y;
		this.z += vec.z;
	}

}
