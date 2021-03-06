#version 330 core

layout (location = 0) out vec4 out_Color;
layout (location = 1) out vec4 out_BrightColor;

in vec2 textureCoords1;
in vec2 textureCoords2;
in float blend;

uniform sampler2D particleTexture;

void main(void){

	vec4 colour1 = texture(particleTexture, textureCoords1);
	vec4 colour2 = texture(particleTexture, textureCoords2);
	
	out_Color = mix(colour1, colour2, blend);
	
	out_BrightColor = vec4(0);

}