package engine.renderEngine;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import engine.utils.TRMath;

public class TRFrustumCuller {
		
	private Matrix4f viewMatrix = null;
	
	private TRProjectionCtx projection;
	
	private static final Vector4f vec4 = new Vector4f(0, 0, 0, 0), eyevec = new Vector4f(0, 0, 0, 0);
	
	
	public TRFrustumCuller(TRProjectionCtx projection) {
		this.setProjection(projection);
	}
	
	public void updateViewMatrix(Matrix4f matin) {
		this.viewMatrix = matin;
	}
	
	public void setProjection(TRProjectionCtx projection) {
		this.projection = projection;
	}
	
	/*public boolean isPointOutside(Vector3f point) {
		
		vec4.set(point.x, point.y, point.z, 1);
		Matrix4f.transform(viewMatrix, vec4, eyevec);
		
		// positive is behind
		if (eyevec.z > -nearPlane || eyevec.z < -farPlane) { // behind near plane or beyond far plane
			return true;
		}
		
		// z * 2 * tan(a / 2)
		float width = eyevec.z * 2.f * (float) Math.tan(Math.toRadians(70 / 2.f));
		if (width / 2.f > eyevec.x || eyevec.x > -width / 2.f) { // too much to left or too much to right
			return true;
		}

		// h = width / ratio
		float height = width / TRDisplayManager.getAspectRatio();
		if (height / 2.f > eyevec.y || eyevec.y > -height / 2.f) { // too high or too low
			return true;
		}
		
		return false;
	}*/
	
	public boolean isSphereOutside(float cx, float cy, float cz, float radius) {
		
		vec4.set(cx, cy, cz, 1);
		Matrix4f.transform(viewMatrix, vec4, eyevec);
		
		// positive is behind
		if (eyevec.z > -this.projection.nearPlane + radius || eyevec.z < -this.projection.farPlane - radius) { // behind near plane or beyond far plane
			//System.out.println("depth");
			return true;
		}
		
		float ang = 70.f;
		
		// z * 2 * tan(a / 2)
		float width = eyevec.z * 2.f * (float) Math.tan(Math.toRadians(ang / 2.f));
		float d = radius / (float) Math.cos(Math.toRadians(ang / 2.f));
		if (width / 2.f - d > eyevec.x || eyevec.x > -width / 2.f + d) { // too much to left or too much to right
			//System.out.println("fat");
			return true;
		}

		// h = width / ratio
		float height = width / TRDisplayManager.getAspectRatio();
		float beta = ang / TRDisplayManager.getAspectRatio();
		float xd = radius / (float) Math.cos(Math.toRadians(beta / 2.f));
		if (height / 2.f - xd > eyevec.y || eyevec.y > -height / 2.f + xd) { // too high or too low
			//System.out.println("drugs");
			return true;
		}
		
		return false;
	}
	
	public boolean isSphereOutside(Vector3f center, float radius) {
		return this.isSphereOutside(center.x, center.y, center.z, radius);
	}

}
