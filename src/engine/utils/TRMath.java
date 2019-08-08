package engine.utils;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import engine.renderEngine.TRDisplayManager;
import engine.scene.entities.camera.TRCamera;

public class TRMath {
	
	public static final int SF_DIRECTION_AZIMUTH_RIGHT = -90;
	public static final int SF_DIRECTION_AZIMUTH_LEFT = 90;
	public static final int SF_DIRECTION_AZIMUTH_NEUTRAL = 0;
	
	public static Vector3f xTranslation = new Vector3f(0, 0, 0);
	
	public static float relativePosShiftX(int direction, float angdeg, float magnitude) {
		if (magnitude == 0) {
			return 0;
		}
		float angrad = (float) Math.toRadians(angdeg + direction);
		return (float) (Math.sin(angrad) * magnitude);
	}
	
	public static float relativePosShiftZ(int direction, float angdeg, float magnitude) {
		if (magnitude == 0) {
			return 0;
		}
		float angrad = (float) Math.toRadians(angdeg + direction);
		return (float) (Math.cos(angrad) * magnitude);
	}
	
	public static Vector3f vecShifts(float rotY, Vector3f in, float magSide, float magHeight, float magFront) {
		return new Vector3f(
				
				in.x 
				+ TRMath.relativePosShiftX(TRMath.SF_DIRECTION_AZIMUTH_RIGHT, rotY, magSide)
				- TRMath.relativePosShiftX(TRMath.SF_DIRECTION_AZIMUTH_NEUTRAL, rotY, -magFront)
				,
				in.y + magHeight
				,
				in.z
				+ TRMath.relativePosShiftZ(TRMath.SF_DIRECTION_AZIMUTH_RIGHT, rotY, magSide)
				- TRMath.relativePosShiftZ(TRMath.SF_DIRECTION_AZIMUTH_NEUTRAL, rotY, -magFront)
				
				);
	}
	
	public static Vector3f fullPos(float rotY, float rotX, Vector3f in, float magSide, float magHeight, float magFront, 
			float distToRotOrigin) {
		
		float forward = magFront - Math.abs(distToRotOrigin / (90 / rotX));
		
		return new Vector3f(
				
				in.x 
				+ TRMath.relativePosShiftX(TRMath.SF_DIRECTION_AZIMUTH_RIGHT, rotY, magSide)
				- TRMath.relativePosShiftX(TRMath.SF_DIRECTION_AZIMUTH_NEUTRAL, rotY, -forward)
				,
				(float) (in.y + magHeight - Math.sin(Math.toRadians(rotX)) * distToRotOrigin)
				,
				in.z
				+ TRMath.relativePosShiftZ(TRMath.SF_DIRECTION_AZIMUTH_RIGHT, rotY, magSide)
				- TRMath.relativePosShiftZ(TRMath.SF_DIRECTION_AZIMUTH_NEUTRAL, rotY, -forward)
				
				);
	}
	
	public static boolean nIsWithin(float n, float min, float max) {
		return n <= max && n >= min;
	}
	
	public static float distance(Vector3f a, Vector3f b) {
		float dx = a.x - b.x;
		float dy = a.y - b.y;
		float dz = a.z - b.z;
		return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
	}
	
