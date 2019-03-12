package engine.scene.skybox;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import engine.renderEngine.Loader;
import engine.renderEngine.models.RawModel;
import engine.scene.contexts.SkyContext;
import engine.scene.entities.camera.Camera;

public class SkyboxRenderSystem {
	
	private static final float SIZE = 100000f;
	
	private static final float[] VERTICES = {        
		    -SIZE,  SIZE, -SIZE,
		    -SIZE, -SIZE, -SIZE,
		    SIZE, -SIZE, -SIZE,
		     SIZE, -SIZE, -SIZE,
		     SIZE,  SIZE, -SIZE,
		    -SIZE,  SIZE, -SIZE,

		    -SIZE, -SIZE,  SIZE,
		    -SIZE, -SIZE, -SIZE,
		    -SIZE,  SIZE, -SIZE,
		    -SIZE,  SIZE, -SIZE,
		    -SIZE,  SIZE,  SIZE,
		    -SIZE, -SIZE,  SIZE,

		     SIZE, -SIZE, -SIZE,
		     SIZE, -SIZE,  SIZE,
		     SIZE,  SIZE,  SIZE,
		     SIZE,  SIZE,  SIZE,
		     SIZE,  SIZE, -SIZE,
		     SIZE, -SIZE, -SIZE,

		    -SIZE, -SIZE,  SIZE,
		    -SIZE,  SIZE,  SIZE,
		     SIZE,  SIZE,  SIZE,
		     SIZE,  SIZE,  SIZE,
		     SIZE, -SIZE,  SIZE,
		    -SIZE, -SIZE,  SIZE,

		    -SIZE,  SIZE, -SIZE,
		     SIZE,  SIZE, -SIZE,
		     SIZE,  SIZE,  SIZE,
		     SIZE,  SIZE,  SIZE,
		    -SIZE,  SIZE,  SIZE,
		    -SIZE,  SIZE, -SIZE,

		    -SIZE, -SIZE, -SIZE,
		    -SIZE, -SIZE,  SIZE,
		     SIZE, -SIZE, -SIZE,
		     SIZE, -SIZE, -SIZE,
		    -SIZE, -SIZE,  SIZE,
		     SIZE, -SIZE,  SIZE
		};
	
	//private String[] NIGHT_TEXTURE_FILES = {"right", "left", "top", "bottom", "back", "front"};
	//private String[] NIGHT_TEXTURE_FILES = {"nightRight", "nightLeft", "nightTop", "nightBottom", "nightBack", "nightFront"};
	String name = "high";
	private String[] NIGHT_TEXTURE_FILES = {name + "RT", name + "LF", name + "DN", name + "UP", name + "BK", name + "FT"};
	
	private RawModel cube;
	private int texture;
	private int nightTexture;
	private SkyboxShader shader;
		
	public SkyboxRenderSystem(Matrix4f projMatrix) {
		cube = Loader.loadToVAO(VERTICES, 3);
		//texture = Loader.loadCubeMap(TEXTURE_FILES);
		nightTexture = Loader.loadCubeMap(NIGHT_TEXTURE_FILES);
		shader = new SkyboxShader();
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projMatrix);
		shader.stop();
	}
	
	public void render(Camera camera, SkyContext ctx) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_SRC_ALPHA);
		GL11.glDepthMask(false);
		shader.start();
		shader.loadViewMatrix(camera);
		shader.loadFogColour(ctx.skyR, ctx.skyG, ctx.skyB);
		GL30.glBindVertexArray(cube.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		bindTextures();
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	// GL_TEXTURE(%d) are consecutive
	private void bindTextures() {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, nightTexture);
		shader.loadBlendFactor(0);
	}
	
	public SkyboxShader getShader() {
		return shader;
	}
	
}
