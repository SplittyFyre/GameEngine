package water;

import org.lwjgl.util.vector.Vector3f;

public class WaterTile {
	
	public float size;
	
	private float height;
	private float counter = 0;
	private float x,z;
	
	public WaterTile(float centerX, float centerZ, float height, float size)   {
		this.x = centerX;
		this.z = centerZ;
		this.height = height;
		this.size = size;
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
