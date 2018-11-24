package entities.projectiles;

import org.lwjgl.util.vector.Vector3f;

import box.TaskManager;
import entities.Entity;
import models.TexturedModel;

public abstract class Projectile extends Entity {
	
	protected float dx, dy, dz;
	protected boolean isDead = false;
	protected float damage;
	
	public abstract float getDamage();
	
	public Projectile(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, float damage) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.damage = damage;
	}
	
	public Projectile(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, float damage, float dx, float dy, float dz) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.dx = dx;
		this.dy = dy;
		this.dz = dz;
		this.damage = damage;
	}

	@Override
	public void respondToCollision() {
		isDead = true;
		TaskManager.burnParticleSystem.generateParticles(this.getPosition());
	}
	
	public void setDead() {
		isDead = true;
	}

}
