package engine.collision;

import org.lwjgl.util.vector.Vector3f;

public class BoundingBox {
	
	public float minX, minY, minZ;
	public float maxX, maxY, maxZ;
	public float sphereRadius;
	
	public BoundingBox(float minX, float minY, float minZ, float maxX, float maxY, float maxZ, float sphereRadius) {
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
		this.sphereRadius = sphereRadius;
	}
	
	public BoundingBox(BoundingBox boundingBox) {
		this.minX = boundingBox.minX;
		this.minY = boundingBox.minY;
		this.minZ = boundingBox.minZ;
		this.maxX = boundingBox.maxX;
		this.maxY = boundingBox.maxY;
		this.maxZ = boundingBox.maxZ;
		this.sphereRadius = boundingBox.sphereRadius;
	}
	
	public boolean intersects(BoundingBox other) {
		
		return ((this.minX <= other.maxX && this.maxX >= other.minX) && 
				(this.minY <= other.maxY && this.maxY >= other.minY) && 
				(this.minZ <= other.maxZ && this.maxZ >= other.minZ));
		
	}
	
	public boolean intersects(BoundingBox other, float inflation) {
		
		return ((this.minX * inflation <= other.maxX && this.maxX * inflation >= other.minX) && 
				(this.minY * inflation <= other.maxY && this.maxY * inflation >= other.minY) && 
				(this.minZ * inflation <= other.maxZ && this.maxZ * inflation >= other.minZ));
		
	}
	
	public void printSpecs(String bbID) {
		
		System.out.println(bbID + "*********************");
		System.out.println(this.minX);
		System.out.println(this.minY);
		System.out.println(this.minZ);
		System.out.println(this.maxX);
		System.out.println(this.maxY);
		System.out.println(this.maxZ);
		System.out.println(bbID + "*********************");
		
	}
	
	public Vector3f[] getBoxVertices() {
		Vector3f[] retval = new Vector3f[8];
		float y = minY;
		retval[0] = new Vector3f(minX, y, minZ);
		retval[1] = new Vector3f(minX, y, maxZ);
		retval[2] = new Vector3f(maxX, y, minZ);
		retval[3] = new Vector3f(maxX, y, maxZ);
		y = maxY;
		retval[4] = new Vector3f(minX, y, minZ);
		retval[5] = new Vector3f(minX, y, maxZ);
		retval[6] = new Vector3f(maxX, y, minZ);
		retval[7] = new Vector3f(maxX, y, maxZ);
		return retval;
	}

}
