package engine.water.dudv;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import engine.renderEngine.DisplayManager;
import engine.renderEngine.Loader;
import engine.renderEngine.models.RawModel;
import engine.scene.entities.Light;
import engine.scene.entities.camera.Camera;
import engine.utils.SFMath;

public class DUDVWaterRenderer {
	
	private static final String DUDV_MAP = "waterDUDV";
	//private static final String DUDV_MAP = "dudv";
	private static final String NORMAL_MAP = "normal";
	private static final float WAVE_SPEED = 0.03f;

	private RawModel quad;
	private DUDVWaterShader shader;
	private WaterFrameBuffers fbos;
	
	public DUDVWaterShader getShader() {
		return shader;
	}
	
	public WaterFrameBuffers getFBOs() {
		return fbos;
	}
	
	private float moveFactor = 0;
	
	private int dudvTexture;
	private int normalMap;

	public DUDVWaterRenderer(Matrix4f projectionMatrix) {
		this.shader = new DUDVWaterShader();
		this.fbos = new WaterFrameBuffers();
		dudvTexture = Loader.loadTexture(DUDV_MAP);
		normalMap = Loader.loadTexture(NORMAL_MAP);
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		setUpVAO();
	}

	public void render(List<DUDVWaterTile> water, Camera camera, Light sun) {
		prepareRender(camera, sun);	
		for (DUDVWaterTile tile : water) {
			tile.update();
			Matrix4f modelMatrix = SFMath.createTransformationMatrix(
					new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), 0, 0, 0,
					tile.size);
			shader.loadModelMatrix(modelMatrix);
			shader.loadColourOffset(tile.getColourOffset());
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
		}
		unbind();
	}
	
	private void prepareRender(Camera camera, Light sun){
		shader.start();
		shader.loadViewMatrix(camera);
		moveFactor += WAVE_SPEED * DisplayManager.getFrameTime();
		moveFactor %= 1;
		shader.loadMoveFactor(moveFactor);
		shader.loadLight(sun);
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getReflectionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, dudvTexture);
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, normalMap);
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionDepthTexture());
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	private void unbind(){
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		GL11.glDisable(GL11.GL_BLEND);
		shader.stop();
	}

	private void setUpVAO() {
		// Just x and z vertex positions here, y is set in v.shader
		float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
		quad = Loader.loadToVAO(vertices, 2);
	}
	
	public void cleanUp() {
		shader.cleanUp();
		fbos.cleanUp();
	}

}
