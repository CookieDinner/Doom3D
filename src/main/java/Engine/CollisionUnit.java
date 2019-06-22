package Engine;

import Entities.Entity;
import lombok.Getter;
import org.joml.Vector2f;

import java.util.ArrayList;

@Getter
public class CollisionUnit {
    private ArrayList<Entity> CollisionEntitiesList;

    public CollisionUnit() {
        CollisionEntitiesList = new ArrayList();
    }

    public void addToList(Entity entity){
        CollisionEntitiesList.add(entity);
    }

    public void delFromList(Entity entity){
        CollisionEntitiesList.remove(entity);
    }

    public Vector2f checkIfCollisionExistWithAnyEntity(Entity entity){
        for (Entity entity_to_check: CollisionEntitiesList){
            if(Utils.checkCollisionWithObject(entity,entity_to_check))
                return Utils.ceilOrFloorTheResult(entity,entity_to_check);
        }
        return null;
    }


}
