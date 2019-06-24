package Entities;

import Engine.Utils;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

@Getter
@Setter
public class Enemy extends LiveEntity {
    public Enemy(float posX, float posZ, Model model, int health, int damage) {
        super(posX, posZ, model, health, damage);
    }

    private Vector3f toPlayerVector;

    public Vector3f getToPlayerVector() {
        return toPlayerVector;
    }

    public void setToPlayerVector(Player player) {
        this.toPlayerVector = new Vector3f().set(this.getPosX() - player.getPosX(),0, this.getPosZ() - player.getPosZ());
        toPlayerVector.normalize();
    }


    public void moveInPlayerDirection(Player player, float distanceInOneStep){

        if (Utils.distance2DBetween2Points(player.getPosX(),player.getPosZ(),getPosX(),getPosZ()) <= 200){
            if (getPosX() > player.getPosX()) setPosX(getPosX()-distanceInOneStep);
            else setPosX(getPosX()+distanceInOneStep);
            if (getPosZ() > player.getPosZ()) setPosZ(getPosZ()-distanceInOneStep);
            else setPosZ(getPosZ()+distanceInOneStep);
        }

    }


}
