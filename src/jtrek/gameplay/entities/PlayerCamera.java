package jtrek.gameplay.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import engine.scene.entities.camera.TRCamera;
import jtrek.gameplay.entities.players.Player;

public class PlayerCamera extends TRCamera {
	
	private float distanceFrom = 50;
	private float angleAround = 0;
	
	private Player player;
	
	public PlayerCamera(Player player) {
		this.player = player;
		player.camera = this;
	}
	
	public void setAngleAround(float angle) {
		this.angleAround = angle;
	}
	
	@Override
	public void move() {
		calculateMovement();
		calculateZoom();
		float horizDistance = calculateHorizDistance();
		float verticDistance = calculateVerticDistance();
		calculateCameraPos(horizDistance, verticDistance);
		this.yaw = 180 - (player.getRotY() + angleAround);
		this.yaw %= 360;
	}

	public float getDistanceFrom() {
		return distanceFrom;
	}

	public void setDistanceFrom(float param) {
		this.distanceFrom = param;
	}
	
	private float calculateHorizDistance() {
		return (float) (distanceFrom * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVerticDistance() {
		return (float) (distanceFrom * Math.sin(Math.toRadians(pitch)));
	}
	
	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.5f;
		distanceFrom -= zoomLevel;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_MINUS)) {
			distanceFrom += 5f;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_EQUALS)) {
			distanceFrom -= 5f;
		}
		
	}
	
	private void calculateCameraPos(float horiz, float vertic) {
		float theta = player.getRotY() + angleAround;
		float offsetX = (float) (horiz * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horiz * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + vertic + 3.0f;
	}
	
	private void calculateMovement() {
		if (Mouse.isButtonDown(2)) {
			pitch -= Mouse.getDY() * 0.1f;
			angleAround -= Mouse.getDX() * 0.3f;
		}
	}
	
}
