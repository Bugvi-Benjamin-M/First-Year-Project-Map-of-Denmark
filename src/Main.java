import Controller.CanvasController;
import Controller.InfobarController;
import Controller.ToolbarController;
import Controller.WindowController;
import Helpers.FileHandler;
import Model.Model;
import View.Window;

/**
 * Created by Jakob on 06-03-2017.
 */
public class Main {

    public static void main(String[] args) {
        Window window = new Window();
        Model model = Model.getInstance();
        CanvasController canvasController = CanvasController.getInstance(window);
        ToolbarController toolbarController = ToolbarController.getInstance(window);
        InfobarController infobarController = new InfobarController(window);
        WindowController windowController = WindowController.getInstance(window);
        FileHandler.loadDefault("/defaultosm.osm");
    }
}
