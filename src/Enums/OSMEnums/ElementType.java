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

    HIGHWAY, //veje
    HIGHWAY_LEVEL_5_6, //Veje der kun vises på lvl 5 og 6

    RAIL,
    BRIDGE,
    PIER,

    PARKING,

    AMENITY,
}
