package utils;

public class SFQuaternion {
	
	public float x, y, z, w;
	
	public float getRotation() {
		return (float) Math.toDegrees(Math.acos(x));
	}
	
}
