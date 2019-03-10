package renderEngine.guis.render;

import org.lwjgl.util.vector.Vector4f;

import renderEngine.ShaderProgram;

public class FlareModeShader extends ShaderProgram{
	
	private static final String VERTEX_FILE = "src/renderEngine/guis/render/flareVertexShader.glsl";
	private static final String FRAGMENT_FILE = "src/renderEngine/guis/render/flareFragmentShader.glsl";
	
	private int location_transform;
	private int location_brightness;

	public FlareModeShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public void loadTransformation(Vector4f vec){
		super.load4dVector(location_transform, vec);
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