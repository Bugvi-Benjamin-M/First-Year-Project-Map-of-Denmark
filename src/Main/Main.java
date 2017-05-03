package Main;

import Controller.*;
import Controller.ToolbarControllers.ToolbarController;
import Enums.OSMEnums.ElementType;
import Helpers.DefaultSettings;
import Helpers.FileHandler;
import Helpers.Utilities.DebugWindow;
import Helpers.Utilities.FPSCounter;
import Model.Model;
import RouteSearch.Graph;
import RouteSearch.Edge;
import KDtree.KDTree;
import View.PopupWindow;

import javax.swing.*;

/**
 * Created by Jakob on 06-03-2017.
 */
public class Main {

    public static final FPSCounter FPS_COUNTER = new FPSCounter();

    public static long LOAD_TIME;
    private static SplashScreen screen;
    private static boolean loadDefaultFile;

    public static void main(String[] args)
    {
        long startTime = System.nanoTime();
        createControllers();
        PreferencesController.getInstance().setupPreferences();
        MainWindowController.getInstance().setProgressBarTheme();
        splashScreenInit();

        Model model = Model.getInstance();
        CanvasController.getInstance().setupAsObserver();
        FileHandler.loadDefaultResource();
        if (!PreferencesController.getInstance().getStartupFileNameSetting().equals(DefaultSettings.DEFAULT_FILE_NAME)) {
            try {
                FileHandler.fileChooserLoad(PreferencesController.getInstance().getStartupFilePathSetting());
                loadDefaultFile = false;
            } catch (Exception e) {
                e.printStackTrace();
                PopupWindow.infoBox(null,"Could Not Find Preferred Startup File: "+PreferencesController.getInstance().getStartupFileNameSetting()+".\n" +
                "Loading "+DefaultSettings.DEFAULT_FILE_NAME,"Preferred Startup File Not Found!");
                loadDefaultFile = true;
            }
        }

        splashScreenDestruct();
        SwingUtilities.invokeLater(() -> {
            MainWindowController.getInstance().setupMainWindow();
            SettingsWindowController.getInstance().setupSettingsWindow();
            model.modelHasChanged();
            MainWindowController.getInstance().showWindow();
            if (loadDefaultFile) CanvasController.adjustToBounds();
            else CanvasController.adjustToDynamicBounds();
            CanvasController.repaintCanvas();
            LOAD_TIME = System.nanoTime() - startTime;
            System.out.println("System loadtime: " + (LOAD_TIME / 1000000) + " ms");
            DebugWindow.getInstance().setLoadtimeLabel();
        });

        Graph graph = model.getGraph();
        RouteSearch.Dijkstra dijk = new RouteSearch.Dijkstra(graph, 497325210L, Enums.TravelType.VEHICLE);
        for(Edge edge : dijk.pathTo(715938988L)){
            //FIXME: Road names here
            //System.out.println(edge.either() + " --> " + edge.other(edge.either()));
        }
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
