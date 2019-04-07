package jtrek.gameplay;

import java.util.List;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import engine.objStuff.OBJParser;
import engine.renderEngine.Loader;
import engine.renderEngine.models.RawModel;
import engine.renderEngine.models.TexturedModel;
import engine.renderEngine.textures.ModelTexture;
import engine.renderEngine.textures.TerrainTexturePack;
import engine.scene.entities.StaticEntity;
import engine.scene.entities.TREntity;
import engine.scene.terrain.TRTerrain;
import engine.water.dudv.DUDVWaterTile;
import jtrek.box.TM;

public class Island {
	
	private TRTerrain terrain;
	private DUDVWaterTile water;
	
	private Vector3f position;
	
	private float size;
	
	public Island(TerrainTexturePack texturePack, int blendMap,
			List<TRTerrain> terrains, List<DUDVWaterTile> waters, List<TREntity> entities, float x, float y, float z, float size) {
		terrain = new TRTerrain(128, x, y, z, size, texturePack, blendMap, 575);
		terrains.add(terrain);
		water = new DUDVWaterTile("waterDUDV", "normal", x, z, y, size / 2);
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
			List<TRTerrain> terrains, List<DUDVWaterTile> waters, List<TREntity> entities, float x, float y, float z, float size, String heightMap, float maxHeight) {
		terrain = new TRTerrain(x, y, z, size, texturePack, blendMap, heightMap, maxHeight);
		terrains.add(terrain);
		water = new DUDVWaterTile("waterDUDV", "normal", x, z, y, size / 2);
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
	
	public TRTerrain getTerrain() {
		return terrain;
	}

	public DUDVWaterTile getWater() {
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
			List<TRTerrain> terrains, List<DUDVWaterTile> waters, List<TREntity> entities, float x, float y, float z, float size, int seed) {
		
		this.position = new Vector3f(x, y, z);
		
		//terrain = new TRTerrain(128, x, y, z, size, texturePack, blendMap, seed, 575);
		terrain = new TRTerrain(x, y, z, size, texturePack, blendMap, "crop1", 20000);
		
		terrains.add(terrain);
		water = new DUDVWaterTile("waterDUDV", "normal", x, z, y - 5000, size / 2);
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
