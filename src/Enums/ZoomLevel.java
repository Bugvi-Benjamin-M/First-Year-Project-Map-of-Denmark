package Enums;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 27-03-2017.
 * @project BFST
 */
public enum ZoomLevel {
    LOW_LEVEL,
    LEVEL_2,
    LEVEL_3,
    LEVEL_4,
    HIGH_LEVEL;

    public int getAmountOfNodesAtLevel(ZoomLevel level) {
        switch (level) {
            case LOW_LEVEL:
                return 1;
            case LEVEL_2:
                return 3;
            case LEVEL_3:
                return 8;
            case LEVEL_4:
                return 13;
            case HIGH_LEVEL:
                return 21;
        }
        throw new IllegalArgumentException();
    }
}
