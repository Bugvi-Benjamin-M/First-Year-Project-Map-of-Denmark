package Main;

import Controller.*;
import Exceptions.FileWasNotFoundException;
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

    private static SplashScreen screen;
    private static boolean programLoadedDefault;

    public static void main(String[] args) {


        long startTime = System.currentTimeMillis();

        splashScreenInit();

        Model model = Model.getInstance();

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

        MainWindowController.getInstance();

        CanvasController.getInstance(MainWindowController.getInstance().getWindow());
        ToolbarController.getInstance(MainWindowController.getInstance().getWindow());
        SearchController.getInstance(MainWindowController.getInstance().getWindow());
        InfobarController.getInstance(MainWindowController.getInstance().getWindow());

        CanvasController.adjustToBounds();
        model.modelHasChanged();

        long stopTime = System.currentTimeMillis();
        DebugWindow.getInstance().setLoadtimeLabel(stopTime - startTime);
    }

    private static void loadDefaultResource() throws FileWasNotFoundException {
        try {
            long startTime = System.currentTimeMillis();
            // FileHandler.loadResource(DEFAULT_RESOURCE);
            long stopTime = System.currentTimeMillis();
            System.out.println("Loading time: "+(stopTime-startTime)+" ms");
            throw new FileWasNotFoundException("");
        } catch (FileWasNotFoundException e) {
            throw new FileWasNotFoundException("Program was not able to load default resource \""+DEFAULT_RESOURCE+"\"" +
                    "\nLoading from coastlines instead.");
        }
    }

    private static void splashScreenDestruct() {
      screen.setScreenVisible(false);
      screen = null;
      MainWindowController.getInstance().getWindow().show();
    }

    private static void splashScreenInit() {
      ImageIcon myImage = new ImageIcon(Main.class.getResource("/denmark.gif"));
      screen = new SplashScreen(myImage);
      screen.setLocationRelativeTo(null);
      screen.setScreenVisible(true);
    }

    public static void notifyThemeChange() {
        CanvasController.getInstance(MainWindowController.getInstance().getWindow()).themeHasChanged();
        ToolbarController.getInstance(MainWindowController.getInstance().getWindow()).themeHasChanged();
        InfobarController.getInstance(MainWindowController.getInstance().getWindow()).themeHasChanged();
    }

    public static void notifyKeyToggle(boolean status) {
        CanvasController.getInstance(MainWindowController.getInstance().getWindow()).toggleKeyBindings(status);
        ToolbarController.getInstance(MainWindowController.getInstance().getWindow()).toggleKeyBindings(status);
        SettingsWindowController.getInstance().toggleKeyBindings(status);
        MainWindowController.getInstance().toggleKeyBindings(status);
        InfobarController.getInstance(MainWindowController.getInstance().getWindow()).toggleKeyBindings(status);
    }

    public static boolean didTheProgramLoadDefault() {
        return programLoadedDefault;
    }
}
