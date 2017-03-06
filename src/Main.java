import Controller.*;
import Model.*;
import View.*;

/**
 * Created by Jakob on 06-03-2017.
 */
public class Main {
    private Window window;

    public static void main(String[] args) {
        Window window = new Window();
        CanvasController canvasController = new CanvasController();
        ToolbarController toolbarController = new ToolbarController(window);
    }
}
