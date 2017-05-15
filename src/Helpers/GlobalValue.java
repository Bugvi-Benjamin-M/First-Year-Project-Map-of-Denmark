package Helpers;

import Controller.MainWindowController;
import Enums.ZoomLevel;
import Helpers.Utilities.DebugWindow;

/**
 * GlobalValue is a collection of values that needs to be stored
 * across different packages and classes in the program.
 */
public class GlobalValue {

    // DEFAULT VALUES
    public static final String DEFAULT_COASTLINE_FILE = "/coastlines.zip";
    public static final String DEFAULT_BIN_RESOURCE = "/Danmark.bin";
    public static final String DEFAULT_RESOURCE = "/denmark-latest.zip";
    public static final boolean DEBUG_MODE_ACTIVE = false; // CHANGE ME TO PREVENT LOADING DEFAULT


    private static ZoomLevel zoomLevel = ZoomLevel.LEVEL_6;
    private static boolean didProgramLoadDefault = false;
    private static boolean isAddNewPointActive = false;
    private static boolean isLoading = false;
    private static boolean isSaving = false;

    /**
     * Change whether the program has loaded the default file
     */
    public static void setDidProgramLoadDefault(boolean truth_value)
    {
        didProgramLoadDefault = truth_value;
    }

    /**
     * Whether the program loaded the default file or not
     */
    public static boolean getDidProgramLoadDefault()
    {
        return didProgramLoadDefault;
    }

    /**
     * Determines the size of the large search field
     */
    public static int getSearchFieldLargeSize()
    {
        return (int)(MainWindowController.getInstance()
                         .getWindow()
                         .getFrame()
                         .getWidth()
            / 3.2);
    }

    /**
     * Determines the size of the smaller search field
     */
    public static int getSearchFieldSmallSize()
    {
        return (int)(MainWindowController.getInstance()
                         .getWindow()
                         .getFrame()
                         .getWidth()
            / 1.6);
    }

    /**
     * Retrieves the search field start x-coordinate in the
     * toolbar
     */
    public static int getSearchFieldStartX()
    {
        return (int)(MainWindowController.getInstance()
                         .getWindow()
                         .getFrame()
                         .getWidth()
            / 2.909);
    }

    /**
     * Retrieve the standard toolbar height
     */
    public static int getToolbarHeight() { return 100; }

    /**
     * Retrieve the current zoom level
     */
    public static ZoomLevel getZoomLevel() { return zoomLevel; }

    /**
     * Sets the current zoom level
     * @param zoom_factor the current zoom factor
     */
    public static void setZoomLevel(double zoom_factor)
    {
        ZoomLevel.setZoomFactor(zoom_factor);
        zoomLevel = ZoomLevel.getZoomLevel();
        DebugWindow.getInstance().setZoomLabel();
        DebugWindow.getInstance().setZoomFactorLabel();
    }

    /**
     * Retrieves the standard width of the information bar
     */
    public static int getLargeInformationBarWidth() {
        return 400;
    }

    /**
     * Changes whether add new point function is possible
     */
    public static void setIsAddNewPointActive(boolean status) {
        isAddNewPointActive = status;
    }

    /**
     * Find out whether it is possible to add new points
     */
    public static boolean isAddNewPointActive() {
        return isAddNewPointActive;
    }

    /**
     * Retrieves the standard width of the information bar
     * based upon the system type
     */
    public static int getSmallInformationBarHeight() {
        if(OSDetector.isMac()) return 150;
        else if(OSDetector.isWindows()) return 160;
        else return 150;
    }

    /**
     * Returns whether the program is currently loading
     */
    public static boolean isLoading() {
        return isLoading;
    }

    /**
     * Change whether the program is currently loading
     */
    public static void setIsLoading(boolean status) {
        isLoading = status;
    }

    /**
     * Returns whether the program is currently saving the model
     */
    public static boolean isSaving() {return isSaving;}

    /**
     * Changes whether the program is currently saving the model
     */
    public static void setIsSaving(boolean status) {isSaving = status;}
}
