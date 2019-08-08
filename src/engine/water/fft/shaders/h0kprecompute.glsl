#version 430 core

layout(local_size_x = 16, local_size_y = 16) in;

layout (binding = 0, rgba32f) writeonly uniform image2D texture_h0k;
layout (binding = 1, rgba32f) writeonly uniform image2D texture_h0minusk;


uniform sampler2D noise_re0;
uniform sampler2D noise_re1;
uniform sampler2D noise_im0;
uniform sampler2D noise_im1;


uniform int N;
uniform int horizDim;
uniform float amplitude;
uniform vec2 windVec;
uniform float windSpeed;


#define PI 3.141592653589793238462643383279502884197169
#define TWOPI 6.283185307179586476925286766559005768394338

const float g = 9.81;

vec4 boxMullerRandGaussian() {

	vec2 texCoord = vec2(gl_GlobalInvocationID.xy) / float(N);

	float nr0 = clamp(texture(noise_re0, texCoord).r + 0.00001, 0, 1);
	float ni0 = clamp(texture(noise_im0, texCoord).r + 0.00001, 0, 1);
	float nr1 = clamp(texture(noise_re1, texCoord).r + 0.00001, 0, 1);
	float ni1 = clamp(texture(noise_im1, texCoord).r + 0.00001, 0, 1);


	float u0 = TWOPI * nr0;
	float v0 = sqrt(-2.0 * log(ni0));
	float u1 = TWOPI * nr1;
	float v1 = sqrt(-2.0 * log(ni1));

	return vec4(v0 * cos(u0), v0 * sin(u0), v1 * cos(u1), v1 * sin(u1));
}


float phillips(vec2 k) {

	float L = (windSpeed * windSpeed) / g;
	float lenK = max(length(k), 0.00001);
	float lenKSquared = lenK * lenK;

	float kdotw = abs(dot( normalize(k), normalize(windVec) ));

			   
	float ph = (amplitude / (lenKSquared * lenKSquared)) * // A / k^4
				exp(-(1.0 / (lenKSquared * L * L))) *
				//exp(-1.0 / (lenKSquared * L * L)) *
				//exp(-lenKSquared * pow(L / 2000.0, 0.2)) *
				kdotw * kdotw;
	return ph;
}


void main(void) {

	float n = gl_GlobalInvocationID.x, m = gl_GlobalInvocationID.y;
	// k is the wavevector
	vec2 k = vec2(TWOPI * n / float(horizDim), TWOPI * m / float(horizDim));

	vec4 gaussRnd = boxMullerRandGaussian();


	float h0k = clamp(sqrt(phillips(k) / 2.0), 0, 1000000);
	float h0minusk = clamp(sqrt(phillips(-k) / 2.0), 0, 1000000);

/*float L = windSpeed * windSpeed / g;
	float lenK = max(length(k), 0.0001);
	float lenKSquared = lenK * lenK;
	
	
	float h0k = clamp(sqrt((amplitude/(lenKSquared * lenKSquared)) * pow(dot(normalize(k), normalize(windVec)), 4.0) * exp(-(1.0 / (lenKSquared * L * L))) * 
exp(-lenKSquared * pow(L / 2000.0, 0.2)))/sqrt(2.0), 0, 1000000);
	

float h0minusk = clamp(sqrt((amplitude/(lenKSquared * lenKSquared)) * pow(dot(normalize(-k), normalize(windVec)), 4.0) * exp(-(1.0 / (lenKSquared * L * L))) * 
exp(-lenKSquared * pow(L / 2000.0, 0.2)))/sqrt(2.0), 0, 1000000);*/
	

	imageStore(texture_h0k, ivec2(gl_GlobalInvocationID.xy), vec4(gaussRnd.xy * h0k, 0.0, 1.0));
	imageStore(texture_h0minusk, ivec2(gl_GlobalInvocationID.xy), vec4(gaussRnd.zw * h0minusk, 0.0, 1.0));

}

