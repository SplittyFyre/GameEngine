package renderEngine.models;

import renderEngine.textures.ModelTexture;

public class TexturedModel {
	
	private RawModel rawModel;
	private ModelTexture texture;

	public TexturedModel(RawModel model, ModelTexture texture){
		this.rawModel = model;
		this.texture = texture;
	}
	
	public TexturedModel(TexturedModel model) {
		this.rawModel = model.rawModel;
		this.texture = model.texture;
	}

	public RawModel getRawModel() {
		return rawModel;
	}

	public ModelTexture getTexture() {
		return texture;
	}
	
	public void setTexture(ModelTexture in) {
		this.texture = in;
	}

}
