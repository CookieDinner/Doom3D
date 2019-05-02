import org.joml.*;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.lang.Math;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;


public class Main {

    // The window handle
    private long window;
    private float aspectRatio;
    Matrix4f M = new Matrix4f();
    Matrix4f P = new Matrix4f();
    Matrix4f V = new Matrix4f();
    private MyCube cube = new MyCube();
    ShaderProgram sp = new ShaderProgram();

    float speed=0;
    float obrspeed=0;
    private int vaoId = 0;
    private int vboId = 0;

    public void run() {

        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(1280, 720, "Doom3D", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true);
            if (action==GLFW_PRESS) {
                if (key == GLFW_KEY_LEFT) speed=3.14f;
                if (key == GLFW_KEY_RIGHT) speed = -3.14f;
                if (key == GLFW_KEY_UP) obrspeed=0.1f;
                if (key == GLFW_KEY_DOWN) obrspeed=-0.1f;
            }
            if (action==GLFW_RELEASE) {
                speed=0;
                obrspeed=0;
            }
        });

        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            glfwGetWindowSize(window, pWidth, pHeight);
            aspectRatio = (float)pWidth.get(0)/(float)pHeight.get(0);
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
    }


    private void setupCube(){
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(cube.myCubeVertexCount);
        verticesBuffer.put(cube.myCubeVertices.length);
        verticesBuffer.flip();


        // Create a new Vertex Array Object in memory and select it (bind)
        // A VAO can have up to 16 attributes (VBO's) assigned to it by default
        vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);

        // Create a new Vertex Buffer Object in memory and select it (bind)
        // A VBO is a collection of Vectors which in this case resemble the location of each vertex.
        vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
        // Put the VBO in the attributes list at index 0
        GL20.glVertexAttribPointer(0, 4, GL11.GL_FLOAT, false, 0, 0);
        // Deselect (bind to 0) the VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        // Deselect (bind to 0) the VAO
        GL30.glBindVertexArray(0);
    }


    private void drawScene(long window, float angle, float obroc){

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

//
//        V.lookAt(
//                new Vector3f(0,0,-5),
//                new Vector3f(0,0,0),
//                new Vector3f(0,1,0));
//        P.perspective(50.0f*(float)Math.PI/180.0f, aspectRatio,0.01f,50.0f);
//        M.identity().rotate(angle,obroc,0,1);
////        sp.use();


        // Bind to the VAO that has all the information about the quad vertices
        GL30.glBindVertexArray(vaoId);
        GL20.glEnableVertexAttribArray(0);

        // Draw the vertices
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.myCubeVertexCount);

        // Put everything back to default (deselect)
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);




//
//        FloatBuffer fbP = BufferUtils.createFloatBuffer(16);
//        FloatBuffer fbV = BufferUtils.createFloatBuffer(16);
//        FloatBuffer fbM = BufferUtils.createFloatBuffer(16);
//
//        glUniformMatrix4fv(sp.u("P"),false,P.get(fbP));
//        glUniformMatrix4fv(sp.u("V"),false,V.get(fbV));
//        glUniformMatrix4fv(sp.u("M"),false,M.get(fbM));
//
//
//        glEnableVertexAttribArray(sp.a("vertex"));
//        glVertexAttribPointer(sp.a("vertex"),4,GL_FLOAT,false,0,cube.makeFloatBuffer(cube.myCubeVertices));
//
//        glEnableVertexAttribArray(sp.a("normal"));
//        glVertexAttribPointer(sp.a("normal"),4,GL_FLOAT,false,0,cube.makeFloatBuffer(cube.myCubeNormals));
//
//        glEnableVertexAttribArray(sp.a("color"));
//        glVertexAttribPointer(sp.a("color"),4,GL_FLOAT,false,0,cube.makeFloatBuffer(cube.myCubeColors));
//
//        glDrawArrays(GL_TRIANGLES, 0, cube.myCubeVertexCount);
//
//        glDisableVertexAttribArray(sp.a("vertex"));
//        glDisableVertexAttribArray(sp.a("normal"));
//        glDisableVertexAttribArray(sp.a("color"));

        glfwSwapBuffers(window);
    }

    private void loop() {



        GL.createCapabilities();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        setupCube();

        float angle=0; //Aktualny kąt obrotu obiektu
        float obroc=0;
        glfwSetTime(0); //Zeruj timer



        while ( !glfwWindowShouldClose(window) ) {
            angle+=speed*glfwGetTime(); //Zwiększ/zmniejsz kąt obrotu na podstawie prędkości i czasu jaki upłynał od poprzedniej klatki
            obroc+=obrspeed*glfwGetTime();
            if(obroc==1)obroc=0;

            drawScene(window, angle, obroc );
            glfwPollEvents();

        }
    }

    public static void main(String[] args) {
        new Main().run();
    }

}