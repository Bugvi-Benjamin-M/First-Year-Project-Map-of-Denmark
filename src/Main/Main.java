package Main;

import Controller.*;
import Helpers.FileHandler;
import Model.Model;
import View.PopupWindow;

import java.io.FileNotFoundException;

/**
 * Created by Jakob on 06-03-2017.
 */
public class Main {

    public static final String DEFAULT_RESOURCE = "/denmark-latest.zip";

    public static void main(String[] args) {

        Model.getInstance();

        WindowController windowController = MainWindowController.getInstance();

        CanvasController.getInstance(windowController.getWindow());
        ToolbarController.getInstance(windowController.getWindow());
        InfobarController.getInstance(windowController.getWindow());

        try {
            long startTime = System.currentTimeMillis();
            FileHandler.loadResource(DEFAULT_RESOURCE);
            long stopTime = System.currentTimeMillis();
            System.out.println("Loading time: " + (stopTime - startTime) + " ms");
        } catch (FileNotFoundException e) {
            PopupWindow.errorBox(null,"Program was not able to load up \""+DEFAULT_RESOURCE+"\"");
        }
    }

    public static void notifyThemeChange() {
        CanvasController.getInstance(MainWindowController.getInstance().getWindow()).themeHasChanged();
        ToolbarController.getInstance(MainWindowController.getInstance().getWindow()).themeHasChanged();
        MainWindowController.getInstance().themeHasChanged();
        InfobarController.getInstance(MainWindowController.getInstance().getWindow()).themeHasChanged();
        SettingsWindowController.getInstance().themeHasChanged();
    }

    public static void notifyKeyToggle(boolean status) {

    }
}
