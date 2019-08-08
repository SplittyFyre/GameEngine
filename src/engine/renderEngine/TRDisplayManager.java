package engine.renderEngine;

import java.nio.ByteBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Vector2f;

public class TRDisplayManager {
	
	private static String[] ICON_PATHS = {/*"sficon16", "sficon32",*/ "sficon"};
	
	private static int WIDTH;
	private static int HEIGHT;
	private static int FPS_CAP;
	
	private static long lastFrameTime;
	private static float delta;
	
	private static Runnable resizeCallBack;
	
	public static void createDisplay(int width, int height, int fpsCap, String title, Runnable resizeCallBack) {
		WIDTH = width;
		HEIGHT = height;
		FPS_CAP = fpsCap;
		TRDisplayManager.resizeCallBack = resizeCallBack;
		
		ContextAttribs attributes = new ContextAttribs(4, 3).withForwardCompatible(true).withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setInitialBackground(0.569f, 0.869f, 0.6969f);
			Display.setResizable(true);
			Display.create(new PixelFormat().withDepthBits(24), attributes);
			GL11.glEnable(GL13.GL_MULTISAMPLE);
			Display.setTitle("Tesseract Engine (Java) " + title);
			
			ByteBuffer[] icons = new ByteBuffer[ICON_PATHS.length];
			
			for (int i = 0; i < ICON_PATHS.length; i++) {
				icons[i] = ByteBuffer.allocateDirect(1);
				icons[i] = Loader.loadIcon(ICON_PATHS[i]);
			}

			//Display.setIcon(icons);
			
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
		lastFrameTime = getCurrentTime();
	}
	
	public static void setNativeCursor(org.lwjgl.input.Cursor cursor) {
		try {
			Mouse.setNativeCursor(cursor);
		} catch (LWJGLException e) {
			System.out.println("Failed to set native cursor");
			e.printStackTrace();
		}
	}
	
	public static void updateDisplay() {
		if (Display.wasResized()) {
			GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
			System.out.println(Display.getWidth() + " " + Display.getHeight());
			if (TRDisplayManager.resizeCallBack != null)
				TRDisplayManager.resizeCallBack.run();
		}
		Display.sync(FPS_CAP);
		Display.update();
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = currentFrameTime;	
	}
	
	public static float getFrameDeltaTime() {
		return delta;
	}
	
	public static void closeDisplay() {
		Display.destroy();
	}
	
	public static long getCurrentTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}
	
	public static Vector2f getNormalizedMouseCoords() {
		return new Vector2f(-1.0f + 2.0f * Mouse.getX() / Display.getWidth()
				, 1.0f - 2.0f * Mouse.getY() / Display.getHeight());
	}
	
	public static float getAspectRatio() {
		//System.out.println((float) Display.getWidth() / (float) Display.getHeight());
		return (float) Display.getWidth()/ (float) Display.getHeight();
		//return (float) Display.getWidth() / (float) Display.getHeight();
	}
	
}
