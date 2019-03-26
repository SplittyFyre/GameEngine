package engine.utils;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import engine.collision.BoundingBox;
import engine.scene.entities.camera.Camera;
import engine.scene.terrain.Terrain;

public class RaysCast {

	private static final int RECURSION_COUNT = 200;
	private static final float RAY_RANGE = 2400;

	private Vector3f currentRay = new Vector3f();

	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	private Camera camera;
	
	private Terrain terrain;
	private Vector3f currentTerrainPoint;

	public RaysCast(Camera cam, Matrix4f projection, Terrain terrain) {
		camera = cam;
		projectionMatrix = projection;
		viewMatrix = SFMath.createViewMatrix(camera);
		this.terrain = terrain;
	}
	
	public Vector3f getCurrentTerrainPoint() {
		return currentTerrainPoint;
	}

	public Vector3f getCurrentRay() {
		return currentRay;
	}

	public void update() {
		viewMatrix = SFMath.createViewMatrix(camera);
		currentRay = calculateMouseRay();
		if (intersectionInRange(0, RAY_RANGE, currentRay)) {
			currentTerrainPoint = binarySearch(0, 0, RAY_RANGE, currentRay);
		} else {
			currentTerrainPoint = null;
		}
	}
	
	/**This method casts the ray, it will cast a ray from the mouse cursor on the 2d screen and
	 * use inverted matrices and calculations to cast into world space. **/
	
	private Vector3f calculateMouseRay() {
		float mouseX = Mouse.getX();
		float mouseY = Mouse.getY();
		Vector2f normalizedCoords = getNormalisedDeviceCoordinates(mouseX, mouseY);
		Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1.0f, 1.0f);
		Vector4f eyeCoords = toEyeCoords(clipCoords);
		Vector3f worldRay = toWorldCoords(eyeCoords);
		
