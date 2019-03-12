package renderEngine.textures;

public class TerrainTexturePack {
	
	private int backgroundTexture;
	private int rTexture;
	private int gTexture;
	private int bTexture;
	private float tiling;
	
	public TerrainTexturePack(int backtext, int rtext, int gtext, int btext, float tiling) {		
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
