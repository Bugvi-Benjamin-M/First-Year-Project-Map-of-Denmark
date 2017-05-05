package Main;

import Controller.*;
import Controller.ToolbarControllers.ToolbarController;
import Exceptions.FileWasNotFoundException;
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
import java.io.FileNotFoundException;
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
        try {
            FileHandler.loadDefaultResource(true);
        } catch (FileNotFoundException | FileWasNotFoundException e) {
            e.printStackTrace();
        }
        loadDefaultFile = true;
        if(!PreferencesController.getInstance().getStartupFileNameSetting().equals(DefaultSettings.DEFAULT_FILE_NAME)) {
            try {
                FileHandler.fileChooserLoad(PreferencesController.getInstance().getStartupFilePathSetting());
                loadDefaultFile = false;
            } catch (FileWasNotFoundException | FileNotFoundException e) {
                PopupWindow.infoBox(null, "Could Not Find Preferred Startup File: " + PreferencesController.getInstance().getStartupFileNameSetting() + ".\n" +
                        "Loading " + DefaultSettings.DEFAULT_FILE_NAME, "Preferred Startup File Not Found!");
                try {
                    FileHandler.loadDefaultResource(true);
                } catch (FileNotFoundException | FileWasNotFoundException e1) {
                    e1.printStackTrace();
                }
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
            CanvasController.getInstance().updateCanvasPOI();
            CanvasController.repaintCanvas();
            LOAD_TIME = System.nanoTime() - startTime;
            System.out.println("System loadtime: " + (LOAD_TIME / 1000000) + " ms");
            DebugWindow.getInstance().setLoadtimeLabel();
        });

        long time = System.currentTimeMillis();
        long start = 2186106984L;
        long end = 497314113L;

        GraphFactory factory = model.getGraphFactory();
        RouteSearch.Dijkstra dijk = new RouteSearch.Dijkstra(factory.getGraph(),
                start, end, Enums.TravelType.VEHICLE);
        Iterable<Edge> iterator = dijk.path();
        if (iterator != null) {
            factory.setRoute(iterator);
            List<Road> route = factory.getRoute();
            List<Float> lengths = factory.getLengths();
            if (route != null && route.size() != 0) {
                for (int i = 0; i < route.size(); i++) {
                    System.out.println(route.get(i).getName() +
                        ": "+lengths.get(i)+" m");
                }
                CanvasController.getInstance().getMapCanvas().setRoute(route, factory.getRouteRefs());
            } else {
                System.out.println("No route found...");
            }
        } else {
            System.out.println("No path found...");
        }
        System.out.println("Route time: "+(System.currentTimeMillis() - time) + " ms");
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
