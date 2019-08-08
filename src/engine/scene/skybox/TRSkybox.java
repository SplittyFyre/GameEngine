package engine.scene.skybox;

import engine.renderEngine.TRDisplayManager;
import engine.renderEngine.Loader;
import engine.renderEngine.models.RawModel;

public class TRSkybox {
	
	private final RawModel cube;
	private final float size;
	
	private SkyboxTexture texture1, texture2;
	public float blend;
	
	public float lowerFadeBound = -1000000, upperFadeBound = -1000000;
	public void setFadeBounds(float lower, float upper) {
		lowerFadeBound = lower;
		upperFadeBound = upper;
	}
	
	private float bloomEffect = 0;
	public float getBloomEffect() {
		return bloomEffect;
	}

	public void setBloomEffect(float bloomEffect) {
		this.bloomEffect = bloomEffect;
	}

	public float rotSpeed = 0, rotation = 0;
	public void rotate() {
		this.rotation += this.rotSpeed * TRDisplayManager.getFrameDeltaTime();
	}

	public SkyboxTexture getTexture1() {
		return texture1;
	}

	public void setTexture1(SkyboxTexture texture1) {
		this.texture1 = texture1;
	}

	public SkyboxTexture getTexture2() {
		return texture2;
	}

	public void setTexture2(SkyboxTexture texture2) {
		this.texture2 = texture2;
	}
	
	
	public void setTexture1(String[] textureFiles) {
		this.texture1 = new SkyboxTexture(textureFiles);
	}
	public void setTexture2(String[] textureFiles) {
		this.texture2 = new SkyboxTexture(textureFiles);
	}
	

	public float getBlend() {
		return blend;
	}

	public void setBlend(float blend) {
		this.blend = blend;
	}
	public RawModel getCube() {
		return cube;
	}
	

	public TRSkybox(float size) {
		this.size = size;
		this.cube = Loader.loadToVAO( new float[] {   
		    -size,  size, -size,
		    -size, -size, -size,
		    size, -size, -size,
		     size, -size, -size,
		     size,  size, -size,
		    -size,  size, -size,
	
		    -size, -size,  size,
		    -size, -size, -size,
		    -size,  size, -size,
		    -size,  size, -size,
		    -size,  size,  size,
		    -size, -size,  size,
	
		     size, -size, -size,
		     size, -size,  size,
		     size,  size,  size,
		     size,  size,  size,
		     size,  size, -size,
		     size, -size, -size,
	
		    -size, -size,  size,
		    -size,  size,  size,
		     size,  size,  size,
		     size,  size,  size,
		     size, -size,  size,
		    -size, -size,  size,
	
		    -size,  size, -size,
		     size,  size, -size,
		     size,  size,  size,
		     size,  size,  size,
		    -size,  size,  size,
		    -size,  size, -size,
	
		    -size, -size, -size,
		    -size, -size,  size,
		     size, -size, -size,
		     size, -size, -size,
		    -size, -size,  size,
		     size, -size,  size
		}, 3);
	}
	/**
	 * returns {name + "RT", name + "LF", name + "DN", name + "UP", name + "BK", name + "FT"}
	 */
	public static String[] locateSkyboxTextures(String name) {
		return new String[] {name + "RT", name + "LF", name + "DN", name + "UP", name + "BK", name + "FT"};
	}
	
	public static String[] skyboxTexturesInFolder(String foldername) {
		String pathpart = foldername + '/';
		return new String[] {pathpart + "RT", pathpart + "LF", pathpart + "DN", pathpart + "UP", pathpart + "BK", pathpart + "FT"};
	}

}
