package Enums;

/**
 * Created by Jakob on 08-03-2017.
 */
public enum ToolType {
    SAVE("Save"),
    LOAD("Load"),
    UNKNOWN("unknown");

    private String tag;

    ToolType(String message) {
        tag = message;
    }

    @Override
    public String toString() {
        return tag;
    }
}
