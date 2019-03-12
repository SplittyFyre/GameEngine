package engine.postProcessing.effects.contrast;

import engine.renderEngine.ShaderProgram;

public class ContrastShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/engine/postProcessing/effects/contrast/contrastVertex.txt";
	private static final String FRAGMENT_FILE = "src/engine/postProcessing/effects/contrast/contrastFragment.txt";
	
	public ContrastShader() {
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
