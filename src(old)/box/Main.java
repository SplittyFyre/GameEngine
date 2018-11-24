package box;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import collision.CollisionManager;
import entities.BorgVessel;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.players.PlayerWarshipVoyager;
import models.RawModel;
import models.TexturedModel;
import objStuff.OBJParser;
import particles.ParticleWatcher;
import postProcessing.Fbo;
import postProcessing.PostProcessing;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import utils.RaysCast;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

public class Main {
	
	private static List<Entity> entities = new ArrayList<Entity>();
	private static List<Entity> planets = new ArrayList<Entity>();
	private static List<Entity> enemies = new ArrayList<Entity>();
	
	public static List<List<Entity>> scene = new ArrayList<List<Entity>>();
	public static List<Entity> flatScene = new ArrayList<Entity>();
	
	public static void main(String[] args) {
		
		List<Terrain> terrains = new ArrayList<Terrain>();
		List<Light> lights = new ArrayList<Light>();
		List<WaterTile> waters = new ArrayList<WaterTile>();
		
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		MasterRenderer renderer = new MasterRenderer(loader);
		ParticleWatcher.init(loader, renderer.getProjectionMatrix());
		
		//TERRAIN TEXTURE********************************************************************
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		//OTHER TERRAIN STUFF****************************************************************
		
		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightMap");
		terrains.add(terrain);
		
		//FERNS******************************************************************************
		
		RawModel fernRaw = OBJParser.loadObjModel("fernModel", loader);
		ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
		fernTextureAtlas.setNumRows(2);
		TexturedModel fern = new TexturedModel(fernRaw, fernTextureAtlas);
		
		fern.getTexture().setUseFakeLighting(true);
		fern.getTexture().setTransparent(true);
		
		//PLAYERS****************************************************************************
		
		RawModel playerRaw = OBJParser.loadObjModel("starshipsomeone's", loader);
		TexturedModel playerText = new TexturedModel(playerRaw, new ModelTexture(loader.loadTexture("bottom")));
		
		RawModel apacheRaw = OBJParser.loadObjModel("apache", loader);
		TexturedModel apacheShip = new TexturedModel(apacheRaw, new ModelTexture(loader.loadTexture("dartship")));
		
		RawModel voyagerRaw = OBJParser.loadObjModel("warship_voyager_model", loader);
		TexturedModel voyagerShip = new TexturedModel(voyagerRaw, new ModelTexture(loader.loadTexture("warship_voyager_texture")));
		
		//PINE TREES*************************************************************************
		
		RawModel pineRaw = OBJParser.loadObjModel("pine", loader);
		TexturedModel pineText = new TexturedModel(pineRaw, new ModelTexture(loader.loadTexture("pine")));
		
		pineText.getTexture().setTransparent(true);
		pineText.getTexture().setUseFakeLighting(true);
		
		//LAMPS******************************************************************************
		
		RawModel lampRaw = OBJParser.loadObjModel("lampModel", loader);
		TexturedModel lampText = new TexturedModel(lampRaw, new ModelTexture(loader.loadTexture("lamp")));
		
		lampText.getTexture().setUseFakeLighting(true);
		
		//TORPEDOES**************************************************************************
		
		RawModel pretorpedo = OBJParser.loadObjModel("photon", loader);
		TexturedModel torpedo = new TexturedModel(pretorpedo, new ModelTexture(loader.loadTexture("photon")));
		
		//BOLTS******************************************************************************
		
		RawModel prebolt = OBJParser.loadObjModel("bolt", loader);
		TexturedModel bolt = new TexturedModel(prebolt, new ModelTexture(loader.loadTexture("bolt")));
		bolt.getTexture().setUseFakeLighting(true);
		
		//PHASERS****************************************************************************
		
		RawModel prephaser = OBJParser.loadObjModel("bolt", loader);
		TexturedModel phaser = new TexturedModel(prephaser, new ModelTexture(loader.loadTexture("orange")));
		phaser.getTexture().setUseFakeLighting(true);
		
		//BULLETS****************************************************************************
		
		RawModel prebullet = OBJParser.loadObjModel("bullet", loader);
		TexturedModel bullet = new TexturedModel(prebullet, new ModelTexture(loader.loadTexture("white")));
		bullet.getTexture().setUseFakeLighting(true);
		
		//PLANETS****************************************************************************
		
		RawModel plane = OBJParser.loadObjModel("photon", loader);
		TexturedModel planet = new TexturedModel(plane, new ModelTexture(loader.loadTexture("ponet")));
		
		//ENEMIES****************************************************************************
		
		RawModel borgRaw = OBJParser.loadObjModel("borge", loader);
		TexturedModel borgShip = new TexturedModel(borgRaw, new ModelTexture(loader.loadTexture("borge")));
		borgShip.getTexture().setUseFakeLighting(true);
		
		//END TEXTURE SECTION****************************************************************
		
		Random random = new Random();
		
		Light sun = new Light(new Vector3f(10000, 10000, -10000), new Vector3f(1.3f, 1.3f, 1.3f));
		lights.add(sun);
		
		for (int i = 0; i < 3; i++) {
			Light light = new Light(new Vector3f(random.nextFloat() * 1000000, random.nextFloat() * 1000000, random.nextFloat() * 1000000), 
					new Vector3f(1.3f, 1.3f, 1.3f));
			lights.add(light);
		}
		
		PlayerWarshipVoyager player = new PlayerWarshipVoyager(voyagerShip, new Vector3f(0, 0, 0), 0, 0, 0, 10);
		entities.add(player);
		
		//OTHER UTILS************************************************************************
		
		Camera camera = new Camera(player);
		RaysCast caster = new RaysCast(camera, renderer.getProjectionMatrix(), terrain);
		
		//SPECIAL WATER COMPONENTS***********************************************************
		
		WaterFrameBuffers buffers = new WaterFrameBuffers();
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), buffers);
		waters.add(new WaterTile(100, 20, -50));
		
		//ADDING RANDOM STUFF (PLACE HOLDER?)*************************************************
		
		for (int i = 0; i < 50; i++) {
			
			BorgVessel borj = new BorgVessel(borgShip, new Vector3f(random.nextFloat() * 1000, random.nextFloat() * 1000, random.nextFloat() * 1000), 0, 0, 0, 10);
			enemies.add(borj);
			
		}
		
		enemies.add(new BorgVessel(borgShip, new Vector3f(0, 0, 0), 0, 0, 0, 10));
		
		for (int i = 0; i < 69; i++) {
			
			BorgVessel borj = new BorgVessel(borgShip, new Vector3f(random.nextFloat() * 1000, random.nextFloat() * 100, random.nextFloat() * 1000), 0, 0, 0, 10);
			enemies.add(borj);
		}

		Fbo fbo = new Fbo(Display.getWidth(), Display.getHeight());
		Fbo output = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_TEXTURE);
		PostProcessing.init(loader);
		
		/**MAIN GAME LOOP*******************************************************************
		   MAIN GAME LOOP*******************************************************************
		   MAIN GAME LOOP*******************************************************************/
		
		while (!Display.isCloseRequested()) {
			
			System.exit(-1);
			//player.update(caster, torpedo, bolt, celestial_object);
			//player.update(bullet, bolt);
			player.update(phaser, caster);
			camera.move();
			caster.update();
			ParticleWatcher.update();
			
			scene.add(entities);
			scene.add(enemies);
			scene.add(player.projectiles);
			
			flatScene.clear();
			flatScene.addAll(enemies);
			flatScene.addAll(entities);
			flatScene.addAll(player.projectiles);
			
			fbo.bindFrameBuffer();
			
			checkDamageToEnemies();
			renderer.renderAll(flatScene, terrains, lights, camera);
			CollisionManager.checkCollisions();
			waterRenderer.render(waters, camera, sun);
			ParticleWatcher.renderParticles(camera);
			
			fbo.unbindFrameBuffer();
			fbo.resolveToFbo(output);
			PostProcessing.doPostProcessing(output.getColourTexture());
			
			DisplayManager.updateDisplay();
		}
		
		/**MAIN GAME LOOP*******************************************************************
		   MAIN GAME LOOP*******************************************************************
		   MAIN GAME LOOP*******************************************************************/
		
		//CLEAN UP***************************************************************************
		
		PostProcessing.cleanUp();
		fbo.cleanUp();
		output.cleanUp();
		waterShader.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		ParticleWatcher.cleanUp();
		DisplayManager.closeDisplay();
		
		//END OF main (String[] args)
		
	}
	
	private static void checkDamageToEnemies() {
		
		for (int i = 0; i < enemies.size(); i++) {

            BorgVessel enemy = (BorgVessel) enemies.get(i);
            
            if (enemy.isDead()) {
            	enemies.remove(i);
            	break;
            }
		}
	}
		
}
