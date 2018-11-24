package scene.entities.projectiles;

import org.lwjgl.util.vector.Vector3f;

import renderEngine.models.TexturedModel;
import scene.entities.Entity;

public abstract class Projectile extends Entity {
	
	protected float dx, dy, dz;
	protected boolean isDead = false;
	protected float damage;
	
	public abstract float getDamage();
	public abstract void update();
	
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
	
	public Projectile(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scaleX, float scaleY, float scaleZ
			, float damage, float dx, float dy, float dz) {
		super(model, position, rotX, rotY, rotZ, scaleX, scaleY, scaleZ);
		this.dx = dx;
		this.dy = dy;
		this.dz = dz;
		this.damage = damage;
	}
	
	protected void setDead() {
		this.isDead = true;
	}

	public boolean isDead() {
		return isDead;
	}

}
