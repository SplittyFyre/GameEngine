package renderEngine;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLContext;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import collision.BoundingBox;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import renderEngine.models.RawModel;
import renderEngine.textures.TextureData;

public class Loader {
	
	private static List<Integer> vaos = new ArrayList<Integer>();
	private static List<Integer> vbos = new ArrayList<Integer>();
	private static List<Integer> textures = new ArrayList<Integer>();
	private static float anisoAmout = Math.min(4, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
	
	public static RawModel loadToVAO(float[] positions,float[] textureCoords,float[] normals,int[] indices){
		
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0,3,positions);
		storeDataInAttributeList(1,2,textureCoords);
		storeDataInAttributeList(2,3,normals);
		unbindVAO();
		
		return new RawModel(vaoID,indices.length, null);
	}
	
	public static RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices, BoundingBox aabb){
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		unbindVAO();
		return new RawModel(vaoID, indices.length, aabb);
	}
	
	public static int loadVAOID(float[] positions, float[] textureCoords){
		int vaoID = createVAO();
		storeDataInAttributeList(0, 2, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		unbindVAO();
		return vaoID;
	}
	
	
	public static RawModel loadToVAO(float[] positions) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, 2, positions);
		unbindVAO();
		return new RawModel(vaoID, positions.length / 2, null);
	}
	
	private static int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	/**When the RawModel does not require a bounding box (say, a terrain piece or even water)
	 * then null can be used instead of a vector3f, just don't access it!**/
	
	public static RawModel loadToVAO(float[] positions, int dimensions){
		int vaoID = createVAO();
		storeDataInAttributeList(0, dimensions, positions);
		unbindVAO();

		return new RawModel(vaoID, positions.length / dimensions, null);
		
	}
	
	private static void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data){
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private static void unbindVAO(){
		GL30.glBindVertexArray(0);
	}
	
	public static int loadTexture(String fileName) {
		
		Texture texture = null;
		
		try {
			texture = TextureLoader.getTexture("PNG", Class.class.getResourceAsStream("/res/" + fileName + ".png"));
			
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_MAX_TEXTURE_LOD_BIAS, -0.4f);
			if (GLContext.getCapabilities().GL_EXT_texture_filter_anisotropic) {
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, anisoAmout);
			}
			else {
				System.out.println("Your driver does not support Anisotropic Filtering");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Tried to load texture " + fileName + ".png, error occured");
			System.exit(-1);
		}
		textures.add(texture.getTextureID());
		
		return texture.getTextureID();
	}
	
	public static void cleanUp(){
		
		for (int vao : vaos){
			GL30.glDeleteVertexArrays(vao);
		}
		for (int vbo : vbos){
			GL15.glDeleteBuffers(vbo);
		}
		for (int texture : textures){
			GL11.glDeleteTextures(texture);
		}
		
	}
	
	public static int loadCubeMap(String[] textureFiles) {
		
		int texID = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);
		
		for (int i = 0; i < textureFiles.length; i++) {
			TextureData data = decodeTextureFile(textureFiles[i]);
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
		}
		
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		textures.add(texID);
		
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		
		return texID;
		
	}
	
	private static TextureData decodeTextureFile(String fileName) {
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		
		try {
			@SuppressWarnings("resource")
			InputStream in = Class.class.getResourceAsStream("/res/" + fileName + ".png");
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buffer, width * 4, Format.RGBA);
			buffer.flip();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Tried to load texture " + fileName + ", didn't work");
			System.exit(-1);
		}
		return new TextureData(buffer, width, height);
	}
	
	private static void bindIndicesBuffer(int[] indices){
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	private static IntBuffer storeDataInIntBuffer(int[] data){
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	private static FloatBuffer storeDataInFloatBuffer(float[] data){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	public static ByteBuffer loadIcon(String path) {

		ByteBuffer buf = null;
		
		try {
			InputStream is = Class.class.getResourceAsStream("/res/" + path + ".png");
			PNGDecoder dec = new PNGDecoder(is);
			buf = ByteBuffer.allocateDirect(dec.getWidth() * dec.getHeight() * 4);
			dec.decode(buf, dec.getWidth() * 4, PNGDecoder.Format.RGBA);
			buf.flip();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return buf;
		
	}
	
	public static Cursor loadCursor(String pathmsrc) {
		
		BufferedImage img = null;
		try {
			img = ImageIO.read(Class.class.getResourceAsStream("/res/" + pathmsrc + ".png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		final int w = img.getWidth();
	    final int h = img.getHeight();

	    int rgbData[] = new int[w * h];

	    for (int i = 0; i < rgbData.length; i++) {
	        int x = i % w;
	        int y = h - 1 - i / w; // this will also flip the image vertically

	        rgbData[i] = img.getRGB(x, y);
	    }

	    IntBuffer buffer = BufferUtils.createIntBuffer(w * h);
	    buffer.put(rgbData);
	    buffer.rewind();

	    Cursor cursor = null;
		try {
			cursor = new Cursor(w, h, 2, h - 2, 1, buffer, null);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

	    return cursor;
		
	}
	
}
