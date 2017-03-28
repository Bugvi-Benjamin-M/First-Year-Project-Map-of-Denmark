package Main;

import Controller.*;
import Helpers.FileHandler;
import Helpers.Utilities.FPSCounter;
import Model.Model;
import View.MapCanvas;
import View.PopupWindow;

import java.io.FileNotFoundException;

/**
 * Created by Jakob on 06-03-2017.
 */
public class Main {

    public static final FPSCounter FPS_COUNTER = new FPSCounter();

    public static final String DEFAULT_RESOURCE = "/denmark-latest.zip";

    public static void main(String[] args) {

        Model.getInstance();

        WindowController windowController = MainWindowController.getInstance();

        CanvasController canvasController = CanvasController.getInstance(windowController.getWindow());
        ToolbarController.getInstance(windowController.getWindow());
        InfobarController.getInstance(windowController.getWindow());

        try {
            loadDefaultResource();
        } catch (Exception e) {
            PopupWindow.errorBox(null,e.getMessage());
            Model model = Model.getInstance();
            System.out.println("Bounds: minlon "+model.getMinLongitude()+" - maxlon "+model.getMaxLongitude());
            canvasController.loadFromCoastlines();
        }

        FPS_COUNTER.start();
    }

    private static void loadDefaultResource() throws Exception {
        try {
            throw new FileNotFoundException();
            /*
            long startTime = System.currentTimeMillis();
            FileHandler.loadResource(DEFAULT_RESOURCE);
            long stopTime = System.currentTimeMillis();
            System.out.println("Loading time: " + (stopTime - startTime) + " ms");
            */
        } catch (FileNotFoundException e) {
            throw new Exception("Program was not able to load default resource \""+DEFAULT_RESOURCE+"\"" +
                    "\nLoading from coastlines instead.");
        }
    }

    public static void notifyThemeChange() {
        CanvasController.getInstance(MainWindowController.getInstance().getWindow()).themeHasChanged();
        ToolbarController.getInstance(MainWindowController.getInstance().getWindow()).themeHasChanged();
        InfobarController.getInstance(MainWindowController.getInstance().getWindow()).themeHasChanged();
    }

    public static void notifyKeyToggle(boolean status) {
        CanvasController.getInstance(MainWindowController.getInstance().getWindow()).toggleKeyBindings(status);
        ToolbarController.getInstance(MainWindowController.getInstance().getWindow()).toggleKeyBindings(status);
    }
}
