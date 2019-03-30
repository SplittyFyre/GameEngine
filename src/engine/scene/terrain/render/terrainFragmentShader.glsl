#version 330 core

in vec2 pass_textureCoordinates;
in vec3 surfaceNormal;
in vec3 toLightVector[4];
in vec3 toCameraVector;
in float visibility;

layout (location = 0) out vec4 out_Color;
layout (location = 1) out vec4 out_BrightColor;

uniform sampler2D backgroundTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMap;

uniform vec3 lightColour[4];
uniform vec3 attenuation[4];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColour;

uniform bool base;

uniform float height;

in float vtxheight;

uniform float tiling;

uniform float ambientLightLvl;

uniform bool useAltitudeVarying;
uniform vec3 vecCaps;
uniform float maxheight;

uniform int lightsInUse;

void main(void){

	vec4 blendMapColour = texture(blendMap, pass_textureCoordinates);
	
	float backTextureAmount = 1 - (blendMapColour.r + blendMapColour.g + blendMapColour.b);
	
	vec2 tiledCoords = pass_textureCoordinates * tiling;
	
	// samples needed in any condition
	vec4 backgroundSample = texture(backgroundTexture, tiledCoords);
	vec4 rSample = texture(rTexture, tiledCoords);
	vec4 gSample = texture(gTexture, tiledCoords);
	vec4 bSample = texture(bTexture, tiledCoords);
	
	vec4 totalColour;
	
	bool freak = false;
	
	if (useAltitudeVarying) { // use altitude cap thing
	
		if (vtxheight > vecCaps.z * maxheight) {
			totalColour = bSample;
		}
		else if (vtxheight > vecCaps.y * maxheight) {
			freak = true;
			totalColour = gSample;
		}
		else if (vtxheight > vecCaps.x * maxheight) {
			totalColour = rSample;
		}
		else {
			totalColour = backgroundSample; 
		}
	
	}
	else { // use blendMap
	
		vec4 backgroundTextureColour = backgroundSample * backTextureAmount;
		vec4 rTextureColour = rSample * blendMapColour.r;
		vec4 gTextureColour = gSample * blendMapColour.g;
		vec4 bTextureColour = bSample * blendMapColour.b;
	
		totalColour = backgroundTextureColour + rTextureColour + gTextureColour + bTextureColour;
	}

	//totalColour = mix(totalColour, texture(rTexture, tiledCoords), clamp(-((vtxheight + 10 - height) / (10 - -10)), 0, 1));

	vec3 unitNormal = normalize(surfaceNormal);
	
	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);
	
	for (int i = 0; i < lightsInUse; i++){
		
		float distance = length(toLightVector[i]);
		float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
		vec3 unitLightVector = normalize(toLightVector[i]);
		float nDotl = dot(unitNormal,unitLightVector);
		
		float brightness = max(nDotl, 0.0);
		
		vec3 unitVectorToCamera = normalize(toCameraVector);
		vec3 lightDirection = -unitLightVector;
		vec3 reflectedLightDirection = reflect(lightDirection,unitNormal);
		float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
		
		specularFactor = max(specularFactor, 0.0);
		
		float dampedFactor = pow(specularFactor,shineDamper);
		
		totalDiffuse = totalDiffuse + (brightness * lightColour[i]) / attFactor;
		totalSpecular = totalSpecular + (dampedFactor * reflectivity * lightColour[i]) / attFactor;
	
	}
	
	totalDiffuse = max(totalDiffuse, ambientLightLvl);
	
	out_Color = vec4(totalDiffuse,1.0) * totalColour + vec4(totalSpecular,1.0);
	out_Color = mix(vec4(skyColour,1.0),out_Color, visibility);
	out_BrightColor = vec4(0);
}