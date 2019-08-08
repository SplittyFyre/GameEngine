package engine.water.dudv;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import engine.renderEngine.ShaderProgram;
import engine.scene.contexts.SkyContext;
import engine.scene.entities.Light;
import engine.scene.entities.camera.TRCamera;
import engine.utils.TRMath;

public class DUDVWaterShader extends ShaderProgram {

	private final static String VERTEX_FILE = "src/engine/water/dudv/waterVertex.glsl";
	private final static String FRAGMENT_FILE = "src/engine/water/dudv/waterFragment.glsl";

	private int location_modelMatrix;
	private int location_viewMatrix;
	private int location_projectionMatrix;
	private int location_reflectionTexture;
	private int location_refractionTexture;
	private int location_dudvMap;
	private int location_movedFactor;
	private int location_cameraPosition;
	private int location_normalMap;
	private int location_lightPosition;
	private int location_lightColour;
	private int location_depthMap;

	public DUDVWaterShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_viewMatrix = getUniformLocation("viewMatrix");
		location_modelMatrix = getUniformLocation("modelMatrix");
		location_reflectionTexture = getUniformLocation("reflectionTexture");
		location_refractionTexture = getUniformLocation("refractionTexture");
		location_dudvMap = getUniformLocation("dudvMap");
		location_movedFactor = getUniformLocation("movedFactor");
		location_cameraPosition = getUniformLocation("cameraPosition");
		location_normalMap = getUniformLocation("normalMap");
		location_lightPosition = getUniformLocation("lightPosition");
		location_lightColour = getUniformLocation("lightColour");
		location_depthMap = getUniformLocation("depthMap");
		addUniformVariable("colourOffset");
		
		addUniformVariable("waveIntensity");
		addUniformVariable("shineDamper");
		addUniformVariable("reflectivity");
		
		addUniformVariable("nearPlane");
		addUniformVariable("farPlane");
		
		addUniformVariable("tiling");
		
		addUniformVariable("skyColour");
		addUniformVariable("density");
		addUniformVariable("gradient");
	}
	
	public void loadSkyContext(SkyContext ctx) {
		super.loadFloat(uniformLocationOf("density"), ctx.fogDensity);
		super.loadFloat(uniformLocationOf("gradient"), ctx.fogGradient);
		super.loadVector(uniformLocationOf("skyColour"), ctx.skyR, ctx.skyG, ctx.skyB);
	}
	
	public void loadTiling(float tiling) {
		super.loadFloat(uniformLocationOf("tiling"), tiling);
	}
	
	public void loadWaveIntensity(float wi) {
		super.loadFloat(uniformLocationOf("waveIntensity"), wi);
	}
	
	public void loadShineVariables(DUDVWaterTile tile) {
		super.loadFloat(uniformLocationOf("shineDamper"), tile.getShineDamper());
		super.loadFloat(uniformLocationOf("reflectivity"), tile.getReflectivity());
	}
	
	public void loadFrustumPlanes(float near, float far) {
		super.loadFloat(getUniformLocation("nearPlane"), near);
		super.loadFloat(getUniformLocation("farPlane"), far);
	}
	
	public void loadColourOffset(Vector3f colourOffset) {
		super.loadVector(uniformLocationOf("colourOffset"), colourOffset);
	}
	
	public void connectTextureUnits(){
		super.loadInt(location_reflectionTexture, 0);
		super.loadInt(location_refractionTexture, 1);
		super.loadInt(location_dudvMap, 2);
		super.loadInt(location_normalMap, 3);
		super.loadInt(location_depthMap, 4);
	}
	
	public void loadLight(Light light) {
		super.loadVector(location_lightColour, light.getColour());
		super.loadVector(location_lightPosition, light.getPosition());
	}
	
	public void loadMovedFactor(float factor) {
		super.loadFloat(location_movedFactor, factor);
	}

	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix(location_projectionMatrix, projection);
	}
	
	public void loadViewMatrix(TRCamera camera){
		Matrix4f viewMatrix = camera.getViewMatrix();
		loadMatrix(location_viewMatrix, viewMatrix);
		super.loadVector(location_cameraPosition, camera.getPosition());
	}

	public void loadModelMatrix(Matrix4f modelMatrix){
		loadMatrix(location_modelMatrix, modelMatrix);
	}

}
