package engine.scene.skybox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import engine.renderEngine.ShaderProgram;
import engine.scene.entities.camera.TRCamera;
import engine.utils.TRMath;

public class SkyboxShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/engine/scene/skybox/skyboxVertexShader.glsl";
	private static final String FRAGMENT_FILE = "src/engine/scene/skybox/skyboxFragmentShader.glsl";
		
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_fogColour;
	private int location_cubeMap;
	private int location_cubeMap2;
	private int location_blendFactor;
		
	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(location_projectionMatrix, matrix);
	}

	public void loadViewMatrix(TRCamera camera, float rotation) {
		Matrix4f matrix = new Matrix4f(camera.getViewMatrix());
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;
		Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(1, 0, 0), matrix, matrix);
		super.loadMatrix(location_viewMatrix, matrix);
	}
	
	public void loadFogColour(float r, float g, float b) {
		super.loadVector(location_fogColour, r, g, b);
	}
	
	public void connectTextureUnits() {
		super.loadInt(location_cubeMap, 0);
		super.loadInt(location_cubeMap2, 1);
	}
	
	public void loadBlendFactor(float factor) {
		super.loadFloat(location_blendFactor, factor);
	}
	
	public void loadHasOnlyOneTexture(boolean b) {
		super.loadBoolean(uniformLocationOf("hasOnlyOneTexture"), b);
	}
	
	public void loadFadeLimits(float lower, float upper) {
		super.loadFloat(uniformLocationOf("lowerFadeLimit"), lower);
		super.loadFloat(uniformLocationOf("upperFadeLimit"), upper);
	}
	
	public void loadBloomEffect(float f) {
		super.loadFloat(uniformLocationOf("bloomEffect"), f);
	}
	
	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_fogColour = super.getUniformLocation("fogColour");
		location_cubeMap = super.getUniformLocation("cubeMap");
	    location_cubeMap2 = super.getUniformLocation("cubeMap2");
        location_blendFactor = super.getUniformLocation("blendFactor");
        addUniformVariable("hasOnlyOneTexture");
        addUniformVariable("lowerFadeLimit");
        addUniformVariable("upperFadeLimit");
        addUniformVariable("bloomEffect");
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
