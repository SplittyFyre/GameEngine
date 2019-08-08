#version 330 core

in vec2 position;

out vec2 textureCoords;

uniform vec4 transform;

void main(void) {
	
	textureCoords = position + vec2(0.5, 0.5);
	textureCoords.y = 1.0 - textureCoords.y;
	
	vec2 screenPosition = position * transform.zw + transform.xy;
	
	screenPosition.x = screenPosition.x * 2.0 - 1.0;
	screenPosition.y = screenPosition.y * -2.0 + 1.0;
	
	// when rendering occlude dummy, depth test enabled, rendered at very back
	// when rendering flares, depth test disabled + alpha blending, so that still works
	gl_Position = vec4(screenPosition, 0.9999, 1.0);

}