package engine.water.fft;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL30;

public class FFTEngine {
	
	public int tex;
	
	public static final int N = 256;
	public static final int GLImFormat = GL30.GL_RGBA32F;
	
	public FFTEngine(int tex) {
		
		this.tex = tex;
		
		
		
	}
	
	public static void generateRandomNoiseTex(String outfile) {
		
		Random rand = new Random();
		
		BufferedImage img = new BufferedImage(N, N, BufferedImage.TYPE_INT_ARGB);
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				int rn = rand.nextInt(256);
				img.setRGB(i, j, new Color(rn, rn, rn).getRGB());
			}
		}
		
		try {
			ImageIO.write(img, "PNG", new File(outfile + ".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
