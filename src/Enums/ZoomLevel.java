package Enums;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 27-03-2017.
 * @project BFST
 */
public enum ZoomLevel {
    LEVEL_0(1)
    , // close and detailed
    LEVEL_1(1)
    ,
    LEVEL_2(2)
    ,
    LEVEL_3(5)
    ,
    LEVEL_4(11)
    ,
    LEVEL_5(17)
    ,
    LEVEL_6(21); // abstract far away

    private int nodesAtLevel;
    private static double zoom_factor = 0;

    ZoomLevel(int nodesAtLevel) { this.nodesAtLevel = nodesAtLevel; }

    public static ZoomLevel getZoomLevel()
    {
        if (zoom_factor <= 150) { // LEVEL_6
            return ZoomLevel.LEVEL_6;
        } else if (zoom_factor <= 250) { // LEVEL_5
            return ZoomLevel.LEVEL_5;
        } else if (zoom_factor <= 350) { // LEVEL_4
            return ZoomLevel.LEVEL_4;
        } else if (zoom_factor <= 400) { // LEVEL_3
            return ZoomLevel.LEVEL_3;
        } else if (zoom_factor <= 450) { // LEVEL_2
            return ZoomLevel.LEVEL_2;
        } else if (zoom_factor <= 570) { // LEVEL_1
            return ZoomLevel.LEVEL_1;
        } else { // LEVEL_0
            return ZoomLevel.LEVEL_0;
        }
    }

    public static void setZoomFactor(double zoomFactor)
    {
        zoom_factor = zoomFactor;
    }

    public static void resetZoomFactor() { zoom_factor = 0; }

    public static double getZoomFactor() { return zoom_factor; }

    public int getNodesAtLevel() { return nodesAtLevel; }

    public static int getNodesAtMaxLevel()
    {
        return ZoomLevel.LEVEL_6.getNodesAtLevel();
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Current Level: ");
        sb.append(this.name());
        sb.append(" (displaying every ");
        sb.append(nodesAtLevel);
        sb.append(" nodes)");
        return sb.toString();
    }

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
}
