package engine.renderEngine.models;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

public class TerrainLODModel extends RawModel {
	
	private int[] vbolod, vbolodsize;
	private int currentLODLevel = 0;

	public TerrainLODModel(int vaoID, int[] vbolod, int[] vbolodsize) {
		super(vaoID, vbolodsize[0], null);
		this.vbolod = vbolod;
		this.vbolodsize = vbolodsize;
	}
	
	public void ensureLODLevel(int lod_level) {
		
		// avoid unnecessary opengl crap
		if (lod_level == currentLODLevel) {
			return;
		}
		
		currentLODLevel = lod_level;
		
		GL30.glBindVertexArray(super.vaoID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbolod[lod_level]);
		GL30.glBindVertexArray(0);
	
		super.vertexCount = vbolodsize[lod_level];
		
	}

}
