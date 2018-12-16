package Enums.OSMEnums;

/**
 * Defines the different types of road objects. Is used
 * to draw specific roads in specific ways and on specific
 * zoom levels.
 */
public enum RoadType {
    MOTORWAY,           // Motorvej
    MOTORWAY_LINK,
    TRUNK_ROAD,         // Landevej
    TRUNK_ROAD_LINK,
    PRIMARY_ROAD,       // Hovedvej
    PRIMARY_ROAD_LINK,
    SECONDARY_ROAD,     // Større landeveje
    SECONDARY_ROAD_LINK,
    TERTIARY_ROAD,      // Landeveje
    TERTIARY_ROAD_LINK,
    UNCLASSIFIED_ROAD,  // Ikke-klassificeret veje, såsom mindre landevej
    RESIDENTIAL_ROAD,   // Byvej
    LIVING_STREET,      // Stillevej
    SERVICE_ROAD,       // Service vej
    RACEWAY,            // Racerbane
    PEDESTRIAN_STREET,  // Gågader
    TRACK,              // Markvej
    STEPS,              // Stier
    FOOTWAY,            // Gåstier
    BRIDLEWAY,          // Ridesti
    CYCLEWAY,           // Cykelstier

    PATH,               // Temporary setting
    ROAD,               // Temporary setting
    UNKNOWN,            // Ukendte elementer
}
