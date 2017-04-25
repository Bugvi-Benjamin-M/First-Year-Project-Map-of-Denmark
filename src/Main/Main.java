package Main;

import Controller.*;
import Controller.ToolbarControllers.ToolbarController;
import Helpers.DefaultSettings;
import Helpers.FileHandler;
import Helpers.Utilities.DebugWindow;
import Helpers.Utilities.FPSCounter;
import Model.Model;
import View.PopupWindow;

import javax.swing.*;

/**
 * Created by Jakob on 06-03-2017.
 */
public class Main {

    public static final FPSCounter FPS_COUNTER = new FPSCounter();
    private static final String DEFAULT_RESOURCE = "/denmark-latest.zip";

    private static final boolean DEBUG_MODE_ACTIVE = false; // CHANGE ME TO PREVENT LOADING DEFAULT
    private static final boolean SAVE_AFTER_LOAD = true; // CHANGE ME TO PREVENT SAVING BIN

    public static long LOAD_TIME;
    private static SplashScreen screen;

    public static void main(String[] args)
    {

        long startTime = System.nanoTime();

        splashScreenInit();
        Model model = Model.getInstance();
        createControllers();
        PreferencesController.getInstance().setupPreferences();
        if(PreferencesController.getInstance().getStartupFileNameSetting().equals(DefaultSettings.DEFAULT_FILE_NAME)) FileHandler.loadDefaultResource();
        else {
            try {
                FileHandler.fileChooserLoad(PreferencesController.getInstance().getStartupFilePathSetting());
            } catch (RuntimeException e) {
                PopupWindow.infoBox(null, "Could Not Find Preferred Startup File: " + PreferencesController.getInstance().getStartupFileNameSetting() + ".\n" +
                "Loading " + DefaultSettings.DEFAULT_FILE_NAME, "Preferred Startup File Not Found!");
                FileHandler.loadDefaultResource();
            }
        }
        splashScreenDestruct();
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
        InfobarController.getInstance();
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
