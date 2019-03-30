#version 330 core

#define MAX_LIGHTS 4

in vec3 position;
in vec2 textureCoordinates;
in vec3 normal;

out vec2 pass_textureCoordinates;
out vec3 surfaceNormal;
out vec3 toLightVector[MAX_LIGHTS];
out vec3 toCameraVector;
out float visibility;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition[MAX_LIGHTS];

uniform bool useFakeLight;

uniform float numberOfRows;
uniform vec2 offset;

uniform float density;
uniform float gradient;

uniform vec4 plane;

uniform int lightsInUse;

void main(void) {
	
	vec4 worldPosition = transformationMatrix * vec4(position,1.0);
	
	gl_ClipDistance[0] = dot(worldPosition, plane);
	
	vec4 positionRelativeToCam = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * viewMatrix * worldPosition;
	pass_textureCoordinates = (textureCoordinates / numberOfRows) + offset;
	
	vec3 actualNormal = normal;
	
	if (useFakeLight) {
		actualNormal = vec3(0.0, 1.0, 0.0);
	}
	
	surfaceNormal = (transformationMatrix * vec4(actualNormal, 0.0)).xyz;
	
	for (int i = 0; i < lightsInUse; i++) {
		toLightVector[i] = lightPosition[i] - worldPosition.xyz;
	}
	toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;
	
	float distance = length(positionRelativeToCam.xyz);
	visibility = exp(-pow((distance * density), gradient));
	visibility = clamp(visibility, 0.0, 0.85);
	
}