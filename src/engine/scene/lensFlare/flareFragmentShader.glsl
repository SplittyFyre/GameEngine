#version 330 core

in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D guiTexture;

uniform float brightness;

void main(void){

	out_Color = texture(guiTexture, textureCoords);
	
	out_Color.a *= brightness * 0.75;

}