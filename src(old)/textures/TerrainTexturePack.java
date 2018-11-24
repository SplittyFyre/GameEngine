package textures;

public class TerrainTexturePack {
	
	private TerrainTexture backgroundTexture;
	private TerrainTexture rTexture;
	private TerrainTexture gTexture;
	private TerrainTexture bTexture;
	
	public TerrainTexturePack(TerrainTexture backtext, TerrainTexture rtext, TerrainTexture gtext, TerrainTexture btext) {
		
		this.backgroundTexture = backtext;
		this.rTexture = rtext;
		this.gTexture = gtext;
		this.bTexture = btext;
		
	}
	
	public TerrainTexture getBackgroundTexture() {
		return backgroundTexture;
	}
	public TerrainTexture getrTexture() {
		return rTexture;
	}
	public TerrainTexture getgTexture() {
		return gTexture;
	}
	public TerrainTexture getbTexture() {
		return bTexture;
	}

}
