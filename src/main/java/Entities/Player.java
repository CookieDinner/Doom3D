package Entities;

public class Player extends LiveEntity{
    public Player(float posX, float posZ, Model model, float minX, float maxX, float minZ, float maxZ, int health, int damage) {
        super(posX, posZ, null, minX, maxX, minZ, maxZ, health, damage);
    }

    public void receiveDamage(int damage){
        setHealth(getHealth()-damage);
        System.out.println(getHealth()); //todo zakomentowac
    }
}
