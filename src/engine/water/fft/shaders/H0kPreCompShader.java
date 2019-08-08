package engine.water.fft.shaders;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Vector2f;

import engine.renderEngine.ShaderProgram;

public class H0kPreCompShader extends ShaderProgram {

	private static final String SHADER_LOCATION = "src/engine/water/fft/shaders/h0kprecompute.glsl";
	
	public H0kPreCompShader() {
		super(SHADER_LOCATION);
	}

	@Override
	protected void getAllUniformLocations() {
		addUniformVariable("noise_re0");
		addUniformVariable("noise_re1");
		addUniformVariable("noise_im0");
		addUniformVariable("noise_im1");
		
		addUniformVariable("N");
		addUniformVariable("horizDim");
		addUniformVariable("amplitude");
		addUniformVariable("windVec");
		addUniformVariable("windSpeed");
	}
	
	public void loadNoise(int n0, int n1, int n2, int n3) {
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, n0);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, n1);
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, n2);
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, n3);
		
		super.loadInt(uniformLocationOf("noise_re0"), 0);
		super.loadInt(uniformLocationOf("noise_re1"), 1);
		super.loadInt(uniformLocationOf("noise_im0"), 2);
		super.loadInt(uniformLocationOf("noise_im1"), 3);
	}
	
	public void loadWaterFactors(int N, int horizDim, float amplitude, Vector2f windVec, float windSpeed) {
		super.loadInt(uniformLocationOf("N"), N);
		super.loadInt(uniformLocationOf("horizDim"), horizDim);
		super.loadFloat(uniformLocationOf("amplitude"), amplitude);
		super.load2dVector(uniformLocationOf("windVec"), windVec);
		super.loadFloat(uniformLocationOf("windSpeed"), windSpeed);
	}
	

	@Override
	protected void bindAttributes() {
		
	}

}
