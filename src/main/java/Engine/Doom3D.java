package Engine;

import lombok.Setter;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWCursorPosCallback;

import static org.lwjgl.glfw.GLFW.*;

@Setter
public class Doom3D implements GameLogicInterface {

    private final float PI =3.14f;

    private float speed_x=0;

    private float speed_y=0;

    private float angle_x=0; //Aktualny kąt obrotu obiektu

    private float angle_y=0; //Aktualny kąt obrotu obiektu

    private float movementSpeed = 20.0f;

    private double lastMouseX, lastMouseY;
    private double offsetX, offsetY;
    private double yaw, pitch;

    private boolean first = true;

    Vector3f camPos = new Vector3f(0.0f, 10.0f, 20.0f);
    Vector3f camFront = new Vector3f(1.0f, 0.0f, 0.0f);
    Vector3f camUp = new Vector3f(0.0f, 1.0f, 0.0f);
    Vector3f camRight = new Vector3f().set(camFront).cross(camUp).normalize();


    private float xang, yang;



    private final RendererUnit rendererUnit;

    public Doom3D() {
        rendererUnit = new RendererUnit();
    }

    @Override
    public void init(Window window) throws Exception {
        rendererUnit.initBuffer();
        //Mouse callback function gets invoked every time there is a mouse event
        glfwSetCursorPosCallback(window.getWindowHandle(), new GLFWCursorPosCallback(){
            @Override
            public void invoke(long win, double mouseX, double mouseY) {

                if (first){
                    lastMouseX = mouseX;
                    lastMouseY = mouseY;
                    first = false;
                }
                offsetX = mouseX - lastMouseX;
                offsetY = lastMouseY - mouseY;
                lastMouseX = (float)mouseX;
                lastMouseY = (float)mouseY;
                double mouse_sensitivity = 0.15f;
                offsetX *= mouse_sensitivity;
                offsetY *= mouse_sensitivity;

                yaw += offsetX;
                pitch += offsetY;

                if(pitch > 89.0f)
                    pitch = 89.0f;
                if(pitch < -89.0f)
                    pitch = -89.0f;

                Vector3f front = new Vector3f(
                        (float)(Math.cos(Math.toRadians(yaw))*Math.cos(Math.toRadians(pitch))),
                        (float)(Math.sin(Math.toRadians(pitch))),
                        (float)(Math.sin(Math.toRadians(yaw))*Math.cos(Math.toRadians(pitch))));
                front.normalize();
                camFront.set(front);

            }
        });
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

        //Because of the camFront vector getting close to 0 while looking 90 degrees up or down it's hard to get any kind of forward/backwards movement
        //Therefore we are recalculating the camRight vector on every frame, then taking the cross product of camRight and camUp vector
        //and using that as the direction vector instead

        camRight.set(camFront).cross(camUp).normalize();
            //W
        if (window.isKeyPressed(GLFW_KEY_W) && !window.isKeyPressed(GLFW_KEY_D) && !window.isKeyPressed(GLFW_KEY_A) && !window.isKeyPressed(GLFW_KEY_S)) {
            camPos.x -= new Vector3f().set(camRight).cross(camUp).normalize().x * (float) glfwGetTime() * movementSpeed;
            camPos.z -= new Vector3f().set(camRight).cross(camUp).normalize().z * (float) glfwGetTime() * movementSpeed;
        }
            //W+D
        if (window.isKeyPressed(GLFW_KEY_W) && window.isKeyPressed(GLFW_KEY_D) && !window.isKeyPressed(GLFW_KEY_S)) {
            camPos.x -= new Vector3f().set(camRight).cross(camUp).sub(camRight).normalize().x * (float) glfwGetTime() * movementSpeed;
            camPos.z -= new Vector3f().set(camRight).cross(camUp).sub(camRight).normalize().z * (float) glfwGetTime() * movementSpeed;
        }
            //W+A
        if (window.isKeyPressed(GLFW_KEY_W) && window.isKeyPressed(GLFW_KEY_A) && !window.isKeyPressed(GLFW_KEY_S)) {
            camPos.x -= new Vector3f().set(camRight).cross(camUp).add(camRight).normalize().x * (float) glfwGetTime() * movementSpeed;
            camPos.z -= new Vector3f().set(camRight).cross(camUp).add(camRight).normalize().z * (float) glfwGetTime() * movementSpeed;
        }
            //S
        if (window.isKeyPressed(GLFW_KEY_S) && !window.isKeyPressed(GLFW_KEY_D) && !window.isKeyPressed(GLFW_KEY_A) && !window.isKeyPressed(GLFW_KEY_W)) {
            camPos.x += new Vector3f().set(camRight).cross(camUp).normalize().x * (float) glfwGetTime() * movementSpeed;
            camPos.z += new Vector3f().set(camRight).cross(camUp).normalize().z * (float) glfwGetTime() * movementSpeed;
        }
            //S+D
        if (window.isKeyPressed(GLFW_KEY_S) && window.isKeyPressed(GLFW_KEY_D) && !window.isKeyPressed(GLFW_KEY_W)) {
            camPos.x += new Vector3f().set(camRight).cross(camUp).add(camRight).normalize().x * (float) glfwGetTime() * movementSpeed;
            camPos.z += new Vector3f().set(camRight).cross(camUp).add(camRight).normalize().z * (float) glfwGetTime() * movementSpeed;
        }
            //S+A
        if (window.isKeyPressed(GLFW_KEY_S) && window.isKeyPressed(GLFW_KEY_A) && !window.isKeyPressed(GLFW_KEY_W)) {
            camPos.x += new Vector3f().set(camRight).cross(camUp).sub(camRight).normalize().x * (float) glfwGetTime() * movementSpeed;
            camPos.z += new Vector3f().set(camRight).cross(camUp).sub(camRight).normalize().z * (float) glfwGetTime() * movementSpeed;
        }
            //D
        if (window.isKeyPressed(GLFW_KEY_D) && !window.isKeyPressed(GLFW_KEY_W) && !window.isKeyPressed(GLFW_KEY_S) && !window.isKeyPressed(GLFW_KEY_A)) {
            camPos.add(new Vector3f().set(camRight).mul((float) glfwGetTime() * movementSpeed));
        }
            //A
        if (window.isKeyPressed(GLFW_KEY_A) && !window.isKeyPressed(GLFW_KEY_W) && !window.isKeyPressed(GLFW_KEY_S) && !window.isKeyPressed(GLFW_KEY_D)) {
            camPos.sub(new Vector3f().set(camRight).mul((float) glfwGetTime() * movementSpeed));
        }
            //UP
        if (window.isKeyPressed(GLFW_KEY_SPACE) && !window.isKeyPressed(GLFW_KEY_LEFT_CONTROL))
            camPos.add(new Vector3f().set(camUp).normalize().mul((float)glfwGetTime()*movementSpeed));
            //DOWN
        if (window.isKeyPressed(GLFW_KEY_LEFT_CONTROL) && !window.isKeyPressed(GLFW_KEY_SPACE))
            camPos.sub(new Vector3f().set(camUp).normalize().mul((float)glfwGetTime()*movementSpeed));

    }

    @Override
    public void update() {
    }



    @Override
    public void render(Window window){
        angle_x+=speed_x*glfwGetTime(); //Zwiększ/zmniejsz kąt obrotu na podstawie prędkości i czasu jaki upłynał od poprzedniej klatki
        angle_y+=speed_y*glfwGetTime(); //Zwiększ/zmniejsz kąt obrotu na podstawie prędkości i czasu jaki upłynał od poprzedniej klatki

        glfwSetTime(0); //Zeruj timer
        rendererUnit.render(window, angle_x, angle_y, camPos, camFront, camUp);
    }

    @Override
    public void cleanup() {
        rendererUnit.cleanup();
    }
}