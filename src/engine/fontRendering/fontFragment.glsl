#version 330 core

in vec2 pass_textureCoords;

out vec4 outColour;

uniform vec4 colour;
uniform sampler2D fontAtlas;

void main(void){

	/*float zeta = colour.a == 1 ? texture(fontAtlas, pass_textureCoords).a : texture(fontAtlas, pass_textureCoords).a - (texture(fontAtlas, pass_textureCoords).a - colour.a);
	float beta = texture(fontAtlas, pass_textureCoords).a == 1 ? colour.a : texture(fontAtlas, pass_textureCoords).a;
	float beta = texture(fontAtlas, pass_textureCoords).a == 1 ? colour.a : zeta;*/

	outColour = vec4(colour.rgb, texture(fontAtlas, pass_textureCoords).a);

}