package entities;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;

public class StaticEntity extends Entity {

	public StaticEntity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}

	@Override
	public void respondToCollision() {
		
	}

}
