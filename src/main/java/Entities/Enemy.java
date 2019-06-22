package Entities;

public class Enemy extends LiveEntity {
    public Enemy(float posX, float posZ, Model model, float minX, float maxX, float minZ, float maxZ, int health, int damage) {
        super(posX, posZ, model, minX, maxX, minZ, maxZ, health, damage);
    }
}
