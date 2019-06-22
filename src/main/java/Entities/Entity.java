package Entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Entity {
    private float posX;
    private float posZ;
    private Model model;
    private float minX;
    private float maxX;
    private float minZ;
    private float maxZ;

    public Entity(float posX, float posZ, Model model, float minX, float maxX, float minZ, float maxZ) {
        this.posX = posX;
        this.posZ = posZ;
        this.model = model;
        this.minX = minX;
        this.maxX = maxX;
        this.minZ = minZ;
        this.maxZ = maxZ;
    }
}
