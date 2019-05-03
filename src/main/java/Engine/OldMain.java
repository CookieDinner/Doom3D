package Engine;

import Entities.MyCube;
import org.joml.*;
import org.lwjgl.*;
import org.lwjgl.opengl.*;

import java.lang.Math;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;


public class OldMain implements FileLoader {

    public void run() {

        System.out.println("Hello LWJGL " + Version.getVersion() + "!");
    }

    private void drawScene(long window, float angle_x, float angle_y){

//        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//
//        sp.use();
//        GL30.glBindVertexArray(vaoId);
//
//        V.identity().lookAt(
//                new Vector3f(0,0,-5),
//                new Vector3f(0,0,0),
//                new Vector3f(0,1,0));
//        P.identity().perspective(50.0f*(float)Math.PI/180.0f, aspectRatio,0.01f,50.0f);
//        M.identity().rotate(angle_x,new Vector3f(0f,1.0f,0f),M).rotate(angle_y,new Vector3f(1.0f,0f,0f),M);
//
//
//
//        FloatBuffer fbP = BufferUtils.createFloatBuffer(16);
//        FloatBuffer fbV = BufferUtils.createFloatBuffer(16);
//        FloatBuffer fbM = BufferUtils.createFloatBuffer(16);
//
//        GL20.glUniformMatrix4fv(sp.u("P"),false,P.get(fbP));
//        GL20.glUniformMatrix4fv(sp.u("V"),false,V.get(fbV));
//        GL20.glUniformMatrix4fv(sp.u("M"),false,M.get(fbM));
//
//        glEnableVertexAttribArray(sp.a("vertex"));
//        GL20.glVertexAttribPointer(sp.a("vertex"),4,GL_FLOAT,false,0,cube.fbVertex);
//
//        glEnableVertexAttribArray(sp.a("normal"));
//        GL20.glVertexAttribPointer(sp.a("normal"),4,GL_FLOAT,false,0,cube.fbNormals);
//
//        glEnableVertexAttribArray(sp.a("color"));
//        GL20.glVertexAttribPointer(sp.a("color"),4,GL_FLOAT,false,0,cube.fbColors);
//
//        glDrawArrays(GL_TRIANGLES, 0, cube.myCubeVertexCount);
//
//        glDisableVertexAttribArray(sp.a("vertex"));
//        glDisableVertexAttribArray(sp.a("normal"));
//        glDisableVertexAttribArray(sp.a("color"));
//
//        GL30.glBindVertexArray(0);
//
//        glfwSwapBuffers(window);
    }

    private void loop() {


//
//        GL.createCapabilities();
//
//        glClearColor(0.0f, 0.3f, 0.4f, 0.0f);
//
//        sp.load();
//        initBuffer();
//
//        glEnable(GL_DEPTH_TEST);
//        glDepthFunc(GL_LESS);
//
//        float angle_x=0; //Aktualny kąt obrotu obiektu
//        float angle_y=0; //Aktualny kąt obrotu obiektu
//        glfwSetTime(0); //Zeruj timer
//
//        while ( !glfwWindowShouldClose(window) ) {
//            angle_x+=speed_x*glfwGetTime(); //Zwiększ/zmniejsz kąt obrotu na podstawie prędkości i czasu jaki upłynał od poprzedniej klatki
//            angle_y+=speed_y*glfwGetTime(); //Zwiększ/zmniejsz kąt obrotu na podstawie prędkości i czasu jaki upłynał od poprzedniej klatki
//            glfwSetTime(0); //Zeruj timer
//            drawScene(window,angle_x,angle_y); //Wykonaj procedurę rysującą
//            glfwPollEvents();
//        }
    }

    public void initBuffer(){


    }

    public static void main(String[] args) {
        new OldMain().run();
    }

}