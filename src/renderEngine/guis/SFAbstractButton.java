package renderEngine.guis;

import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.textures.GUITexture;

public abstract class SFAbstractButton implements IButton, IGUI {
	
	protected GUITexture buttonTexture;
	private Vector2f defaultScale;
	private boolean isHidden = true, isHovering = false;
	private float px = 0, py = 0;
	private Vector2f origin = null;
	
	public SFAbstractButton(List<IGUI> list, String texture, Vector2f position, Vector2f scale) {
		buttonTexture = new GUITexture(Loader.loadTexture(texture), position, scale);
		defaultScale = new Vector2f(scale);
		list.add(this);
	}
	
	public SFAbstractButton(List<IGUI> list, String texture, Vector2f origin, Vector2f position, Vector2f scale) {
		buttonTexture = new GUITexture(Loader.loadTexture(texture), origin, position, scale);
		defaultScale = new Vector2f(scale);
		this.origin = origin;
		list.add(this);
	}
	
	public SFAbstractButton(List<IGUI> list, String texture, Vector2f position, Vector2f scale, float hitboxXP, float hitboxYP) {
		buttonTexture = new GUITexture(Loader.loadTexture(texture), position, scale);
		defaultScale = new Vector2f(scale);
		this.px = hitboxXP;
		this.py = hitboxYP;
		list.add(this);
	}
	
	public SFAbstractButton(String texture, Vector2f position, Vector2f scale) {
		buttonTexture = new GUITexture(Loader.loadTexture(texture), position, scale);
		defaultScale = new Vector2f(scale);
	}
	
	public SFAbstractButton(String texture, Vector2f position, Vector2f scale, float hitboxXP, float hitboxYP) {
		buttonTexture = new GUITexture(Loader.loadTexture(texture), position, scale);
		defaultScale = new Vector2f(scale);
		this.px = hitboxXP;
		this.py = hitboxYP;
	}
	
	@Override
	public void show(List<GUITexture> textures) {
		buttonShow(textures);
	}
	
	@Override
	public void update() {
		buttonUpdate();
	}
	
	@Override
	public void hide(List<GUITexture> textures) {
		buttonHide(textures);
	}
	
	public GUITexture getTexture() {
		return buttonTexture;
	}
	
	public void move(float dx, float dy) {
		buttonTexture.getPosition().x += dx;
		buttonTexture.getPosition().y += dy;
	}
	
	@Override
	public void buttonUpdate() {
		if (!isHidden) {
			Vector2f location = buttonTexture.getPosition();
			Vector2f scale = buttonTexture.getScale();
			Vector2f mouseCoordinates = DisplayManager.getNormalizedMouseCoords();
			if (mouseIntersect(location, mouseCoordinates, scale)) {
				whileHovering(this);
				if (!isHovering) {
					isHovering = true;
					onStartHover(this);
				}
				while (Mouse.next()) { 
					if (Mouse.getEventButtonState() && Mouse.isButtonDown(0) && Mouse.getEventButton() == 0) {
				        onClick(this);    
				    }
				}

				if (Mouse.isButtonDown(0))
					whileHolding(this);
				
			} else if (isHovering) {
				isHovering = false;
				onStopHover(this);
			}
		}
	}
	
	@Override
	public void buttonShow(List<GUITexture> textures) {
		if (isHidden) {
			textures.add(buttonTexture);
			isHidden = false;
		}
	}
	
	@Override
	public void buttonHide(List<GUITexture> textures) {
		if (!isHidden) {
			textures.remove(buttonTexture);
			isHidden = true;
		}
	}
	
	@Override
	public void resetScale() {
		buttonTexture.setScale(defaultScale);
	}
	
	@Override
	public void playHoverAnimation(float scaleIncrease) {
		buttonTexture.setScale(new Vector2f(defaultScale.x + scaleIncrease, defaultScale.y + scaleIncrease));
	}
	
	private boolean mouseIntersect(Vector2f location, Vector2f mouseCoordinates, Vector2f scale) {
		return location.y + py + scale.y > -mouseCoordinates.y && 
				location.y - py - scale.y < -mouseCoordinates.y && 
				location.x + px + scale.x > mouseCoordinates.x && 
				location.x - px - scale.x < mouseCoordinates.x;
	}
	
	public boolean isHidden() {
		return isHidden;
	}
	
}
