package renderEngine;

import org.lwjgl.util.vector.Matrix4f;

import scene.Scene;
import scene.entities.Camera;

public class RenderEngine {
	
	private MasterRenderSystem renderer;
	private static Matrix4f passMatrix;
	private static final Matrix4f permenantNormalMatrix = Camera.createProjectionMatrix();
	private static final Matrix4f permenantLargeMatrix = Camera.createLargerProjectionMatrix();
	
	private RenderEngine(MasterRenderSystem renderer) {
		this.renderer = renderer;
	}
	
	public void renderScene(Scene scene) {
		renderer.renderMainPass(scene);
	}
	
	public void renderMiniMapScene(Scene scene) {
		renderer.setProjectionMatrix(permenantLargeMatrix);
		renderer.renderMiniMapPass(scene);
		renderer.setProjectionMatrix(permenantNormalMatrix);
	}
	
	public void cleanUp() {
		renderer.cleanUp();
	}
	
	public static RenderEngine init() {
		Loader loader = new Loader();
		passMatrix = Camera.createProjectionMatrix();
		MasterRenderSystem mRenderer = new MasterRenderSystem(loader, passMatrix);
		return new RenderEngine(mRenderer);
	}
	
	public Matrix4f getProjectionMatrix() {
		return passMatrix;
	}

}
