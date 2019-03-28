package engine.renderEngine;

import org.lwjgl.util.vector.Matrix4f;

import engine.postProcessing.Fbo;
import engine.scene.TRScene;
import engine.scene.entities.camera.Camera;

public class TRRenderEngine {
	
	public static final int RENDER_ENTITIES_BIT = 1;
	public static final int RENDER_TERRAIN_BIT = 2;
	public static final int RENDER_SKYBOX_BIT = 4;
	public static final int RENDER_DUDVWATER_BIT = 8;
	
	private MasterRenderSystem renderer;
	private static Matrix4f passMatrix;
	private static final Matrix4f permenantNormalMatrix = Camera.createProjectionMatrix(1, 200000, Camera.STD_FOV);
	
	private static final Matrix4f distortedProjectionMatrix = Camera.createDistortedProjectionMatrix(1, 200000);
	
	private TRRenderEngine(MasterRenderSystem renderer) {
		this.renderer = renderer;
	}
	
	public void renderScene(TRScene scene, Fbo fbo) {
		renderer.renderMainPass(scene, fbo);
	}
	
	public void renderMiniMapScene(TRScene scene) {
		//renderer.setProjectionMatrix(permenantLargeMatrix);
		renderer.renderMiniMapPass(scene);
		//renderer.setProjectionMatrix(permenantNormalMatrix);
	}
	
	public void cleanUp() {
		renderer.cleanUp();
	}
	
	public static TRRenderEngine init(int renderAvailableMasks) {
		passMatrix = permenantNormalMatrix;
		MasterRenderSystem renderer = new MasterRenderSystem(renderAvailableMasks, passMatrix);
		return new TRRenderEngine(renderer);
	}
	
	public Matrix4f getProjectionMatrix() {
		return passMatrix;
	}

}
