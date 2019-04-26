package engine.scene.terrain;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

import org.lwjgl.util.vector.Vector3f;

import engine.renderEngine.textures.TRTerrainTexturePack;

public class TRTerrainGrid {
	
	private final TRTerrain[][] terrains;
	private final int terrainsPerSide;
	private Vector3f position;
	
	// center position
	public Vector3f getPosition() {
		return position;
	}
	
	public int getTerrainsPerSide() {
		return terrainsPerSide;
	}
	
	public TRTerrain getTerrainAt(int i, int j) {
		if (i >= terrainsPerSide || j >= terrainsPerSide) {
			throw new IndexOutOfBoundsException("index is greater than number of terrains on each side");
		}
		return terrains[i][j];
	}
	
	public TRTerrainGrid(List<TRTerrain> terrainList, int blendMap, TRTerrainTexturePack texPack, Vector3f centerPos, float terrainSize, float heightFactor, int verticesPerGridSide, String formatFile) {
		this.position = centerPos;
		@SuppressWarnings("resource")
		Scanner fin = new Scanner(new BufferedReader(new InputStreamReader(Class.class.getResourceAsStream("/res/" + formatFile + ".trheight"))));
		
		int sideLen = fin.nextInt();
		
		short[] colours = new short[sideLen * sideLen];
		for (int i = 0; i < sideLen * sideLen; i++) {
			colours[i] = fin.nextShort();
		}
		
		fin.close(); 
		
		if (sideLen % verticesPerGridSide != 0) { 
			throw new RuntimeException(String.format("Error: heightmap side %d is not evenly divisible by verticesPerGridSide %d", sideLen, verticesPerGridSide));
		}
		
		terrainsPerSide = sideLen / verticesPerGridSide;
		this.terrains = new TRTerrain[terrainsPerSide][terrainsPerSide];
		
		float a = terrainsPerSide / 2f * terrainSize;
		float topLeftX = position.x - a;
		float topLeftZ = position.z - a;
		
		// ith height of the jth chunk
		for (int i = 0; i < terrainsPerSide; i++) {
			for (int j = 0; j < terrainsPerSide; j++) {
				float x = topLeftX + j * terrainSize;
				float z = topLeftZ + i * terrainSize;
				int xstart = i * verticesPerGridSide;
				int zstart = j * verticesPerGridSide;
				//System.out.println(eachChunkOffset + " " + chunkNum);
				
				TRTerrain terrain =
						new TRTerrain(verticesPerGridSide, x, position.y, z, terrainSize, texPack, blendMap, verticesPerGridSide, sideLen, colours, xstart, zstart, heightFactor);
				terrains[i][j] = terrain;
				terrainList.add(terrain);
			}
		}
	}

}
