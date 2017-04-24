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

    // BIOMES
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

    // PLACES - NAMES
    CITY_NAME, // navne på byer over 100k
    TOWN_NAME, // navne på byer 10k-100k
    VILLAGE_NAME, // navne på landsbyer -10k
    HAMLET_NAME, // navne på byer -200
    SUBURB_NAME, // navne på forstader
    QUARTER_NAME, // Kvarter (mindre end suburb men større end neighbourhood)
    NEIGHBOURHOOD_NAME, // navngivet nabolag

    HIGHWAY, // All roads (Used for nearestNeighbourSearch)
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
    PARKING,

    // AMENITIES
    BAR, // night
    NIGHT_CLUB, // Night
    FAST_FOOD, // Night

    HOSPITAL, // Default
    PLACE_OF_WORSHIP, // Default
}
