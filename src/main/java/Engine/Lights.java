package Engine;

import Entities.Model;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL20;


public class Lights {

    private ShaderProgram shaderProgram;
    //Initializations of the light sources go here
    private float lights[][][]={                    //Parameters for all the lights, each 1st dimension array corresponds to 1 light
                                                    //2nd dimension represents the specific attribute of the light
                                                    //3rd dimension includes all the parameters of the specific light attributes
            {
                    {0.0f,   300.0f,    0.0f,   1.0f},  //coordinates
                    {0.3f,   0.3f,       0.3f,   1.0f}, //color of the light
                    {50.0f},                           //phong's constant
                    {30.3f}                            //size of the light source
            },
            {
                    {-5.0f,   7.0f,    0.0f,   1.0f},
                    {0.6f,    0.7f,     0.8f,   1.0f},
                    {100.0f},
                    {1.0f}
            },
            {
                    {-5.0f,   7.0f,    12.0f,   1.0f},
                    {0.8f,   0.7f,    0.6f,   1.0f},
                    {100.0f},
                    {1.0f}
            }
    };

    private Matrix4f M = new Matrix4f();

    private Model light;


    public Lights(ShaderProgram shaderProgram){
        this.shaderProgram = shaderProgram;
        light = new Model(shaderProgram, "lamp.obj","lamp.png","lampdiffuse.png","sky.png");
    }
    public void createLights(){

        //Passing the lights' uniform variables to the vertex shader
        //Scene lighting (sun)
        GL20.glUniform4fv(shaderProgram.u("i_lights[0].pos"), lights[0][0]);
        GL20.glUniform4fv(shaderProgram.u("i_lights[0].color"), lights[0][1]);
        GL20.glUniform1fv(shaderProgram.u("i_lights[0].phconst"), lights[0][2]);

        //1st small light
        GL20.glUniform4fv(shaderProgram.u("i_lights[1].pos"), lights[1][0]);
        GL20.glUniform4fv(shaderProgram.u("i_lights[1].color"), lights[1][1]);
        GL20.glUniform1fv(shaderProgram.u("i_lights[1].phconst"), lights[1][2]);

        //2nd small light

        GL20.glUniform4fv(shaderProgram.u("i_lights[2].pos"), lights[2][0]);
        GL20.glUniform4fv(shaderProgram.u("i_lights[2].color"), lights[2][1]);
        GL20.glUniform1fv(shaderProgram.u("i_lights[2].phconst"), lights[2][2]);

        //After adding a light remember to change the "lightsCount" accordingly in the vertex and fragment shaders,
        //Add the passing of the specific light array here
        //And, of course, don't repeat the light indexes
    }
    public void drawLights(Matrix4f P, Matrix4f V) {
        createLights();

        for (int i = 0; i < 3; i++) {
            M.identity().translate(lights[i][0][0], lights[i][0][1], lights[i][0][2]);
            M.scale(lights[i][3][0], lights[i][3][0], lights[i][3][0]);

            if (i == 0)
                M.rotate(-3.14f,new Vector3f(1.0f,0.0f,0.0f));
            light.draw(M,V,P,0);
        }
    }
}
