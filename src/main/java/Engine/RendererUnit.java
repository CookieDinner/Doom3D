package Engine;

import Entities.Model;
import Entities.MyCube;
import de.matthiasmann.twl.utils.PNGDecoder;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL15.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_LESS;
import static org.lwjgl.opengl.GL15.GL_TRIANGLES;
import static org.lwjgl.opengl.GL15.glClearColor;
import static org.lwjgl.opengl.GL15.glDepthFunc;
import static org.lwjgl.opengl.GL15.glDrawArrays;
import static org.lwjgl.opengl.GL15.glEnable;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;

public class RendererUnit implements FileLoader{

    //REDUNDANT
    /*private int vboId;

    private int vboId1;

    private int vboId2;

    private int vaoId;*/

    private Matrix4f P = new Matrix4f();
    private Matrix4f V = new Matrix4f();
    private Matrix4f M = new Matrix4f();
    private MyCube cube = new MyCube();

    private Vector3f camDir = new Vector3f();
    private Vector3f camRight = new Vector3f();
    private Vector3f camUp = new Vector3f();
    private ShaderProgram shaderProgram;
    private ShaderProgram shader2;

    private Model gun = new Model("handgun.obj");
    private Lights lights;

    private int tex0;
    private int tex1;
    private int tex2;
    private int tex3;


    public RendererUnit() {
    }

