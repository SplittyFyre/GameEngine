package scene.entities;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import collision.BoundingBox;
import renderEngine.models.TexturedModel;

public abstract class Entity {

	private TexturedModel model;
	private Vector3f position;
	private float rotX, rotY, rotZ;
	private float scaleX, scaleY, scaleZ;
	private BoundingBox boundingBox;
	private final BoundingBox staticBoundingBox;
	
	public Entity matpremul = null;
	
	public boolean customRotationAxis = false;
	public boolean ignoreRY = false;
	public Vector3f customOrigin = null;
	
	public Vector4f highlight = null;
	
	public boolean translucent = false;
	
	public Vector4f getHighlight() {
		return highlight;
	}

	public void setHighlight(Vector4f highlight) {
		this.highlight = highlight;
	}

	private int textureIndex = 0;
	
	public abstract void respondToCollision();
	
	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scaleX = scale;
		this.scaleY = scale;
		this.scaleZ = scale;
		this.boundingBox = new BoundingBox(this.getModel().getRawModel().getBoundingBox());
		this.staticBoundingBox = new BoundingBox(boundingBox);
	}
	
	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scaleX, float scaleY, float scaleZ) {
		
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.scaleZ = scaleZ;
		this.boundingBox = new BoundingBox(this.getModel().getRawModel().getBoundingBox());
		this.staticBoundingBox = new BoundingBox(boundingBox);
	}
	
	public Entity(TexturedModel model, int index, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scaleX = scale;
		this.scaleY = scale;
		this.scaleZ = scale;
		this.boundingBox = new BoundingBox(this.getModel().getRawModel().getBoundingBox());
		this.staticBoundingBox = new BoundingBox(boundingBox);
		
	}

	public float getTextureXOffset() {
		int column = textureIndex % model.getTexture().getNumRows();
		
		return (float) column / (float) model.getTexture().getID();
	}
	
	public float getTextureYOffset() {
		int row = textureIndex / model.getTexture().getNumRows();
		
		return (float) row / (float) model.getTexture().getNumRows();
	}
	
	public void move(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	public void rotate(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public Vector3f getScale() {
		return new Vector3f(scaleX, scaleY, scaleZ);
	}

	public void setScale(float scaleX, float scaleY, float scaleZ) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.scaleZ = scaleZ;
	}
	
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}
	
	public BoundingBox getStaticBoundingBox() {
		return new BoundingBox(staticBoundingBox);
	}

}
