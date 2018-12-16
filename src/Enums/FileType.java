package Enums;


public enum FileType {
    OSM,
    ZIP,
    BIN;

    @Override
    public String toString()
    {
        return super.toString().toLowerCase();
    }
    public String getExtension() { return "." + toString(); }
}
