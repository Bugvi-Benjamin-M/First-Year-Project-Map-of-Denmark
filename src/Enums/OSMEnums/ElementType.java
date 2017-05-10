package Enums.OSMEnums;

/**
 * Created by Jakob on 06-03-2017.
 */
public enum ElementType {
    COASTLINE,
    COUNTRY_BOUNDARY_LAND,

    BUILDING, // pt alle bygninger

    UNKNOWN, // ukendte elementer

    WATER, // Søer og lign.
    WETLAND, //Sump, marsk etc.

    // BIOMES AREAS
    PARK,
    FOREST,
    GRASSLAND,
    HEATH,
    GRASS,
    FARMLAND,
    MEADOW,
    COMMON_LAND,
    BEACH,
    SPORTSPITCH,
    SPORTSTRACK,
    PLAYGROUND,
    HEDGE,
    RIVER,
    //ARTIFICIAL BIOME (SHARES THE SAME FEATURES AS BIOME, BUT IS NOT NATURAL).
    AIRPORT_TAXIWAY,
    AIRPORT_RUNWAY,
    DRAIN,

    // ROADS
    MOTORWAY, // Motorvej
    MOTORWAY_LINK,
    TRUNK_ROAD, // Landevej
    TRUNK_ROAD_LINK,
    PRIMARY_ROAD,
    PRIMARY_ROAD_LINK,
    SECONDARY_ROAD,
    SECONDARY_ROAD_LINK,
    TERTIARY_ROAD,
    TERTIARY_ROAD_LINK,
    UNCLASSIFIED_ROAD,
    RESIDENTIAL_ROAD,
    LIVING_STREET, // Stillevej
    SERVICE_ROAD,
    BUS_GUIDEWAY,
    ESCAPE, // sikkerhedsveje ?
    RACEWAY,
    PEDESTRIAN_STREET, // Gågader
    TRACK, // Markvej
    STEPS,
    FOOTWAY,
    BRIDLEWAY, // Ridesti
    CYCLEWAY,
    PATH, // Temporary setting
    ROAD, // Temporary setting

    RAIL,
    BRIDGE,
    PIER,

    PARKING,

    AMENITY,
}
