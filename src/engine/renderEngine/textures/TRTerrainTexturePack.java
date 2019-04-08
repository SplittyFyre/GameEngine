package engine.renderEngine.textures;

public class TRTerrainTexturePack {
	
	private int backgroundTexture;
	private int rTexture;
	private int gTexture;
	private int bTexture;
	private float tiling;
	
	// TODO: come up with better name
	/**
	 *<p> tex1, tex2, tex3, tex4 </p>
	 *<p> background, r, g, b </p>
	 * 
	 * use cap1, cap2, cap3
	 */
	public boolean useAsAltitudeBasedTextures = false;
	
	public float cap1 = 0.25f, cap2 = 0.5f, cap3 = 0.75f;
	
	public TRTerrainTexturePack(int backtext, int rtext, int gtext, int btext, float tiling) {		
		this.backgroundTexture = backtext;
		this.rTexture = rtext;
		this.gTexture = gtext;
		this.bTexture = btext;
		this.tiling = tiling;	
	}
	
	public int getBackgroundTexture() {
		return backgroundTexture;
	}
	public int getrTexture() {
		return rTexture;
	}
	public int getgTexture() {
		return gTexture;
	}
	public int getbTexture() {
		return bTexture;
	}
	public float getTiling() {
		return tiling;
	}

}
