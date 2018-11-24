package renderEngine;

import java.nio.ByteBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Vector2f;

public class DisplayManager {
	
	private static String[] ICON_PATHS = {"sficon16", "sficon32", "sficon"};
	
	private static final int WIDTH = 3200;
	private static final int HEIGHT = 1800;
	private static final int FPS_CAP = 120;
	
	private static long lastFrameTime;
	private static float delta;
	
	public static Cursor cursor;
	public static Cursor target;
	
	public static final int NORMAL = 0;
	public static final int TARGET = 1;
	
	public static int currentCursor = NORMAL; 
	
	public static void createDisplay() {	
		
		ContextAttribs attributes = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setInitialBackground(0.569f, 0.869f, 0.6969f);
			//Display.setInitialBackground(0, 0, 0);
			Display.setResizable(true);
			Display.create(new PixelFormat().withDepthBits(24), attributes);
			GL11.glEnable(GL13.GL_MULTISAMPLE);
			Display.setTitle("This game is Work In Progress -Oscar");
			
			ByteBuffer[] icons = new ByteBuffer[ICON_PATHS.length];
			
			for (int i = 0; i < ICON_PATHS.length; i++) {
				icons[i] = ByteBuffer.allocateDirect(1);
				icons[i] = Loader.loadIcon(ICON_PATHS[i]);
			}
			
			Display.setIcon(icons);

			cursor = Loader.loadCursor("cursor");
			target = Loader.loadCursor("bullseye");
			
			Mouse.setNativeCursor(cursor);
			
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
		lastFrameTime = getCurrentTime();
	}
	
	public static void updateDisplay() {
		if (Display.wasResized()) {
			System.out.println("Resized");
			GL11.glViewport(0, 0, WIDTH, HEIGHT);
		}
		Display.sync(FPS_CAP);
		Display.update();
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = currentFrameTime;
		//System.out.println("FPS: " + (delta));
		
		/*if (delta > 0.02f && getCurrentTime() > 5) {
			System.err.println("Warning: Low Framerate");
		}*/
		
	}
	
	public static float getFrameTime() {
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
		return (float) Display.getHeight() / (float) Display.getWidth();
		//return (float) Display.getWidth() / (float) Display.getHeight();
	}
	
}