    public int readTexture(String filename) {
        int tex = 0;
        glActiveTexture(GL_TEXTURE0);
        try {
            FileInputStream in = new FileInputStream(generateAbsolutePath("/src/main/models/",filename));
            PNGDecoder dec = new PNGDecoder(in);
            ByteBuffer buf = ByteBuffer.allocateDirect(4*dec.getWidth()*dec.getHeight());
            dec.decode(buf, dec.getWidth()*4, PNGDecoder.Format.RGBA);
            buf.flip();
            tex = glGenTextures();
            glBindTexture(GL_TEXTURE_2D,tex);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, dec.getWidth(), dec.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tex;
    }

    public void initBuffer() throws Exception {

        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader("vertex.glsl");
        shaderProgram.createFragmentShader("fragment.glsl");
        shader2 = new ShaderProgram();
        shader2.createVertexShader("vertex_simp.glsl");
        shader2.createFragmentShader("fragment_simp.glsl");
        shaderProgram.link();
        shader2.link();
        shaderProgram.bind();
        lights = new Lights(shaderProgram);

        glEnable(GL_DEPTH_TEST);
        //glEnable(GL_MULTISAMPLE);
        glDepthFunc(GL_LESS);
        //glShadeModel(GL_SMOOTH);

        //Culling the faces
        //glCullFace(GL_FRONT);
        //glEnable(GL_CULL_FACE);

        tex0 = readTexture("guntex.png");
        tex1 = readTexture("stone-wall.png");
        tex2 = readTexture("bricks.png");
        tex3 = readTexture("light.png");



        //REDUNTANT

        // Create a new Vertex Array Object in memory and select it (bind)
        // A VAO can have up to 16 attributes (VBO's) assigned to it by default
       /* vaoId = GL30.glGenVertexArrays();
        glBindVertexArray(vaoId);

        // Create a new Vertex Buffer Object in memory and select it (bind)
        // A VBO is a collection of Vectors which in this case resemble the location of each vertex.
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL_ARRAY_BUFFER, cube.fbVertex, GL_STATIC_DRAW);
        // Put the VBO in the attributes list at index 0
        GL20.glVertexAttribPointer(shaderProgram.a("vertex"), 4, GL11.GL_FLOAT, false, 0, 0);
        // Deselect (bind to 0) the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        vboId1 = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId1);
        GL15.glBufferData(GL_ARRAY_BUFFER, cube.fbNormals, GL_STATIC_DRAW);
        // Put the VBO in the attributes list at index 0
        GL20.glVertexAttribPointer(shaderProgram.a("normal"), 4, GL11.GL_FLOAT, false, 0, 0);
        // Deselect (bind to 0) the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        vboId2 = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId2);
        GL15.glBufferData(GL_ARRAY_BUFFER, cube.fbColors, GL_STATIC_DRAW);
        // Put the VBO in the attributes list at index 0
        GL20.glVertexAttribPointer(shaderProgram.a("color"), 4, GL11.GL_FLOAT, false, 0, 0);
        // Deselect (bind to 0) the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        // Deselect (bind to 0) the VAO
        glBindVertexArray(0);*/

    }

    public void render(Window window, float angle_x, float angle_y, Vector3f camPos, Vector3f camFront, Vector3f up){
        //shader2.bind();
        clearBuffers();
        glClearColor(0.1f, 0.2f, 0.5f, 1.0f);
        glfwSetInputMode(window.getWindowHandle(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        if (window.isResized()) {
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        //camDir.set(camPos).sub(camTarget).normalize();
        camRight.set(camDir).cross(up).normalize();
        camUp.set(camRight).cross(camDir);

        V.identity().lookAt(camPos, new Vector3f().set(camPos).add(camFront), up);
        P.identity().perspective(50.0f*(float)Math.PI/180.0f, window.getScreenRatio(),0.01f,800.0f);

        //shaderProgram.bind();

        //Drawing the lights and initializing them in the shader at the same time
        lights.drawLights(P, V, tex3);
        //glBindVertexArray(vaoId);


        //Rendering the ground

        M.identity().translate(0,-3,0).rotate(-3.14f/2,new Vector3f(1.0f,0.0f,0.0f));
        M.scale(1000.0f,1000.0f,0.5f);

        GL20.glUniformMatrix4fv(shaderProgram.u("P"),false,P.get(BufferUtils.createFloatBuffer(16)));
        GL20.glUniformMatrix4fv(shaderProgram.u("V"),false,V.get(BufferUtils.createFloatBuffer(16)));
        GL20.glUniformMatrix4fv(shaderProgram.u("M"),false,M.get(BufferUtils.createFloatBuffer(16)));

        GL20.glUniform1i(shaderProgram.u("textureMap0"),0);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D,tex2);

        glEnableVertexAttribArray(shaderProgram.a("vertex"));
        GL20.glVertexAttribPointer(shaderProgram.a("vertex"),4,GL_FLOAT,false,0,cube.fbVertex);
        glEnableVertexAttribArray(shaderProgram.a("normal"));
        GL20.glVertexAttribPointer(shaderProgram.a("normal"),4,GL_FLOAT,false,0,cube.fbNormals);
        glEnableVertexAttribArray(shaderProgram.a("texCoord0"));
        GL20.glVertexAttribPointer(shaderProgram.a("texCoord0"),4,GL_FLOAT,false,0,cube.fbTexCoords);

        glDrawArrays(GL_TRIANGLES, 0, cube.myCubeVertexCount);

        glDisableVertexAttribArray(shaderProgram.a("vertex"));
        glDisableVertexAttribArray(shaderProgram.a("normal"));
        glDisableVertexAttribArray(shaderProgram.a("texCoord0"));



        // Rendering the colored cube
        //shader2.unbind();
        //shaderProgram.bind();
        M.identity().translate(5,10,6).rotate(angle_x,new Vector3f(0f,1.0f,0f),M).rotate(angle_y,new Vector3f(1.0f,0f,0f),M);

        GL20.glUniformMatrix4fv(shaderProgram.u("P"),false,P.get(BufferUtils.createFloatBuffer(16)));
        GL20.glUniformMatrix4fv(shaderProgram.u("V"),false,V.get(BufferUtils.createFloatBuffer(16)));
        GL20.glUniformMatrix4fv(shaderProgram.u("M"),false,M.get(BufferUtils.createFloatBuffer(16)));

        GL20.glUniform1i(shaderProgram.u("textureMap0"),0);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D,tex1);


        glEnableVertexAttribArray(shaderProgram.a("vertex"));
        GL20.glVertexAttribPointer(shaderProgram.a("vertex"),4,GL_FLOAT,false,0,cube.fbVertex);
        glEnableVertexAttribArray(shaderProgram.a("normal"));
        GL20.glVertexAttribPointer(shaderProgram.a("normal"),4,GL_FLOAT,false,0,cube.fbVertexNormals);
        glEnableVertexAttribArray(shaderProgram.a("texCoord0"));
        GL20.glVertexAttribPointer(shaderProgram.a("texCoord0"),2,GL_FLOAT,false,0,cube.fbTexCoords);

        glDrawArrays(GL_TRIANGLES, 0, cube.myCubeVertexCount);

        glDisableVertexAttribArray(shaderProgram.a("vertex"));
        glDisableVertexAttribArray(shaderProgram.a("normal"));
        glDisableVertexAttribArray(shaderProgram.a("texCoord0"));

        glDisable(GL_TEXTURE_2D);





        M.identity().translate(5,10,6);

        GL20.glUniformMatrix4fv(shaderProgram.u("P"),false,P.get(BufferUtils.createFloatBuffer(16)));
        GL20.glUniformMatrix4fv(shaderProgram.u("V"),false,V.get(BufferUtils.createFloatBuffer(16)));
        GL20.glUniformMatrix4fv(shaderProgram.u("M"),false,M.get(BufferUtils.createFloatBuffer(16)));

        GL20.glUniform1i(shaderProgram.u("textureMap0"),0);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D,tex0);


        glEnableVertexAttribArray(shaderProgram.a("vertex"));
        GL20.glVertexAttribPointer(shaderProgram.a("vertex"),4,GL_FLOAT,false,0,gun.fbVertex);
        glEnableVertexAttribArray(shaderProgram.a("normal"));
        GL20.glVertexAttribPointer(shaderProgram.a("normal"),4,GL_FLOAT,false,0,gun.fbVertexNormals);
        glEnableVertexAttribArray(shaderProgram.a("texCoord0"));
        GL20.glVertexAttribPointer(shaderProgram.a("texCoord0"),2,GL_FLOAT,false,0,gun.fbTexCoords);

        glDrawArrays(GL_TRIANGLES, 0, gun.VertexCount);

        glDisableVertexAttribArray(shaderProgram.a("vertex"));
        glDisableVertexAttribArray(shaderProgram.a("normal"));
        glDisableVertexAttribArray(shaderProgram.a("texCoord0"));


        glDisable(GL_TEXTURE_2D);





        //glBindVertexArray(0);

    }

    public void clearBuffers() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
        glDisable(GL_TEXTURE0);
        glDeleteTextures(tex0);
        glDisableVertexAttribArray(0);

        //REDUNDANT
        // Delete the VBO
        /*glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboId);*/

        // Delete the VAO
        /*glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);*/
    }
}
