package Engine;

import Entities.MyCube;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;

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

public class RendererUnit {


    private int vboId;

    private int vboId1;

    private int vboId2;

    private int vaoId;

    Matrix4f M = new Matrix4f();
    Matrix4f P = new Matrix4f();
    Matrix4f V = new Matrix4f();
    private MyCube cube = new MyCube();

    private ShaderProgram shaderProgram;

    public RendererUnit() {
    }

    public void initBuffer() throws Exception {

        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader("vertex.glsl");
        shaderProgram.createFragmentShader("fragment.glsl");
        shaderProgram.link();

        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);

        // Create a new Vertex Array Object in memory and select it (bind)
        // A VAO can have up to 16 attributes (VBO's) assigned to it by default
        vaoId = GL30.glGenVertexArrays();
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
        glBindVertexArray(0);

    }


    public void render(Window window, float angle_x, float angle_y) {
        clearBuffers();
        glClearColor(0.0f, 0.3f, 0.4f, 0.0f);


        if (window.isResized()) {
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }


        V.identity().lookAt(
                new Vector3f(0,0,-5),
                new Vector3f(0,0,0),
                new Vector3f(0,1,0));
        P.identity().perspective(50.0f*(float)Math.PI/180.0f, window.getScreenRatio(),0.01f,50.0f);
        M.identity().rotate(angle_x,new Vector3f(0f,1.0f,0f),M).rotate(angle_y,new Vector3f(1.0f,0f,0f),M);



        shaderProgram.bind();

        glBindVertexArray(vaoId);


        FloatBuffer fbP = BufferUtils.createFloatBuffer(16);
        FloatBuffer fbV = BufferUtils.createFloatBuffer(16);
        FloatBuffer fbM = BufferUtils.createFloatBuffer(16);

        GL20.glUniformMatrix4fv(shaderProgram.u("P"),false,P.get(fbP));
        GL20.glUniformMatrix4fv(shaderProgram.u("V"),false,V.get(fbV));
        GL20.glUniformMatrix4fv(shaderProgram.u("M"),false,M.get(fbM));

        glEnableVertexAttribArray(shaderProgram.a("vertex"));
        GL20.glVertexAttribPointer(shaderProgram.a("vertex"),4,GL_FLOAT,false,0,cube.fbVertex);

        glEnableVertexAttribArray(shaderProgram.a("normal"));
        GL20.glVertexAttribPointer(shaderProgram.a("normal"),4,GL_FLOAT,false,0,cube.fbNormals);

        glEnableVertexAttribArray(shaderProgram.a("color"));
        GL20.glVertexAttribPointer(shaderProgram.a("color"),4,GL_FLOAT,false,0,cube.fbColors);

        glDrawArrays(GL_TRIANGLES, 0, cube.myCubeVertexCount);

        glDisableVertexAttribArray(shaderProgram.a("vertex"));
        glDisableVertexAttribArray(shaderProgram.a("normal"));
        glDisableVertexAttribArray(shaderProgram.a("color"));

        glBindVertexArray(0);

        shaderProgram.unbind();


    }

    public void clearBuffers() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }

        glDisableVertexAttribArray(0);

        // Delete the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboId);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }
}
