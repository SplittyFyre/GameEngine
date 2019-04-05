package jtrek.box;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import engine.objStuff.OBJParser;
import engine.renderEngine.Loader;
import engine.renderEngine.TRDisplayManager;
import engine.renderEngine.TRRenderEngine;
import engine.renderEngine.models.TexturedModel;
import engine.renderEngine.textures.ModelTexture;
import engine.scene.TRScene;
import engine.scene.entities.Light;
import engine.scene.entities.StaticEntity;
import engine.scene.entities.camera.RogueCamera;
import engine.scene.particles.ParticleWatcher;

public class DevMain {

	public static void main(String[] args) {
		
		TRDisplayManager.createDisplay(3200, 1800, 120, "Testing", null);
		
		TRRenderEngine engine = new TRRenderEngine(
				TRRenderEngine.RENDER_ENTITIES_BIT | 
				TRRenderEngine.RENDER_TERRAIN_BIT | 
				TRRenderEngine.RENDER_SKYBOX_BIT | 
				TRRenderEngine.RENDER_DUDVWATER_BIT,
				1, 200000);
		ParticleWatcher.init(engine.getProjectionMatrix());
		
		TRScene scene = new TRScene();
		
		ModelTexture tex = new ModelTexture(Loader.loadTexture("white"));
		tex.setReflectivity(10);
		tex.setShineDamper(15);
		TexturedModel model = new TexturedModel(OBJParser.loadObjModel("dragon"), tex);
		
		StaticEntity entity = new StaticEntity(model, new Vector3f(0, 0, -50), 0, 0, 0, 10);
		scene.getEntities().add(entity);
				
		scene.setCamera(new RogueCamera());
		
		scene.addLight(new Light(new Vector3f(0, 40, 0), new Vector3f(1.3f, 1.3f, 1.3f)));
		
		while (!Display.isCloseRequested()) {
			
			scene.getCamera().move();
			
			engine.renderScene(scene, null);
			
			TRDisplayManager.updateDisplay();
		}
		
		engine.cleanUp();
		
		TRDisplayManager.closeDisplay();

	}

}
