package engine.scene.entities;

import org.lwjgl.util.vector.Vector3f;

import engine.renderEngine.models.TexturedModel;

public class StaticEntity extends TREntity {

	public StaticEntity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}
	
	public StaticEntity(TexturedModel model, int index, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, index, position, rotX, rotY, rotZ, scale);
	}


}
