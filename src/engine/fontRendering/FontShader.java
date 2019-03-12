package engine.fontRendering;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import engine.renderEngine.ShaderProgram;

public class FontShader extends ShaderProgram{

	private static final String VERTEX_FILE = "src/engine/fontRendering/fontVertex.glsl";
	private static final String FRAGMENT_FILE = "src/engine/fontRendering/fontFragment.glsl";
	
	private int location_colour;
	private int location_translation;
	
	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_colour = super.getUniformLocation("colour");
		location_translation = super.getUniformLocation("translation");	
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}
	
	public void loadColour(Vector4f colour) {
		super.load4dVector(location_colour, colour);
	}
	
	public void loadTranslation(Vector2f translation) {
		super.load2dVector(location_translation, translation);
	}

}
