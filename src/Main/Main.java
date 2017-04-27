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
    private static boolean isArgumentGiven;
    private static boolean loadDefaultFile;

    public static long LOAD_TIME;
    private static SplashScreen screen;

    public static void main(String[] args)
    {

        long startTime = System.nanoTime();
        switch (args.length) {
            case 1:
                isArgumentGiven = true;
                loadDefaultFile = false;
                break;
            case 0:
                isArgumentGiven = false;
                loadDefaultFile = true;
                break;
            default:
                PopupWindow.infoBox(null, "The Number of Arguments Exceeded One.\n" +
                        "Loading Default File.", "Wrong Argument Type");
                args = null;
                isArgumentGiven = false;
                loadDefaultFile = true;
                break;
        }



        splashScreenInit();
        Model model = Model.getInstance();
        createControllers();
        PreferencesController.getInstance().setupPreferences();
        FileHandler.loadDefaultResource();
        if(isArgumentGiven) {
            try {
                loadDefaultFile = false;
                FileHandler.loadResource(args[0], false);
            } catch (NullPointerException e) {
                loadDefaultFile = true;
                PopupWindow.infoBox(null, "Failed to Load Given Argument. Initialising Standard Startup.", "Failed to Load Argument");
            }
        } else if(!PreferencesController.getInstance().getStartupFileNameSetting().equals(DefaultSettings.DEFAULT_FILE_NAME)) {
            try {
                loadDefaultFile = false;
                FileHandler.fileChooserLoad(PreferencesController.getInstance().getStartupFilePathSetting());
            } catch (Exception e) {
                e.printStackTrace();
                PopupWindow.infoBox(null, "Could Not Find Preferred Startup File: " + PreferencesController.getInstance().getStartupFileNameSetting() + ".\n" +
                        "Loading " + DefaultSettings.DEFAULT_FILE_NAME, "Preferred Startup File Not Found!");
                FileHandler.loadDefaultResource();
                loadDefaultFile = true;
            }
        }

        splashScreenDestruct();
        SwingUtilities.invokeLater(() -> {
            MainWindowController.getInstance().setupMainWindow();
            SettingsWindowController.getInstance().setupSettingsWindow();
            model.modelHasChanged();
            MainWindowController.getInstance().showWindow();
            if(loadDefaultFile) CanvasController.adjustToBounds();
            else CanvasController.adjustToDynamicBounds();

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
        InformationBarController.getInstance();
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
