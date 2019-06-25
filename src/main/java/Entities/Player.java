package Entities;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

public class Player extends LiveEntity{
    public Player(float posX, float posZ, Model model, float minX, float maxX, float minZ, float maxZ, int health, int damage) {
        super(posX, posZ, null, health, damage);
    }

    @Getter
    @Setter
    private boolean canBeHurt = false;
    @Getter
    @Setter
    private boolean canShoot = false;
    @Getter
    @Setter
    private boolean showShootAnimation = false;

    private Vector3f lookAheadVector;

    public Vector3f getLookAheadVector() {
        return lookAheadVector;
    }

    public void setLookAheadVector(Vector3f camUp, Vector3f camRight) {
        lookAheadVector = new Vector3f();
        camRight.cross(camUp,lookAheadVector);
        lookAheadVector.normalize();
    }


    @Override
    public void receiveDamage(int damage) {
        super.receiveDamage(damage);
        canBeHurt = false;
    }

    public void healPlayer(){
        setHealth(getHealth()+25);
        if (getHealth()>100) setHealth(100);
    }

    public void debugPosition(){
        System.out.println("TO SA POZYCJE X:  "+ getPosX() + "   oraz Z:    "+ getPosZ());
    }
}
