package Entities;

public class Enemy extends LiveEntity {
    public Enemy(float posX, float posZ, Model model, float minX, float maxX, float minZ, float maxZ, int health, int damage) {
        super(posX, posZ, model, minX, maxX, minZ, maxZ, health, damage);
    }

    public void move(float x, float z){
        setPosX(x);
        setPosZ(z);
    }

    public void moveInPlayerDirection(float playerX, float playerZ, float distanceInOneStep){
        if (getPosX() > playerX) setPosX(getPosX()-distanceInOneStep);
        else setPosX(getPosX()+distanceInOneStep);
        if (getPosZ() > playerZ) setPosZ(getPosZ()-distanceInOneStep);
        else setPosZ(getPosZ()+distanceInOneStep);
    }


}
