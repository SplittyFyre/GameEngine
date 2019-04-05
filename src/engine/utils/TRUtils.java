package engine.utils;

import java.util.Random;

import engine.fontMeshCreator.FontType;
import engine.renderEngine.Loader;

public class TRUtils {
	
	public static Random rng = new Random();
	public static FontType trFont = new FontType(Loader.loadTexture("segoeUI"), "segoeUI");


}
