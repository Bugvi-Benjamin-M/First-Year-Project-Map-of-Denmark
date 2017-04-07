package Enums;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 27-03-2017.
 * @project BFST
 */
public enum ZoomLevel {
    LEVEL_0(1),    // close and detailed
    LEVEL_1(5),
    LEVEL_2(13),
    LEVEL_3(21);    // abstract far away

    private int nodesAtLevel;
    private static double zoom_factor;

    ZoomLevel(int nodesAtLevel) {
        this.nodesAtLevel = nodesAtLevel;
    }

    public static ZoomLevel getZoomLevel() {
        if (zoom_factor <= 100) {
            return ZoomLevel.LEVEL_3;
        } else if (zoom_factor <= 200) {
            return ZoomLevel.LEVEL_2;
        } else if (zoom_factor <= 300) {
            return ZoomLevel.LEVEL_1;
        } else {
            return ZoomLevel.LEVEL_0;
        }
    }

    public static void setZoomFactor(double zoomFactor) {
        zoom_factor = zoomFactor;
    }

    public static void resetZoomFactor(){
        zoom_factor = 0;
    }

    public static double getZoomFactor() {
        return zoom_factor;
    }

    public int getNodesAtLevel() {
        return nodesAtLevel;
    }

    public static int getNodesAtMaxLevel() {
        return ZoomLevel.LEVEL_3.getNodesAtLevel();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Current Level: ");
        sb.append(this.name());
        sb.append(" (displaying every ");
        sb.append(nodesAtLevel);
        sb.append(" nodes)");
        return sb.toString();
    }
}
