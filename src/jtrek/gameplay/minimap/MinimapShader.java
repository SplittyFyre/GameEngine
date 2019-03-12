package jtrek.gameplay.minimap;

import engine.renderEngine.ShaderProgram;

public class MinimapShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "src/jtrek/gameplay/minimap/minimapVertex.txt";
	private static final String FRAGMENT_FILE = "src/jtrek/gameplay/minimap/minimapFragment.txt";

	public MinimapShader() {
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
