package Engine;

import static org.lwjgl.glfw.GLFW.*;

public class GameEngine implements Runnable {

    private final Window window;

    private final Thread gameLoopThread;

    private final GameLogicInterface gameLogic;

    public GameEngine(String windowTitle, int width, int height, boolean vSync, GameLogicInterface gameLogic) throws Exception {
        gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
        window = new Window(windowTitle, width, height, vSync);
        this.gameLogic = gameLogic;
    }

    public void start() {
        String osName = System.getProperty("os.name");
        if (osName.contains("Mac")) {
            gameLoopThread.run();
        } else {
            gameLoopThread.start();
        }
    }

    @Override
    public void run() {
        try {
            init();
            gameLoop();
        } catch (Exception excp) {
            excp.printStackTrace();
        }
    }

    protected void init() throws Exception {
        window.init();
        gameLogic.init();
    }

    protected void gameLoop() {


        glfwSetTime(0); //Zeruj timer
        while (!window.windowShouldClose()) {
            input();
            update();
            render();
        }
        gameLogic.cleanup();
        window.destroyAndClean();
    }


    protected void input() {
        gameLogic.input(window);
    }

    protected void update() {
        gameLogic.update();
    }

    protected void render() {
        gameLogic.render(window);
        window.update();
    }
}