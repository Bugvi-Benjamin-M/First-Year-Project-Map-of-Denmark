package Enums;


public enum ToolType {
    SAVE("Save")
    ,
    LOAD("Load")
    ,
    SETTINGS("Settings")
    ,
    SEARCHBAR("Search")
    ,
    UNKNOWN("Unknown")
    ,
    MENU("Menu")
    ,
    SEARCHBUTTON("Search")
    ,
    POI("Places")
    ,
    ROUTES("Routes");

    private String tag;

    ToolType(String message) { tag = message; }

    @Override
    public String toString()
    {
        return tag;
    }
}
