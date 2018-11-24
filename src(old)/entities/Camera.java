package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private float distanceFrom = 50;
	private float angleAround = 0;
	private float pitch = 20;
	private float yaw;
	private float roll;
	
	private Vector3f position = new Vector3f(100, 35, 50);
	private Entity player;
	
	public Camera(Entity player) {
		this.player = player;
	}
	
	public void move() {
		
		calculateAngleAround();
		calculatePitch();
		calculateZoom();
		float horizDistance = calculateHorizDistance();
		float verticDistance = calculateVerticDistance();
		calculateCameraPos(horizDistance, verticDistance);
		this.yaw = 180 - (player.getRotY() + angleAround);
		
	}

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
	
	private void calculateCameraPos(float horiz, float vertic) {
		
		float theta = player.getRotY() + angleAround;
		float offsetX = (float) (horiz * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horiz * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + vertic + 3.0f;
		
		
	}
	
	private float calculateHorizDistance() {
		return (float) (distanceFrom * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVerticDistance() {
		return (float) (distanceFrom * Math.sin(Math.toRadians(pitch)));
	}
	
	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFrom -= zoomLevel;
	}
	
	private void calculatePitch() {
		if (Mouse.isButtonDown(1)) {
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
		}
		
	}
	
	private void calculateAngleAround() {
		if (Mouse.isButtonDown(0)) {
			float angleChange = Mouse.getDX() * 0.3f;
			angleAround -= angleChange;
		}
	}

}
