package Enums;

/**
 * Class details:
 * The ZoomLevel enum represents how much the user has zoomed in or out
 * on the MapCanvas. The ZoomLevel is used by multiple different classes
 * and as such it is a sort of global value.
 *
 * The current ZoomLevel is, among other things, used for determining
 * which elements to be drawn and used for determining the complexity
 * of a coastline segment.
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 27-03-2017
 */
public enum ZoomLevel {
    LEVEL_0, // close and detailed
    LEVEL_1,
    LEVEL_2,
    LEVEL_3,
    LEVEL_4,
    LEVEL_5,
    LEVEL_6; // abstract far away

    private static double zoom_factor = 0;

    /**
     * Changes the current zoom factor value into something different
     */
    public static void setZoomFactor(double zoomFactor)
    {
        zoom_factor = zoomFactor;
    }

    /**
     * Returns the current zoom level based on the current zoom factor
     */
    public static ZoomLevel getZoomLevel()
    {
        //FIXME
        return ZoomLevel.LEVEL_2;
/*
        if (zoom_factor <= 150) {           // LEVEL_6
            return ZoomLevel.LEVEL_6;
        } else if (zoom_factor <= 250) {    // LEVEL_5
            return ZoomLevel.LEVEL_5;
        } else if (zoom_factor <= 350) {    // LEVEL_4
            return ZoomLevel.LEVEL_4;
        } else if (zoom_factor <= 400) {    // LEVEL_3
            return ZoomLevel.LEVEL_3;
        } else if (zoom_factor <= 450) {    // LEVEL_2
            return ZoomLevel.LEVEL_2;
        } else if (zoom_factor <= 570) {    // LEVEL_1
            return ZoomLevel.LEVEL_1;
        } else {                            // LEVEL_0
            return ZoomLevel.LEVEL_0;
        }*/
    }

    /**
     * Returns the current zoom factor value
     */
    public static double getZoomFactor() { return zoom_factor; }

    /**
     * Retrieves the number of nodes to skip whenever a path is out of view
     */
    public static int getNodesAtMaxLevel()
    {
        return 100;
    }

    /**
     * Retrieves an epsilon value for path generalization based
     * upon the current zoom level.
     */
    public double getEpsilonValueBasedOnZoomLevel()
    {
        switch (this) {
        case LEVEL_0:
            return 0.98;
        case LEVEL_1:
            return 0.75;
        case LEVEL_2:
            return 0.50;
        case LEVEL_3:
            return 0.40;
        case LEVEL_4:
            return 0.30;
        case LEVEL_5:
            return 0.20;
        default: // LEVEL 6
            return 0.10;
        }
    }

    /**
     * Generates a string representation of the current ZoomLevel
     */
    @Override
    public String toString()
    {
        return "Current Level: " +
                this.name();
    }
}
