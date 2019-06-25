package Engine;

import Entities.Player;
import Entities.Sound;
import lombok.Setter;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import static org.lwjgl.glfw.GLFW.*;

@Setter
public class Doom3D implements GameLogicInterface {

    private final float PI =3.14f;

    private float speed_x=0;

    private float speed_y=0;

    private float angle_x=0; //Aktualny kąt obrotu obiektu

    private float angle_y=0; //Aktualny kąt obrotu obiektu

    private float baseMovementSpeed = 40.0f;
    private float movementSpeed = baseMovementSpeed;

    private double lastMouseX, lastMouseY;
    private double offsetX, offsetY;
    private double yaw, pitch;

    private Player player;
    Vector3f spawnPoint = new Vector3f(-35.0f, 17.0f, 5.0f);
    Vector3f camPos = new Vector3f().set(spawnPoint);
    Vector3f camFront = new Vector3f(1.0f, -0.09f, 0.0f);
    Vector3f camUp = new Vector3f(0.0f, 1.0f, 0.0f);
    Vector3f camRight = new Vector3f().set(camFront).cross(camUp).normalize();
    private int mouseButton = 99;


    private final RendererUnit rendererUnit;
    private final CollisionUnit collisionUnit;
    private int noSound =0;
    private int upperBoundOfDelay = 10;
    private int delayForHurtingPLayer;
    private int delayForShootingEnemies;
    private int counterForRotating =0;
    private boolean first = true;
    private int counterBound =60;

    public Doom3D() {
        player = new Player(camPos.x,camPos.z,null,-10,10,-10,10,100,30);
        collisionUnit = new CollisionUnit(player);
        rendererUnit = new RendererUnit(collisionUnit,player);
        delayForHurtingPLayer = upperBoundOfDelay;
        delayForShootingEnemies = upperBoundOfDelay;
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
                //pitch += offsetY;

                /*if(pitch > 89.0f)
                    pitch = 89.0f;
                if(pitch < -89.0f)
                    pitch = -89.0f;*/
                pitch = -5.0f;

                Vector3f front = new Vector3f(
                        (float)(Math.cos(Math.toRadians(yaw))*Math.cos(Math.toRadians(pitch))),
                        (float)(Math.sin(Math.toRadians(pitch))),
                        (float)(Math.sin(Math.toRadians(yaw))*Math.cos(Math.toRadians(pitch))));
                front.normalize();
                camFront.set(front);

            }
        });
        glfwSetMouseButtonCallback(window.getWindowHandle(), new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if(button == GLFW_MOUSE_BUTTON_LEFT && action == 1) {
                    mouseButton = GLFW_MOUSE_BUTTON_LEFT;
                }
                if(button == GLFW_MOUSE_BUTTON_LEFT && action == 0) {
                    mouseButton = 99;
                }
            }
        });
    }

    @Override
    public void input(Window window) {
        Vector3f camPosBeforeChange = new Vector3f(camPos.x, camPos.y, camPos.z);


        movementSpeed = baseMovementSpeed;
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
        if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT))
            movementSpeed+= 50.0f;

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




        // Bardzo WAZNA LINIJKA !!! Sprawdza czy którys z modeli nie przeszkadza playerowi.

        player.move(camPos.x,camPos.z);
        collisionUnit.abandonMovingChangesWhenDetectedCollision(player,camPosBeforeChange.x,camPosBeforeChange.z);
        //todo byc moze tylko najblizszy bo obecnie pierwszy z listy


        if (delayForHurtingPLayer == upperBoundOfDelay && !player.isCanBeHurt()) {
            player.setCanBeHurt(true);
            delayForHurtingPLayer = 0;
        }



        if (noSound<10) noSound++;
        if (player.checkIfEntityDied(100,null,null) || window.isKeyPressed(GLFW_KEY_P)){

            if (noSound == 10) {
                new Thread(new Sound("player_death1.wav",0,0)).start();
//              new Sound("player_death1.wav",0,0).playWAV();
                noSound=0;
            }


            player.move(spawnPoint.x, spawnPoint.z);
            yaw = 0.0f;
            camFront.set(1.0f,-0.09f,0.0f);
        }
        camPos.x = player.getPosX();
        camPos.z = player.getPosZ();

        player.setLookAheadVector(camUp,camRight);

        if (!player.isCanBeHurt() && delayForHurtingPLayer < upperBoundOfDelay) delayForHurtingPLayer++;


    }

    @Override
    public void update() {
    }



    @Override
    public void render(Window window){
        angle_x+=speed_x*glfwGetTime(); //Zwiększ/zmniejsz kąt obrotu na podstawie prędkości i czasu jaki upłynał od poprzedniej klatki
        angle_y+=speed_y*glfwGetTime(); //Zwiększ/zmniejsz kąt obrotu na podstawie prędkości i czasu jaki upłynał od poprzedniej klatki

        glfwSetTime(0); //Zeruj timer

        if (!player.isCanShoot() && delayForShootingEnemies == upperBoundOfDelay) {
            player.setCanShoot(true);
            player.setShowShootAnimation(true);
            delayForShootingEnemies = 0;
        }

        if (!player.isCanShoot() && delayForShootingEnemies < upperBoundOfDelay) delayForShootingEnemies++;
        if (delayForShootingEnemies == upperBoundOfDelay/4) player.setShowShootAnimation(false);


        rendererUnit.render(window, angle_x, angle_y, camPos, camFront, camUp, camRight, mouseButton, counterForRotating, counterBound);
        if(counterForRotating<counterBound) counterForRotating++;
        else if (counterForRotating==counterBound   ) counterForRotating=0;
    }

    @Override
    public void cleanup() {
        rendererUnit.cleanup();
    }
}
