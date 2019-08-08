package engine.objStuff;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import engine.collision.BoundingBox;
import engine.renderEngine.Loader;
import engine.renderEngine.models.RawModel;
import internal.ResourceStreamClass;

public class OBJParser {

	public static RawModel loadObjModel(String fileName) {
		
		InputStreamReader isr = new InputStreamReader(Class.class.getResourceAsStream("/res/" + fileName + ".obj"));
		
		BufferedReader reader = new BufferedReader(isr);
		String line;
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		float[] verticesArray = null;
		float[] normalsArray = null;
		float[] textureArray = null;
		int[] indicesArray = null;
		
		try {

			while (true) {
				line = reader.readLine();
				String[] currentLine = line.split(" ");
				
				if (line.startsWith("v ")) {
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					vertices.add(vertex);
				} 
				else if (line.startsWith("vt ")) {
					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]));
					textures.add(texture);
				} 
				else if (line.startsWith("vn ")) {
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					normals.add(normal);
				} 
				else if (line.startsWith("f ")) {
					textureArray = new float[vertices.size() * 2];
					normalsArray = new float[vertices.size() * 3];
					break;
				}
				
			}

			while (line != null) {
				
				if (!line.startsWith("f ")) {
					line = reader.readLine();
					continue;
				}
				
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");
				
				processVertex(vertex1, indices, textures, normals, textureArray, normalsArray);
				processVertex(vertex2, indices, textures, normals, textureArray, normalsArray);
				processVertex(vertex3, indices, textures, normals, textureArray, normalsArray);
				
				line = reader.readLine();
				
			}
			
			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		verticesArray = new float[vertices.size() * 3];
		indicesArray = new int[indices.size()];
		
		int vertexPointer = 0;
		
		
		BoundingBox aabb = new BoundingBox(0, 0, 0, 0, 0, 0, 0);
		
		for (Vector3f vertex : vertices) {
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;
			
			float radius = vertex.length();
			if (radius > aabb.sphereRadius) {
				aabb.sphereRadius = radius;
			}
			
			if (vertex.x < aabb.minX)
				aabb.minX = vertex.x;
			else if (vertex.x > aabb.maxX)
				aabb.maxX = vertex.x;
			
			if (vertex.y < aabb.minY)
				aabb.minY = vertex.y;
			else if (vertex.y > aabb.maxY)
				aabb.maxY = vertex.y;
			
			if (vertex.z < aabb.minZ)
				aabb.minZ = vertex.z;
			else if (vertex.z > aabb.maxZ)                       
				aabb.maxZ = vertex.z;
		}
		
		for (int i = 0; i < indices.size(); i++)
			indicesArray[i] = indices.get(i);
		
		
		return Loader.loadToVAO(verticesArray, textureArray, normalsArray, indicesArray, aabb);
		
	}

	private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures, 
			List<Vector3f> normals, float[] textureArray, 
			float[] normalsArray) {
		
		int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
		indices.add(currentVertexPointer); 
		Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
		textureArray[currentVertexPointer * 2] = currentTex.x;
		textureArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;
		Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
		normalsArray[currentVertexPointer * 3] = currentNorm.x;
		normalsArray[currentVertexPointer * 3 + 1] = currentNorm.y;
		normalsArray[currentVertexPointer * 3 + 2] = currentNorm.z;	
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static RawModel loadObjModelWProperTexSeams(String fileName) {
		
		InputStreamReader isr = new InputStreamReader(ResourceStreamClass.class.getResourceAsStream("/res/" + fileName + ".obj"));
		
        BufferedReader reader = new BufferedReader(isr);
        String line;
        List<Vertex> vertices = new ArrayList<Vertex>();
        List<Vector2f> textures = new ArrayList<Vector2f>();
        List<Vector3f> normals = new ArrayList<Vector3f>();
        List<Integer> indices = new ArrayList<Integer>();
        try {
            while (true) {
                line = reader.readLine();
                if (line.startsWith("v ")) {
                    String[] currentLine = line.split(" ");
                    Vector3f vertex = new Vector3f(Float.valueOf(currentLine[1]),
                            Float.valueOf(currentLine[2]),
                            Float.valueOf(currentLine[3]));
                    Vertex newVertex = new Vertex(vertices.size(), vertex);
                    vertices.add(newVertex);
 
                } 
                else if (line.startsWith("vt ")) {
                    String[] currentLine = line.split(" ");
                    Vector2f texture = new Vector2f(Float.valueOf(currentLine[1]),
                            Float.valueOf(currentLine[2]));
                    textures.add(texture);
                } 
                else if (line.startsWith("vn ")) {
                    String[] currentLine = line.split(" ");
                    Vector3f normal = new Vector3f(Float.valueOf(currentLine[1]),
                            Float.valueOf(currentLine[2]),
                            Float.valueOf(currentLine[3]));
                    normals.add(normal);
                } 
                else if (line.startsWith("f ")) {
                    break;
                }
            }
            while (line != null && line.startsWith("f ")) {
                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");
                processVertex(vertex1, vertices, indices);
                processVertex(vertex2, vertices, indices);
                processVertex(vertex3, vertices, indices);
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }
        removeUnusedVertices(vertices);
        float[] verticesArray = new float[vertices.size() * 3];
        float[] texturesArray = new float[vertices.size() * 2];
        float[] normalsArray = new float[vertices.size() * 3];
        BoundingBox bb = convertDataToArrays(vertices, textures, normals, verticesArray,
                texturesArray, normalsArray);
        int[] indicesArray = convertIndicesListToArray(indices);
			
        
        
        return Loader.loadToVAO(verticesArray, texturesArray, normalsArray, indicesArray, bb);
    }
 
    private static void processVertex(String[] vertex, List<Vertex> vertices, List<Integer> indices) {
        int index = Integer.parseInt(vertex[0]) - 1;
        Vertex currentVertex = vertices.get(index);
        int textureIndex = Integer.parseInt(vertex[1]) - 1;
        int normalIndex = Integer.parseInt(vertex[2]) - 1;
        if (!currentVertex.isSet()) {
            currentVertex.setTextureIndex(textureIndex);
            currentVertex.setNormalIndex(normalIndex);
            indices.add(index);
        } 
        else {
            dealWithAlreadyProcessedVertex(currentVertex, textureIndex, normalIndex, indices,
                    vertices);
        }
    }
 
    private static int[] convertIndicesListToArray(List<Integer> indices) {
        int[] indicesArray = new int[indices.size()];
        for (int i = 0; i < indicesArray.length; i++) {
            indicesArray[i] = indices.get(i);
        }
        return indicesArray;
    }
 
    private static BoundingBox convertDataToArrays(List<Vertex> vertices, List<Vector2f> textures,
            List<Vector3f> normals, float[] verticesArray, float[] texturesArray,
            float[] normalsArray) {
    	
    	BoundingBox aabb = new BoundingBox(0, 0, 0, 0, 0, 0, 0);
    	
        for (int i = 0; i < vertices.size(); i++) {
            Vertex currentVertex = vertices.get(i);
            Vector3f position = currentVertex.getPosition();
            Vector2f textureCoord = textures.get(currentVertex.getTextureIndex());
            Vector3f normalVector = normals.get(currentVertex.getNormalIndex());
            verticesArray[i * 3] = position.x;
            verticesArray[i * 3 + 1] = position.y;
            verticesArray[i * 3 + 2] = position.z;
            texturesArray[i * 2] = textureCoord.x;
            texturesArray[i * 2 + 1] = 1 - textureCoord.y;
            normalsArray[i * 3] = normalVector.x;
            normalsArray[i * 3 + 1] = normalVector.y;
            normalsArray[i * 3 + 2] = normalVector.z;
            
            
            float radius = position.length();
            
            if (radius > aabb.sphereRadius) {
            	aabb.sphereRadius = radius;
            }
            
            
            if (position.x < aabb.minX)
				aabb.minX = position.x;
			else if (position.x > aabb.maxX)
				aabb.maxX = position.x;
			
			if (position.y < aabb.minY)
				aabb.minY = position.y;
			else if (position.y > aabb.maxY)
				aabb.maxY = position.y;
			
			if (position.z < aabb.minZ)
				aabb.minZ = position.z;
			else if (position.z > aabb.maxZ)                       
				aabb.maxZ = position.z;
        }
        return aabb;
    }
 
    private static void dealWithAlreadyProcessedVertex(Vertex previousVertex, int newTextureIndex,
            int newNormalIndex, List<Integer> indices, List<Vertex> vertices) {
        if (previousVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex)) {
            indices.add(previousVertex.getIndex());
        } 
        else {
            Vertex anotherVertex = previousVertex.getDuplicateVertex();
            if (anotherVertex != null) {
                dealWithAlreadyProcessedVertex(anotherVertex, newTextureIndex, newNormalIndex,
                        indices, vertices);
            } 
            else {
                Vertex duplicateVertex = new Vertex(vertices.size(), previousVertex.getPosition());
                duplicateVertex.setTextureIndex(newTextureIndex);
                duplicateVertex.setNormalIndex(newNormalIndex);
                previousVertex.setDuplicateVertex(duplicateVertex);
                vertices.add(duplicateVertex);
                indices.add(duplicateVertex.getIndex());
            }
 
        }
    }
     
    private static void removeUnusedVertices(List<Vertex> vertices){
        for (Vertex vertex : vertices){
            if (!vertex.isSet()){
                vertex.setTextureIndex(0);
                vertex.setNormalIndex(0);
            }
        }
    }

}
