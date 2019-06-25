package Entities;

import Engine.Utils;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BonusHealthItem extends Entity{

    private float distanceToPlayer;
    private boolean enable;
    private int respawnDelay;
    private int respawnUpperBound;

    public BonusHealthItem(float posX, float posZ, Model model) {
        super(posX, posZ, model);
        distanceToPlayer = Float.MAX_VALUE;
        respawnUpperBound = 200;
        respawnDelay = 0;
        enable=true;
    }

    public void updateDistanceToPLayer(Player player){
        distanceToPlayer = Utils.distance2DBetween2Points(player.getPosX(),player.getPosZ(),getPosX(),getPosZ());
    }

    public void HealPlayerIfInsideArea(Player player, float radius){
        if (distanceToPlayer<radius) {
            player.healPlayer();
            enable=false;
            new Thread(new Sound("bonus2.wav",15,0)).start();
        }
    }

    public void respawnIfTimeExceeded(){
        if (!enable){
            respawnDelay++;

            if (respawnDelay==respawnUpperBound){
                enable = true;
                respawnDelay = 0;
            }
        }
    }

}
