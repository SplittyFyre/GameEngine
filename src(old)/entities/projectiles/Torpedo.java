package entities.projectiles;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderEngine.DisplayManager;

public class Torpedo extends Projectile {
	
	private float elapsedTime = 0;
	
	public boolean isDead() {
		return super.isDead;
	}

	public Torpedo(TexturedModel model, int index, Vector3f position, float rotX, float rotY, float rotZ, float scale, float damage, float dx, float dy, float dz) {
		super(model, position, rotX, rotY, rotZ, scale, damage, dx, dy, dz);
	}

	public void update() {
		elapsedTime += DisplayManager.getFrameTime();
		super.move(dx, dy, dz);
		if (elapsedTime > 1) 
			super.isDead = true;
	}

	@Override
	public float getDamage() {
		return this.damage;
	}
	
}
