package Engine;

public interface GameLogicInterface {
    void init(Window window) throws Exception;

    void input(Window window);

    void update();

    void render(Window window);

    void cleanup();
}
