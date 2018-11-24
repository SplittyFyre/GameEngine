package fontRendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontMeshCreator.TextMeshData;
import renderEngine.Loader;

public class TextMaster {
	
	private static Map<FontType, List<GUIText>> texts = new HashMap<FontType, List<GUIText>>();
	private static Map<FontType, List<GUIText>> texts2 = new HashMap<FontType, List<GUIText>>();
	private static FontRenderer renderer;
	
	public static void init() {
		renderer = new FontRenderer();
	}
	
	public static void drawText() {
		renderer.render(texts);
	}
	
	public static void drawSecondaryText() {
		renderer.render(texts2);
	}
	
	public static void addText(GUIText text) {
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		int vao = Loader.loadVAOID(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vao, data.getVertexCount());
		List<GUIText> textBatch = texts.get(font);
		if (textBatch == null) {
			textBatch = new ArrayList<GUIText>();
			texts.put(font, textBatch);
		}
		textBatch.add(text);
	}
	
	public static void addTextToSecondaryBuffer(GUIText text) {
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		int vao = Loader.loadVAOID(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vao, data.getVertexCount());
		List<GUIText> textBatch = texts2.get(font);
		if (textBatch == null) {
			textBatch = new ArrayList<GUIText>();
			texts2.put(font, textBatch);
		}
		textBatch.add(text);
	}
	
	public static void removeText(GUIText text) {
		List<GUIText> textBatch = texts.get(text.getFont());
		textBatch.remove(text);
		if (textBatch.isEmpty()) {
			texts.remove(text.getFont());
		}
	}
	
	public static void removeTextFromSecondaryBuffer(GUIText text) {
		List<GUIText> textBatch = texts2.get(text.getFont());
		textBatch.remove(text);
		if (textBatch.isEmpty()) {
			texts2.remove(text.getFont());
		}
	}
	
	public static void cleanUp(){
        renderer.cleanUp();
    }

}
