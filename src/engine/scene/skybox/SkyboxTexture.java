package engine.scene.skybox;

import engine.renderEngine.Loader;

public class SkyboxTexture {
	
	private int texture;
	
	/**
	 * {"right", "left", "top", "bottom", "back", "front"}
	 */
	public void setTexture(String[] textureFiles) {
		this.texture = Loader.loadCubeMap(textureFiles);
	}
	
	public int getTexture() {
		return texture;
	}
	
	/**
	 * {"right", "left", "top", "bottom", "back", "front"}
	 */
	public SkyboxTexture(String[] textureFiles) {
		setTexture(textureFiles);
	}

}
