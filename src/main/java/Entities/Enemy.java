package Entities;

import Engine.Utils;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

@Getter
@Setter
public class Enemy extends LiveEntity {
    private Vector3f toPlayerVector;
    private float spawnPointX;
    private float spawnPointZ;
    private boolean died;
    private int respawnDelay;
    private int respawnUpperBound;



    public Enemy(float posX, float posZ, Model model, int health, int damage) {
        super(posX, posZ, model, health, damage);
        spawnPointX = posX;
        spawnPointZ = posZ;
        died = false;
        respawnUpperBound = 1800;
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

        if (Utils.distance2DBetween2Points(player.getPosX(),player.getPosZ(),getPosX(),getPosZ()) <= 250){
            if (getPosX() > player.getPosX()) setPosX(getPosX()-distanceInOneStep);
            else setPosX(getPosX()+distanceInOneStep);
            if (getPosZ() > player.getPosZ()) setPosZ(getPosZ()-distanceInOneStep);
            else setPosZ(getPosZ()+distanceInOneStep);
        }

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
    public boolean checkIfEntityDied(int maxHealth) {
        boolean result = super.checkIfEntityDied(maxHealth);
        if (result) {
            move(-1000,-1000);
            died=true;
        }
        return result;
    }
}
