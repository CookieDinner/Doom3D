package Entities;

import Engine.ShaderProgram;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    public boolean checkIfEntityDied(int maxHealth, ArrayList deathspots, Model splat){

        if (getHealth() <=0){
            setHealth(maxHealth);
            return true;
        }
        return false;
    }

    public void receiveDamage(int damage){
        setHealth(getHealth()-damage);
    }
}