	public static float distance(Vector2f a, Vector2f b) {
		float dx = a.x - b.x;
		float dy = a.y - b.y;
		return (float) Math.sqrt(dx * dx + dy * dy);
	}
	
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}

	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale, float rotation, boolean flipped) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		if (flipped)
			Matrix4f.rotate((float) Math.PI, new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0, 0, 1), matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f createTransformationMatrix(Vector2f translation, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale, scale, 1f), matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, Vector3f scale) {
		
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(Vector3f.add(translation, xTranslation, null), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.scale(scale, matrix, matrix);
		
		return matrix;
	}
	
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
		
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(Vector3f.add(translation, xTranslation, null), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
		
		return matrix;
	}
	
	public static Matrix4f createTransformationMatrixspec(Vector3f translation, float rx, float ry, float rz, float scale) {
		
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(Vector3f.add(translation, xTranslation, null), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
		
		return matrix;
	}
	
	public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotOrigin, float rx, float ry, float rz, Vector3f scale) {
		
		Vector3f.add(translation, xTranslation, translation);
		
		Matrix4f matrix = new Matrix4f();
		Matrix4f matrix2 = new Matrix4f();
		matrix.setIdentity();
		matrix2.setIdentity();
		
		Vector3f dif = Vector3f.sub(translation, rotOrigin, null);
		Vector3f ndif = new Vector3f(-dif.x, -dif.y, -dif.z);
		
		Matrix4f.translate(translation, matrix, matrix);
		
		Matrix4f.translate(ndif, matrix2, matrix2);
		
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0), matrix2, matrix2);
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0), matrix2, matrix2);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1), matrix2, matrix2);
		
		Matrix4f.translate(dif, matrix2, matrix2);
		
		Matrix4f.mul(matrix, matrix2, matrix);
		
		Matrix4f.scale(scale, matrix, matrix);
		
		return matrix;
	}
	
	public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotOrigin, float rx, float ry, float rz, Vector3f scale, boolean ignoreRY) {
		
		Vector3f.add(translation, xTranslation, translation);
		
		Matrix4f matrix = new Matrix4f();
		Matrix4f matrix2 = new Matrix4f();
		matrix.setIdentity();
		matrix2.setIdentity();
		
		Vector3f dif = Vector3f.sub(translation, rotOrigin, null);
		Vector3f ndif = new Vector3f(-dif.x, -dif.y, -dif.z);
		
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
		
		Matrix4f.translate(ndif, matrix2, matrix2);
		
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0), matrix2, matrix2);
		
		Matrix4f.translate(dif, matrix2, matrix2);
		
		Matrix4f.mul(matrix, matrix2, matrix);
		
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);
		
		Matrix4f.scale(scale, matrix, matrix);
		
		return matrix;
	}
	
	public static Matrix4f createViewMatrix(TRCamera camera) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), viewMatrix,
				viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
		
		return viewMatrix;
	}
	
	public static Vector3f rotateToFaceVector(Vector3f rotatingObjectPosition, Vector3f destination) {
		Vector3f a = rotatingObjectPosition;
		Vector3f b = destination;
		Vector3f front = new Vector3f();
		Vector3f.sub(b, a, front);
		if (front.length() != 0)
			front.normalise();
		float requiredRotY = (float) Math.toDegrees(Math.atan2(front.x, front.z));
		float requiredRotX = (float) Math.toDegrees(Math.asin(front.y));
		return new Vector3f(requiredRotX, requiredRotY, 0);
	}
	
	public static float Y_rotateToFaceVector(Vector3f rotatingObjectPosition, Vector3f destination) {
		Vector3f a = rotatingObjectPosition;
		Vector3f b = destination;
		Vector3f front = new Vector3f();
		Vector3f.sub(b, a, front);
		if (front.length() != 0)
			front.normalise();
		return (float) Math.toDegrees(Math.atan2(front.x, front.z));
	}
	
	public static Vector3f moveToVector(Vector3f a, Vector3f b, float speed) {
		
		float f = TRDisplayManager.getFrameDeltaTime() * speed;
		Vector3f d = Vector3f.sub(a, b, null);
		if (d.length() != 0)
			d.normalise();
		float xy = (float) Math.sqrt(d.x * d.x + d.z * d.z);
		float ca = (float) Math.sqrt(1 - (d.y * d.y));
		
		return new Vector3f(d.x / xy * ca * f, d.y * f, d.z / xy * ca * f);
	}
	
	public static Vector3f vecoffset(Vector3f vec, float x, float y, float z) {
		return new Vector3f(vec.x + x, vec.y + y, vec.z + z);
	}
	
	
	
	
		
	public static Vector2f sqr2 = new Vector2f(0.01f, 0.01f * TRDisplayManager.getAspectRatio());
	public static Vector2f sqr4 = new Vector2f(0.025f, 0.025f * TRDisplayManager.getAspectRatio());
	public static Vector2f sqr8 = new Vector2f(0.045f, 0.045f * TRDisplayManager.getAspectRatio());
	
	public static Vector2f sqrgui(float scale) {
		return new Vector2f(scale, scale * TRDisplayManager.getAspectRatio());
	}
	
	public static Vector2f coordtext(Vector2f pos) {
		return coordtext(pos.x, pos.y);
	}
	
	public static Vector2f coordtextcenter(Vector2f pos, float scaleX, float scaleY) {
		Vector2f retval = coordtext(pos.x, pos.y);
		retval.x -= (scaleX / 2f);
		retval.y -= (scaleY / 2f);
		return retval;
	}
	
	public static Vector2f coordtextcenter(Vector2f pos, float scaleX, float scaleY, float yscalediv) {
		Vector2f retval = coordtext(pos.x, pos.y);
		retval.x -= (scaleX / 2f);
		retval.y -= (scaleY / yscalediv);
		return retval;
	}
	
	public static Vector2f coordtext(float x, float y) {	
		return new Vector2f(((x / 2) + 0.5f), -(y / 2) + 0.5f);
	}
	
	public static float coordtextx(float x) {
		return (x / 2) + 0.5f;
	}
	
	public static float coordtexty(float y) {
		return -(y / 2) + 0.5f;
	}
	
	
	public static void transformAndSet_inplace(Matrix4f left, Vector3f right, Vector3f dest) {
		float x = left.m00 * right.x + left.m10 * right.y + left.m20 * right.z + left.m30 * 1.0f;
		float y = left.m01 * right.x + left.m11 * right.y + left.m21 * right.z + left.m31 * 1.0f;
		float z = left.m02 * right.x + left.m12 * right.y + left.m22 * right.z + left.m32 * 1.0f;

		dest.x = x;
		dest.y = y;
		dest.z = z;
	}
	
	public static float maxInVector(Vector3f vec) {
		return Math.max(vec.x, Math.max(vec.y, vec.z));
	}
	
	public static void mulVecByScalar(Vector3f vec, float scalar) {
		vec.x *= scalar; vec.y *= scalar; vec.z *= scalar;
	}

}
