package jtrek.gameplay.entities.hostiles;

import org.lwjgl.util.vector.Vector3f;

import engine.renderEngine.models.TexturedModel;
import engine.scene.entities.TREntity;
import jtrek.gameplay.entities.entityUtils.ITakeDamage;

public abstract class Enemy extends TREntity implements ITakeDamage {
	
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
