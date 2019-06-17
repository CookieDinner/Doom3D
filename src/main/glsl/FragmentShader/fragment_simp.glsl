#version 460


//Zmienne interpolowane
in vec4 ic;
in vec2 iTexCoord0;

uniform sampler2D textureMap0;

out vec4 pixelColor; //Zmienna wyjsciowa fragment shadera. Zapisuje sie do niej ostateczny (prawie) kolor piksela

void main(void) {
	vec4 kd=texture(textureMap0,iTexCoord0);
	pixelColor=kd;
}
