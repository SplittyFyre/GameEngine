package engine.scene.entities.camera;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public abstract class TRCamera {
	
	public static final float STD_FOV = 50;
	
	protected float pitch = 20;
	protected float yaw;
	protected float roll;
	
	protected Vector3f position = new Vector3f(0, 0, 0);
	
	public void invertPitch() {
		this.pitch = -pitch;
	}
	
	public abstract void move();

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	
	public void setYaw(float yaw) {
		this.yaw = yaw;
	}
	
	public void setRoll(float roll) {
		this.roll = roll;
	}
	
	public static Matrix4f createProjectionMatrix(float NEAR_PLANE, float FAR_PLANE, float FOV) {
		
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		Matrix4f projecMat = new Matrix4f();
		projecMat.m00 = x_scale;
		projecMat.m11 = y_scale;
		projecMat.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projecMat.m23 = -1;
		projecMat.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projecMat.m33 = 0;
		
		return projecMat;
	}

}
