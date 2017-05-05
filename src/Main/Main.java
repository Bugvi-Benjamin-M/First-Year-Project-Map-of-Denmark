package Main;

import Controller.*;
import Controller.ToolbarControllers.ToolbarController;
import Enums.OSMEnums.ElementType;
import Helpers.DefaultSettings;
import Helpers.FileHandler;
import Helpers.Utilities.DebugWindow;
import Helpers.Utilities.FPSCounter;
import Model.Elements.Road;
import Model.Model;
import RouteSearch.Graph;
import RouteSearch.Edge;
import KDtree.KDTree;
import RouteSearch.GraphFactory;
import View.PopupWindow;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

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

        long start = 2186106984L;
        long end = 497314113L;

        GraphFactory factory = model.getGraphFactory();
        RouteSearch.Dijkstra dijk = new RouteSearch.Dijkstra(factory.getGraph(),
                start, Enums.TravelType.VEHICLE);
        Iterable<Edge> iterator = dijk.pathTo(end);
        if (iterator != null) {
            factory.setRoute(iterator);
            List<Road> route = factory.getRoute();
            if (route != null && route.size() != 0) {
                for (Road road : route) {
                    System.out.println(road.getName());
                }
                CanvasController.getInstance().getMapCanvas().setRoute(route, factory.getRouteRefs());
            } else {
                System.out.println("No route found...");
            }
        } else {
            System.out.println("No path found...");
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
