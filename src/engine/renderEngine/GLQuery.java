package engine.renderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

public class GLQuery {
	
	private final int id;
	private final int type;
	
	public boolean isInUse = false;
	
	public GLQuery(int type) {
		this.type = type;
		this.id = GL15.glGenQueries();
	}
	
	public void start() {
		GL15.glBeginQuery(type, id);
		isInUse = true;
	}
	
	public void end() {
		GL15.glEndQuery(type);
	}
	
	public void delete() {
		GL15.glDeleteQueries(id);
	}
	
	
	public int getResult() {
		isInUse = false;
		return GL15.glGetQueryObjecti(id, GL15.GL_QUERY_RESULT);
	}
	
	public boolean isResultReady() {
		return GL15.glGetQueryObjecti(id, GL15.GL_QUERY_RESULT_AVAILABLE) == GL11.GL_TRUE;
	}

}
