package renderEngine.models;

import collision.BoundingBox;

public class RawModel {
	
	private int vaoID;
	private int vertexCount;
	private BoundingBox boundingBox;

	public RawModel(int vaoID, int vertexCount, BoundingBox aabb){
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		this.boundingBox = aabb;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
	
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}
}
