#version 330 core

#define MAX_LIGHTS 4

in vec2 pass_textureCoordinates;
in vec3 surfaceNormal;
in vec3 toLightVector[MAX_LIGHTS];
in vec3 toCameraVector;
in float visibility;

layout (location = 0) out vec4 outColour;
layout (location = 1) out vec4 outBrightColour;

uniform sampler2D modelTexture;
uniform sampler2D specularMap;
uniform bool usesSpecularMap;
uniform vec3 lightColour[MAX_LIGHTS];
uniform vec3 attenuation[MAX_LIGHTS];
uniform float shineDamper;
uniform float reflectivity;

uniform vec3 skyColour;

uniform float brightDamper;

uniform float celllvl;
uniform bool useCellShading;

uniform float ambientLightLvl;

uniform int lightsInUse;

void main(void) {

	float level;

	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitVectorToCamera = normalize(toCameraVector);
	
	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);
	
	for (int i = 0; i < lightsInUse; i++) {
	
		float distance = length(toLightVector[i]);
		float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
		vec3 unitLightVector = normalize(toLightVector[i]);
		float NDotL = dot(unitNormal, unitLightVector);
		float brightness = max(NDotL, 0.0);
		
		if (useCellShading) {
			level = floor(brightness * celllvl);
			brightness = level / celllvl;
		}
		
		vec3 lightDirection = -unitLightVector;
		vec3 reflectedLightDirection = reflect(lightDirection,unitNormal);
		float specularFactor = dot(reflectedLightDirection , unitVectorToCamera);
		specularFactor = max(specularFactor, 0.0);
		float dampedFactor = pow(specularFactor,shineDamper);
		 
		if (useCellShading) {
			level = floor(dampedFactor * celllvl);
			dampedFactor = level / celllvl;
		}
		
		totalDiffuse = totalDiffuse + (brightness * lightColour[i]) / attFactor;
		totalSpecular = totalSpecular + (dampedFactor * reflectivity * lightColour[i]) / attFactor;
	}
	
	totalDiffuse = max(totalDiffuse, ambientLightLvl);
	
	vec4 textureColour = texture(modelTexture, pass_textureCoordinates);
	
	if (textureColour.a < 0.5) {
		discard;
	}
	
	outBrightColour = vec4(0.0);
	
	if (usesSpecularMap) {
		vec4 mapInfo = texture(specularMap, pass_textureCoordinates);
		totalSpecular *= mapInfo.r;
		if (mapInfo.g > 0.5) {
			outBrightColour = textureColour + vec4(totalSpecular, 1.0);
			totalDiffuse = vec3(1.0);
		}
	}
	

	outColour = vec4(totalDiffuse, 1.0) * textureColour + vec4(totalSpecular, 1.0);
	outColour = mix(vec4(skyColour, 1.0), outColour, visibility);
	
	
	//be sure to change name
	outBrightColour *= brightDamper;
}