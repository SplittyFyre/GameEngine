package engine.scene.entities.render;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import engine.renderEngine.ShaderProgram;
import engine.scene.TRScene;
import engine.scene.contexts.SkyContext;
import engine.scene.entities.Light;
import engine.scene.entities.camera.TRCamera;
import engine.utils.TRMath;

public class EntityShader extends ShaderProgram{
		
	private static final String VERTEX_FILE = "src/engine/scene/entities/render/entityVertexShader.glsl";
	private static final String FRAGMENT_FILE = "src/engine/scene/entities/render/entityFragmentShader.glsl";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	
	private int location_lightPosition[];
	private int location_lightColour[];
	private int location_attenuation[];
	
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_useFakeLight;
	private int location_skyColour;
	private int location_numberOfRows;
	private int location_offset;
	private int location_plane;
	private int location_specularMap;
	private int location_usesSpecularMap;
	private int location_modelTexture;
	private int location_brightDamper;
	private int location_highlight;


	public EntityShader() {
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
		location_specularMap = super.getUniformLocation("specularMap");
		location_usesSpecularMap = super.getUniformLocation("usesSpecularMap");
		location_modelTexture = super.getUniformLocation("modelTexture");
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		
		super.addUniformVariable("useFakeLight");
		
		location_skyColour = super.getUniformLocation("skyColour");
		location_numberOfRows = super.getUniformLocation("numberOfRows");
		location_offset = super.getUniformLocation("offset");
		location_plane = super.getUniformLocation("plane");
		location_brightDamper = super.getUniformLocation("brightDamper");
		location_highlight = super.getUniformLocation("highlight");
		
		addUniformVariable("density");
		addUniformVariable("gradient");
		
		addUniformVariable("celllvl");
		addUniformVariable("useCellShading");
		
		addUniformVariable("ambientLightLvl");
		addUniformVariable("lightsInUse");
		
		location_lightColour = new int[TRScene.MAX_LIGHTS];
		location_lightPosition = new int[TRScene.MAX_LIGHTS];
		location_attenuation = new int[TRScene.MAX_LIGHTS];
		
		for (int i = 0; i < TRScene.MAX_LIGHTS; i++) {
			
			location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
			location_lightColour[i] = super.getUniformLocation("lightColour[" + i + "]");
			location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
			
		}
		
	}
	
	public void loadLightsInUse(int lightsInUse) {
		super.loadInt(uniformLocationOf("lightsInUse"), lightsInUse);
	}
	
	public void loadAmbientLight(float lvl) {
		super.loadFloat(uniformLocationOf("ambientLightLvl"), lvl);
	}
	
	public void connectTextureUnits() {
		super.loadInt(location_modelTexture, 0);
		super.loadInt(location_specularMap, 1);
	}
	
	public void loadUseSpecularMap(boolean flag) {
		super.loadBoolean(location_usesSpecularMap, flag);
	}
	
	public void loadClipPlane(Vector4f plane) {
		super.load4dVector(location_plane, plane);
	}
	
	public void loadNumberOfRows(int num) {
		super.loadFloat(location_numberOfRows, num);
	}
	
	public void loadBrightDamper(float f) {
		super.loadFloat(location_brightDamper, f);
	}
	
	public void loadOffset(float x, float y) {
		super.load2dVector(location_offset, new Vector2f(x, y));
	}
	
	public void loadShineVariables(float damper, float reflectivity) {
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
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
	
	public void loadFakeLight(boolean fakeLight) {
		//long start = System.nanoTime();
		super.loadBoolean(uniformLocationOf("useFakeLight"), fakeLight);
		//long end = System.nanoTime();
		//System.out.println("time for loadBoolean: " + (end - start));
	}
	
	public void loadSkyContext(SkyContext ctx) {
		super.loadFloat(uniformLocationOf("density"), ctx.fogDensity);
		super.loadFloat(uniformLocationOf("gradient"), ctx.fogGradient);
		super.loadVector(location_skyColour, new Vector3f(ctx.skyR, ctx.skyG, ctx.skyB));
	}
	
	public void loadCellShadingStatus(boolean useCellShading, float cellLevel) {
		super.loadBoolean(uniformLocationOf("useCellShading"), useCellShading);
		if (useCellShading) {
			super.loadFloat(uniformLocationOf("celllvl"), cellLevel);
		}
	}

}
