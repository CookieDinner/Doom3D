package Engine;

import lombok.Getter;
import lombok.Setter;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryUtil.NULL;

@Getter
@Setter
public class Window {
    private final String title;

    private int width;

    private int height;

    private long windowHandle;

    private boolean resized;

    private boolean vSync;

    public Window(String title, int width, int height, boolean vSync) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
        this.resized = false;
    }


    public void init(){
        GLFWErrorCallback.createPrint(System.err).set();

        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_SAMPLES, 4);

        // This is optional. It forces program to use the newest OpenGL - necessary for MAC
//        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
//        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
//        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
//        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        if ( windowHandle == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup resize callback
        glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) -> {
            this.width = width;
            this.height = height;
            this.setResized(true);
        });

        //Callback on keyboard
//        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
//            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
//                glfwSetWindowShouldClose(window, true);
//            if (action==GLFW_PRESS) {
//                if (key==GLFW_KEY_LEFT) speed_x=-PI/2;
//                if (key==GLFW_KEY_RIGHT) speed_x=PI/2;
//                if (key==GLFW_KEY_UP) speed_y=PI/2;
//                if (key==GLFW_KEY_DOWN) speed_y=-PI/2;
//            }
//            if (action==GLFW_RELEASE) {
//                if (key==GLFW_KEY_LEFT) speed_x=0;
//                if (key==GLFW_KEY_RIGHT) speed_x=0;
//                if (key==GLFW_KEY_UP) speed_y=0;
//                if (key==GLFW_KEY_DOWN) speed_y=0;
//            }
//        });


        // Get the resolution of the primary monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(
                windowHandle,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        );

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowHandle);

        if (vSync==true) {
            // Enable v-sync
            glfwSwapInterval(1);
        }
        else
            glfwSwapInterval(0);

        // Make the window visible
        glfwShowWindow(windowHandle);


        //Very important to invoke this
        GL.createCapabilities();

        // Set the clearBuffers color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

    }

    public void closeWindow(){
        glfwSetWindowShouldClose(windowHandle, true);
    }

    public void destroyAndClean(){
        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }


    public void update() {
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
    }

    public void setBackgroundColor(float r, float g, float b, float alpha) {
        glClearColor(r, g, b, alpha);
    }

    public boolean isKeyPressed(int keyCode) {
        return glfwGetKey(windowHandle, keyCode) == GLFW_PRESS;
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(windowHandle);
    }

    public float getScreenRatio(){
        return (float)width/height;
    }

}
