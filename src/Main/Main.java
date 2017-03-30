package Main;

import Controller.*;
import Helpers.FileHandler;
import Model.Model;
import View.PopupWindow;

import java.io.FileNotFoundException;

import javax.swing.UIManager;
import javax.swing.ImageIcon;


/**
 * Created by Jakob on 06-03-2017.
 */
public class Main {

    public static final String DEFAULT_RESOURCE = "/denmark-latest.zip";
    private static SplashScreen screen;


    public static void main(String[] args) {

        splashScreenInit();

        Model.getInstance();

        try {
            long startTime = System.currentTimeMillis();
            FileHandler.loadResource(DEFAULT_RESOURCE);
            long stopTime = System.currentTimeMillis();
            System.out.println("Loading time: " + (stopTime - startTime) + " ms");
        } catch (FileNotFoundException e) {
            PopupWindow.errorBox(null,"Program was not able to load up \""+DEFAULT_RESOURCE+"\"");
        }

        splashScreenDestruct();

        WindowController windowController = MainWindowController.getInstance();

        CanvasController.getInstance(windowController.getWindow());
        ToolbarController.getInstance(windowController.getWindow());
        InfobarController.getInstance(windowController.getWindow());

        CanvasController.adjustToBounds();
        CanvasController.resetBounds();
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
}
