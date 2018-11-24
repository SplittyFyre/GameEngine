package renderEngine.guis;

import java.util.List;

import renderEngine.textures.GUITexture;

public interface IButton {
	
	void onClick(IButton button);
	void whileHolding(IButton button);
	void onStartHover(IButton button);
	void onStopHover(IButton button);
	void whileHovering(IButton button);
	void buttonShow(List<GUITexture> textures);
	void buttonHide(List<GUITexture> textures);
	void playHoverAnimation(float scaleIncrease);
	void resetScale();
	void buttonUpdate();
	
}
