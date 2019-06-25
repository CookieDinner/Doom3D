package Engine;

import static org.lwjgl.glfw.GLFW.*;

public class GameEngine implements Runnable {

    private final Window window;

    private final Thread gameLoopThread;

    private final GameLogicInterface gameLogic;

    private int framesCounter=0;
    private long timeElapsed=0;

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
        gameLogic.init(window);
    }

    protected void gameLoop() {
        long start,end;
        glfwSetTime(0); //Zeruj timer
        while (!window.windowShouldClose()) {
            start = System.nanoTime();


            input();
            update();
            render();

            end=System.nanoTime();
            if (timeElapsed>1_000_000_000){
                System.out.println("FPS:   "+(float) framesCounter/timeElapsed*1_000_000_000);
                timeElapsed=0;
                framesCounter=0;
            }else{
                timeElapsed += end-start;
                framesCounter++;
            }


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