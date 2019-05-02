import org.joml.*;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.lang.Math;
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
                if (key==GLFW_KEY_LEFT) glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
                if (key==GLFW_KEY_RIGHT) glClearColor(0.0f, 1.0f, 0.0f, 0.0f);
                if (key==GLFW_KEY_UP) glClearColor(0.0f, 0.0f, 1.0f, 0.0f);
                if (key==GLFW_KEY_DOWN) glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
            }
            if (action==GLFW_RELEASE) {
                if (key==GLFW_KEY_LEFT) glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                if (key==GLFW_KEY_RIGHT) glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                if (key==GLFW_KEY_UP) glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                if (key==GLFW_KEY_DOWN) glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
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
        V.lookAt(
                new Vector3f(0,0,-5),
                new Vector3f(0,0,0),
                new Vector3f(0,1,0));
        P.perspective(50.0f*(float)Math.PI/180.0f, aspectRatio,0.01f,50.0f);
        M.identity();
        sp.use();
        //glUniformMatrix4fv(sp.u("P"),
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    private void loop() {



        GL.createCapabilities();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        while ( !glfwWindowShouldClose(window) ) {
            drawScene(window, 0, 0 );
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }

}