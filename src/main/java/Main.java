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

    private static final float PI = 3.14f;
    // The window handle
    private long window;
    private float aspectRatio;
    private float speed_x=0;
    private float speed_y=0;
    private int vaoId=0;
    private int vboId=0;
    private int vboId1=0;
    private int vboId2=0;

    Matrix4f M = new Matrix4f();
    Matrix4f P = new Matrix4f();
    Matrix4f V = new Matrix4f();
    private MyCube cube = new MyCube();
    ShaderProgram sp = new ShaderProgram();

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
                if (key==GLFW_KEY_LEFT) speed_x=-PI/2;
                if (key==GLFW_KEY_RIGHT) speed_x=PI/2;
                if (key==GLFW_KEY_UP) speed_y=PI/2;
                if (key==GLFW_KEY_DOWN) speed_y=-PI/2;
            }
            if (action==GLFW_RELEASE) {
                if (key==GLFW_KEY_LEFT) speed_x=0;
                if (key==GLFW_KEY_RIGHT) speed_x=0;
                if (key==GLFW_KEY_UP) speed_y=0;
                if (key==GLFW_KEY_DOWN) speed_y=0;
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


    private void drawScene(long window, float angle_x, float angle_y){

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        sp.use();
        GL30.glBindVertexArray(vaoId);

        V.identity().lookAt(
                new Vector3f(0,0,-5),
                new Vector3f(0,0,0),
                new Vector3f(0,1,0));
        P.identity().perspective(50.0f*(float)Math.PI/180.0f, aspectRatio,0.01f,50.0f);
        M.identity().rotate(angle_x,new Vector3f(0f,1.0f,0f),M).rotate(angle_y,new Vector3f(1.0f,0f,0f),M);



        FloatBuffer fbP = BufferUtils.createFloatBuffer(16);
        FloatBuffer fbV = BufferUtils.createFloatBuffer(16);
        FloatBuffer fbM = BufferUtils.createFloatBuffer(16);

        glUniformMatrix4fv(sp.u("P"),false,P.get(fbP));
        glUniformMatrix4fv(sp.u("V"),false,V.get(fbV));
        glUniformMatrix4fv(sp.u("M"),false,M.get(fbM));

        glEnableVertexAttribArray(sp.a("vertex"));
        glVertexAttribPointer(sp.a("vertex"),4,GL_FLOAT,false,0,cube.fbVertex);

        glEnableVertexAttribArray(sp.a("normal"));
        glVertexAttribPointer(sp.a("normal"),4,GL_FLOAT,false,0,cube.fbNormals);

        glEnableVertexAttribArray(sp.a("color"));
        glVertexAttribPointer(sp.a("color"),4,GL_FLOAT,false,0,cube.fbColors);

        glDrawArrays(GL_TRIANGLES, 0, cube.myCubeVertexCount);

        glDisableVertexAttribArray(sp.a("vertex"));
        glDisableVertexAttribArray(sp.a("normal"));
        glDisableVertexAttribArray(sp.a("color"));

        GL30.glBindVertexArray(0);

        glfwSwapBuffers(window);
    }

    private void loop() {



        GL.createCapabilities();

        glClearColor(0.0f, 0.3f, 0.4f, 0.0f);

        sp.load();
        initBuffer();

        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);

        float angle_x=0; //Aktualny kąt obrotu obiektu
        float angle_y=0; //Aktualny kąt obrotu obiektu
        glfwSetTime(0); //Zeruj timer

        while ( !glfwWindowShouldClose(window) ) {
            angle_x+=speed_x*glfwGetTime(); //Zwiększ/zmniejsz kąt obrotu na podstawie prędkości i czasu jaki upłynał od poprzedniej klatki
            angle_y+=speed_y*glfwGetTime(); //Zwiększ/zmniejsz kąt obrotu na podstawie prędkości i czasu jaki upłynał od poprzedniej klatki
            glfwSetTime(0); //Zeruj timer
            drawScene(window,angle_x,angle_y); //Wykonaj procedurę rysującą
            glfwPollEvents();

        }
    }

    public void initBuffer(){

        // Create a new Vertex Array Object in memory and select it (bind)
        // A VAO can have up to 16 attributes (VBO's) assigned to it by default
        vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);

        // Create a new Vertex Buffer Object in memory and select it (bind)
        // A VBO is a collection of Vectors which in this case resemble the location of each vertex.
        vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, cube.fbVertex, GL15.GL_STATIC_DRAW);
        // Put the VBO in the attributes list at index 0
        GL20.glVertexAttribPointer(sp.a("vertex"), 4, GL11.GL_FLOAT, false, 0, 0);
        // Deselect (bind to 0) the VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);



        vboId1 = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId1);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, cube.fbNormals, GL15.GL_STATIC_DRAW);
        // Put the VBO in the attributes list at index 0
        GL20.glVertexAttribPointer(sp.a("normal"), 4, GL11.GL_FLOAT, false, 0, 0);
        // Deselect (bind to 0) the VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);



        vboId2 = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId2);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, cube.fbColors, GL15.GL_STATIC_DRAW);
        // Put the VBO in the attributes list at index 0
        GL20.glVertexAttribPointer(sp.a("color"), 4, GL11.GL_FLOAT, false, 0, 0);
        // Deselect (bind to 0) the VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        // Deselect (bind to 0) the VAO
        GL30.glBindVertexArray(0);
    }

    public static void main(String[] args) {
        new Main().run();
    }

}