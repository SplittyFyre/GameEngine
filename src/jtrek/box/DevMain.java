package jtrek.box;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import engine.objStuff.OBJParser;
import engine.renderEngine.DisplayManager;
import engine.renderEngine.Loader;
import engine.renderEngine.TRRenderEngine;
import engine.renderEngine.models.TexturedModel;
import engine.renderEngine.textures.ModelTexture;
import engine.scene.ICScene;
import engine.scene.entities.StaticEntity;
import engine.scene.entities.camera.RogueCamera;
import engine.scene.particles.ParticleWatcher;

public class DevMain {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		TRRenderEngine engine = TRRenderEngine.init(
				TRRenderEngine.RENDER_ENTITIES_BIT | 
				TRRenderEngine.RENDER_TERRAIN_BIT | 
				TRRenderEngine.RENDER_SKYBOX_BIT | 
				TRRenderEngine.RENDER_DUDVWATER_BIT);
		ParticleWatcher.init(engine.getProjectionMatrix());
		
		ICScene scene = new ICScene();
		
		TexturedModel model = new TexturedModel(OBJParser.loadObjModel("dragon"), new ModelTexture(Loader.loadTexture("white")));
		
		StaticEntity entity = new StaticEntity(model, new Vector3f(0, 0, -50), 0, 0, 0, 10);
		scene.getEntities().add(entity);
		
		scene.setCamera(new RogueCamera());
		
		while (!Display.isCloseRequested()) {
			
			scene.getCamera().move();
			
			engine.renderScene(scene, null);
			
			DisplayManager.updateDisplay();
		}
		
		engine.cleanUp();
		
		DisplayManager.closeDisplay();

	}

}
