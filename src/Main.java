import Controller.*;
import Helpers.FileHandler;
import Model.*;
import View.*;

import java.io.InputStream;
import java.util.Observer;

/**
 * Created by Jakob on 06-03-2017.
 */
public class Main {
    private Window window;

    public static void main(String[] args) {
        Window window = new Window();
        Model model = Model.getInstance();
        CanvasController canvasController = new CanvasController(window);
        ToolbarController toolbarController = new ToolbarController(window);
        InfobarController infobarController = new InfobarController(window);
        FileHandler.loadDefault();
    }
}
