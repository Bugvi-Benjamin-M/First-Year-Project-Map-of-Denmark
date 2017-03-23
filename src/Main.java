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
        InfobarController infobarController = new InfobarController(window);
        WindowController windowController = WindowController.getInstance(window);
        long starttime = System.currentTimeMillis();
        long stoptime = System.currentTimeMillis();
        System.out.println(stoptime - starttime);

        try {
            FileHandler.loadDefault("/denmark-latest.zip");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
