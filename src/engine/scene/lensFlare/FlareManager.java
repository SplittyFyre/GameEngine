package engine.scene.lensFlare;

import javax.activity.InvalidActivityException;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import engine.renderEngine.guis.render.GUIRenderer;
import engine.scene.entities.camera.TRCamera;
import engine.utils.TRMath;

public class FlareManager {
	
	private static final Vector2f SCREEN_CENTER = new Vector2f(0.5f, 0.5f);
	
	private final FlareTexture[] flareTextures;
	private final float spacing;
	
	private GUIRenderer renderer;
	
	public FlareManager(GUIRenderer renderer, float spacing, FlareTexture... textures) {
		this.spacing = spacing;
		this.flareTextures = textures;
		this.renderer = renderer;
	}

	
	public void render(TRCamera camera, Vector3f sunPos, Matrix4f projectionMatrix) {
		Vector2f sunCoords = toScreenSpace(sunPos, TRMath.createViewMatrix(camera), projectionMatrix);
		if (sunCoords == null) {
			return;
		}
		Vector2f sunToCenter = Vector2f.sub(SCREEN_CENTER, sunCoords, null);
		float brightness = 1 - (sunToCenter.length() / 0.6f); // if sundist < 0.6 then brightness > 1
		if (brightness > 0) {
			calcFlarePositions(sunToCenter, sunCoords);
			try {
				renderer.renderFlareMode(flareTextures, brightness);
			} catch (InvalidActivityException e) {
				System.err.println("Error: provided GUIRenderer does not support Flare Rendering");
				System.exit(-1);
				e.printStackTrace();
			}
		}
	}
	
	private void calcFlarePositions(Vector2f sunToCenter, Vector2f sunCoords) {
		for (int i = 0; i < flareTextures.length; i++) {
			Vector2f direction = new Vector2f(sunToCenter);
			direction.scale(i * spacing);
			Vector2f flarePos = Vector2f.add(sunCoords, direction, null);
			flareTextures[i].setPosition(flarePos);
		}
	}
	
	private static Vector2f toScreenSpace(Vector3f worldPos, Matrix4f viewMat, Matrix4f projectionMat) {
		Vector4f coords = new Vector4f(worldPos.x, worldPos.y, worldPos.z, 1);
		Matrix4f.transform(viewMat, coords, coords);
		Matrix4f.transform(projectionMat, coords, coords);
		if (coords.w <= 0) { // if not on screen
			return null;
		}
		// perspective divison + weird coordinate conversion thing
		float x = (coords.x / coords.w + 1) / 2.f;
		float y = 1 - ((coords.y / coords.w + 1) / 2.f);
		return new Vector2f(x, y);
	}
	
}
