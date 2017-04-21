package Helpers;

import Controller.MainWindowController;
import Enums.ZoomLevel;
import Helpers.Utilities.DebugWindow;

import static Enums.ZoomLevel.LEVEL_3;

/**
 * Created by Búgvi Magnussen on 02-04-2017.
 */
public class GlobalValue {

    public static final String DEFAULT_COASTLINE_FILE = "/coastlines.zip";
    public static final String DEFAULT_BIN_RESOURCE = "/Danmark.bin";
    public static final String DEFAULT_RESOURCE = "/denmark-latest.zip";
    public static final boolean DEBUG_MODE_ACTIVE = false;  // CHANGE ME TO PREVENT LOADING DEFAULT
    public static final int MAX_ZOOM_DECREASE = -30;

    private static ZoomLevel zoomLevel = ZoomLevel.LEVEL_6;
    private static double maxZoom = MAX_ZOOM_DECREASE;

    private static boolean markCoastlines = false;
    private static boolean didProgramLoadDefault = false;

    public static void setDidProgramLoadDefault(boolean truth_value) {
        didProgramLoadDefault = truth_value;
    }

    public static boolean getDidProgramLoadDefault() {
        return didProgramLoadDefault;
    }

    public static void setMaxZoom(double zoom_value) {
        maxZoom = zoom_value;
    }

    public static double getMaxZoom() {return maxZoom;}

    public static int getSearchFieldLargeSize() {
        return (int) (MainWindowController.getInstance().getWindow().getFrame().getWidth() / 3.2);
    }

    public static int getSearchFieldSmallSize() {
        return (int) (MainWindowController.getInstance().getWindow().getFrame().getWidth() / 1.7);
    }

    public static int getSearchFieldStartX() {
        return (int) (MainWindowController.getInstance().getWindow().getFrame().getWidth() / 2.909);
    }


    public static int getToolbarHeight() {
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
