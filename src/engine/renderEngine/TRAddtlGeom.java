package engine.renderEngine;

import org.lwjgl.util.vector.Matrix4f;

public class TRAddtlGeom {
	
	public Matrix4f transform;
	public float xtexoff, ytexoff;
	
	public TRAddtlGeom(Matrix4f transform, float xtexoff, float ytexoff) {
		this.transform = transform;
		this.xtexoff = xtexoff;
		this.ytexoff = ytexoff;
	}

}
