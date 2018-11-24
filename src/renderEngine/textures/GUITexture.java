package renderEngine.textures;

import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import renderEngine.guis.IGUI;

public class GUITexture implements IGUI {
	
	private boolean hidden = true;
	
	private int textureID;
	private Vector2f position;
	private Vector2f scale;
	private float rotation;
	private boolean flipped = false;
	
	public boolean flagAlpha = false;
	public float custAlpha = 0;
	
	public GUITexture(int textureID, Vector2f position, Vector2f scale) {
		this.textureID = textureID;
		this.position = position;
		this.scale = scale;
		this.rotation = 0;
	}
	
	public GUITexture(int textureID, Vector2f origin, Vector2f position, Vector2f scale) {
		this.textureID = textureID;
		this.position = Vector2f.add(origin, position, null);
		this.scale = scale;
		this.rotation = 0;
	}
	
	public GUITexture(int textureID, Vector2f position, Vector2f scale, float rotation) {
		this.textureID = textureID;
		this.position = position;
		this.scale = scale;
		this.rotation = rotation;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	
	public boolean isFlipped() {
		return flipped;
	}

	public void setFlipped(boolean flag) {
		this.flipped = flag;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

	public int getTextureID() {
		return textureID;
	}

	public Vector2f getPosition() {
		return position;
	}

	public Vector2f getScale() {
		return scale;
	}
	
	public void setScale(Vector2f scale) {
		this.scale = scale;
	}
	
	public void setTexture(int textureID) {
		this.textureID = textureID;
	}

	@Override
	@Deprecated
	public void update() {
		
	}

	@Override
	public void hide(List<GUITexture> textures) {
		if (!hidden) {
			textures.remove(this);
			hidden = true;
		}
	}

	@Override
	public void show(List<GUITexture> textures) {
		if (hidden) {
			textures.add(this);
			hidden = false;
		}
	}

}
