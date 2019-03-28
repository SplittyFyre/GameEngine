package engine.scene.entities.camera;

import org.lwjgl.input.Keyboard;

import engine.renderEngine.DisplayManager;

public class RogueCamera extends Camera {
	
	private static float SPEED = 100;
	private static float UPSPEED = 50;

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
			yaw -= 90f * DisplayManager.getFrameDeltaTime();
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			yaw += 90f * DisplayManager.getFrameDeltaTime();
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			up = UPSPEED;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			up = -UPSPEED;
		}
		
		float distanceMoved = mov * DisplayManager.getFrameDeltaTime();
		
		float dx = (float) (distanceMoved * Math.sin(Math.toRadians(180 - yaw)));
		float dy = up * DisplayManager.getFrameDeltaTime();
		float dz = (float) (distanceMoved * Math.cos(Math.toRadians(180 - yaw)));
		
		position.x += dx;
		position.y += dy;
		position.z += dz;
		
	}

}