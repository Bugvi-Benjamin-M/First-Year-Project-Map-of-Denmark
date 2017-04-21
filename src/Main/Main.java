package Main;

import Controller.CanvasController;
import Controller.InfobarController;
import Controller.MainWindowController;
import Controller.SettingsWindowController;
import Controller.ToolbarControllers.ToolbarController;
import Exceptions.FileWasNotFoundException;
import Helpers.FileHandler;
import Helpers.Utilities.DebugWindow;
import Helpers.Utilities.FPSCounter;
import Model.Model;
import View.PopupWindow;

import javax.swing.*;


import Parser.Address;

/**
 * Created by Jakob on 06-03-2017.
 */
public class Main {

    public static final FPSCounter FPS_COUNTER = new FPSCounter();
    private static final String DEFAULT_RESOURCE = "/denmark-latest.zip";

    private static final boolean DEBUG_MODE_ACTIVE = false;  // CHANGE ME TO PREVENT LOADING DEFAULT
    private static final boolean SAVE_AFTER_LOAD = true;     // CHANGE ME TO PREVENT SAVING BIN

    public static long LOAD_TIME;
    private static SplashScreen screen;
    private static boolean programLoadedDefault;

    public static void main(String[] args) {

        long startTime = System.nanoTime();

        splashScreenInit();

        Model model = Model.getInstance();
        SwingUtilities.invokeLater(() -> {
        try {
            loadDefaultResource();
            splashScreenDestruct();
            programLoadedDefault = true;
        } catch (FileWasNotFoundException e) {
            splashScreenDestruct();
            PopupWindow.warningBox(null,e.getMessage());
            model.loadFromCoastlines();
            programLoadedDefault = false;
        }
            createControllers();
            MainWindowController.getInstance().setupMainWindow();
            SettingsWindowController.getInstance().setupSettingsWindow();
            model.modelHasChanged();
            MainWindowController.getInstance().showWindow();
            MainWindowController.getInstance().transferFocusToMapCanvas();
        });

            LOAD_TIME = System.nanoTime() - startTime;
            System.out.println("System loadtime: "+(LOAD_TIME / 1000000) + " ms");
            DebugWindow.getInstance().setLoadtimeLabel();
    }

    private static void createControllers() {
        MainWindowController.getInstance();
        ToolbarController.getInstance();
        CanvasController.getInstance();
        InfobarController.getInstance();
        SettingsWindowController.getInstance();
    }

    private static void loadDefaultResource() throws FileWasNotFoundException {
        try {
            long startTime = System.currentTimeMillis();
            if (!DEBUG_MODE_ACTIVE) {
                try {
                    FileHandler.loadBin("/Danmark.bin", true);
                } catch (FileWasNotFoundException e) {
                    FileHandler.loadResource(DEFAULT_RESOURCE, true);
                }
            }
            long stopTime = System.currentTimeMillis();
            System.out.println("Resource load time: "+(stopTime-startTime)+" ms");
            if (DEBUG_MODE_ACTIVE) throw new FileWasNotFoundException("");
        } catch (FileWasNotFoundException e) {
            throw new FileWasNotFoundException("Program was not able to load default resource \""+DEFAULT_RESOURCE+"\"" +
                    "\nLoading from coastlines instead.");
        }
    }

    private static void splashScreenDestruct() {
      screen.setScreenVisible(false);
      screen = null;
    }

    private static void splashScreenInit() {
      ImageIcon myImage = new ImageIcon(Main.class.getResource("/middelfart.jpg")); //denmark.gif
      screen = new SplashScreen(myImage);
      screen.setLocationRelativeTo(null);
      screen.setScreenVisible(true);
    }

    public static boolean didTheProgramLoadDefault() {
        return programLoadedDefault;
    }
}
