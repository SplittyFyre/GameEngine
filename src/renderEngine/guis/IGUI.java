package renderEngine.guis;

import java.util.List;

import renderEngine.textures.GUITexture;

public interface IGUI {
	
	void update();
	void hide(List<GUITexture> textures);
	void show(List<GUITexture> textures);
	
}