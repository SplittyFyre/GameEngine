package engine.renderEngine;

import org.lwjgl.util.vector.Matrix4f;

import engine.postProcessing.Fbo;
import engine.scene.TRScene;
import engine.scene.entities.camera.TRCamera;

public class TRRenderEngine {
	
	public static final int RENDER_ENTITIES_BIT = 1;
	public static final int RENDER_TERRAIN_BIT = 2;
	public static final int RENDER_SKYBOX_BIT = 4;
	public static final int RENDER_DUDVWATER_BIT = 8;
	
	private MasterRenderSystem renderer;
	
	private final float nearPlane, farPlane;
	
	//private static final Matrix4f permenantNormalMatrix = Camera.createProjectionMatrix(1, 200000, Camera.STD_FOV);
	private final Matrix4f projectionMatrix;
		
	public static float nearPlaneInUse;
	public static float farPlaneInUse;
	
	public TRRenderEngine(int renderAvailableMasks, float nearPlane, float farPlane) {
		this.nearPlane = nearPlane;
		this.farPlane = farPlane;
		this.projectionMatrix = TRCamera.createProjectionMatrix(nearPlane, farPlane, TRCamera.STD_FOV);
		this.renderer = new MasterRenderSystem(renderAvailableMasks, projectionMatrix);
	}
	
	public void renderScene(TRScene scene, Fbo fbo) {
		nearPlaneInUse = nearPlane;
		farPlaneInUse = farPlane;
		renderer.renderMainPass(scene, fbo);
	}
	
	public void cleanUp() {
		renderer.cleanUp();
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

}
