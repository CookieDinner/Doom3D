package Engine;

import Entities.Enemy;
import Entities.Entity;
import Entities.Player;
import lombok.Getter;
import org.joml.Vector2f;

import java.util.ArrayList;


@Getter
public class CollisionUnit {
    private ArrayList<Entity> collisionEntitiesList;
    private Player player;

    public CollisionUnit(Player player) {
        collisionEntitiesList = new ArrayList();
        this.player = player;
        collisionEntitiesList.add(this.player);
    }

    public void addToList(Entity entity){
        collisionEntitiesList.add(entity);
    }

    public void delFromList(Entity entity){
        collisionEntitiesList.remove(entity);
    }

    public Vector2f checkIfCollisionExistWithAnyEntity(Entity entity){
        for (Entity entity_to_check: collisionEntitiesList){

            if (!entity.equals(entity_to_check)){
                if(Utils.checkCollisionWithObject(entity,entity_to_check)){
                    if ( player.isCanBeHurt() && ( entity.equals(player) && entity_to_check instanceof Enemy)
//                            ||(entity_to_check.equals(player) && entity instanceof Enemy)
                    ){
                        player.receiveDamage(((Enemy) entity_to_check).getDamage()); //todo ile zycia zabiera
                    }
                    return Utils.ceilOrFloorTheResult(entity,entity_to_check);
                }
            }

        }
        return null;
    }


    public void abandonMovingChangesWhenDetectedCollision(Entity entity,float oldXCoordinate, float oldZCoordinate){

        Vector2f whichFunctionShouldBeUsed;
        if ((whichFunctionShouldBeUsed = checkIfCollisionExistWithAnyEntity(entity)) != null) {

            if (whichFunctionShouldBeUsed.x==0)
                oldXCoordinate = oldXCoordinate-1;
            else oldXCoordinate = oldXCoordinate+1;

            if (whichFunctionShouldBeUsed.y==0)// y because it's 2nd artibute
                oldZCoordinate = oldZCoordinate-1;
            else oldZCoordinate = oldZCoordinate+1;

            entity.move(oldXCoordinate,oldZCoordinate);
        }
    }


}
