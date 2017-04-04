package Helpers;

import Controller.MainWindowController;
import Enums.ZoomLevel;
import Helpers.Utilities.DebugWindow;

import static Enums.ZoomLevel.*;

/**
 * Created by Búgvi Magnussen on 02-04-2017.
 */
public class GlobalValue {

    private static ZoomLevel zoomLevel = LEVEL_3;

    public static int getSearchFieldSize() {
        return (int) (MainWindowController.getInstance().getWindow().getFrame().getWidth() / 3.2);
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

}
