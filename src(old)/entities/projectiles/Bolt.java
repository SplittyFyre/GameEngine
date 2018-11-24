package entities.projectiles;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderEngine.DisplayManager;

public class Bolt extends Projectile {
	
	private float movementCoefficient;
	private float elapsedTime = 0;
	private static final int SPEED = 500;
	
	public boolean isDead() {
		return super.isDead;
	}

	public Bolt(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, float damage, float movementCoefficient) {
		super(model, position, rotX, rotY, rotZ, scale, damage);
		this.movementCoefficient = movementCoefficient;
	}

	public void update() {
		
		float distanceMoved = (SPEED + movementCoefficient) * DisplayManager.getFrameTime();
		float dx = (float) (distanceMoved * Math.sin(Math.toRadians(super.getRotY())));
		float dy = (float) (distanceMoved * Math.sin(Math.toRadians(super.getRotX())));
		float dz = (float) (distanceMoved * Math.cos(Math.toRadians(super.getRotY())));
		super.move(dx, -dy, dz);
		elapsedTime += DisplayManager.getFrameTime();
		if (elapsedTime > 1)
			super.isDead = true;
		
	}

	@Override
	public float getDamage() {
		return this.damage;
	}
	
}