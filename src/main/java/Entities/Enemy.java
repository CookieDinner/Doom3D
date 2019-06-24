package Entities;

public class Enemy extends LiveEntity {
    public Enemy(float posX, float posZ, Model model, int health, int damage) {
        super(posX, posZ, model, health, damage);
    }

    public void moveInPlayerDirection(float playerX, float playerZ, float distanceInOneStep){
        if (getPosX() > playerX) setPosX(getPosX()-distanceInOneStep);
        else setPosX(getPosX()+distanceInOneStep);
        if (getPosZ() > playerZ) setPosZ(getPosZ()-distanceInOneStep);
        else setPosZ(getPosZ()+distanceInOneStep);
    }


}
