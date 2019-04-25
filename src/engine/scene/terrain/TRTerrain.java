package engine.scene.terrain;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import engine.renderEngine.Loader;
import engine.renderEngine.models.RawModel;
import engine.renderEngine.textures.TRTerrainTexturePack;
import engine.utils.SFMath;

public class TRTerrain {
	
	private static final float MAX_8_COLOUR = 256 * 256 * 256;
	private static final float MAX_16_GREYSCALE = Short.MAX_VALUE;
	
	private float x;
	private float y;
	private float z;
	private RawModel model;
	private TRTerrainTexturePack texturePack;
	private int blendMap;
	private float size;
	
	private boolean isSeeded;
	private int seed;
	
	private float maxHeight;
	
	public float getMaxHeight() {
		return maxHeight;
	}

	private float[][] heights;
	private int vertexCnt;
	
	public TRTerrain(float x, float y, float z, float size, TRTerrainTexturePack texturePack, int blendMap, String heightMap, float heightFactor) {
		this.size = size;
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = x - (size / 2);
		this.y = y;
		this.z = z - (size / 2);
		this.model = generateTerrainHeightMap(heightMap, heightFactor);
	}
	
	public TRTerrain(float x, float y, float z, float size, TRTerrainTexturePack texturePack, int blendMap, String formatFile, float heightFactor, boolean b) {
		this.size = size;
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = x - (size / 2);
		this.y = y;
		this.z = z - (size / 2);
		this.model = generateTerrainSpecialFormat(formatFile, heightFactor);
	}
	
	public TRTerrain(int vertexCnt, float x, float y, float z, float size, TRTerrainTexturePack texturePack, int blendMap, float amplitude) {
		this.vertexCnt = vertexCnt;
		this.size = size;
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = x - (size / 2);
		this.y = y;
		this.z = z - (size / 2);
		this.model = generateTerrainGenerator(vertexCnt, amplitude);
	}
	
	public TRTerrain(int vertexCnt, float x, float y, float z, float size, TRTerrainTexturePack texturePack, int blendMap, int seed, float amplitude) {
		this.vertexCnt = vertexCnt;
		this.size = size;
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = x - (size / 2);
		this.y = y;
		this.z = z - (size / 2);
		this.isSeeded = true;
		this.seed = seed;
		this.model = generateTerrainGenerator(vertexCnt, amplitude);
	}
	
	public TRTerrain(int vertexCnt, float x, float y, float z, float size, TRTerrainTexturePack texturePack, int blendMap, int gridSideLen, int totalSideLen, short[] gridHeights, int offsetX, int offsetZ, float heightFactor) {
		this.vertexCnt = vertexCnt;
		this.size = size;
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = x - (size / 2);
		this.y = y;
		this.z = z - (size / 2);
		this.model = generateTerrainFromGridInfo(gridSideLen, totalSideLen, gridHeights, offsetX, offsetZ, heightFactor);
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public RawModel getModel() {
		return model;
	}

	public TRTerrainTexturePack getTexturePack() {
		return texturePack;
	}

	public int getBlendMap() {
		return blendMap;
	}

	public float getTerrainHeight(float worldX, float worldZ) {
		
		float terrainX = worldX - this.x;
		float terrainZ = worldZ - this.z;
		float gridSize = size / ((float) heights.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSize);
		int gridZ = (int) Math.floor(terrainZ / gridSize);
		
		if (gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0)
			return 0;
		
		float xCoord = (terrainX % gridSize) / gridSize;
		float zCoord = (terrainZ % gridSize) / gridSize;
		
		float answer;
		
		if (xCoord <= (1 - zCoord)) {
			answer = SFMath.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ], 0), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		} 
		else {
			answer = SFMath.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}
		
