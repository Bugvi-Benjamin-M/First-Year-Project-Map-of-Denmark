package Enums;


public enum BoundType {
    MIN_LONGITUDE("minlon")
    ,
    MAX_LONGITUDE("maxlon")
    ,
    MIN_LATITUDE("minlat")
    ,
    MAX_LATITUDE("maxlat");

    private String type;

    BoundType(String type) { this.type = type; }

    @Override
    public String toString()
    {
        return type;
    }
}
