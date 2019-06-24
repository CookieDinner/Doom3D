package Engine;

public class Main{

    public static void main(String[] args) {
        try {
            boolean vSync = true;

            GameLogicInterface gameLogic = new Doom3D();
            GameEngine gameEngine = new GameEngine("DOOM 3D GAME", 800, 600, vSync, gameLogic);
            gameEngine.start();

        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}