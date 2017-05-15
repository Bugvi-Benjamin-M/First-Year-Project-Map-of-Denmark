package Main;

import Controller.*;
import Controller.ToolbarControllers.ToolbarController;
import Exceptions.FileWasNotFoundException;
import Helpers.DefaultSettings;
import Helpers.FileHandler;
import Helpers.HelperFunctions;
import Helpers.Utilities.DebugWindow;
import Helpers.Utilities.FPSCounter;
import Model.Model;
import View.PopupWindow;

import javax.swing.*;
import java.io.FileNotFoundException;


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
        //CanvasController.getInstance().setupAsObserver();
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
            System.out.println("System loadtime: " +
                    HelperFunctions.convertNanotimeToTime(LOAD_TIME));
            DebugWindow.getInstance().setLoadtimeLabel();
            CanvasExtrasController.getInstance().updateDistance();
        });
    }

    private static void createControllers()
    {
        PreferencesController.getInstance();
        MainWindowController.getInstance();
        ToolbarController.getInstance();
        CanvasController.getInstance();
        SettingsWindowController.getInstance();
        JourneyPlannerBarController.getInstance();
        CanvasExtrasController.getInstance();
    }

    public static void splashScreenDestruct()
    {
        screen.setScreenVisible(false);
        screen = null;
    }

    public static void splashScreenInit()
    {
        ImageIcon myImage = new ImageIcon(Main.class.getResource("/vejle.jpg")); // denmark.gif
        screen = new SplashScreen(myImage);
        screen.setLocationRelativeTo(null);
        screen.setScreenVisible(true);
    }
}
