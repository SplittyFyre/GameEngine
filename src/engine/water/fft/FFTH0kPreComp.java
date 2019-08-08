package engine.water.fft;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL42;
import org.lwjgl.util.vector.Vector2f;

import engine.renderEngine.Loader;
import engine.water.fft.shaders.H0kPreCompShader;

public class FFTH0kPreComp {
	
	private int h0kTexture, h0minuskTexture;
	
	public int getH0kTexture() {
		return this.h0kTexture;
	}
	
	public int getH0_minus_kTexture() {
		return this.h0minuskTexture;
	}
	
	public FFTH0kPreComp(int horizontalDim, float amplitude, Vector2f windVector, float windSpeed) {
		this.h0kTexture = Loader.genEmptyTexture2D(FFTEngine.N, FFTEngine.N, FFTEngine.GLImFormat);
		this.h0minuskTexture = Loader.genEmptyTexture2D(FFTEngine.N, FFTEngine.N, FFTEngine.GLImFormat);
		                                         
		int noise0 = Loader.loadTexture("fftnoiseTWO/FFTNoise256_1");
		int noise1 = Loader.loadTexture("fftnoiseTWO/FFTNoise256_2");
		int noise2 = Loader.loadTexture("fftnoiseTWO/FFTNoise256_3");
		int noise3 = Loader.loadTexture("fftnoiseTWO/FFTNoise256_4");
		
		H0kPreCompShader shader = new H0kPreCompShader();
		
		
		shader.start();
		
		GL42.glBindImageTexture(0, h0kTexture, 0, false, 0, GL15.GL_WRITE_ONLY, GL30.GL_RGBA32F);
		GL42.glBindImageTexture(1, h0minuskTexture, 0, false, 0, GL15.GL_WRITE_ONLY, GL30.GL_RGBA32F);
		
		shader.loadNoise(noise0, noise1, noise2, noise3);
		shader.loadWaterFactors(FFTEngine.N, horizontalDim, amplitude, windVector, windSpeed);
		
		shader.dispatchCompute(FFTEngine.N / 16, FFTEngine.N / 16, 1);
		shader.stop();
		
	}

}
