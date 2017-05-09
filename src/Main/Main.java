package Main;

import Controller.*;
import Controller.ToolbarControllers.ToolbarController;
import Exceptions.FileWasNotFoundException;
import Helpers.DefaultSettings;
import Helpers.FileHandler;
import Helpers.Utilities.DebugWindow;
import Helpers.Utilities.FPSCounter;
import Model.Addresses.Value;
import Model.Elements.RoadEdge;
import Model.Model;
import RouteSearch.RoadGraphFactory;
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

            try {
                // dijkstra(model);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static void dijkstra(Model model){
        long time = System.currentTimeMillis();

        RoadGraphFactory factory = model.getGraphFactory();
        System.out.println("starting route search...");

        ArrayList<Value> list = Model.getInstance().getTst().get("Rued Langgaards Vej 7");

        for(Value val : list){
            System.out.println("X: " + val.getX() + "; Y: " + val.getY());
        }

        RoadEdge start = factory.getRoad("Eratosvej");
        RoadEdge end = factory.getRoad("Vester Oddevej");
        new Thread() {
            public void run() {
                RouteSearch.RouteDijkstra dijk = new RouteSearch.RouteDijkstra(
                        factory.getGraph(), start.getEither(),
                        end.getEither(), Enums.TravelType.VEHICLE);
                Iterable<RoadEdge> iterator = dijk.path();
                if (iterator != null) {
                    factory.setRoute(iterator);
                    List<RoadEdge> route = factory.getRoute();
                    if (route != null && route.size() != 0) {
                        for (int i = 0; i < route.size(); i++) {
                            System.out.println(route.get(i).getName() +
                                ": "+route.get(i).getLength()+" m");
                        }
                        CanvasController.getInstance().getMapCanvas().setRoute(route);
                    } else {
                        System.out.println("No route found...");
                    }
                } else {
                    System.out.println("No path found...");
                }
                System.out.println("Route time: "+(System.currentTimeMillis() - time) + " ms");
               // JourneyPlannerBarController.printRouteDescription();
            }
        }.start();
    }

    private static void createControllers()
    {
        PreferencesController.getInstance();
        MainWindowController.getInstance();
        ToolbarController.getInstance();
        CanvasController.getInstance();
        SettingsWindowController.getInstance();
        JourneyPlannerBarController.getInstance();
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
