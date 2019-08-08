package engine.renderEngine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL43;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import engine.InternalStreamClass;

public abstract class ShaderProgram {
	
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	private boolean isCompute = false;
	
	protected Map<String, Integer> uniformLocationsHashMap = new HashMap<String, Integer>();
	
	protected void addUniformVariable(String varName) {
		uniformLocationsHashMap.put(varName, GL20.glGetUniformLocation(programID, varName));
	}
	protected int uniformLocationOf(String varName) {
		return uniformLocationsHashMap.get(varName);
	}
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	public ShaderProgram(String vertexFile, String fragmentFile) {
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID); 
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glValidateProgram(programID);
		getAllUniformLocations();
	}
	
	public ShaderProgram(String computeFile) {
		this.isCompute = true;
		int computeID = loadShader(computeFile, GL43.GL_COMPUTE_SHADER);
		this.programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, computeID);
		bindAttributes(); // not really for a compute shader
		GL20.glLinkProgram(programID);
		GL20.glDetachShader(programID, computeID);
		GL20.glDeleteShader(computeID);
		GL20.glValidateProgram(programID);
		getAllUniformLocations();
	}
	
	protected abstract void getAllUniformLocations();
	
	protected int getUniformLocation(String uniformName) {
		return GL20.glGetUniformLocation(programID, uniformName);
	}
	
	public void start(){
		GL20.glUseProgram(programID);
	}
	
	public void stop(){
		GL20.glUseProgram(0);
	}
	
	// first run glUseProgram, then dispatch compute
	public void dispatchCompute(int groupsX, int groupsY, int groupsZ) {
		GL43.glDispatchCompute(groupsX, groupsY, groupsZ);
	}
	
	public void cleanUp(){
		stop();
		GL20.glDeleteProgram(programID);
	}
	
	protected abstract void bindAttributes();
	
	protected void bindAttribute(int attribute, String variableName) {
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	
	protected void loadFloat(int location, float value) {
		GL20.glUniform1f(location, value);
	}
	
	protected void loadInt(int location, int value) {
		GL20.glUniform1i(location, value);
	}
	
	protected void load4dVector(int location, Vector4f vector) {
		GL20.glUniform4f(location, vector.x, vector.y, vector.z, vector.w);
	}
	
	protected void load4dVector(int location, float x, float y, float z, float w) {
		GL20.glUniform4f(location, x, y, z, w);
	}
	
	protected void loadVector(int location, Vector3f vector) {
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}
	
	protected void loadVector(int location, float x, float y, float z) {
		GL20.glUniform3f(location, x, y, z);
	}
	
	protected void load2dVector(int location, Vector2f vector) {
		GL20.glUniform2f(location, vector.x, vector.y);
	}
	
	protected void loadBoolean(int location, boolean value) {
		GL20.glUniform1i(location, value ? 1 : 0);
	}
	
	protected void loadMatrix(int location, Matrix4f matrix) {
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4(location, false, matrixBuffer);
	}
	
	@SuppressWarnings("resource")
	private static int loadShader(String file, int type) {
		file = file.replaceFirst("src", "");
		StringBuilder shaderSource = new StringBuilder();
		try{
			InputStream in = InternalStreamClass.class.getResourceAsStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while((line = reader.readLine()) != null) {
				shaderSource.append(line).append("//\n");
			}
			reader.close();
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		
		if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println();
			System.err.println("Shader compilation error at : " + file + "	Details:");
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.exit(-1);
		}
		return shaderID;
	}

}
