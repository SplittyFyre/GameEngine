package engine.postProcessing;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import engine.postProcessing.effects.bloom.BrightFilter;
import engine.postProcessing.effects.bloom.CombineFilter;
import engine.postProcessing.effects.contrast.ContrastModification;
import engine.postProcessing.effects.gaussianBlur.HorizontalBlur;
import engine.postProcessing.effects.gaussianBlur.VerticalBlur;
import engine.renderEngine.Loader;
import engine.renderEngine.models.RawModel;

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

	public static void init() {
		System.out.println("post re init");
		quad = Loader.loadToVAO(POSITIONS, 2);
		contrastMod = new ContrastModification();
		hBlur = new HorizontalBlur(Display.getWidth() / 2, Display.getHeight() / 2);
		vBlur = new VerticalBlur(Display.getWidth() / 2, Display.getHeight() / 2);
		hBlur2 = new HorizontalBlur(Display.getWidth() / 8, Display.getHeight() / 8);
		vBlur2 = new VerticalBlur(Display.getWidth() / 8, Display.getHeight() / 8);
 		brightFilter = new BrightFilter(Display.getWidth() / 2, Display.getHeight() / 2);
		combiner = new CombineFilter(Display.getWidth(), Display.getHeight());
	}
	
	public static void doPostProcessing(int colourTexture, int brightTexture) {
		start();
		hBlur.render(brightTexture);
		vBlur.render(hBlur.getOutputTexture());
		hBlur2.render(vBlur.getOutputTexture());
		vBlur2.render(hBlur2.getOutputTexture());
		combiner.render(colourTexture, vBlur2.getOutputTexture());
		contrastMod.render(combiner.getOutputTexture());
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
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
	}
	
	private static void end() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
	
	public static CombineFilter getCombineFilter() {
		return combiner;
	}

}
