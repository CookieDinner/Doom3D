package Engine;

import Entities.Model;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL15.GL_LESS;
import static org.lwjgl.opengl.GL15.glClearColor;
import static org.lwjgl.opengl.GL15.glDepthFunc;
import static org.lwjgl.opengl.GL15.glEnable;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;

public class RendererUnit implements FileLoader{

    //REDUNDANT
    /*private int vboId;

    private int vboId1;

    private int vboId2;

    private int vaoId;*/

    private Matrix4f P = new Matrix4f();
    private Matrix4f V = new Matrix4f();
    private Matrix4f M = new Matrix4f();

    private Vector3f camDir = new Vector3f();
    private Vector3f camRight = new Vector3f();
    private Vector3f camUp = new Vector3f();
    private ShaderProgram shaderProgram;
    private ShaderProgram shader2;

    //Models declarations

    private Model gun;
    private Model cube;
    private Model ground;
    private Model skybox;
    private Model dragon;

    private Lights lights;



    public RendererUnit() {
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
        glDepthFunc(GL_LESS);

        //Culling the faces
        //glCullFace(GL_BACK);
        //glEnable(GL_CULL_FACE);


        gun = new Model(shaderProgram, "handgun.obj", "guntex.png","gundiffuse.png","darksky.png");
        cube = new Model(shaderProgram, "cube.obj", "metal.png", "metaldiffuse.png","sky.png");
        skybox = new Model(shaderProgram, "skybox.obj", "skybox.png","black.png","black.png");
        ground = new Model(shaderProgram, "ground.obj", "bricks.png","bricksdiffuse.png","black.png");
        dragon = new Model(shaderProgram, "dragon.obj", "dragon.png","dragondiffuse.png","black.png");



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
        clearBuffers();
        glClearColor(0.1f, 0.2f, 0.5f, 1.0f);
        glfwSetInputMode(window.getWindowHandle(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        if (window.isResized()) {
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        camRight.set(camDir).cross(up).normalize();
        camUp.set(camRight).cross(camDir);

        V.identity().lookAt(camPos, new Vector3f().set(camPos).add(camFront), up);
        P.identity().perspective(50.0f*(float)Math.PI/180.0f, window.getScreenRatio(),0.01f,500000.0f);

        //Drawing the lights and initializing them in the shader at the same time
        lights.drawLights(P, V);
        //glBindVertexArray(vaoId);



        M.identity().translate(300.0f,-9.50f,0.0f).rotate(-3.14f/2,new Vector3f(0.0f,1.0f,0.0f)).scale(20.0f,20.0f,20.0f);
        dragon.draw(M,V,P,3);

        M.identity().translate(5,10,6).rotate(angle_x,new Vector3f(0f,1.0f,0f)).rotate(angle_y,new Vector3f(1.0f,0f,0f));
        Matrix4f Mtemp = M;
        cube.draw(M,V,P,6); //1

        M = Mtemp;
        M.translate(0.7f,4.8f,2.5f);
        Mtemp = M;
        M.scale(0.2f,0.2f,0.2f);
        gun.draw(M, V, P, 9); //2

        M = Mtemp;

        M.translate(-12.0f,0.0f,71.0f).rotate(3.14f, new Vector3f(0.0f,1.0f,0.0f));
        gun.draw(M,V,P, 12); //2

        // GROUND AND THE SKYBOX DRAWN AT THE END
        M.identity().translate(0,0,0);
        M.scale(40.0f,1.0f,40.0f);
        ground.draw(M,V,P, 15); //1


        M.identity().rotate(3.14f,new Vector3f(0.0f,0.0f,1.0f)).translate(0.0f,-6000.0f,0.0f).scale(15000.0f,15000.0f,15000.0f);
        skybox.draw(M,V,P, 18); //1




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
