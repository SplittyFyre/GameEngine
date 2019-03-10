#version 330 core

in vec3 textureCoords;

layout (location = 0) out vec4 out_Color;
layout (location = 1) out vec4 out_BrightColor;

uniform samplerCube cubeMap;
uniform samplerCube cubeMap2;
uniform float blendFactor;
uniform vec3 fogColour;

void main(void){

	/*vec4 texture1 = texture(cubeMap, textureCoords);
	vec4 texture2 = texture(cubeMap2, textureCoords);
	vec4 finalColour = mix(texture1, texture2, blendFactor);
	
	float factor = (textureCoords.y - lowerLimit) / (upperLimit - lowerLimit);
	factor = clamp(factor, 0.0, 1.0);
    //out_Color = out_Color = mix(vec4(fogColour, 1.0), finalColour, factor);*/
    
    out_Color = texture(cubeMap, textureCoords);
    
    out_BrightColor = vec4(0);
}