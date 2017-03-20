import Controller.*;
import Helpers.FileHandler;
import Model.Model;
import View.Window;

import javax.swing.*;
import java.awt.*;

import java.io.FileNotFoundException;

/**
 * Created by Jakob on 06-03-2017.
 */
public class Main {

    private final static String MAIN_TITLE = "OSM Map Viewer v0.2";
    private Window window;

    public static void main(String[] args) {
        Window window = new Window().title(MAIN_TITLE)
                            .closeOperation(WindowConstants.EXIT_ON_CLOSE)
                            .dimension(new Dimension(1200, 1000))
                            .extendedState(JFrame.MAXIMIZED_BOTH)
                            .layout(new BorderLayout())
                            .relativeTo(null)
                            .show();


        Model model = Model.getInstance();
        CanvasController canvasController = CanvasController.getInstance(window);
        ToolbarController toolbarController = ToolbarController.getInstance(window);
        InfobarController infobarController = InfobarController.getInstance(window);
        WindowController windowController = MainWindowController.getInstance(window);
        try {
            FileHandler.loadDefault("/defaultosm.osm");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
