package engine.postProcessing.effects.gaussianBlur;

import engine.renderEngine.ShaderProgram;

public class VerticalBlurShader extends ShaderProgram{

	private static final String VERTEX_FILE = "src/engine/postProcessing/effects/gaussianBlur/verticalBlurVertex.txt";
	private static final String FRAGMENT_FILE = "src/engine/postProcessing/effects/gaussianBlur/blurFragment.txt";
	
	private int location_targetHeight;
	
	protected VerticalBlurShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	protected void loadTargetHeight(float height) {
		super.loadFloat(location_targetHeight, height);
	}

	@Override
	protected void getAllUniformLocations() {	
		location_targetHeight = super.getUniformLocation("targetHeight");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
}
