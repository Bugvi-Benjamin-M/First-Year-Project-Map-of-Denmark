package Main;

import Controller.*;
import Controller.ToolbarControllers.ToolbarController;
import Enums.OSMEnums.ElementType;
import Helpers.FileHandler;
import Helpers.Utilities.DebugWindow;
import Helpers.Utilities.FPSCounter;
import Model.Model;
import RouteSearch.GraphFactory;
import KDtree.KDTree;

import javax.swing.*;

/**
 * Created by Jakob on 06-03-2017.
 */
public class Main {

    public static final FPSCounter FPS_COUNTER = new FPSCounter();

    public static long LOAD_TIME;
    private static SplashScreen screen;

    public static void main(String[] args)
    {

        long startTime = System.nanoTime();
        splashScreenInit();

        Model model = Model.getInstance();
        FileHandler.loadDefaultResource();
        new Thread(() -> {
            KDTree roads = Model.getInstance().getElements(ElementType.HIGHWAY);
            Model.getInstance().setGraph((new GraphFactory(roads)).getGraph());
            System.out.println(model.getGraph().toString());
        }).start();
        splashScreenDestruct();
        createControllers();

        PreferencesController.getInstance().setupPreferences();
        SwingUtilities.invokeLater(() -> {
            MainWindowController.getInstance().setupMainWindow();
            SettingsWindowController.getInstance().setupSettingsWindow();
            model.modelHasChanged();
            MainWindowController.getInstance().showWindow();
            MainWindowController.getInstance().transferFocusToMapCanvas();

            LOAD_TIME = System.nanoTime() - startTime;
            System.out.println("System loadtime: " + (LOAD_TIME / 1000000) + " ms");
            DebugWindow.getInstance().setLoadtimeLabel();
        });
    }

    private static void createControllers()
    {
        PreferencesController.getInstance();
        MainWindowController.getInstance();
        ToolbarController.getInstance();
        CanvasController.getInstance();
        SettingsWindowController.getInstance();
    }

    public static void splashScreenDestruct()
    {
        screen.setScreenVisible(false);
        screen = null;
    }

    public static void splashScreenInit()
    {
        ImageIcon myImage = new ImageIcon(Main.class.getResource("/middelfart.jpg")); // denmark.gif
        screen = new SplashScreen(myImage);
        screen.setLocationRelativeTo(null);
        screen.setScreenVisible(true);
    }
}
