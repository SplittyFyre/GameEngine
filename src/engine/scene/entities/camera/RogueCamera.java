package engine.scene.entities.camera;

import java.awt.RenderingHints.Key;

import org.lwjgl.input.Keyboard;

import engine.renderEngine.TRDisplayManager;

public class RogueCamera extends TRCamera {
	
	private static float SPEED = 1000;
	private static float UPSPEED = 500;

	@Override
	public void move() {
		
		float mov = 0;
		float up = 0;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			mov = SPEED;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			mov = -SPEED;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			yaw -= 90f * TRDisplayManager.getFrameDeltaTime();
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			yaw += 90f * TRDisplayManager.getFrameDeltaTime();
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			up = UPSPEED;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			up = -UPSPEED;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_K)) {
			pitch -= 45f * TRDisplayManager.getFrameDeltaTime();
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_J)) {
			pitch += 45f * TRDisplayManager.getFrameDeltaTime();
		}
		
		float distanceMoved = mov * TRDisplayManager.getFrameDeltaTime();
		
		float dx = (float) (distanceMoved * Math.sin(Math.toRadians(180 - yaw)));
		float dy = up * TRDisplayManager.getFrameDeltaTime();
		float dz = (float) (distanceMoved * Math.cos(Math.toRadians(180 - yaw)));
		
		position.x += dx;
		position.y += dy;
		position.z += dz;
		
		
		
	}

}
