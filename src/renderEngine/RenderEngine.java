package renderEngine;

import org.lwjgl.util.vector.Matrix4f;

import postProcessing.Fbo;
import scene.ICScene;
import scene.entities.camera.Camera;

public class RenderEngine {
	
	private MasterRenderSystem renderer;
	private static Matrix4f passMatrix;
	private static final Matrix4f permenantNormalMatrix = Camera.createProjectionMatrix(1, 200000, Camera.STD_FOV);
	
	private static final Matrix4f distortedProjectionMatrix = Camera.createDistortedProjectionMatrix(1, 200000);
	
	private RenderEngine(MasterRenderSystem renderer) {
		this.renderer = renderer;
	}
	
	public void renderScene(ICScene scene, Fbo fbo) {
		renderer.renderMainPass(scene, fbo);
	}
	
	public void renderMiniMapScene(ICScene scene) {
		//renderer.setProjectionMatrix(permenantLargeMatrix);
		renderer.renderMiniMapPass(scene);
		//renderer.setProjectionMatrix(permenantNormalMatrix);
	}
	
	public void cleanUp() {
		renderer.cleanUp();
	}
	
	public static RenderEngine init() {
		passMatrix = permenantNormalMatrix;
		MasterRenderSystem renderer = new MasterRenderSystem(passMatrix);
		return new RenderEngine(renderer);
	}
	
	public Matrix4f getProjectionMatrix() {
		return passMatrix;
	}

}
