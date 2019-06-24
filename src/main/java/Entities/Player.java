package Entities;

import org.joml.Vector3f;

public class Player extends LiveEntity{
    public Player(float posX, float posZ, Model model, float minX, float maxX, float minZ, float maxZ, int health, int damage) {
        super(posX, posZ, null, health, damage);
    }

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
        /*
        Pierwsza regresja:

        27  -0.83
        49  -0.90
        23  -0.80
        67  -0.93
        123 -0.95
        227 -0.98
        150 -0.96
        175 -0.965
        200 -0.97

        Drugra regresja (zaczyna się po odległości 130):
        150 -0.9592
        173.5   -0.9652
        198 -0.9698
        253 -0.9770
        287 -0.9800
         */

        float y;
//        if (distance<130)
            y = -0.001f * distance + -0.836f;
//        else y = -0.00015f * distance + -0.9389f;
        System.out.println("TO jest Y     "+ y);
        if (value< y) return true;
        return false;
    }
}
