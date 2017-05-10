package Enums.OSMEnums;

/**
 * Created by Jakob on 09-05-2017.
 */
public enum AmenityType {

    BAR, // night
    NIGHT_CLUB, // Night
    FAST_FOOD, // Night

    HOSPITAL, // Default
    PLACE_OF_WORSHIP, // Default
    PARKING_AMENITY, //Default
    SPORT_AMENITY, //Default
    RAILWAY_STATION, //Default
    RAILWAY_STATION_AREA, //Default
    AIRPORT_AMENITY, //Default
    UNIVERSITY, //Default

    // PLACES - NAMES
    CITY_NAME, // navne på byer over 100k
    TOWN_NAME, // navne på byer 10k-100k
    VILLAGE_NAME, // navne på landsbyer -10k
    HAMLET_NAME, // navne på byer -200
    SUBURB_NAME, // navne på forstader
    QUARTER_NAME, // Kvarter (mindre end suburb men større end neighbourhood)
    NEIGHBOURHOOD_NAME, // navngivet nabolag

    UNKNOWN, // ukendte elementer
}
