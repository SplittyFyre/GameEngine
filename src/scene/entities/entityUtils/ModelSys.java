package scene.entities.entityUtils;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class ModelSys {
	
	public static Vector3f pos(Matrix4f mat, Vector3f vec) {
		Vector4f modelpos = new Vector4f(vec.x, vec.y, vec.z, 1);
		Vector4f real = Matrix4f.transform(mat, modelpos, null);
		return new Vector3f(real.x, real.y, real.z);
	}

}
