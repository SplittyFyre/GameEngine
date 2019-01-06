package gameplay.entities.hostiles;

import org.lwjgl.util.vector.Vector3f;

import gameplay.entities.entityUtils.ITakeDamage;
import renderEngine.models.TexturedModel;
import scene.entities.Entity;

public abstract class Enemy extends Entity implements ITakeDamage {
	
	public Enemy(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}
	
	public Enemy(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scaleX, float scaleY, float scaleZ) {
		super(model, position, rotX, rotY, rotZ, scaleX, scaleY, scaleZ);
	}
	
	protected boolean hasDied = false;
	
	public boolean isDead() {
		return hasDied;
	}
	
	public void setDead() {
		 hasDied = true;
	}

	public abstract void update();

}
