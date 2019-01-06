package collision;

public class BoundingBox {
	
	public float minX;
	public float minY;
	public float minZ;
	public float maxX;
	public float maxY;
	public float maxZ;
	
	public BoundingBox(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}
	
	public BoundingBox(BoundingBox boundingBox) {
		
		this.minX = boundingBox.minX;
		this.minY = boundingBox.minY;
		this.minZ = boundingBox.minZ;
		this.maxX = boundingBox.maxX;
		this.maxY = boundingBox.maxY;
		this.maxZ = boundingBox.maxZ;
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

}