		return answer;
	}
	
	private RawModel generateTerrainGenerator(int vtxcnt, float amplitude) {
		
		NoiseGenerator generator;
		float highestHeight = Float.NEGATIVE_INFINITY;
		
		if (isSeeded)
			generator = new NoiseGenerator(this.seed, amplitude);
		else 
			generator = new NoiseGenerator(amplitude);
		
		heights = new float[vtxcnt][vtxcnt];
		int count = vtxcnt * vtxcnt;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (vtxcnt - 1) * (vtxcnt - 1)];
		int vertexPointer = 0;
		
		for(int i = 0; i < vtxcnt; i++) {
			
			for(int j = 0; j < vtxcnt; j++) {
				
				vertices[vertexPointer * 3] = j / ((float) vtxcnt - 1) * this.size;
				float height = getHeightGenerator(j, i, generator);
				
				if (height > highestHeight) {
					highestHeight = height;
				}
				
				heights[j][i] = height;
				vertices[vertexPointer * 3 + 1] = height;
				vertices[vertexPointer * 3 + 2] = i / ((float) vtxcnt - 1) * this.size;
				Vector3f normal = calculateNormalGenerator(j, i, generator);
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				textureCoords[vertexPointer * 2] = j / ((float) vtxcnt - 1);
				textureCoords[vertexPointer * 2 + 1] = i / ((float) vtxcnt - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		
		for(int gz = 0; gz < vtxcnt - 1; gz++) {
			
			for(int gx = 0; gx < vtxcnt - 1; gx++) {
				
				int topLeft = (gz * vtxcnt) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * vtxcnt) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		
		this.maxHeight = highestHeight;
		
		return Loader.loadToVAO(vertices, textureCoords, normals, indices, null);
	}
		
	private RawModel generateTerrainHeightMap(String heightMap, float heightFactor) {
				
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(Class.class.getResource("/res/" + heightMap + ".png"));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		float highestHeight = Float.NEGATIVE_INFINITY;
		
		this.vertexCnt = image.getHeight();
		int vtxcnt = this.vertexCnt;
		
		heights = new float[vtxcnt][vtxcnt];
		int count = vtxcnt * vtxcnt;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (vtxcnt - 1) * (vtxcnt - 1)];
		int vertexPointer = 0;
		
		for(int i = 0; i < vtxcnt; i++) {
			
			for(int j = 0; j < vtxcnt; j++) {
				
				vertices[vertexPointer * 3] = j / ((float) vtxcnt - 1) * this.size;
				float height = getHeightHeightMap(j, i, image, heightFactor);
				if (height > highestHeight) {
					highestHeight = height;
				}
				heights[j][i] = height;
				vertices[vertexPointer * 3 + 1] = height;
				vertices[vertexPointer * 3 + 2] = i / ((float) vtxcnt - 1) * this.size;
				Vector3f normal = calculateNormalHeightMap(j, i, image, heightFactor);
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				textureCoords[vertexPointer * 2] = j / ((float) vtxcnt - 1);
				textureCoords[vertexPointer * 2 + 1] = i / ((float) vtxcnt - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		
		for(int gz = 0; gz < vtxcnt - 1; gz++) {
			
			for(int gx = 0; gx < vtxcnt - 1; gx++) {
				
				int topLeft = (gz * vtxcnt) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * vtxcnt) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		
		this.maxHeight = highestHeight;
		
		return Loader.loadToVAO(vertices, textureCoords, normals, indices);
	}
	
	
	private RawModel generateTerrainSpecialFormat(String formatFile, float heightFactor) {

		@SuppressWarnings("resource")
		Scanner fin = new Scanner(new BufferedReader(new InputStreamReader(Class.class.getResourceAsStream("/res/" + formatFile + ".trheight"))));
		
		int sideLen = fin.nextInt();
		
		short[] colours = new short[sideLen * sideLen];
		for (int i = 0; i < sideLen * sideLen; i++) {
			colours[i] = fin.nextShort();
		}
		
		fin.close();
		
		
		
		float highestHeight = Float.NEGATIVE_INFINITY;
		
		this.vertexCnt = sideLen;
		int vtxcnt = this.vertexCnt;
		
		heights = new float[vtxcnt][vtxcnt];
		int count = vtxcnt * vtxcnt;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (vtxcnt - 1) * (vtxcnt - 1)];
		int vertexPointer = 0;
		
		for(int i = 0; i < vtxcnt; i++) {
			
			for(int j = 0; j < vtxcnt; j++) {
				
				vertices[vertexPointer * 3] = j / ((float) vtxcnt - 1) * this.size;
				float height = getHeightSpecialFormat(j, i, sideLen, sideLen, colours, 0, 0, heightFactor);
				if (height > highestHeight) {
					highestHeight = height;
				}
				heights[j][i] = height;
				vertices[vertexPointer * 3 + 1] = height;
				vertices[vertexPointer * 3 + 2] = i / ((float) vtxcnt - 1) * this.size;
				Vector3f normal = calculateNormalSpecialFormat(j, i, sideLen, sideLen, colours, 0, 0, heightFactor);
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				textureCoords[vertexPointer * 2] = j / ((float) vtxcnt - 1);
				textureCoords[vertexPointer * 2 + 1] = i / ((float) vtxcnt - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		
		for(int gz = 0; gz < vtxcnt - 1; gz++) {
			
			for(int gx = 0; gx < vtxcnt - 1; gx++) {
				
				int topLeft = (gz * vtxcnt) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * vtxcnt) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		
		this.maxHeight = highestHeight;
				
		return Loader.loadToVAO(vertices, textureCoords, normals, indices);
	}
	
	private RawModel generateTerrainFromGridInfo(int gridSideLen, int totalSideLen, short[] colours, int offsetX, int offsetZ, float heightFactor) {
		
		float highestHeight = Float.NEGATIVE_INFINITY;
		
		int vtxcnt = this.vertexCnt;
		
		heights = new float[vtxcnt][vtxcnt];
		int count = vtxcnt * vtxcnt;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (vtxcnt - 1) * (vtxcnt - 1)];
		int vertexPointer = 0;
		
		for(int i = 0; i < vtxcnt; i++) {
			for(int j = 0; j < vtxcnt; j++) {
				
				vertices[vertexPointer * 3] = j / ((float) vtxcnt - 1) * this.size;
				float height = getHeightSpecialFormat(i, j, gridSideLen, totalSideLen, colours, offsetX, offsetZ, heightFactor);
				if (height > highestHeight) {
					highestHeight = height;
				}
				heights[j][i] = height;
				vertices[vertexPointer * 3 + 1] = height;
				vertices[vertexPointer * 3 + 2] = i / ((float) vtxcnt - 1) * this.size;
				Vector3f normal = calculateNormalSpecialFormat(i, j, gridSideLen, totalSideLen, colours, offsetX, offsetZ, heightFactor);
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				textureCoords[vertexPointer * 2] = j / ((float) vtxcnt - 1);
				textureCoords[vertexPointer * 2 + 1] = i / ((float) vtxcnt - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		
		for(int gz = 0; gz < vtxcnt - 1; gz++) {
			
			for(int gx = 0; gx < vtxcnt - 1; gx++) {
				
				int topLeft = (gz * vtxcnt) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * vtxcnt) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		
		this.maxHeight = highestHeight;
				
		return Loader.loadToVAO(vertices, textureCoords, normals, indices);
	}
	
	private Vector3f calculateNormalSpecialFormat(int x, int z, int gridSideLen, int totalSideLen, short[] colours, int offsetX, int offsetZ, float heightFactor) {
		
		float heightL = getHeightSpecialFormat(x - 1, z, gridSideLen, totalSideLen, colours, offsetX, offsetZ, heightFactor);
		float heightR = getHeightSpecialFormat(x + 1, z, gridSideLen, totalSideLen, colours, offsetX, offsetZ, heightFactor);
		float heightD = getHeightSpecialFormat(x, z - 1, gridSideLen, totalSideLen, colours, offsetX, offsetZ, heightFactor);
		float heightU = getHeightSpecialFormat(x, z + 1, gridSideLen, totalSideLen, colours, offsetX, offsetZ, heightFactor);
		Vector3f normal = new Vector3f(heightL - heightR, 2f * (this.size / (this.vertexCnt - 1f)), heightD - heightU);
		normal.normalise();
		 
		return normal;
		
	}
	
	
	private float getHeightSpecialFormat(int x, int z, int gridSideLen, int totalSideLen, short[] colours, int offsetX, int offsetZ, float heightFactor) {
		int flattenedPos = ((x + offsetX) + (totalSideLen) * (z + offsetZ));
		//System.out.printf("x: %d, z: %d, offX: %d, offZ: %d, sidelen: %d, flattenedPos: %d\n", x, z, offsetX, offsetZ, sideLen, flattenedPos);
		
		if (x < 0 || x >= gridSideLen || z < 0 || z >= gridSideLen) {
			return 0;
		}
		
		float height = colours[flattenedPos];
		height /= MAX_16_GREYSCALE;
		height *= heightFactor;

		return height;
	}
	
	
	private Vector3f calculateNormalHeightMap(int x, int z, BufferedImage image, float heightFactor) {
		
		float heightL = getHeightHeightMap(x - 1, z, image, heightFactor);
		float heightR = getHeightHeightMap(x + 1, z, image, heightFactor);
		float heightD = getHeightHeightMap(x, z - 1, image, heightFactor);
		float heightU = getHeightHeightMap(x, z + 1, image, heightFactor);
		Vector3f normal = new Vector3f(heightL - heightR, 2f * (this.size / (this.vertexCnt - 1f)), heightD - heightU);
		normal.normalise();
		 
		return normal;
		
	}
	
	private float getHeightHeightMap(int x, int z, BufferedImage image, float heightFactor) {
		
		if (x < 0 || x >= image.getHeight() || z < 0 || z >= image.getHeight()) {
			return 0;
		}
		
		float height = image.getRGB(x, z);
		height += MAX_8_COLOUR / 2f;
		height /= MAX_8_COLOUR / 2f;
		height *= heightFactor;
		
		return height;
		
		
	}
	
	private Vector3f calculateNormalGenerator(int x, int z, NoiseGenerator generator) {
		
		float heightL = getHeightGenerator(x - 1, z, generator);
		float heightR = getHeightGenerator(x + 1, z, generator);
		float heightD = getHeightGenerator(x, z - 1, generator);
		float heightU = getHeightGenerator(x, z + 1, generator);
		Vector3f normal = new Vector3f(heightL - heightR, 2f * (this.size / (this.vertexCnt - 1f)), heightD - heightU);
		normal.normalise();
		
		return normal;
		
	}
	
	private float getHeightGenerator(int x, int z, NoiseGenerator generator) {
		return generator.generateHeight(x, z);
	}
	
	public void addVec(Vector3f vec) {
		this.x += vec.x;
		this.y += vec.y;
		this.z += vec.z;
	}

}
