package postProcessing;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import models.RawModel;
import postProcessing.effects.bloom.BrightFilter;
import postProcessing.effects.bloom.CombineFilter;
import postProcessing.effects.contrast.ContrastModification;
import postProcessing.effects.gaussianBlur.HorizontalBlur;
import postProcessing.effects.gaussianBlur.VerticalBlur;
import renderEngine.Loader;

public class PostProcessing {
	
	private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };	
	private static RawModel quad;
	private static ContrastModification contrastMod;
	private static HorizontalBlur hBlur;
	private static VerticalBlur vBlur;
	private static HorizontalBlur hBlur2;
	private static VerticalBlur vBlur2;
	private static BrightFilter brightFilter;
	private static CombineFilter combiner;

	public static void init(Loader loader) {
		quad = loader.loadToVAO(POSITIONS, 2);
		contrastMod = new ContrastModification();
		hBlur = new HorizontalBlur(Display.getWidth() / 5, Display.getHeight() / 5);
		vBlur = new VerticalBlur(Display.getWidth() / 5, Display.getHeight() / 5);
		hBlur2 = new HorizontalBlur(Display.getWidth() / 8, Display.getHeight() / 8);
		vBlur2 = new VerticalBlur(Display.getWidth() / 8, Display.getHeight() / 8);
		brightFilter = new BrightFilter(Display.getWidth() / 2, Display.getHeight() / 2);
		combiner = new CombineFilter();
	}
	
	public static void doPostProcessing(int colourTexture) {
		start();
		/*brightFilter.render(colourTexture);
		hBlur.render(brightFilter.getOutputTexture());
		vBlur.render(hBlur.getOutputTexture());
		combiner.render(colourTexture, vBlur.getOutputTexture());*/
		contrastMod.render(colourTexture);
		end();
	}
	
	public static void cleanUp() {
		contrastMod.cleanUp();
		hBlur.cleanUp();
		vBlur.cleanUp();
		hBlur2.cleanUp();
		vBlur2.cleanUp();
		brightFilter.cleanUp();
		combiner.cleanUp();
	}
	
	private static void start() {
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	private static void end() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}


}
