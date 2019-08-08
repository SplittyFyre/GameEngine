package engine.scene.terrain.render;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import engine.renderEngine.ShaderProgram;
import engine.scene.TRScene;
import engine.scene.contexts.SkyContext;
import engine.scene.entities.Light;
import engine.scene.entities.camera.TRCamera;
import engine.utils.TRMath;

public class TerrainShader extends ShaderProgram {
		
	private static final String VERTEX_FILE = "src/engine/scene/terrain/render/terrainVertexShader.glsl";
	private static final String FRAGMENT_FILE = "src/engine/scene/terrain/render/terrainFragmentShader.glsl";
	
	private int location_lightPosition[];
	private int location_lightColour[];
	private int location_attenuation[];
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_skyColour;
	private int location_backgroundTexture;
	private int location_rTexture;
	private int location_gTexture;
	private int location_bTexture;
	private int location_blendMap;
	private int location_plane;
	private int location_base;
	private int location_height;

	public TerrainShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoordinates");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_skyColour = super.getUniformLocation("skyColour");
		location_backgroundTexture = super.getUniformLocation("backgroundTexture");
		location_rTexture = super.getUniformLocation("rTexture");
		location_gTexture = super.getUniformLocation("gTexture");
		location_bTexture = super.getUniformLocation("bTexture");
		location_blendMap = super.getUniformLocation("blendMap");
		location_plane = super.getUniformLocation("plane");
		location_base = super.getUniformLocation("base");
		location_height = super.getUniformLocation("height");
		
		location_lightColour = new int[TRScene.MAX_LIGHTS];
		location_lightPosition = new int[TRScene.MAX_LIGHTS];
		location_attenuation = new int[TRScene.MAX_LIGHTS];
		
		addUniformVariable("density");
		addUniformVariable("gradient");
		
		addUniformVariable("tiling");
		addUniformVariable("ambientLightLvl");
		
		addUniformVariable("useAltitudeVarying");
		addUniformVariable("vecCaps");
		addUniformVariable("maxheight");
		
		addUniformVariable("lightsInUse");
		
		addUniformVariable("yOffset");
		
		for (int i = 0; i < TRScene.MAX_LIGHTS; i++) {
			location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
			location_lightColour[i] = super.getUniformLocation("lightColour[" + i + "]");
			location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
		}
		
	}
	
	public void loadYOffset(float y) {
		super.loadFloat(uniformLocationOf("yOffset"), y);
	}
	
	public void loadLightsInUse(int lightsInUse) {
		super.loadInt(uniformLocationOf("lightsInUse"), lightsInUse);
	}
	
	public void connectTextureUnits() {
		super.loadInt(location_backgroundTexture, 0);
		super.loadInt(location_rTexture, 1);
		super.loadInt(location_gTexture, 2);
		super.loadInt(location_bTexture, 3);
		super.loadInt(location_blendMap, 4);	
	}
	
	public void loadUseAltitudeVarying(boolean b) {
		super.loadBoolean(uniformLocationOf("useAltitudeVarying"), b);
	}
	
	public void loadHeightTextureCaps(float c1, float c2, float c3) {
		super.loadVector(uniformLocationOf("vecCaps"), c1, c2, c3);
	}
	
	public void loadMaxHeight(float h) {
		super.loadFloat(uniformLocationOf("maxheight"), h);
	}
	
	public void loadAmbientLight(float lvl) {
		super.loadFloat(uniformLocationOf("ambientLightLvl"), lvl);
	}
	
	public void loadTiling(float in) {
		super.loadFloat(uniformLocationOf("tiling"), in);
	}
	
	public void loadHeight(float height) {
		super.loadFloat(location_height, height);
	}
	
	public void loadShineVariables(float damper,float reflectivity){
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadBase(boolean base) {
		super.loadBoolean(location_base, base);
	}
	
	public void loadLights(List<Light> lights){
		
		for (int i = 0; i < lights.size(); i++) {
			super.loadVector(location_lightPosition[i], lights.get(i).getPosition());
			super.loadVector(location_lightColour[i], lights.get(i).getColour());
			super.loadVector(location_attenuation[i], lights.get(i).getAttenuation());	
		}
		
	}
	
	public void loadViewMatrix(TRCamera camera) {
		super.loadMatrix(location_viewMatrix, camera.getViewMatrix());
	}
	
	public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
	public void loadSkyContext(SkyContext ctx) {
		super.loadFloat(uniformLocationOf("density"), ctx.fogDensity);
		super.loadFloat(uniformLocationOf("gradient"), ctx.fogGradient);
		super.loadVector(location_skyColour, ctx.skyR, ctx.skyG, ctx.skyB);
	}
	
	public void loadClipPlane(Vector4f plane) {
		super.load4dVector(location_plane, plane);
	}

}
