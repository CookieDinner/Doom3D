package Engine;

import Entities.Sound;

public class Main{

    public static void main(String[] args) {
        try {
            boolean vSync = true;

            GameLogicInterface gameLogic = new Doom3D();
            GameEngine gameEngine = new GameEngine("DOOM 3D GAME", 1280, 720, vSync, gameLogic);
            gameEngine.start();
            new Thread(new Sound("back_short.wav",22,1000)).start();

        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}