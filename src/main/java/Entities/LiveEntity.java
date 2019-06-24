package Entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Random;

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

    public boolean checkIfEntityDied(){
        Random random = new Random();

        if (getHealth() <=0){
            setHealth(100);
            setPosZ(random.nextInt(500));
            setPosX(random.nextInt(500));
            return true;
        }
        return false;
    }

    public void receiveDamage(int damage){
        setHealth(getHealth()-damage);
        System.out.println(getHealth()); //todo zakomentowac
    }
}
