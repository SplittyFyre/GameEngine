package engine.renderEngine;

import org.lwjgl.util.vector.Matrix4f;

import engine.scene.entities.camera.TRCamera;

public class TRProjectionCtx {
	
	public final float nearPlane, farPlane, fov;
	private final Matrix4f mat;
	
	public Matrix4f getMatrix() {
		return mat;
	}
	
	public TRProjectionCtx(float nearPlane, float farPlane, float fov) {
		this.nearPlane = nearPlane;
		this.farPlane = farPlane;
		this.fov = fov;
		this.mat = TRCamera.createProjectionMatrix(nearPlane, farPlane, fov);
	}

}
