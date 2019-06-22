package Engine;

import Entities.Entity;
import org.joml.Vector2f;

public class Utils {

    public static boolean checkCollisionWithObject(Entity first_Entity, Entity second_Entity){

//        System.out.println(first_Entity.getPosX() +"   "+ second_Entity.getModel().getMinX()  +"     " +  second_Entity.getModel().getMaxX());
//        System.out.println(first_Entity.getPosZ() +"   "+ second_Entity.getModel().getMinZ()  +"     " +  second_Entity.getModel().getMaxZ());


        if (first_Entity.getPosX() < second_Entity.getMaxX()
                && first_Entity.getPosX() > second_Entity.getMinX()){

            if (first_Entity.getPosZ() < second_Entity.getMaxZ()
                    && first_Entity.getPosZ() > second_Entity.getMinZ()) return true;
        }
        return false;
    }


    // 0 - floor, 1 - ceil
    public static Vector2f ceilOrFloorTheResult(Entity first_Entity, Entity second_Entity){
        Vector2f vector2f = new Vector2f(0,0);

        if (Math.abs(first_Entity.getPosX()-second_Entity.getMinX()) >
                Math.abs(first_Entity.getPosX()-second_Entity.getMaxX()))
            vector2f.x=1;

        if (Math.abs(first_Entity.getPosZ()-second_Entity.getMinZ()) >
                Math.abs(first_Entity.getPosZ()-second_Entity.getMaxZ()))
            vector2f.y=1;
        return  vector2f;

    }

}
