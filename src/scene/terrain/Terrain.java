package scene.terrain;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.Loader;
import renderEngine.models.RawModel;
import renderEngine.textures.TerrainTexture;
import renderEngine.textures.TerrainTexturePack;
import utils.SFMath;

public class Terrain {
	
	private static final float MAX_PIXEL_COLOUR = 256 * 256 * 256;
	
	private float x;
	private float y;
	private float z;
	private RawModel model;
	private TerrainTexturePack texturePack;
	private TerrainTexture blendMap;
	private float size;
	
	public boolean base = false;
	public boolean isSeeded = false;
	private int seed;
	
	float[][] heights;
	
	public Terrain(float x, float y, float z, float size, TerrainTexturePack texturePack, TerrainTexture blendMap, String heightMap, float maxHeight) {
		this.size = size;
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = x - (size / 2);
		this.y = y;
		this.z = z - (size / 2);
		this.model = generateTerrain(heightMap, maxHeight);
	}
	
	public Terrain(float x, float y, float z, float size, TerrainTexturePack texturePack, TerrainTexture blendMap) {
		this.size = size;
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = x - (size / 2);
		this.y = y;
		this.z = z - (size / 2);
		this.model = generateTerrain(128);
	}
	
	public Terrain(float x, float y, float z, float size, TerrainTexturePack texturePack, TerrainTexture blendMap, int seed) {
		this.size = size;
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = x - (size / 2);
		this.y = y;
		this.z = z - (size / 2);
		this.seed = seed;
		this.isSeeded = true;
		this.model = generateTerrain(128);
	}
	
	public Terrain(float x, float y, float z, float size, TerrainTexturePack texturePack, TerrainTexture blendMap, boolean base) {
		this.size = size;
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = x - (size / 2);
		this.y = y;
		this.z = z - (size / 2);
		this.base = base;
		this.model = generateTerrain(32);
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

	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}

	public TerrainTexture getBlendMap() {
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
	
	private RawModel generateTerrain(int VERTEX_COUNT) {
		
		NoiseGenerator generator;
		
		if (isSeeded) {
			generator = new NoiseGenerator(this.seed);
		}
		else {
			generator = new NoiseGenerator(this.base);
		}
		
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
		int vertexPointer = 0;
		
		for(int i = 0; i < VERTEX_COUNT; i++) {
			
			for(int j = 0; j < VERTEX_COUNT; j++) {
				
				vertices[vertexPointer * 3] = j / ((float) VERTEX_COUNT - 1) * this.size;
				float height = getHeight(j, i, generator);
				heights[j][i] = height;
				vertices[vertexPointer * 3 + 1] = height;
				vertices[vertexPointer * 3 + 2] = i / ((float) VERTEX_COUNT - 1) * this.size;
				Vector3f normal = calculateNormal(j, i, generator);
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				textureCoords[vertexPointer * 2] = j / ((float) VERTEX_COUNT - 1);
				textureCoords[vertexPointer * 2 + 1] = i / ((float) VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		
		for(int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
			
			for(int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
				
				int topLeft = (gz * VERTEX_COUNT) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return Loader.loadToVAO(vertices, textureCoords, normals, indices, null);
	}
	
	private float MAX_HEIGHT;
	
	private RawModel generateTerrain(String heightMap, float maxHeight) {
		
		MAX_HEIGHT = maxHeight;
		
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(Class.class.getResource("/res/" + heightMap + ".png"));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		int VERTEX_COUNT = image.getHeight();
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
		int vertexPointer = 0;
		for(int i=0;i<VERTEX_COUNT;i++){
			for(int j=0;j<VERTEX_COUNT;j++){
				vertices[vertexPointer*3] = j/((float)VERTEX_COUNT - 1) * this.size;
				float height = getHeight(j, i, image);
				heights[j][i] = height;
				vertices[vertexPointer*3+1] = height;
				vertices[vertexPointer*3+2] = i/((float)VERTEX_COUNT - 1) * this.size;
				Vector3f normal = calculateNormal(j, i, image);
				normals[vertexPointer*3] = normal.x;
				normals[vertexPointer*3+1] = normal.y;
				normals[vertexPointer*3+2] = normal.z;
				textureCoords[vertexPointer*2] = j/((float)VERTEX_COUNT - 1);
				textureCoords[vertexPointer*2+1] = i/((float)VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<VERTEX_COUNT-1;gz++){
			for(int gx=0;gx<VERTEX_COUNT-1;gx++){
				int topLeft = (gz*VERTEX_COUNT)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return Loader.loadToVAO(vertices, textureCoords, normals, indices);
	}
	
	private Vector3f calculateNormal(int x, int z, BufferedImage image) {
		
		float heightL = getHeight(x - 1, z, image);
		float heightR = getHeight(x + 1, z, image);
		float heightD = getHeight(x, z - 1, image);
		float heightU = getHeight(x, z + 1, image);
		Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		normal.normalise();
		
		return normal;
		
	}
	
	private float getHeight(int x, int z, BufferedImage image) {
		
		if (x < 0 || x >= image.getHeight() || z < 0 || z >= image.getHeight()) {
			return 0;
		}
		
		float height = image.getRGB(x, z);
		height += MAX_PIXEL_COLOUR / 2f;
		height /= MAX_PIXEL_COLOUR / 2f;
		height *= MAX_HEIGHT;
		
		return height;
		
		
	}
	
	private Vector3f calculateNormal(int x, int z, NoiseGenerator generator) {
		
		float heightL = getHeight(x - 1, z, generator);
		float heightR = getHeight(x + 1, z, generator);
		float heightD = getHeight(x, z - 1, generator);
		float heightU = getHeight(x, z + 1, generator);
		Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		normal.normalise();
		
		return normal;
		
	}
	
	private float getHeight(int x, int z, NoiseGenerator generator) {
		return generator.generateHeight(x, z);
	}
	
	public void addVec(Vector3f vec) {
		this.x += vec.x;
		this.y += vec.y;
		this.z += vec.z;
	}

}
