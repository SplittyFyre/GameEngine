package engine.renderEngine.models;

import engine.collision.BoundingBox;

public class RawModel {
	
	protected int vaoID;
	protected int vertexCount;
	private BoundingBox boundingBox;
	public boolean doubleSidedFaces = false;

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
