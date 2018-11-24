package entities;

import org.lwjgl.util.vector.Vector3f;

import entities.entityUtils.ITakeDamage;
import models.TexturedModel;

public class BorgVessel extends Entity implements ITakeDamage {
	
	private float health = 1000;
	private boolean isDead = false;

	public BorgVessel(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}

	public boolean isDead() {
		return isDead;
	}

	@Override
	public void respondToCollision(float damage) {
		health -= damage;
		if (health <= 0)
			this.isDead = true;
	}

	/**
	 * @deprecated
	 * **/
	@Override
	public void respondToCollision() {
		
	}
	
	public void takeDamage(float damage) {
		health -= damage;
		if (health <= 0)
			this.isDead = true;
	}

}
