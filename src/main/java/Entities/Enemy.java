package Entities;

import Engine.Utils;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

import java.util.ArrayList;

@Getter
@Setter
public class Enemy extends LiveEntity {
    private Vector3f toPlayerVector;
    private float spawnPointX;
    private float spawnPointZ;
    private boolean died;
    private int respawnDelay;
    private int respawnUpperBound;
    private float distanceToPlayer = Float.MAX_VALUE;



    public Enemy(float posX, float posZ, Model model, int health, int damage) {
        super(posX, posZ, model, health, damage);
        spawnPointX = posX;
        spawnPointZ = posZ;
        died = false;
        respawnUpperBound = 200;
        respawnDelay = 0;
    }


    public Vector3f getToPlayerVector() {
        return toPlayerVector;
    }

    public void setToPlayerVector(Player player) {
        this.toPlayerVector = new Vector3f().set(this.getPosX() - player.getPosX(),0, this.getPosZ() - player.getPosZ());
        toPlayerVector.normalize();
    }


    public void moveInPlayerDirection(Player player, float distanceInOneStep){

        int radius =20;
        if (distanceToPlayer <= 350 && distanceToPlayer >= radius) { //todo zmienić dolną wartość na równą promieniowi kolizji
            setPosX(getPosX() - (toPlayerVector.x * distanceInOneStep));
            setPosZ(getPosZ() - (toPlayerVector.z * distanceInOneStep));
        }

        if (player.isCanBeHurt() && distanceToPlayer < radius){
            player.receiveDamage(getDamage());
        }


    }

    public void updateDistanceToPLayer(Player player){
        distanceToPlayer = Utils.distance2DBetween2Points(player.getPosX(),player.getPosZ(),getPosX(),getPosZ());

    }

    public void respawnIfTimeExceeded(){
        if (died){
            respawnDelay++;

            if (respawnDelay==respawnUpperBound){
                move(spawnPointX,spawnPointZ);
                died = false;
                respawnDelay = 0;
            }
        }
    }

    @Override
    public boolean checkIfEntityDied(int maxHealth, ArrayList deathspots, Model splat) {
        boolean result = super.checkIfEntityDied(maxHealth, deathspots, splat);
        if (result) {
            deathspots.add(new Entity(getPosX(),getPosZ(),splat));
            move(-1000,-1000);
            died=true;
//            new Thread(new Sound("enemy_death1.wav",0,2000)).start();
//            new Sound("enemy_death1.wav",0,1000).playWAV();
        }
        return result;
    }
}
