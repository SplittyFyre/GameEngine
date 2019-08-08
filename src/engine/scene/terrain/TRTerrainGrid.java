package engine.scene.terrain;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

import org.lwjgl.util.vector.Vector3f;

import engine.renderEngine.textures.TRTerrainTexturePack;
import internal.ResourceStreamClass;

public class TRTerrainGrid {
	
	private final TRTerrain[][] terrains;
	private final int terrainsPerSide;
	private Vector3f position;
	
	public final float topLeftX, topLeftZ, terrainSize;
	
	// center position
	public Vector3f getPosition() {
		return position;
	}
	
	public int getTerrainsPerSide() {
		return terrainsPerSide;
	}
	
	// definantely something wrong here
	public TRTerrain getTerrainUnderPoint(Vector3f point) {
		//System.out.println(point.z + " " + topLeftZ + " " + terrainSize);
		int xi = (int) (Math.floor((point.x - topLeftX + 3000) / terrainSize));
		int zi = (int) (Math.floor((point.z - topLeftZ + 3000) / terrainSize));
		//System.out.println(xi + " " + zi);
		return this.getTerrainAt(zi, xi);
	}
	
	public TRTerrain getTerrainAt(int i, int j) {
		if (i >= terrainsPerSide || j >= terrainsPerSide) {
			throw new IndexOutOfBoundsException("index is greater than number of terrains on each side");
		}
		return terrains[i][j];
	}
	
	public TRTerrainGrid(List<TRTerrain> terrainList, int blendMap, TRTerrainTexturePack texPack, Vector3f centerPos, float terrainSize, float heightFactor, int terrainsPerSide, String formatFile) {
		this.position = centerPos;
		this.terrainsPerSide = terrainsPerSide;
		this.terrainSize = terrainSize;
		@SuppressWarnings("resource")
		Scanner fin = new Scanner(new BufferedReader(new InputStreamReader(ResourceStreamClass.class.getResourceAsStream("/res/" + formatFile + ".trheight"))));
		
		int sideLen = fin.nextInt();
		
		int slm1 = sideLen - 1;
		if (!(slm1 > 0 && ((slm1 & (slm1 - 1)) == 0))) { // if sideLen - 1 not power of two
			fin.close();
			throw new RuntimeException("In trheight format file  " + formatFile + "  , sideLength not power of two + 1");
		}
		
		short[] colours = new short[sideLen * sideLen];
		for (int i = 0; i < sideLen * sideLen; i++) {
			colours[i] = fin.nextShort();
		}
		
		fin.close(); 
		
		
		//terrainsPerSide = sideLen / verticesPerGridSide;
		int verticesPerGridSide = (slm1 / terrainsPerSide) + 1;
		this.terrains = new TRTerrain[terrainsPerSide][terrainsPerSide];
		
		float a = terrainsPerSide / 2f * terrainSize;
		topLeftX = position.x - a;
		topLeftZ = position.z - a;
		
		// ith height of the jth chunk
		for (int i = 0; i < terrainsPerSide; i++) {
			for (int j = 0; j < terrainsPerSide; j++) {
				float x = topLeftX + j * terrainSize;
				float z = topLeftZ + i * terrainSize;
				// for the following two lines: I AM GOD
				int xstart = i * verticesPerGridSide - i;
				int zstart = j * verticesPerGridSide - j;
				//System.out.println(eachChunkOffset + " " + chunkNum);
				
				TRTerrain terrain =
						new TRTerrain(verticesPerGridSide, x, position.y, z, terrainSize, texPack, blendMap, verticesPerGridSide, sideLen, colours, xstart, zstart, heightFactor);
				terrains[i][j] = terrain;
				terrainList.add(terrain);
			}
		}
	}

}
