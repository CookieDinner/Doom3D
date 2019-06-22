package Entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LiveEntity extends Entity {
    private int health;
    private int damage;

    public LiveEntity(float posX, float posZ, Model model, float minX, float maxX, float minZ, float maxZ, int health, int damage) {
        super(posX, posZ, model, minX, maxX, minZ, maxZ);
        this.health = health;
        this.damage = damage;
    }
}