		return worldRay;
	}
	
	/**From eye-space into world coords, our objective. 
	 * Uses inverted view matrix, uses Matrix4f.invert()**/
	
	private Vector3f toWorldCoords(Vector4f eyeCoords) {
		Matrix4f invertedView = Matrix4f.invert(viewMatrix, null);
		Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
		Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalise();
		
		return mouseRay;
	}
	
	/**From homegeneous (frustum) space to eye space
	 *  using inverse projection matrix**/
	
	private Vector4f toEyeCoords(Vector4f clipCoords) {
		Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix, null);
		Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);
		
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
	}
	
	/**Returns normalized device coordinates using simple calculation, 
	 * takes in viewport coordinates. This calculation is done automatically
	 * by OpenGL when going from normal to viewport**/
	
	private Vector2f getNormalisedDeviceCoordinates(float mouseX, float mouseY) {
		float x = (2.0f * mouseX) / Display.getWidth() - 1f;
		float y = (2.0f * mouseY) / Display.getHeight() - 1f;
		
		return new Vector2f(x, y);  
	}
	
	//**********************************************************
	
	public Vector3f getPointOnRay(Vector3f ray, float distance) {
		Vector3f camPos = camera.getPosition();
		Vector3f start = new Vector3f(camPos.x, camPos.y, camPos.z);
		Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
		return Vector3f.add(start, scaledRay, null);
	}
	
	private Vector3f binarySearch(int count, float start, float finish, Vector3f ray) {
		float half = start + ((finish - start) / 2f);
		if (count >= RECURSION_COUNT) {
			Vector3f endPoint = getPointOnRay(ray, half);
			Terrain terrain = getTerrain(endPoint.getX(), endPoint.getZ());
			if (terrain != null) {
				return endPoint;
			} else {
				return null;
			}
		}
		if (intersectionInRange(start, half, ray)) {
			return binarySearch(count + 1, start, half, ray);
		} else {
			return binarySearch(count + 1, half, finish, ray);
		}
	}

	private boolean intersectionInRange(float start, float finish, Vector3f ray) {
		Vector3f startPoint = getPointOnRay(ray, start);
		Vector3f endPoint = getPointOnRay(ray, finish);
		return (!isUnderGround(startPoint) && isUnderGround(endPoint));
	}

	private boolean isUnderGround(Vector3f testPoint) {
		Terrain terrain = getTerrain(testPoint.getX(), testPoint.getZ());
		float height = 0;
		if (terrain != null) {
			height = terrain.getTerrainHeight(testPoint.getX(), testPoint.getZ());
		}
		return testPoint.y < height;
	}

	private Terrain getTerrain(float worldX, float worldZ) {
		return terrain;
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	//**********************************************************

	public boolean penetrates(BoundingBox bb) {
		
		//float tmin = Float.NEGATIVE_INFINITY, tmax = Float.POSITIVE_INFINITY;
		
		Vector3f invdir = new Vector3f(1 / this.getCurrentRay().x, 1 / this.getCurrentRay().y, 1 / this.getCurrentRay().z);
		
		float t1 = (bb.minX - this.getCamera().getPosition().x) * invdir.x;
		float t2 = (bb.maxX - this.getCamera().getPosition().x) * invdir.x;
		
		float tmin = Math.min(t1, t2);
		float tmax = Math.max(t1, t2);
		
		//******************************************************
		
		//tmin = max(tmin, min(t1, t2));
		//tmax = min(tmax, max(t1, t2));
		
		//******************************************************
		
		t1 = (bb.minY - this.getCamera().getPosition().y) * invdir.y;
		t2 = (bb.maxY - this.getCamera().getPosition().y) * invdir.y;
		
		//tmin = Math.max(tmin, Math.min(Math.min(t1, t2), tmax));
		//tmax = Math.min(tmax, Math.max(Math.max(t1, t2), tmin));

		tmin = Math.max(tmin, Math.min(t1, t2));
		tmax = Math.min(tmax, Math.max(t1, t2));
		
		//******************************************************
		
		t1 = (bb.minZ - this.getCamera().getPosition().z) * invdir.z;
		t2 = (bb.maxZ - this.getCamera().getPosition().z) * invdir.z;
		
		tmin = Math.max(tmin, Math.min(t1, t2));
		tmax = Math.min(tmax, Math.max(t1, t2));
		
		return tmax > Math.max(tmin, 0);
	}
	
	public boolean penetrates(BoundingBox bb, float bbinflation) {
		
		//float tmin = Float.NEGATIVE_INFINITY, tmax = Float.POSITIVE_INFINITY;
		
		Vector3f invdir = new Vector3f(1 / this.getCurrentRay().x, 1 / this.getCurrentRay().y, 1 / this.getCurrentRay().z);
		
		float t1 = (bb.minX / bbinflation - this.getCamera().getPosition().x) * invdir.x;
		float t2 = (bb.maxX * bbinflation - this.getCamera().getPosition().x) * invdir.x;
		
		float tmin = Math.min(t1, t2);
		float tmax = Math.max(t1, t2);
		
		//******************************************************
		
		//tmin = max(tmin, min(t1, t2));
		//tmax = min(tmax, max(t1, t2));
		
		//******************************************************
		
		t1 = (bb.minY / bbinflation - this.getCamera().getPosition().y) * invdir.y;
		t2 = (bb.maxY * bbinflation - this.getCamera().getPosition().y) * invdir.y;
		
		//tmin = Math.max(tmin, Math.min(Math.min(t1, t2), tmax));
		//tmax = Math.min(tmax, Math.max(Math.max(t1, t2), tmin));

		tmin = Math.max(tmin, Math.min(t1, t2));
		tmax = Math.min(tmax, Math.max(t1, t2));
		
		//******************************************************
		
		t1 = (bb.minZ / bbinflation - this.getCamera().getPosition().z) * invdir.z;
		t2 = (bb.maxZ * bbinflation - this.getCamera().getPosition().z) * invdir.z;
		
		tmin = Math.max(tmin, Math.min(t1, t2));
		tmax = Math.min(tmax, Math.max(t1, t2));
		
		return tmax > Math.max(tmin, 0);
	}

}
