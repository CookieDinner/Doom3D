#version 130

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

    vec4 kd=color; //Kolor obiektu
    vec4 ld=vec4(1,1,1,1); //Kolor swiatla

    vec4 lp=vec4(0,0,-40,1);//Wsporzedne srodka swiatla w przestrzeni swiata

    vec4 l=normalize(V*(lp-M*vertex)); //Wektor "do swiatla" w przestrzeni oka
    vec4 n=normalize(V*M*normal); //Wektor normalny w przestrzeni oka

    float nl=clamp(dot(n,l),0,1);

    ic=vec4(kd.rgb*nl,kd.a);
    gl_Position=P*V*M*vertex;
}
