package Engine;

import Entities.Model;
import org.joml.Vector3f;

public class Main{

    public static void main(String[] args) {
        try {
            boolean vSync = true;

            GameLogicInterface gameLogic = new Doom3D();
            GameEngine gameEngine = new GameEngine("DOOM 3D GAME", 1920, 1080, vSync, gameLogic);
            gameEngine.start();

        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}