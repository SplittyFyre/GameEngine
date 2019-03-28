#version 330 core

in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D guiTexture;

uniform bool flagAlpha;
uniform float custAlpha;

void main(void){

	out_Color = texture(guiTexture, textureCoords);
	
	if (flagAlpha) {
		out_Color.a = custAlpha;
	}

}