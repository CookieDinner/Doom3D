package Entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LiveEntity extends Entity {
    private int health;
    private int damage;

    public LiveEntity(float posX, float posZ, Model model,int health, int damage) {
        super(posX, posZ, model);
        this.health = health;
        this.damage = damage;
    }

    public boolean checkIfEntityDied(int maxHealth){

        if (getHealth() <=0){
            setHealth(maxHealth);
            return true;
        }
        return false;
    }

    public void receiveDamage(int damage){
        setHealth(getHealth()-damage);
//        System.out.println(getHealth()); //todo zakomentowac
    }
}
