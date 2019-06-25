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
    private ShaderProgram shader3;

    //Models declarations

    private Model gun;
    private Model cube;
    private Model ground;
    private Model skybox;
    private Model hudgun;
    private Model enemy;
    private Model heart;
    private Model shot;
    private Model blood;
    private Model splat;
    private ArrayList<Entity> deathspots = new ArrayList<>();
    private Map map = new Map();
    private LiveEntity dragon;
    private ArrayList<Enemy> enemiesList = new ArrayList<>();
    private ArrayList<Entity> playersHealth = new ArrayList<>();
    private Player player;
    private ArrayList<BonusHealthItem> bonusHearts = new ArrayList<>();

    private CollisionUnit collisionUnit;
    private Lights lights;
    private int noSound=0;


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

        shader3 = new ShaderProgram();
        shader3.createVertexShader("blvertex.glsl");
        shader3.createFragmentShader("blfragment.glsl");

        shader1.link();
        shader2.link();
        shader3.link();

        lights = new Lights(shader1);

        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);

        //Culling the faces
        glEnable(GL_CULL_FACE);


        gun = new Model(shader1, shader2, "handgun.obj", "guntex.png","gundiffuse.png","darksky.png");
        hudgun = new Model(shader1, shader2, "handgun.obj", "guntex.png","gundiffuse.png","black.png");
        cube = new Model(shader1, shader2,"cube.obj", "metal.png", "metaldiffuse.png","sky.png");
        skybox = new Model(shader2, shader2,"skybox.obj", "skybox.png","black.png","black.png");
        ground = new Model(shader1, shader2,"ground.obj", "metal_floor.png","metal_floor_diffuse.png","black.png");
        enemy = new Model(shader1, shader2,"alien.obj", "alien_tex.png", "alien_diffuse.png","black.png");
        heart = new Model(shader1, shader2, "heart.obj", "heart.png", "black.png", "black.png");
        shot = new Model(shader2, shader2, "shot.obj","shot.png","black.png","black.png");
        blood = new Model(shader1, shader2, "blood.obj","blood.png","black.png","black.png");
        splat = new Model(shader3, shader3, "bloodsplat.obj", "bloodsplat.png","bloodsplat.png","black.png");



        /*dragon =new LiveEntity(300,300, new Model(shader1, shader2,"dragon.obj",
                        "dragon.png","dragondiffuse.png","black.png"), 1000,100);
        collisionUnit.addToList(dragon);*/

        bonusHearts.add(new BonusHealthItem(107,315,heart));
        bonusHearts.add(new BonusHealthItem(110,550,heart));
        bonusHearts.add(new BonusHealthItem(511,682,heart));




        enemiesList.add(new Enemy(42,248,enemy,90,20));
        enemiesList.add(new Enemy(243,270,enemy,90,20));
        enemiesList.add(new Enemy(446,84,enemy,90,20));
        enemiesList.add(new Enemy(430,465,enemy,90,20));
        enemiesList.add(new Enemy(87,700,enemy,90,20));
        enemiesList.add(new Enemy(261,755,enemy,90,20));
        enemiesList.add(new Enemy(700,1500,enemy,90,20));
        enemiesList.add(new Enemy(582,1500,enemy,90,20));
        enemiesList.add(new Enemy(466,1500,enemy,90,20));
        enemiesList.add(new Enemy(350,1500,enemy,90,20));



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

    public void render(Window window, float angle_x, float angle_y, Vector3f camPos, Vector3f camFront,
                       Vector3f camUp, Vector3f camRight, int mouseButton, int counter, int counterBound){

        clearBuffers();
        glClearColor(0.6f, 0.2f, 0.5f, 1.0f);
        glfwSetInputMode(window.getWindowHandle(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);


        int texNumber = 3;

        if (window.isResized()) {
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

//        player.debugPosition();

        V.identity().lookAt(camPos, new Vector3f().set(camPos).add(camFront), camUp);
        P.identity().perspective(50.0f*(float)Math.PI/180.0f, window.getScreenRatio(),0.01f,500000.0f);


        //Binding the specific shader before drawing an object that uses it
        //Drawing the lights and initializing them in the shader at the same time
        lights.drawLights(shader1,P, V);
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
        cube.draw(M,V,P,texNumber); //1
        texNumber+=3;

        Matrix4f Mtemp = M;
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
            float scaleEnemiesSize = 1.5f;
            float oldX = enemy.getPosX() , oldZ = enemy.getPosZ();

            enemy.setToPlayerVector(player);
            enemy.updateDistanceToPLayer(player);
//            enemy.moveInPlayerDirection(player,1.2f);
            collisionUnit.abandonMovingChangesWhenDetectedCollision(enemy,oldX,oldZ);

            enemy.checkIfEntityDied(90, deathspots, splat);
            enemy.respawnIfTimeExceeded();

            M.identity()
                    .translate(enemy.getPosX(),1.0f,enemy.getPosZ())
                    .rotate((float) Math.atan2(enemy.getToPlayerVector().x,enemy.getToPlayerVector().z) -0.4f ,0,1,0)
                    .scale(scaleEnemiesSize);
            enemy.getModel().draw(M,V,P, texNumber); //2


            enemy.updateCurrentVectors((new Matrix4f().identity()
                    .translate(enemy.getPosX(),0.0f,enemy.getPosZ())
//                    .rotate(-3.14f/6,new Vector3f(0.0f,1.0f,0.0f))
                    .scale(scaleEnemiesSize) //todo poprawic collision boxy na promieniowe
            ));
            enemy.updateDistanceToPLayer(player);
        }
        texNumber+=3;


        int jumping = counter;
        if (jumping> counterBound/2) jumping -= (jumping-counterBound/2)*2;

        for (BonusHealthItem bonus: bonusHearts){
            if (!bonus.isEnable()) bonus.respawnIfTimeExceeded();
            else{
                bonus.updateDistanceToPLayer(player);
                bonus.HealPlayerIfInsideArea(player,10);
                if (bonus.isEnable()){
                    M.identity()
                            .translate(bonus.getPosX(),0.2f *jumping + 1,bonus.getPosZ())
                            .rotate((3.14f/counterBound)*counter,0,1,0)
                            .scale(3f);
                    bonus.getModel().draw(M,V,P, texNumber); //2
                }
            }
        }
        texNumber+=3;





        // GROUND AND THE SKYBOX DRAWN AT THE END
        M.identity().translate(0,0,0);
        M.scale(60.0f,1.0f,60.0f);
        glCullFace(GL_FRONT);
        ground.draw(M,V,P, texNumber); //1
        texNumber+=3;


        M.identity().rotate(3.14f,new Vector3f(0.0f,0.0f,1.0f)).translate(0.0f,-6000.0f,0.0f).scale(15000.0f,15000.0f,15000.0f);
        skybox.draw(M,V,P, texNumber); //1
        glCullFace(GL_BACK);
        texNumber+=3;

        lights.createLights(shader3);
        for (int i=0;i<deathspots.size();i++){
            M.identity().translate(deathspots.get(i).getPosX(),2.0f,deathspots.get(i).getPosZ()).scale(4.0f,4.0f,4.0f);
            deathspots.get(i).getModel().draw(M,V,P,1);
        }


        // HERE ALL OF THE HUD ELEMENTS WILL BE RENDERED (INCLUDING THE GUN)
        // Those parts absolutely have to be put at the end, because we are completely clearing the View Matrix

        // Clearing all of the depth information in the depth buffers so that there are no intersections of the HUD with the ingame objects
        glClear(GL_DEPTH_BUFFER_BIT);


        if (noSound<10) noSound++;
        if(mouseButton == GLFW_MOUSE_BUTTON_LEFT && player.isShowShootAnimation() ){

            float theClosestEnemyDistance = Float.MAX_VALUE;
            int whichEnemyIsTheClosest=-1, i=0;

            for (Enemy enemy: enemiesList){


                float distance = enemy.getDistanceToPlayer();
                float vectorsMultiplication = enemy.getToPlayerVector().dot(player.getLookAheadVector());


                //new version of shooting
                if (-(1 - Math.atan(1/distance/2)) > vectorsMultiplication){
                    if (theClosestEnemyDistance>distance){
                        theClosestEnemyDistance = distance;
                        whichEnemyIsTheClosest = i;
                    }
                }
                i++;
            }

            if (player.isCanShoot() && whichEnemyIsTheClosest!=-1) {
                Enemy hurt;
                hurt = enemiesList.get(whichEnemyIsTheClosest);
                hurt.receiveDamage(player.getDamage());
                M.identity().translate(hurt.getPosX(),7.0f,hurt.getPosZ())
                        .scale(1.2f,1.2f,1.2f);
                blood.draw(M,V,P,1);
//                System.out.println(enemiesList.get(whichEnemyIsTheClosest).getHealth());
            }

            player.setCanShoot(false);
            if (noSound == 10) {
                new Thread(new Sound("gun_shot.wav",18,0)).start();
                //            new Sound("gun_shot.wav",15,0).playWAV();
                noSound=0;
            }
        }

        M.identity();
        V.identity().translate(-1.75f,-8.0f,-12.0f).rotate(-3.14f, new Vector3f(0.0f,1.0f,0.0f));//.rotate(-3.14f/3, new Vector3f(1.0f,0.0f,0.0f));
        V.scale(0.3f,0.3f,0.3f);

        if (!player.isCanShoot()){
            V.rotate(-3.14f/35, new Vector3f(1.0f,0.0f,0.0f));
            hudgun.draw(M,V,P,texNumber);
            V.identity().translate(-0.032f,-1.82f,-9.0f);
            shot.draw(M,V,P,texNumber);
        }
        else{
            hudgun.draw(M,V,P,texNumber);
        }

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
