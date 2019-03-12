package engine.scene.contexts;

public class SkyContext {
	
	public float fogDensity, fogGradient;
	public float skyR, skyG, skyB;
	
	public SkyContext(float density, float gradient, float r, float g, float b) {
		this.fogDensity = density;
		this.fogGradient = gradient;
		setSkyCol(r, g, b);
	}
	
	public void setSkyCol(float r, float g, float b) {
		this.skyR = r; this.skyG = g; this.skyB = b;
	}

}
