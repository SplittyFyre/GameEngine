#version 400 core

in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D guiTexture;

uniform float flagAlpha;
uniform float custAlpha;

void main(void){

	out_Color = texture(guiTexture, textureCoords);
	
	if (flagAlpha > 0.5) {
		out_Color.a = custAlpha;
	}

}