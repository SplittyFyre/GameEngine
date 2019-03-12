package engine.postProcessing.effects.bloom;

import engine.renderEngine.ShaderProgram;

public class BrightFilterShader extends ShaderProgram{
	
	private static final String VERTEX_FILE = "src/engine/postProcessing/effects/bloom/simpleVertex.txt";
	private static final String FRAGMENT_FILE = "src/engine/postProcessing/effects/bloom/brightFilterFragment.txt";
	
	public BrightFilterShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {	
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
