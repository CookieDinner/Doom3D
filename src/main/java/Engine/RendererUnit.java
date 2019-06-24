package Engine;

import Entities.*;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

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

    private ShaderProgram shader1;
    private ShaderProgram shader2;

    //Models declarations

    private Model gun;
    private Model cube;
    private Model ground;
    private Model skybox;
//    private Model dragon;
    private Model hudgun;
    private Model enemy;
    private Model heart;

    private Map map = new Map();

    //private Entity wall;

//    private Entity cubic;
    private LiveEntity dragon;
    private ArrayList<Enemy> enemiesList = new ArrayList<>();
    private ArrayList<Entity> playersHealth = new ArrayList<>();
    private Player player;

    private CollisionUnit collisionUnit;
    private Lights lights;


    public RendererUnit(CollisionUnit collision, Player player) {
        collisionUnit = collision;
        this.player = player;
    }

    public void initBuffer() throws Exception {

        shader1 = new ShaderProgram();
        shader1.createVertexShader("vertex.glsl");
        shader1.createFragmentShader("fragment.glsl");

        shader2 = new ShaderProgram();
        shader2.createVertexShader("vertex_simp.glsl");
        shader2.createFragmentShader("fragment_simp.glsl");

        shader1.link();
        shader2.link();

        lights = new Lights(shader1);

        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);

        //Culling the faces
        //glCullFace(GL_BACK);
        //glEnable(GL_CULL_FACE);


        gun = new Model(shader1, shader2, "handgun.obj", "guntex.png","gundiffuse.png","darksky.png");
        hudgun = new Model(shader1, shader2, "handgun.obj", "guntex.png","gundiffuse.png","black.png");
        cube = new Model(shader1, shader2,"cube.obj", "metal.png", "metaldiffuse.png","sky.png");
        skybox = new Model(shader2, shader2,"skybox.obj", "skybox.png","black.png","black.png");
        ground = new Model(shader1, shader2,"ground.obj", "metal_floor.png","metal_floor_diffuse.png","black.png");
        enemy = new Model(shader1, shader2,"alien.obj", "alien_tex.png", "alien_diffuse.png","black.png");
        heart = new Model(shader1, shader2, "heart.obj", "heart.png", "black.png", "black.png");


        /*dragon =new LiveEntity(300,300, new Model(shader1, shader2,"dragon.obj",
                        "dragon.png","dragondiffuse.png","black.png"), 1000,100);
        collisionUnit.addToList(dragon);*/

        enemiesList.add(new Enemy(-50,-50,enemy,100,20));
        enemiesList.add(new Enemy(50,50,enemy,100,20));

        for(Enemy i: enemiesList){
            collisionUnit.addToList(i);
        }

        playersHealth.add(new Entity(-7.75f,-10,heart));
        playersHealth.add(new Entity(-6.75f,-10,heart));
        playersHealth.add(new Entity(-5.75f,-10,heart));
        playersHealth.add(new Entity(-4.75f,-10,heart));

        map.load(collisionUnit,shader1,shader2);

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

    public void render(Window window, float angle_x, float angle_y, Vector3f camPos, Vector3f camFront, Vector3f camUp, Vector3f camRight, int mouseButton){
        clearBuffers();
        glClearColor(0.6f, 0.2f, 0.5f, 1.0f);
        glfwSetInputMode(window.getWindowHandle(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        int texNumber = 3;

        if (window.isResized()) {
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        V.identity().lookAt(camPos, new Vector3f().set(camPos).add(camFront), camUp);
        P.identity().perspective(50.0f*(float)Math.PI/180.0f, window.getScreenRatio(),0.01f,500000.0f);


        //Binding the specific shader before drawing an object that uses it
        //Drawing the lights and initializing them in the shader at the same time
        lights.drawLights(P, V);
        //glBindVertexArray(vaoId);


        /*M.identity().translate(dragon.getPosX(),-9.50f,dragon.getPosZ()).rotate(-3.14f/2,new Vector3f(0.0f,1.0f,0.0f)).scale(20.0f,20.0f,20.0f);
        dragon.getModel().draw(M,V,P,texNumber);
        dragon.updateCurrentVectors(new Matrix4f().identity()
                .translate(dragon.getPosX(),-9.50f,dragon.getPosZ())
//                .rotate(-3.14f/2,new Vector3f(0.0f,1.0f,0.0f))
                .scale(20.0f,20.0f,20.0f));
        texNumber+=3;*/

        M.identity();
        map.draw(shader1, shader2,M,V,P);


        M.identity().translate(5,10,6).rotate(angle_x,new Vector3f(0f,1.0f,0f)).rotate(angle_y,new Vector3f(1.0f,0f,0f));
        Matrix4f Mtemp = M;
        cube.draw(M,V,P,texNumber); //1
        texNumber+=3;


        M = Mtemp;
        M.translate(0.7f,4.8f,2.5f);
        Mtemp = M;
        M.scale(0.2f,0.2f,0.2f);
        gun.draw(M, V, P, texNumber); //2
        texNumber+=3;


        M = Mtemp;
        M.translate(-12.0f,0.0f,71.0f).rotate(3.14f, new Vector3f(0.0f,1.0f,0.0f));
        gun.draw(M,V,P, texNumber); //2
        texNumber+=3;


        for(Enemy enemy: enemiesList){
            float oldX = enemy.getPosX() , oldZ = enemy.getPosZ();
            enemy.moveInPlayerDirection(player,0.4f);

            collisionUnit.abandonMovingChangesWhenDetectedCollision(enemy,oldX,oldZ);

            enemy.checkIfEntityDied();
            enemy.setToPlayerVector(player);

            M.identity()
                    .translate(enemy.getPosX(),1.0f,enemy.getPosZ())
                    .rotate((float) Math.atan2(enemy.getToPlayerVector().x,enemy.getToPlayerVector().z) -0.4f ,0,1,0)
                    .scale(1.3f,1.3f,1.3f);
            enemy.getModel().draw(M,V,P, texNumber); //2



            //            System.out.println("++++++++PRZED EDYCJA");
//            System.out.println(enemy.getDefaultMinCollisionBox());
//            System.out.println(enemy.getDefaultMaxCollisionBox());

            enemy.updateCurrentVectors((new Matrix4f().identity()
                    .translate(enemy.getPosX(),0.0f,enemy.getPosZ())
//                    .rotate(-3.14f/6,new Vector3f(0.0f,1.0f,0.0f))
                    .scale(1.3f) //todo poprawic collision boxy
            ));
//
//            System.out.println("++++++++PO EDYCJI");
//            enemy.debugVectors();
//            System.out.println("POZYCJA PLAYERA ----->>>>>"+ camPos);

        }



        // GROUND AND THE SKYBOX DRAWN AT THE END
        M.identity().translate(0,0,0);
        M.scale(60.0f,1.0f,60.0f);
        ground.draw(M,V,P, texNumber); //1
        texNumber+=3;


        M.identity().rotate(3.14f,new Vector3f(0.0f,0.0f,1.0f)).translate(0.0f,-6000.0f,0.0f).scale(15000.0f,15000.0f,15000.0f);
        skybox.draw(M,V,P, texNumber); //1
        texNumber+=3;



        // HERE ALL OF THE HUD ELEMENTS WILL BE RENDERED (INCLUDING THE GUN)
        // Those parts absolutely have to be put at the end, because we are completely clearing the View Matrix
        M.identity();
        V.identity().translate(-1.75f,-8.0f,-12.0f).rotate(-3.14f, new Vector3f(0.0f,1.0f,0.0f));//.rotate(-3.14f/3, new Vector3f(1.0f,0.0f,0.0f));
        V.scale(0.3f,0.3f,0.3f);
        // Clearing all of the depth information in the depth buffers so that there are no intersections of the HUD with the ingame objects
        glClear(GL_DEPTH_BUFFER_BIT);

        if(player.isShowShootAnimation() && mouseButton == GLFW_MOUSE_BUTTON_LEFT){
            V.rotate(-3.14f/14, new Vector3f(1.0f,0.0f,0.0f));
            float theClosestEnemy = Float.MAX_VALUE;
            int whichEnemyIsTheClosest=-1, i=0;

            for (Enemy enemy: enemiesList){


                float distance = Utils.distance2DBetween2Points(enemy.getPosX(),enemy.getPosZ(),
                        player.getPosX(),player.getPosZ());

                float vectorsMultiplication = enemy.getToPlayerVector().dot(player.getLookAheadVector());
                if (player.isEnemyInsideGunViewfinder(distance,vectorsMultiplication)){
                    if (theClosestEnemy>distance){
                        theClosestEnemy = distance;
                        whichEnemyIsTheClosest = i;
                    }
                }

                i++;
            }

            if (player.isCanShoot() && whichEnemyIsTheClosest!=-1) {
                enemiesList.get(whichEnemyIsTheClosest).receiveDamage(player.getDamage());
                player.setCanShoot(false);
            }

        }
        hudgun.draw(M,V,P,texNumber);
        texNumber+=3;



        M.identity();
        for (int i=0;i< player.getHealth()/25;i++){
            Entity heart = playersHealth.get(i);
            V.identity().translate(heart.getPosX(),-4.5f,heart.getPosZ());
            V.scale(0.25f,0.25f,0.25f);
            heart.getModel().draw(M,V,P,texNumber);
        }




        //glBindVertexArray(0);

    }

    public void clearBuffers() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup() {
        if (shader1 != null) {
            shader1.cleanup();
        }
        if (shader2 != null) {
            shader2.cleanup();
        }
        for (int i = 0; i < 100; i++)
            glDisable(GL_TEXTURE0+i);

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
