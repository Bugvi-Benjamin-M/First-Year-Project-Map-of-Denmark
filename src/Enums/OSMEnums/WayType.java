package Enums.OSMEnums;

/**
 * Created by Jakob on 06-03-2017.
 */
public enum WayType {  //TODO Change name to ElementType

    COASTLINE,
    COUNTRY_BOUNDARY_LAND,

    BUILDING, //pt alle bygninger

    UNKNOWN, //ukendte elementer

    WATER, //Søer og lign.

    //ROADS
    MOTORWAY, //Motorvej
    MOTORWAY_LINK,
    TRUNK_ROAD, //Landevej
    TRUNK_ROAD_LINK,
    PRIMARY_ROAD,
    PRIMARY_ROAD_LINK,
    SECONDARY_ROAD,
    SECONDARY_ROAD_LINK,
    TERTIARY_ROAD,
    TERTIARY_ROAD_LINK,
    UNCLASSIFIED_ROAD,
    RESIDENTIAL_ROAD,
    LIVING_STREET, //Stillevej
    SERVICE_ROAD,
    BUS_GUIDEWAY,
    ESCAPE, //sikkerhedsveje ?
    RACEWAY,
    PEDESTRIAN_STERET, //Gågader
    TRACK, //Markvej
    STEPS,
    FOOTWAY,
    BRIDLEWAY, //Ridesti
    CYCLEWAY,
    PATH, //Temporary setting
    ROAD //Temporary setting
}
