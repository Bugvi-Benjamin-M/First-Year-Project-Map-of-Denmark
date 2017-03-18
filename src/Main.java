import Controller.CanvasController;
import Controller.InfobarController;
import Controller.ToolbarController;
import Controller.WindowController;
import Helpers.FileHandler;
import Model.Model;
import View.Window;

import java.io.FileNotFoundException;

/**
 * Created by Jakob on 06-03-2017.
 */
public class Main {
    private Window window;

    public static void main(String[] args) {
        Window window = new Window();
        Model model = Model.getInstance();
        CanvasController canvasController = CanvasController.getInstance(window);
        ToolbarController toolbarController = ToolbarController.getInstance(window);
        InfobarController infobarController = InfobarController.getInstance(window);
        WindowController windowController = WindowController.getInstance(window);
        try {
            FileHandler.loadDefault("/defaultosm.osm");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
