#version 460

//Zmienne jednorodne
uniform mat4 P;
uniform mat4 V;
uniform mat4 M;

//Atrybuty
in vec4 vertex; //wspolrzedne wierzcholka w przestrzeni modelu
in vec4 normal; //wspolrzedne wektora normalnego w przestrzeni modelu
in vec4 color; //kolor wierzchołka

//Zmienne interpolowane
out vec4 ic;

void main(void) {

    ic=vec4(color.rgb*0.5,color.a);
    gl_Position=P*V*M*vertex;
}
