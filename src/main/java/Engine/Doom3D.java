package Engine;

import lombok.Setter;

import static org.lwjgl.glfw.GLFW.*;

@Setter
public class Doom3D implements GameLogicInterface {

    private final float PI =3.14f;

    private float speed_x=0;

    private float speed_y=0;

    private float angle_x=0; //Aktualny kąt obrotu obiektu

    private float angle_y=0; //Aktualny kąt obrotu obiektu

    private final RendererUnit rendererUnit;

    public Doom3D() {
        rendererUnit = new RendererUnit();
    }

    @Override
    public void init() throws Exception {
        rendererUnit.initBuffer();
    }

    @Override
    public void input(Window window) {
        if ( window.isKeyPressed(GLFW_KEY_ESCAPE)) window.closeWindow();
        if (window.isKeyPressed(GLFW_KEY_LEFT)) speed_x=-PI/2;
        else if (window.isKeyPressed(GLFW_KEY_RIGHT)) speed_x=PI/2;
        else speed_x=0;
        if (window.isKeyPressed(GLFW_KEY_UP)) speed_y=PI/2;
        else if (window.isKeyPressed(GLFW_KEY_DOWN)) speed_y=-PI/2;
        else speed_y=0;
    }

    @Override
    public void update() {
    }



    @Override
    public void render(Window window) {
        angle_x+=speed_x*glfwGetTime(); //Zwiększ/zmniejsz kąt obrotu na podstawie prędkości i czasu jaki upłynał od poprzedniej klatki
        angle_y+=speed_y*glfwGetTime(); //Zwiększ/zmniejsz kąt obrotu na podstawie prędkości i czasu jaki upłynał od poprzedniej klatki
        glfwSetTime(0); //Zeruj timer
        rendererUnit.render(window,angle_x,angle_y);
    }

    @Override
    public void cleanup() {
        rendererUnit.cleanup();
    }
}
