package Engine;

import Entities.MyCube;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;

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
                    {50.3f}                            //size of the light source
            },
            {
                    {300.0f, -200.0f,   0.0f,  1.0f},
                    {0.3f,  0.3f,  0.3f,  1.0f},
                    {50.0f},
                    {100.0f}
            },
            {
                    {-5.0f,   0.0f,    0.0f,   1.0f},
                    {0.6f,    0.7f,     0.8f,   1.0f},
                    {100.0f},
                    {0.3f}
            },
            {
                    {-5.0f,   0.0f,    12.0f,   1.0f},
                    {0.8f,   0.7f,    0.6f,   1.0f},
                    {100.0f},
                    {0.3f}
            }
    };

    private Matrix4f M = new Matrix4f();

    private MyCube cube = new MyCube();


    public Lights(ShaderProgram shaderProgram){
        this.shaderProgram = shaderProgram;
    }
    public void createLights(){

        //Passing the lights' uniform variables to the vertex shader
        //Scene lighting (sun)
        GL20.glUniform4fv(shaderProgram.u("i_lights[0].pos"), lights[0][0]);
        GL20.glUniform4fv(shaderProgram.u("i_lights[0].color"), lights[0][1]);
        GL20.glUniform1fv(shaderProgram.u("i_lights[0].phconst"), lights[0][2]);

        //Scene lighting (abyss)
        GL20.glUniform4fv(shaderProgram.u("i_lights[1].pos"), lights[1][0]);
        GL20.glUniform4fv(shaderProgram.u("i_lights[1].color"), lights[1][1]);
        GL20.glUniform1fv(shaderProgram.u("i_lights[1].phconst"), lights[1][2]);

        //1st small light

        GL20.glUniform4fv(shaderProgram.u("i_lights[2].pos"), lights[2][0]);
        GL20.glUniform4fv(shaderProgram.u("i_lights[2].color"), lights[2][1]);
        GL20.glUniform1fv(shaderProgram.u("i_lights[2].phconst"), lights[2][2]);

        //2nd small light
        GL20.glUniform4fv(shaderProgram.u("i_lights[3].pos"), lights[3][0]);
        GL20.glUniform4fv(shaderProgram.u("i_lights[3].color"), lights[3][1]);
        GL20.glUniform1fv(shaderProgram.u("i_lights[3].phconst"), lights[3][2]);

        //After adding a light remember to change the "lightsCount" accordingly in the vertex and fragment shaders,
        //Add the passing of the specific light array here
        //And, of course, don't repeat the light indexes
    }
    public void drawLights(Matrix4f P, Matrix4f V, int tex) {
        createLights();

        for (int i = 0; i < 4; i++) {
            M.identity().translate(lights[i][0][0], lights[i][0][1], lights[i][0][2]);
            M.scale(lights[i][3][0], lights[i][3][0], lights[i][3][0]);

            GL20.glUniform1i(shaderProgram.u("textureMap0"),0);
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D,tex);

            GL20.glUniformMatrix4fv(shaderProgram.u("P"), false, P.get(BufferUtils.createFloatBuffer(16)));
            GL20.glUniformMatrix4fv(shaderProgram.u("V"), false, V.get(BufferUtils.createFloatBuffer(16)));
            GL20.glUniformMatrix4fv(shaderProgram.u("M"), false, M.get(BufferUtils.createFloatBuffer(16)));


            glEnableVertexAttribArray(shaderProgram.a("vertex"));
            GL20.glVertexAttribPointer(shaderProgram.a("vertex"), 4, GL_FLOAT, false, 0, cube.fbVertex);
            glEnableVertexAttribArray(shaderProgram.a("normal"));
            GL20.glVertexAttribPointer(shaderProgram.a("normal"), 4, GL_FLOAT, false, 0, cube.fbNormals);
            glEnableVertexAttribArray(shaderProgram.a("color"));
            GL20.glVertexAttribPointer(shaderProgram.a("color"), 4, GL_FLOAT, false, 0, cube.fbLightColors);

            glDrawArrays(GL_TRIANGLES, 0, cube.myCubeVertexCount);

            glDisableVertexAttribArray(shaderProgram.a("vertex"));
            glDisableVertexAttribArray(shaderProgram.a("normal"));
            glDisableVertexAttribArray(shaderProgram.a("color"));
        }
    }
}
