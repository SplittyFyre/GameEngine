package engine.renderEngine.guis;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

public class GUIStruct implements IGUI {
	
	private List<IGUI> children = new ArrayList<IGUI>();
	private Vector2f position;
	
	public GUIStruct(Vector2f position) {
		this.position = position;
	}
	
	public void addChild(IGUI gui) {
		children.add(gui);
		gui.move(position.x, position.y);
	}
	
	public void addChildWithoutTransform(IGUI gui) {
		children.add(gui);
	}

	@Override
	public void update() {
		for (IGUI child : children) {
			child.update();
		}
	}

	@Override
	public void hide(List<GUITexture> guis) {
		for (IGUI child : children) {
			child.hide(guis);
		}
	}

	@Override
	public void show(List<GUITexture> guis) {
		for (IGUI child : children) {
			child.show(guis);
		}
	}

	@Override
	public void move(float dx, float dy) {
		this.position.x += dx;
		this.position.y += dy;
		for (IGUI child : children) {
			child.move(dx, dy);
		}
	}
	
	public void setPosition(float x, float y) {
		float dx = x - this.position.x;
		float dy = y - this.position.y;
		this.move(dx, dy);
	}
	
	public void setPosition(Vector2f vec) {
		float dx = vec.x - this.position.x;
		float dy = vec.y - this.position.y;
		this.move(dx, dy);
	}

}
