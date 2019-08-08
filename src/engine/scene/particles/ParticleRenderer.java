package engine.scene.particles;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import engine.renderEngine.Loader;
import engine.renderEngine.models.RawModel;
import engine.scene.entities.camera.TRCamera;
import engine.utils.TRMath;

public class ParticleRenderer {
	
	private static final float[] VERTICES = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};
	private static final int MAX_INSTANCES = 10000;
	private static final int INSTANCE_DATA_LEN = 21;
	//16 + 4 + 1
	
	private static final FloatBuffer buf = BufferUtils.createFloatBuffer(MAX_INSTANCES * INSTANCE_DATA_LEN);
	
	private RawModel quad;
	private ParticleShader shader;
	
	private int vbo;
	private int ptr = 0;
	
	private boolean matrixChanged = false;
	private Matrix4f newMatrix = null;
	
	public void setProjectionMatrix(Matrix4f pmat) {
		matrixChanged = true;
		newMatrix = pmat;
	}
	
	protected ParticleRenderer(Matrix4f projectionMatrix) {
		this.vbo = Loader.createEmptyVBO(INSTANCE_DATA_LEN * MAX_INSTANCES); 
		quad = Loader.loadToVAO(VERTICES, 2);
		
		//use for loop instead? (Matrix)				   +
		Loader.addInstancedAttribute(quad.getVaoID(), vbo, 1, 4, INSTANCE_DATA_LEN, 0);
		Loader.addInstancedAttribute(quad.getVaoID(), vbo, 2, 4, INSTANCE_DATA_LEN, 4);
		Loader.addInstancedAttribute(quad.getVaoID(), vbo, 3, 4, INSTANCE_DATA_LEN, 8);
		Loader.addInstancedAttribute(quad.getVaoID(), vbo, 4, 4, INSTANCE_DATA_LEN, 12);
		//4d texture offsets
		Loader.addInstancedAttribute(quad.getVaoID(), vbo, 5, 4, INSTANCE_DATA_LEN, 16);
		//blend factors
		Loader.addInstancedAttribute(quad.getVaoID(), vbo, 6, 1, INSTANCE_DATA_LEN, 20);
		shader = new ParticleShader();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	protected void render(Map<ParticleTexture, List<Particle>> particles, TRCamera camera) {
		final Matrix4f viewMatrix = camera.getViewMatrix();
		prepare();
		for (ParticleTexture texture : particles.keySet()) {
			
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
			shader.loadNumRows(texture.getNumberOfRows());
			
			List<Particle> particleList = particles.get(texture);
			ptr = 0;
			float[] vboData = new float[particleList.size() * INSTANCE_DATA_LEN];
			
			for (Particle particle : particleList) {
				
				updateModelViewMatrix(particle.getPosition(), particle.getRotation(), particle.getScale(), viewMatrix, vboData);
				updateTexCoordInfo(particle, vboData);
				
				if (particle.priority) {
					GL11.glDisable(GL11.GL_DEPTH_TEST);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				}
				
			}
			Loader.updateVBO(vbo, vboData, buf);
			GL31.glDrawArraysInstanced(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount(), particleList.size());
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		}
		finishRendering();
	}
	
	protected void cleanUp() {
		shader.cleanUp();
	}
	
	private void updateTexCoordInfo(Particle p, float[] data) {
		data[ptr++] = p.getTexOffSet1().x;
		data[ptr++] = p.getTexOffSet1().y;
		data[ptr++] = p.getTexOffSet2().x;
		data[ptr++] = p.getTexOffSet2().y;
		data[ptr++] = p.getBlend();
	}
	
	private void updateModelViewMatrix(Vector3f pos, float rot, float scale, final Matrix4f viewMatrix, float[] vboData) {
		Matrix4f modelMatrix = new Matrix4f();
		Matrix4f.translate(pos, modelMatrix, modelMatrix);
		modelMatrix.m00 = viewMatrix.m00;
		modelMatrix.m01 = viewMatrix.m10;
		modelMatrix.m02 = viewMatrix.m20;
		modelMatrix.m10 = viewMatrix.m01;
		modelMatrix.m11 = viewMatrix.m11;
		modelMatrix.m12 = viewMatrix.m21;
		modelMatrix.m20 = viewMatrix.m02;
		modelMatrix.m21 = viewMatrix.m12;
		modelMatrix.m22 = viewMatrix.m22;
		
		Matrix4f.rotate((float) Math.toRadians(rot), new Vector3f(0, 0, 1), modelMatrix, modelMatrix);
		Matrix4f.scale(new Vector3f(scale, scale, scale), modelMatrix, modelMatrix);
		Matrix4f modelViewMatrix = Matrix4f.mul(viewMatrix, modelMatrix, null);
		storeMatrixData(modelViewMatrix, vboData);
		
		/*Matrix4f modelViewMatrix = Matrix4f.mul(viewMatrix, modelMatrix, null);
		Matrix4f.rotate((float) Math.toRadians(rot), new Vector3f(0, 0, 1), modelViewMatrix, 
				modelViewMatrix);
		Matrix4f.scale(new Vector3f(scale, scale, scale), modelViewMatrix, modelViewMatrix);
		storeMatrixData(modelViewMatrix, vboData);*/
		
	}
	
	private void storeMatrixData(Matrix4f matrix, float[] vboData) {
		vboData[ptr++] = matrix.m00;
		vboData[ptr++] = matrix.m01;
		vboData[ptr++] = matrix.m02;
		vboData[ptr++] = matrix.m03;
		vboData[ptr++] = matrix.m10;
		vboData[ptr++] = matrix.m11;
		vboData[ptr++] = matrix.m12;
		vboData[ptr++] = matrix.m13;
		vboData[ptr++] = matrix.m20;
		vboData[ptr++] = matrix.m21;
		vboData[ptr++] = matrix.m22;
		vboData[ptr++] = matrix.m23;
		vboData[ptr++] = matrix.m30;
		vboData[ptr++] = matrix.m31;
		vboData[ptr++] = matrix.m32;
		vboData[ptr++] = matrix.m33;
	}
	
	private void prepare() {
		shader.start();
		if (matrixChanged) {
			this.matrixChanged = false;
			shader.loadProjectionMatrix(this.newMatrix);
		}
		GL30.glBindVertexArray(quad.getVaoID());
		for (int i = 0; i <= 6; i++) {
			GL20.glEnableVertexAttribArray(i);
		}
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glDepthMask(false);
	}
	
	private void finishRendering() {
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		for (int i = 0; i <= 6; i++) {
			GL20.glDisableVertexAttribArray(i);
		}
		GL30.glBindVertexArray(0);
		shader.stop();
	}

}
