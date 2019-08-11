package demo;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;

import engine.objStuff.OBJParser;
import engine.postProcessing.Fbo;
import engine.postProcessing.PostProcessing;
import engine.renderEngine.Loader;
import engine.renderEngine.TRDisplayManager;
import engine.renderEngine.TRProjectionCtx;
import engine.renderEngine.TRRenderEngine;
import engine.renderEngine.guis.GUITexture;
import engine.renderEngine.guis.render.GUIRenderer;
import engine.renderEngine.models.RawModel;
import engine.renderEngine.models.TexturedModel;
import engine.renderEngine.textures.ModelTexture;
import engine.scene.TRScene;
import engine.scene.entities.Light;
import engine.scene.entities.StaticEntity;
import engine.scene.entities.camera.RogueCamera;
import engine.scene.entities.camera.TRCamera;
import engine.scene.particles.Particle;
import engine.scene.particles.ParticleTexture;
import engine.scene.skybox.SkyboxTexture;
import engine.scene.skybox.TRSkybox;

public class Main {

	public static void main(String[] args) {
		
		TRDisplayManager.createDisplay(3200 - 1600, 1800 - 900, 60, "Testing", null);				
		
		TRRenderEngine engine = new TRRenderEngine(
				TRRenderEngine.RENDER_ENTITIES_BIT | 
				TRRenderEngine.RENDER_TERRAIN_BIT |
				TRRenderEngine.RENDER_SKYBOX_BIT | 
				TRRenderEngine.RENDER_DUDVWATER_BIT,
				new TRProjectionCtx(1, 20000, TRCamera.STD_FOV));
		
		PostProcessing.init();
	
		Fbo fbo = new Fbo(Display.getWidth(), Display.getHeight());
		Fbo output = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_TEXTURE);
		Fbo output2 = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_TEXTURE);

				
		TRScene scene = new TRScene();
		scene.addLight(new Light(new Vector3f(2000, 2000, 2000), new Vector3f(1.3f, 1.3f, 1.3f)));

		
		TRCamera cam = new RogueCamera();
		scene.setCamera(cam);

		GUIRenderer guiR = new GUIRenderer();
		List<GUITexture> guis = new ArrayList<GUITexture>();
		
		
		RawModel hRaw = OBJParser.loadObjModelWProperTexSeams("helibody");
		hRaw.doubleSidedFaces = true;
		ModelTexture hTex = new ModelTexture(Loader.loadTexture("helitex"));
		hTex.setReflectivity(0.5f);
		hTex.setShineDamper(10);
		TexturedModel hModel = new TexturedModel(hRaw, hTex);
		
		StaticEntity hEntity = new StaticEntity(hModel, new Vector3f(), 0, 0, 0, 100);
		scene.addEntityToRoot(hEntity);
		
		
		
		RawModel dRaw = OBJParser.loadObjModelWProperTexSeams("dragon");
		ModelTexture dTex = new ModelTexture(Loader.loadTexture("gold"));
		dTex.setReflectivity(2f);
		dTex.setShineDamper(15);
		TexturedModel dModel = new TexturedModel(dRaw, dTex);
		
		StaticEntity dEntity = new StaticEntity(dModel, new Vector3f(500, 0, 0), 0, 0, 0, 25);
		scene.addEntityToRoot(dEntity);
		
		/*TRSkybox skybox = new TRSkybox(10000);
		skybox.setTexture1(new SkyboxTexture(TRSkybox.locateSkyboxTextures("high")));
		scene.setSkybox(skybox);*/
		
		ParticleTexture tex = new ParticleTexture(Loader.loadTexture("sficon"), 1);
		new Particle(tex, new Vector3f(0, 1000, 0), new Vector3f(), 0, 1000, 0, 50);
		
		while (!Display.isCloseRequested()) {
			
			hEntity.rotate(0, TRDisplayManager.getFrameDeltaTime() * 10, 0);
			dEntity.rotate(0, -TRDisplayManager.getFrameDeltaTime() * 10, 0);
			
			cam.update();
			

			engine.renderScene(scene, fbo);
			fbo.resolveToFbo(GL30.GL_COLOR_ATTACHMENT0, output);
			fbo.resolveToFbo(GL30.GL_COLOR_ATTACHMENT1, output2);
			PostProcessing.doPostProcessing(output.getColourTexture(), output2.getColourTexture());
			guiR.render(guis);
			
			TRDisplayManager.updateDisplay();
		}
		
		engine.cleanUp();
		guiR.cleanUp();
		PostProcessing.cleanUp();
		TRDisplayManager.closeDisplay();
	}

}
