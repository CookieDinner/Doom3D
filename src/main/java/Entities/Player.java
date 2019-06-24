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

    public boolean isEnemyInsideGunViewfinder(float distance, float value){

        //Wspołczynniki wyznaczone za pomocą regresji liniowej

        float y;

        if (distance<30)
            y=-0.9600f;
        else if (distance<140)
            y = -0.00015f * distance -0.97968f;
        else
            y = -0.00002f * distance -0.99501f;

        if (value< y) return true;
        return false;
    }

    @Override
    public void receiveDamage(int damage) {
        super.receiveDamage(damage);
        canBeHurt = false;
    }
}
