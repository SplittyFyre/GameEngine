#version 330 core

in vec4 clipSpace;
in vec2 textureCoords;
in vec3 toCameraVector;
in vec3 fromLightVector;

layout (location = 0) out vec4 outColour;
layout (location = 1) out vec4 outBrightColour;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D dudvMap;
uniform sampler2D normalMap;
uniform sampler2D depthMap;
uniform vec3 lightColour;

uniform float movedFactor;

uniform float waveIntensity;
uniform float shineDamper;
uniform float reflectivity;

uniform float nearPlane;
uniform float farPlane;

uniform vec3 colourOffset;

void main(void) {

	vec2 ndc = (clipSpace.xy / clipSpace.w) / 2.0 + 0.5;
	vec2 refractTexCoords = vec2(ndc.x, ndc.y);
	vec2 reflectTexCoords = vec2(ndc.x, -ndc.y);
	
	float depth = texture(depthMap, refractTexCoords).r;
	float floorDistance = 2.0 * nearPlane * farPlane / (farPlane + nearPlane - (2.0 * depth - 1.0) * (farPlane - nearPlane));
	
	depth = gl_FragCoord.z;
	float waterDistance = 2.0 * nearPlane * farPlane / (farPlane + nearPlane - (2.0 * depth - 1.0) * (farPlane - nearPlane));	
	float waterDepth = floorDistance - waterDistance;	
	
	vec2 distortedTexCoords = texture(dudvMap, vec2(textureCoords.x + movedFactor, textureCoords.y)).rg * 0.1;
	distortedTexCoords = textureCoords + vec2(distortedTexCoords.x, distortedTexCoords.y + movedFactor);
	vec2 totalDistortion = (texture(dudvMap, distortedTexCoords).rg * 2.0 - 1.0) * waveIntensity * clamp(waterDepth/20.0, 0.0, 1.0);
	
	refractTexCoords += totalDistortion;
	refractTexCoords = clamp(refractTexCoords, 0.001, 0.999);
	
	reflectTexCoords += totalDistortion;
	reflectTexCoords.x = clamp(reflectTexCoords.x, 0.001, 0.999);
	reflectTexCoords.y = clamp(reflectTexCoords.y, -0.999, -0.001);
	
	vec4 reflectColour = texture(reflectionTexture, reflectTexCoords);
	vec4 refractColour = texture(refractionTexture, refractTexCoords);
	
	refractColour = mix(refractColour, vec4(0.001, 0.001, 0, 0.01), clamp(waterDepth / 80.0, 0.0, 0.5));
	
	vec4 normalMapColour = texture(normalMap, distortedTexCoords);
	vec3 normal = vec3(normalMapColour.r * 2.0 - 1.0, normalMapColour.b * 3.0, normalMapColour.g * 2.0 - 1.0);
	normal = normalize(normal);
	
	vec3 viewVector = normalize(toCameraVector);
	float refractiveFactor = dot(viewVector, normal);
	refractiveFactor = pow(refractiveFactor, 0.5);
	refractiveFactor = clamp(refractiveFactor, 0.0, 1.0);
	
	refractiveFactor = clamp(refractiveFactor, 0.0, 1.0);
	 
	vec3 reflectedLight = reflect(normalize(fromLightVector), normal);
	float specular = max(dot(reflectedLight, viewVector), 0.0);
	specular = pow(specular, shineDamper);
	vec3 specularHighlights = lightColour * specular * reflectivity * clamp(waterDepth/5.0, 0.0, 1.0);

	outColour = mix(reflectColour, refractColour, refractiveFactor);
	outColour = mix(outColour, vec4(0.0, 0.3, 0.5, 1.0), 0.2) + vec4(specularHighlights, 0.0);
	outColour.a = clamp(waterDepth / 5.0, 0.0, 1.0);
		
	outColour.xyz += colourOffset.xyz;
	
	outBrightColour = vec4(0.0);
}