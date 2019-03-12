package engine.scene.terrain;

import java.util.List;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import engine.objStuff.OBJParser;
import engine.renderEngine.Loader;
import engine.renderEngine.models.RawModel;
import engine.renderEngine.models.TexturedModel;
import engine.renderEngine.textures.ModelTexture;
import engine.renderEngine.textures.TerrainTexturePack;
import engine.scene.entities.Entity;
import engine.scene.entities.StaticEntity;
import engine.water.WaterTile;
import jtrek.box.TM;

public class Island {
	
	private Terrain terrain;
	private WaterTile water;
	
	private Vector3f position;
	
	private float size;
	
	public Island(TerrainTexturePack texturePack, int blendMap,
			List<Terrain> terrains, List<WaterTile> waters, List<Entity> entities, float x, float y, float z, float size) {
		terrain = new Terrain(x, y, z, size, texturePack, blendMap);
		terrains.add(terrain);
		terrains.add(new Terrain(x, y, z, size, texturePack, blendMap, true));
		water = new WaterTile(x, z, y, size / 2);
		waters.add(water);
		
		Random random = new Random();
		
		RawModel fernRaw = OBJParser.loadObjModel("fernModel");
		ModelTexture fernTextureAtlas = new ModelTexture(Loader.loadTexture("fern"));
		fernTextureAtlas.setNumRows(2);
		TexturedModel fern = new TexturedModel(fernRaw, fernTextureAtlas);
		
		fern.getTexture().setUseFakeLighting(true);
		fern.getTexture().setTransparent(true);
		
		RawModel pineRaw = OBJParser.loadObjModel("pine");
		TexturedModel pineText = new TexturedModel(pineRaw, new ModelTexture(Loader.loadTexture("pine")));
		
		pineText.getTexture().setTransparent(true);
		pineText.getTexture().setUseFakeLighting(true);
		
		float sz = size / 2;
		
		for (int i = 0; i < 400; i++) {
			
			float x1 = random.nextFloat() * 2 * sz + (x - sz);
			float z1 = random.nextFloat() * 2 * sz + (z - sz);
			float y1 = terrain.getTerrainHeight(x1, z1);
			if (y1 > 0)
				entities.add(new StaticEntity(pineText, new Vector3f(x1, y1, z1), 0, random.nextFloat() * 360, 0,
						0.5f + random.nextFloat() - 0.5f));
			
			
		}
		
		for (int i = 0; i < 250; i++) {
			float x1 = random.nextFloat() * 2 * sz + (x - sz);
			float z1 = random.nextFloat() * 2 * sz + (z - sz);
			float y1 = terrain.getTerrainHeight(x1, z1);
			if (y1 > 0)
				entities.add(new StaticEntity(fern, random.nextInt(4), new Vector3f(x1, y1, z1), 0, random.nextFloat() * 360, 0, 
						0.0125f + random.nextFloat() - 0.5f));
		}
		
	}
	
