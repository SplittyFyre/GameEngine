package engine.scene.particles;

import org.lwjgl.util.vector.Matrix4f;

import engine.renderEngine.ShaderProgram;

public class ParticleShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/engine/scene/particles/particleVertexShader.glsl";
	private static final String FRAGMENT_FILE = "src/engine/scene/particles/particleFragmentShader.glsl";

	private int location_numRows;
	private int location_projectionMatrix;

	public ParticleShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	public void getAllUniformLocations() {
		location_numRows = super.getUniformLocation("numRows");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "modelViewMatrix");
		super.bindAttribute(5, "texOffsets");
		super.bindAttribute(6, "blendFactor");
	}

	protected void loadNumRows(float numRows) {
		super.loadFloat(location_numRows, numRows);
	}

	protected void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(location_projectionMatrix, projectionMatrix);
	}

}
