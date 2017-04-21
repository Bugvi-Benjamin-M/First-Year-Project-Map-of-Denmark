package Helpers;

import Controller.MainWindowController;
import Enums.ZoomLevel;
import Helpers.Utilities.DebugWindow;

import static Enums.ZoomLevel.LEVEL_3;

/**
 * Created by Búgvi Magnussen on 02-04-2017.
 */
public class GlobalValue {

    private static boolean markCoastlines = false;
    private static ZoomLevel zoomLevel = ZoomLevel.LEVEL_6;

    public static int getSearchFieldLargeSize() {
        return (int) (MainWindowController.getInstance().getWindow().getFrame().getWidth() / 3.2);
    }

    public static int getSearchFieldSmallSize() {
        return (int) (MainWindowController.getInstance().getWindow().getFrame().getWidth() / 1.7);
    }

    public static int getSearchFieldStartX() {
        return (int) (MainWindowController.getInstance().getWindow().getFrame().getWidth() / 2.909);
    }


    public static int getToolbarWidth() {
        return 100;
    }

    public static ZoomLevel getZoomLevel() {
        return zoomLevel;
    }

    public static void setZoomLevel(double zoom_factor) {
        ZoomLevel.setZoomFactor(zoom_factor);
        zoomLevel = ZoomLevel.getZoomLevel();
        DebugWindow.getInstance().setZoomLabel();
        DebugWindow.getInstance().setZoomFactorLabel();
    }

    public static void toogleMarkCoastlines() {
        markCoastlines = !markCoastlines;
    }

    public static boolean getMarkCoastlines() {
        return markCoastlines;
    }
}