	public Island(TerrainTexturePack texturePack, int blendMap,
			List<Terrain> terrains, List<WaterTile> waters, List<Entity> entities, float x, float y, float z, float size, String heightMap, float maxHeight) {
		terrain = new Terrain(x, y, z, size, texturePack, blendMap, heightMap, maxHeight);
		terrains.add(terrain);
		terrains.add(new Terrain(x, y, z, size, texturePack, blendMap, true));
		water = new WaterTile(x, z, y, size / 2);
		waters.add(water);
		
		Random random = new Random();
		
		RawModel fernRaw = OBJParser.loadObjModel("fernModel");
		ModelTexture fernTextureAtlas = new ModelTexture(Loader.loadTexture("fern"));
		fernTextureAtlas.setNumRows(2);
		TexturedModel fern = new TexturedModel(fernRaw, fernTextureAtlas);
		
		fern.getTexture().setUseFakeLighting(true);
		fern.getTexture().setTransparent(true);
		
		RawModel pineRaw = OBJParser.loadObjModel("pine");
		TexturedModel pineText = new TexturedModel(pineRaw, new ModelTexture(Loader.loadTexture("pine")));
		
		pineText.getTexture().setTransparent(true);
		pineText.getTexture().setUseFakeLighting(true);
		
		float sz = size / 2;
		
		for (int i = 0; i < 400; i++) {
			
			float x1 = random.nextFloat() * 2 * sz + (x - sz);
			float z1 = random.nextFloat() * 2 * sz + (z - sz);
			float y1 = terrain.getTerrainHeight(x1, z1);
			if (y1 > 0)
				entities.add(new StaticEntity(pineText, new Vector3f(x1, y1, z1), 0, random.nextFloat() * 360, 0,
						0.5f + random.nextFloat() - 0.5f));
			
			
		}
		
		for (int i = 0; i < 250; i++) {
			float x1 = random.nextFloat() * 2 * sz + (x - sz);
			float z1 = random.nextFloat() * 2 * sz + (z - sz);
			float y1 = terrain.getTerrainHeight(x1, z1);
			if (y1 > 0)
				entities.add(new StaticEntity(fern, random.nextInt(4), new Vector3f(x1, y1, z1), 0, random.nextFloat() * 360, 0, 
						0.0125f + random.nextFloat() - 0.5f));
		}
		
	}
	
	public Terrain getTerrain() {
		return terrain;
	}

	public WaterTile getWater() {
		return water;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getSize() {
		return size;
	}

	//CURR
	public Island(TerrainTexturePack texturePack, int blendMap, 
			List<Terrain> terrains, List<WaterTile> waters, List<Entity> entities, float x, float y, float z, float size, int seed) {
		
		this.position = new Vector3f(x, y, z);
		
		terrain = new Terrain(x, y, z, size, texturePack, blendMap, seed);
		//terrain = new Terrain(x, y, z, size, texturePack, blendMap, "heightmap", 2000);
		
		terrains.add(terrain);
		terrains.add(new Terrain(x, y, z, size, texturePack, blendMap, true));
		water = new WaterTile(x, z, y, size / 2, new Vector3f(0.0f, 0.3f, 0.4f));
		waters.add(water);
				
		RawModel fernRaw = OBJParser.loadObjModel("fernModel");
		ModelTexture fernTextureAtlas = new ModelTexture(Loader.loadTexture("fern"));
		fernTextureAtlas.setNumRows(2);
		TexturedModel fern = new TexturedModel(fernRaw, fernTextureAtlas);
		
		//fern.getTexture().setUseFakeLighting(true);
		fern.getTexture().setTransparent(true);
		
		RawModel pineRaw = OBJParser.loadObjModel("biquad");
		TexturedModel pineText = new TexturedModel(pineRaw, new ModelTexture(Loader.loadTexture("evertreesqr")));
		
		pineText.getTexture().setTransparent(true);
		pineText.getTexture().setUseFakeLighting(true);
		
		float sz = size / 2;
		
		for (int i = 0; i < 4000; i++) {
			
			//if (i % 2 == 0) {
				float x1 = TM.rng.nextFloat() * 2 * sz + (x - sz);
				float z1 = TM.rng.nextFloat() * 2 * sz + (z - sz);
				float y1 = terrain.getTerrainHeight(x1, z1);
				if (y1 > 0)
					entities.add(new StaticEntity(pineText, new Vector3f(x1, y1 + y, z1), 0, TM.rng.nextFloat() * 360, 0,
							10.25f + (TM.rng.nextFloat() - 0.5f)));
			//}
			
			
		}
		
		for (int i = 0; i < 250; i++) {
			float x1 = TM.rng.nextFloat() * 2 * sz + (x - sz);
			float z1 = TM.rng.nextFloat() * 2 * sz + (z - sz);
			float y1 = terrain.getTerrainHeight(x1, z1);
			if (y1 > 0)
				entities.add(new StaticEntity(fern, TM.rng.nextInt(4), new Vector3f(x1, y1 + y, z1), 0, TM.rng.nextFloat() * 360, 0, 
						2.5f + (TM.rng.nextFloat() - 0.5f)));
		}
		
	}

}
