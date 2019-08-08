package engine.scene.lensFlare;

import engine.renderEngine.ShaderProgram;

public class FlareModeShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "src/engine/scene/lensFlare/flareVertexShader.glsl";
	private static final String FRAGMENT_FILE = "src/engine/scene/lensFlare/flareFragmentShader.glsl";
	
	private int location_transform;
	private int location_brightness;

	public FlareModeShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public void loadTransformation(float x, float y, float z, float w){
		super.load4dVector(location_transform, x, y, z, w);
	}
	
	@Override
	protected void getAllUniformLocations() {
		location_transform = super.getUniformLocation("transform");
		location_brightness = super.getUniformLocation("brightness");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
	public void loadBrightness(float brightness) {
		super.loadFloat(location_brightness, brightness);
	}
	
}