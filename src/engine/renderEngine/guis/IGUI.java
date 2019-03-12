package engine.renderEngine.guis;

import java.util.List;

public interface IGUI {
	
	void update();
	void hide(List<GUITexture> textures);
	void show(List<GUITexture> textures);
	void move(float dx, float dy);
	
}