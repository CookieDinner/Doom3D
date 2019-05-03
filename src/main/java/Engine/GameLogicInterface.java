package Engine;

public interface GameLogicInterface {
    void init() throws Exception;

    void input(Window window);

    void update();

    void render(Window window);

    void cleanup();
}
