package OSM;

import Enums.BoundType;
import Enums.OSMEnums.ElementType;
import Helpers.LongToPointMap;
import Helpers.Shapes.MultiPolygonApprox;
import Helpers.Shapes.PolygonApprox;
import KDtree.NodeGenerator;
import KDtree.Pointer;
import Model.Addresses.TenarySearchTrie;
import Model.Addresses.Value;
import Model.Elements.*;
import Model.Model;
import RouteSearch.Graph;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.awt.geom.Point2D;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jakob on 06-03-2017.
 */
public final class OSMHandler implements ContentHandler {
    private static OSMHandler handler;
    private NodeGenerator nodeGenerator;

    private LongToPointMap idToNode;
    private Map<Long, OSMWay> idToWay;
    private Map<Long,OSMWayRef> idToRefWay;
    private OSMWay way;
    private OSMWayRef refWay;
    private List<OSMWayRef> refRelation;
    private OSMRelation relation;
    private OSMNode node;
    private ElementType elementType;
    private String name;
    private ElementType place;
    private float longitudeFactor;
    private Model model;
    private int loadednodes, loadedRelations, loadedWays;
    private boolean defaultMode;
    private boolean initialized;
    private float latitude;
    private float longitude;
    private ArrayList<Pointer> cityNames;
    private ArrayList<Pointer> townNames;
    private ArrayList<Pointer> villageNames;
    private ArrayList<Pointer> hamletNames;
    private ArrayList<Pointer> suburbNames;
    private ArrayList<Pointer> quarterNames;
    private ArrayList<Pointer> neighbourhoodNames;
    private ArrayList<Pointer> bars;
    private ArrayList<Pointer> nightClubs;
    private ArrayList<Pointer> fastFoods;
    private boolean specialRelationCase = false;
    private boolean isArea = false;
    private boolean isOneWay = false;
    private boolean isWalkingAllowed = false;
    private boolean isCycleAllowed = false;
    private boolean isVehicleAllowed = false;
    private int maxSpeed;

    private ArrayList<Pointer> railwayStations;
    private ArrayList<Road> roads;

    private String roadName;
    private String roadNumber;
    private String zipCode;
    private String cityName;

    private boolean isAddress;
    private int indexCounter = 1;

    private Graph graph;

    private OSMHandler() {
        idToNode = new LongToPointMap(22);
        idToWay = new HashMap<>();
        idToRefWay = new HashMap<>();
        model = Model.getInstance();
        model.setTst(new TenarySearchTrie());
        nodeGenerator = new NodeGenerator();
        cityNames = new ArrayList<>();
        townNames  = new ArrayList<>();
        villageNames = new ArrayList<>();
        hamletNames = new ArrayList<>();
        suburbNames = new ArrayList<>();
        quarterNames = new ArrayList<>();
        neighbourhoodNames = new ArrayList<>();
        bars = new ArrayList<>();
        nightClubs = new ArrayList<>();
        fastFoods = new ArrayList<>();
        railwayStations = new ArrayList<>();
        roads = new ArrayList<>();
    }

    public void parseDefault(Boolean mode){
        defaultMode = mode;
    }

    public float getLongitudeFactor(){
        return longitudeFactor;
    }

    /**
   * Returns the OSMHandler, which is a singleton.
   * @return OSMHandler.
   */
    public static OSMHandler getInstance()
    {
        if (handler == null) {
            handler = new OSMHandler();
        }
        return handler;
    }

    public static void resetInstance() { handler = null; }

    @Override
    public void setDocumentLocator(Locator locator) {}

    @Override
    public void startDocument() throws SAXException {}

    @Override
    public void endDocument() throws SAXException {
        graph = new Graph();
        for (Road road:roads) {
            graph.addEdges(road);
        }
        model.setGraph(graph);
    }

    @Override
    public void startPrefixMapping(String prefix, String uri)
        throws SAXException {}

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {}

    @Override
    public void startElement(String uri, String localName, String qName,
        Attributes atts) throws SAXException
    {
        switch (qName) {
        case "bounds":
            float minLatitude, maxLatitude, minLongitude, maxLongitude;
            minLatitude = Float.parseFloat(atts.getValue("minlat"));
            maxLatitude = Float.parseFloat(atts.getValue("maxlat"));
            minLongitude = Float.parseFloat(atts.getValue("minlon"));
            maxLongitude = Float.parseFloat(atts.getValue("maxlon"));
            if (defaultMode) {
                float avglat = minLatitude + (maxLatitude - minLatitude) / 2;
                longitudeFactor = (float)Math.cos(avglat / 180 * Math.PI);
            } else {
                float avglat = model.getMinLatitude(false) + (model.getMaxLatitude(false) - model.getMinLatitude(false)) / 2;
                longitudeFactor = (float)Math.cos(avglat / 180 * Math.PI);
            }
            minLongitude *= longitudeFactor;
            maxLongitude *= longitudeFactor;
            minLatitude = -minLatitude;
            maxLatitude = -maxLatitude;
            if (defaultMode) {
                model.setBound(BoundType.MIN_LONGITUDE, minLongitude);
                model.setBound(BoundType.MAX_LONGITUDE, maxLongitude);
                model.setBound(BoundType.MIN_LATITUDE, minLatitude);
                model.setBound(BoundType.MAX_LATITUDE, maxLatitude);
            }
            model.setDynamicBound(BoundType.MIN_LONGITUDE, minLongitude);
            model.setDynamicBound(BoundType.MAX_LONGITUDE, maxLongitude);
            model.setDynamicBound(BoundType.MIN_LATITUDE, minLatitude);
            model.setDynamicBound(BoundType.MAX_LATITUDE, maxLatitude);

            break;
        case "node":
            long id = Long.parseLong(atts.getValue("id"));
            latitude = Float.parseFloat(atts.getValue("lat"));
            longitude = Float.parseFloat(atts.getValue("lon"));
            idToNode.put(id, longitude * longitudeFactor, -latitude);

            if (defaultMode) {
                nodeGenerator.addPoint(
                    new Point2D.Float(longitude * longitudeFactor, -latitude));
            }

            name = "";
            roadName = "";
            roadNumber = "";
            cityName = "";
            zipCode = "";
            isAddress = false;
            place = ElementType.UNKNOWN;

            if (loadednodes % 1000 == 0) System.out.println("NumNodes: "+loadednodes);
            loadednodes++;
            break;
        case "relation":
            long relationID = Long.parseLong(atts.getValue("id"));
            if (relationID == 2365410) { // Dont draw Sydhavnen (Only works with coastlines)
                specialRelationCase = true;
            }
            name = "";
            isArea = false;
            isVehicleAllowed = false;
            isWalkingAllowed = false;
            isCycleAllowed = false;
            maxSpeed = 0;
            relation = new OSMRelation(relationID);
            refRelation = new ArrayList<>();
            elementType = ElementType.UNKNOWN;

            if (loadedRelations % 100 == 0) System.out.println("NumRelations: "+loadedRelations);
            loadedRelations++;
            break;
        case "way":
            if (!initialized && defaultMode) {
                nodeGenerator.initialise();
                for (ElementType type : ElementType.values()) {
                    nodeGenerator.setupTree(model.getElements().get(type));
                }
                initialized = true;

                for (Pointer p : cityNames) {
                    model.getElements().get(ElementType.CITY_NAME).putPointer(p);
                }
                cityNames.clear();

                for (Pointer p : townNames) {
                    model.getElements().get(ElementType.TOWN_NAME).putPointer(p);
                }
                townNames.clear();

                for (Pointer p : villageNames) {
                    model.getElements().get(ElementType.VILLAGE_NAME).putPointer(p);
                }
                villageNames.clear();

                for (Pointer p : hamletNames) {
                    model.getElements().get(ElementType.HAMLET_NAME).putPointer(p);
                }
                hamletNames.clear();

                for (Pointer p : suburbNames) {
                    model.getElements().get(ElementType.SUBURB_NAME).putPointer(p);
                }
                suburbNames.clear();

                for (Pointer p : quarterNames) {
                    model.getElements().get(ElementType.QUARTER_NAME).putPointer(p);
                }
                quarterNames.clear();

                for (Pointer p : neighbourhoodNames) {
                    model.getElements().get(ElementType.NEIGHBOURHOOD_NAME).putPointer(p);
                }
                neighbourhoodNames.clear();

                for (Pointer p : bars) {
                    model.getElements().get(ElementType.BAR).putPointer(p);
                }
                bars.clear();

                for (Pointer p : nightClubs) {
                    model.getElements().get(ElementType.NIGHT_CLUB).putPointer(p);
                }
                nightClubs.clear();

                for (Pointer p : fastFoods) {
                    model.getElements().get(ElementType.FAST_FOOD).putPointer(p);
                }
                fastFoods.clear();

                for (Pointer p : railwayStations){
                    model.getElements().get(ElementType.RAILWAY_STATION).putPointer(p);
                }
                railwayStations.clear();
            }

            way = new OSMWay();
            refWay = new OSMWayRef();
            id = Long.parseLong(atts.getValue("id"));
            elementType = ElementType.UNKNOWN;
            place = ElementType.UNKNOWN;
            idToWay.put(id, way);
            name = "";
            isArea = false;
            isVehicleAllowed = false;
            isWalkingAllowed = false;
            isCycleAllowed = false;
            maxSpeed = 0;

            if (loadedWays % 1000 == 0) System.out.println("NumWays: "+loadedWays);
            loadedWays++;
            break;
        case "nd":
            long ref = Long.parseLong(atts.getValue("ref"));
            way.add(idToNode.get(ref));
            refWay.add(idToNode.get(ref),ref);
            break;
        case "tag":
            String k = atts.getValue("k");
            String v = atts.getValue("v");
            switch (k) {
                case "highway":
                    determineHighway(v);
                    break;
                case "railway":
                    determineRailway(v);
                    break;
                case "name":
                    name = v;
                    break;
                case "natural":
                    determineNatural(v);
                    break;
                case "barrier":
                    determineBarrier(v);
                    break;
                case "place":
                    determinePlace(v);
                    break;
                case "building":
                    elementType = ElementType.BUILDING;
                    break;
                case "man_made":
                    determineManMade(v);
                    break;
                case "aeroway":
                    determineAirport(v);
                    break;
                case "waterway":
                    determineWaterWay(v);
                    break;
                case "leisure":
                    determineLeisure(v);
                    break;
                case "landuse":
                    determineLanduse(v);
                    break;
                case "amenity":
                    determineAmenity(v);
                    break;
                case "area":
                    switch (v) {
                        case ("yes"):
                            isArea = true;
                            break;
                    }
                    break;
                case "addr:city":
                    cityName = v;
                    isAddress = true;
                    break;
                case "addr:housenumber":
                    roadNumber = v;
                    isAddress = true;
                    break;
                case "addr:postcode":
                    zipCode = v;
                    isAddress = true;
                    break;
                case "addr:street":
                    roadName = v;
                    isAddress = true;
                    break;
                case "bicycle":
                    isCycleAllowed = !v.equals("no");
                    break;
                case "foot":
                    isWalkingAllowed = !v.equals("no");
                    break;
                case "vehicle":
                    isVehicleAllowed = !v.equals("no");
                    break;
                case "cycleway":
                    isCycleAllowed = !v.equals("no");
                    break;
                case "maxspeed":
                    try {
                        int i = Integer.parseInt(v);
                        maxSpeed = i;
                    } catch (NumberFormatException e) {
                        // e.printStackTrace();
                    }
                    break;
                case "oneway":
                    isOneWay = v.equals("yes");
                    break;
            }
            break;
        case "member":
            ref = Long.parseLong(atts.getValue("ref"));
            relation.add(idToWay.get(ref));
            refRelation.add(idToRefWay.get(ref));
            break;
        }
    }

    private void determineLanduse(String value) {
        switch (value) {
            case "forest":
                elementType = ElementType.FOREST;
                break;
            case "farmland":
            case "farmyard":
                elementType = ElementType.FARMLAND;
                break;
            case "meadow":
                elementType = ElementType.MEADOW;
                break;
            case "grass":
            case "recreation_ground":
            case "vineyard":
            case "allotments":
            case "cemetery":
            case "greenhouse_horticulture":
            case "orchard":
            case "village_green":
                elementType = ElementType.GRASS;
                break;
            case "basin":
                elementType = ElementType.WATER;
                break;
        }
    }

    private void determineManMade(String value){
        switch (value){
            case "bridge":
                elementType = ElementType.BRIDGE;
                break;
            case "pier":
                elementType = ElementType.PIER;
                break;
        }
    }

    private void determineBarrier (String value){
        switch (value){
            case "hedge":
                elementType = ElementType.HEDGE;
                break;
        }
    }

    private void determineLeisure(String value) {
        switch (value) {
            case "park":
            case "nature_reserve":
            case "dog_park":
            case "garden":
                elementType = ElementType.PARK;
                break;
            case "common":
                elementType = ElementType.COMMON_LAND;
                break;
            case "pitch":
                elementType = ElementType.SPORTSPITCH;
                place = ElementType.SPORT_AMENITY;
                break;
            case "playground":
                elementType = ElementType.PLAYGROUND;
                break;
            case "swimming_pool":
                elementType = ElementType.WATER;
                break;
            case "track":
                elementType = ElementType.SPORTSTRACK;
                break;
        }
    }

    private void determineRailway(String value) {
        switch (value) {
            case "light_rail":
            case "rail":
            case "tram":
            case "monorail":
                elementType = ElementType.RAIL;
                break;
            case "station":
                place = ElementType.RAILWAY_STATION;
                break;
        }
    }

    private void determineAmenity(String value) {
        switch (value) {
            case "bar":
            case "biergarten":
            case "pub":
                place = ElementType.BAR;
                break;
            case "nightclub":
                place = ElementType.NIGHT_CLUB;
                break;
            case "fast_food":
                place = ElementType.FAST_FOOD;
                break;
            case "hospital":
                place = ElementType.HOSPITAL;
                break;
            case "place_of_worship":
                place = ElementType.PLACE_OF_WORSHIP;
                break;
            case "parking":
                place = ElementType.PARKING_AMENITY;
                elementType = ElementType.PARKING;
                break;
        }
    }

    private void determinePlace(String value) {
        switch (value) {
            case "city":
                place = ElementType.CITY_NAME;
                break;
            case "town":
                place = ElementType.TOWN_NAME;
                break;
            case "village":
                place = ElementType.VILLAGE_NAME;
                break;
            case "hamlet":
                place = ElementType.HAMLET_NAME;
                break;
            case "suburb":
                place = ElementType.SUBURB_NAME;
                break;
            case "quarter":
                place = ElementType.QUARTER_NAME;
                break;
            case "neighbourhood":
                place = ElementType.NEIGHBOURHOOD_NAME;
                break;
        }
    }

    private void determineNatural(String value) {
        elementType = elementType.UNKNOWN;
        switch (value) {
            case "water":
                elementType = ElementType.WATER;
                break;
            case "wetland":
                elementType = ElementType.WETLAND;
                break;
            case "grassland":
                elementType = ElementType.GRASSLAND;
                break;
            case "heath":
                elementType = ElementType.HEATH;
                break;
            case "wood":
            case "scrub":
                elementType = ElementType.FOREST;
                break;
            case "tree_row":
                elementType = ElementType.HEDGE;
                break;
            case "beach":
            case "sand":
                elementType = ElementType.BEACH;
                break;
        }
    }

    private void determineWaterWay(String value){
        elementType = ElementType.UNKNOWN;
        switch (value){
            case "river":
            case "riverbank":
            case "stream":
                elementType = ElementType.RIVER;
                break;
            case "drain":
            case "canal":
                elementType = ElementType.DRAIN;
                break;
        }
    }

    private void determineAirport(String value){
        elementType = ElementType.UNKNOWN;
        switch (value){
            case "runway":
                elementType = ElementType.AIRPORT_RUNWAY;
                break;
            case "taxiway":
                elementType = ElementType.AIRPORT_TAXIWAY;
                break;
            case "aerodrome":
                place = ElementType.AIRPORT_AMENITY;
                break;
        }
    }

    private void determineHighway(String value) {
        elementType = ElementType.UNKNOWN;
        switch (value) {
            case "motorway":
                elementType = ElementType.MOTORWAY;
                isVehicleAllowed = true;
                if (maxSpeed == 0) maxSpeed = 130;
                break;
            case "motorway_link":
                elementType = ElementType.MOTORWAY_LINK;
                isVehicleAllowed = true;
                if (maxSpeed == 0) maxSpeed = 130;
                break;
            case "trunk":
                elementType = ElementType.TRUNK_ROAD;
                isVehicleAllowed = true;
                if (maxSpeed == 0) maxSpeed = 90;
                break;
            case "trunk_link":
                elementType = ElementType.TRUNK_ROAD_LINK;
                isVehicleAllowed = true;
                if (maxSpeed == 0) maxSpeed = 90;
                break;
            case "primary":
                elementType = ElementType.PRIMARY_ROAD;
                isVehicleAllowed = true;
                isWalkingAllowed = true;
                isCycleAllowed = true;
                if (maxSpeed == 0) maxSpeed = 80;
                break;
            case "primary_link":
                elementType = ElementType.PRIMARY_ROAD_LINK;
                isVehicleAllowed = true;
                isWalkingAllowed = true;
                isCycleAllowed = true;
                if (maxSpeed == 0) maxSpeed = 80;
                break;
            case "secondary":
                elementType = ElementType.SECONDARY_ROAD;
                isVehicleAllowed = true;
                isWalkingAllowed = true;
                isCycleAllowed = true;
                if (maxSpeed == 0) maxSpeed = 80;
                break;
            case "seconday_link":
                elementType = ElementType.SECONDARY_ROAD_LINK;
                isVehicleAllowed = true;
                isWalkingAllowed = true;
                isCycleAllowed = true;
                if (maxSpeed == 0) maxSpeed = 80;
                break;
            case "tertiary":
                elementType = ElementType.TERTIARY_ROAD;
                isVehicleAllowed = true;
                isWalkingAllowed = true;
                isCycleAllowed = true;
                if (maxSpeed == 0) maxSpeed = 80;
                break;
            case "tertiary_link":
                elementType = ElementType.TERTIARY_ROAD_LINK;
                isVehicleAllowed = true;
                isWalkingAllowed = true;
                isCycleAllowed = true;
                if (maxSpeed == 0) maxSpeed = 80;
                break;
            case "unclassified":
                elementType = ElementType.UNCLASSIFIED_ROAD;
                isVehicleAllowed = true;
                isWalkingAllowed = true;
                isCycleAllowed = true;
                if (maxSpeed == 0) maxSpeed = 80;
                break;
            case "residential":
                elementType = ElementType.RESIDENTIAL_ROAD;
                isVehicleAllowed = true;
                isWalkingAllowed = true;
                isCycleAllowed = true;
                if (maxSpeed == 0) maxSpeed = 50;
                break;
            case "living_street":
                elementType = ElementType.LIVING_STREET;
                isVehicleAllowed = true;
                isWalkingAllowed = true;
                isCycleAllowed = true;
                if (maxSpeed == 0) maxSpeed = 50;
                break;
            case "service":
                elementType = ElementType.SERVICE_ROAD;
                isVehicleAllowed = true;
                isWalkingAllowed = true;
                isCycleAllowed = true;
                if (maxSpeed == 0) maxSpeed = 50;
                break;
            case "bus_guideway":
                elementType = ElementType.BUS_GUIDEWAY;
                break;
            case "escape":
                elementType = ElementType.ESCAPE;
                isVehicleAllowed = true;
                if (maxSpeed == 0) maxSpeed = 50;
                break;
            case "raceway":
                elementType = ElementType.RACEWAY;
                break;
            case "pedestrian":
                elementType = ElementType.PEDESTRIAN_STREET;
                isWalkingAllowed = true;
                break;
            case "track":
                elementType = ElementType.TRACK;
                isVehicleAllowed = true;
                if (maxSpeed == 0) maxSpeed = 50;
                break;
            case "steps":
                elementType = ElementType.STEPS;
                isWalkingAllowed = true;
                break;
            case "footway":
                elementType = ElementType.FOOTWAY;
                isWalkingAllowed = true;
                break;
            case "bridleway":
                elementType = ElementType.BRIDLEWAY;
                isWalkingAllowed = true;
                break;
            case "cycleway":
                elementType = ElementType.CYCLEWAY;
                isCycleAllowed = true;
                break;
            case "path":
                elementType = ElementType.PATH;
                isCycleAllowed = true;
                isWalkingAllowed = true;
                break;
            case "road":
                elementType = ElementType.ROAD;
                isVehicleAllowed = true;
                isWalkingAllowed = true;
                isCycleAllowed = true;
                if (maxSpeed == 0) maxSpeed = 50;
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName){
            case "node":
                switch (place){
                    case CITY_NAME:
                    case TOWN_NAME:
                    case VILLAGE_NAME:
                    case HAMLET_NAME:
                    case SUBURB_NAME:
                    case QUARTER_NAME:
                    case NEIGHBOURHOOD_NAME:
                        addName(place);
                        if(!name.equals("")) {
                            model.getTst().put(name, new Value(0, longitude * longitudeFactor, -latitude, true));
                        }
                        break;
                    case BAR:
                    case NIGHT_CLUB:
                    case FAST_FOOD:
                    case RAILWAY_STATION:
                        addAmenity(place);
                        break;
                }
                if(isAddress) {
                    String key = roadName + " " + roadNumber;
                    if(model.cityEntryExists(cityName + " " + zipCode)){
                        int indexOfCity = model.getCityToIndex(cityName + " " + zipCode);
                        model.getTst().put(key, new Value(indexOfCity,longitude * longitudeFactor, -latitude, false));
                    }else{
                        model.putCityToIndex(cityName + " " + zipCode, indexCounter);
                        model.getTst().put(key, new Value(indexCounter,longitude * longitudeFactor, -latitude, false));
                        indexCounter ++;
                    }
                }

            break;
        case "way":
            switch (elementType) {
                case MOTORWAY:
                case MOTORWAY_LINK:
                case TRUNK_ROAD:
                case TRUNK_ROAD_LINK:
                case PRIMARY_ROAD:
                case PRIMARY_ROAD_LINK:
                case SECONDARY_ROAD:
                case SECONDARY_ROAD_LINK:
                case TERTIARY_ROAD:
                case TERTIARY_ROAD_LINK:
                case UNCLASSIFIED_ROAD:
                case RESIDENTIAL_ROAD:
                case PEDESTRIAN_STREET:
                case LIVING_STREET:
                case SERVICE_ROAD:
                case BUS_GUIDEWAY:
                case ESCAPE:
                case RACEWAY:
                case TRACK:
                case STEPS:
                case FOOTWAY:
                case BRIDLEWAY:
                case CYCLEWAY:
                case PATH:
                case ROAD:
                case RAIL:
                    if (!isArea) {
                        addRoad(elementType, false, false);
                    } else {
                        addRoad(elementType, false, true);
                        isArea = false;
                    }
                    break;
                case BUILDING:
                    addBuilding(elementType, false);
                    if(place == ElementType.RAILWAY_STATION)addAmenity(ElementType.RAILWAY_STATION_AREA);
                    break;
                case WATER:
                case WETLAND:
                case PARK:
                case FOREST:
                case GRASSLAND:
                case HEATH:
                case GRASS:
                case FARMLAND:
                case MEADOW:
                case COMMON_LAND:
                case BEACH:
                case SPORTSPITCH:
                case SPORTSTRACK:
                case PLAYGROUND:
                case PARKING:
                case HEDGE:
                case RIVER:
                case DRAIN:
                case AIRPORT_RUNWAY:
                case AIRPORT_TAXIWAY:
                    addBiome(elementType, false);
                    break;
                case BRIDGE:
                    addManMade(elementType, false, true);
                    break;
                case PIER:
                    addManMade(elementType, false, false);
                    break;
                case UNKNOWN:
                    // UnknownWay unknownWay = new UnknownWay(path);
                    // model.addWayElement(elementType, unknownWay);
                    break;
                }
                switch (place) {
                    case HOSPITAL:
                    case PLACE_OF_WORSHIP:
                    case SPORT_AMENITY:
                    case PARKING_AMENITY:
                    case RAILWAY_STATION_AREA:
                    case AIRPORT_AMENITY:
                        addAmenity(place);
                        break;
                }
                break;
            case "relation":
                switch (elementType) {
                    case MOTORWAY:
                    case MOTORWAY_LINK:
                    case TRUNK_ROAD:
                    case TRUNK_ROAD_LINK:
                    case PRIMARY_ROAD:
                    case PRIMARY_ROAD_LINK:
                    case SECONDARY_ROAD:
                    case SECONDARY_ROAD_LINK:
                    case TERTIARY_ROAD:
                    case TERTIARY_ROAD_LINK:
                    case UNCLASSIFIED_ROAD:
                    case RESIDENTIAL_ROAD:
                    case PEDESTRIAN_STREET:
                    case LIVING_STREET:
                    case SERVICE_ROAD:
                    case BUS_GUIDEWAY:
                    case ESCAPE:
                    case RACEWAY:
                    case TRACK:
                    case STEPS:
                    case FOOTWAY:
                    case BRIDLEWAY:
                    case CYCLEWAY:
                    case PATH:
                    case ROAD:
                    case RAIL:
                        addRoad(elementType, true, true);
                        isArea = false;
                        break;
                    case BUILDING:
                        addBuilding(elementType, true);
                        break;
                    case WATER:
                    case WETLAND:
                    case PARK:
                    case FOREST:
                    case GRASSLAND:
                    case HEATH:
                    case GRASS:
                    case FARMLAND:
                    case MEADOW:
                    case COMMON_LAND:
                    case BEACH:
                    case SPORTSPITCH:
                    case SPORTSTRACK:
                    case PLAYGROUND:
                    case HEDGE:
                    case RIVER:
                    case DRAIN:
                    case AIRPORT_RUNWAY:
                    case AIRPORT_TAXIWAY:
                        addBiome(elementType, true);
                        break;
                    case BRIDGE:
                        addManMade(elementType, true, isArea);
                        break;
                    case PIER:
                        addManMade(elementType, true, false);
                        break;
                }
                break;
        }
    }

    private void addBuilding(ElementType type, boolean isRelation) {
        PolygonApprox polygonApprox;
        polygonApprox = new PolygonApprox(way);
        if (!isRelation) {
            Building building = new Building(polygonApprox);
            for (int i = 0; i < way.size(); i += 5) {
                Pointer p = new Pointer((float)way.get(i).getX(), (float)way.get(i).getY(), building);
                model.getElements().get(type).putPointer(p);
            }
        } else {
            MultiPolygonApprox multiPolygonApprox;
            //relation = OSMRelation.sortWays(relation);
            multiPolygonApprox = new MultiPolygonApprox(relation);
            Building building = new Building(multiPolygonApprox);
            for (int i = 0; i < relation.size(); i++) {
                if (relation.get(i) != null) {
                    for (int j = 0; j < relation.get(i).size(); j += 5) {
                        if (relation.get(i).size() > j) {
                            Pointer p = new Pointer((float)relation.get(i).get(j).getX(), (float)relation.get(i).get(j).getY(), building);
                            model.getElements().get(type).putPointer(p);
                        }
                    }
                }
            }
        }
    }

    private void addName(ElementType type) {
        PlaceName placeName = new PlaceName(longitude * longitudeFactor, -latitude, name);
        Pointer p = new Pointer(longitude * longitudeFactor, -latitude, placeName);
        switch (type) {
            case CITY_NAME:
                cityNames.add(p);
                break;
            case TOWN_NAME:
                townNames.add(p);
                break;
            case VILLAGE_NAME:
                villageNames.add(p);
                break;
            case HAMLET_NAME:
                hamletNames.add(p);
                break;
            case SUBURB_NAME:
                suburbNames.add(p);
                break;
            case QUARTER_NAME:
                quarterNames.add(p);
                break;
            case NEIGHBOURHOOD_NAME:
                neighbourhoodNames.add(p);
                break;
        }
    }
    private void addManMade(ElementType type, boolean isRelation, boolean area){
        if (!isRelation) {
            PolygonApprox polygonApprox = new PolygonApprox(way);
            ManMade manMade;
            manMade = new ManMade(polygonApprox, area);
            for (int i = 0; i < way.size(); i += 5){
                Pointer p = new Pointer((float)way.get(i).getX(), (float)way.get(i).getY(), manMade);
                model.getElements().get(type).putPointer(p);
            }
        }
        else{
            MultiPolygonApprox multiPolygonApprox = new MultiPolygonApprox(relation);
            ManMade manMade;
            manMade = new ManMade(multiPolygonApprox, area);
            for (int i = 0; i < relation.size(); i++){
                if (relation.get(i) != null){
                    for (int j = 0; j < relation.get(i).size(); j += 5){
                        if (relation.get(i).size() > j){
                            Pointer p = new Pointer((float)relation.get(i).get(j).getX(), (float)relation.get(i).get(j).getY(), manMade);
                            model.getElements().get(type).putPointer(p);
                        }
                    }
                }
            }
        }
    }

    private void addRoad(ElementType type, boolean isRelation, boolean area)
    {
        Road road;
        if (!isRelation) {
            PolygonApprox polygonApprox = new PolygonApprox(way);
            if (!area) {
                road = new Road(polygonApprox, name);
            } else {
                road = new Road(polygonApprox, name, area);
            }
            road.setTravelByBikeAllowed(isCycleAllowed);
            road.setTravelByCarAllowed(isVehicleAllowed);
            road.setTravelByFootAllowed(isWalkingAllowed);
            road.setMaxSpeed(maxSpeed);
            road.setOneWay(isOneWay);
            road.setWay(refWay);
            roads.add(road);
            for (int i = 0; i < way.size(); i += 5) {
                Pointer p = new Pointer((float)way.get(i).getX(), (float)way.get(i).getY(), road);
                model.getElements().get(type).putPointer(p);
                model.getElements().get(ElementType.HIGHWAY).putPointer(p);
            }
        } else {
            MultiPolygonApprox multiPolygonApprox = new MultiPolygonApprox(relation);
            if (!area) {
                road = new Road(multiPolygonApprox, name);
            } else {
                road = new Road(multiPolygonApprox, name, true);
            }
            road.setTravelByBikeAllowed(isCycleAllowed);
            road.setTravelByCarAllowed(isVehicleAllowed);
            road.setTravelByFootAllowed(isWalkingAllowed);
            road.setMaxSpeed(maxSpeed);
            road.setOneWay(isOneWay);
            road.setRelation(refRelation);
            roads.add(road);
            for (int i = 0; i < relation.size(); i++) {
                if (relation.get(i) != null) {
                    for (int j = 0; j < relation.get(i).size(); j += 5) {
                        if (relation.get(i).size() > j) {
                            Pointer p = new Pointer((float)relation.get(i).get(j).getX(),
                                (float)relation.get(i).get(j).getY(), road);
                            model.getElements().get(type).putPointer(p);
                            model.getElements().get(ElementType.HIGHWAY).putPointer(p);
                        }
                    }
                }
            }
        }
    }

    private void addBiome(ElementType type, boolean isRelation) {
        if(specialRelationCase == true){
            specialRelationCase = false;
        }else {
            if (!isRelation) {
                PolygonApprox polygonApprox;
                polygonApprox = new PolygonApprox(way);
                Biome biome = new Biome(polygonApprox);
                for (int i = 0; i < way.size(); i += 5) {
                    Pointer p = new Pointer((float)way.get(i).getX(),
                        (float)way.get(i).getY(), biome);
                    model.getElements().get(type).putPointer(p);
                }
            } else {
                MultiPolygonApprox multiPolygonApprox;
                multiPolygonApprox = new MultiPolygonApprox(relation);
                Biome biome = new Biome(multiPolygonApprox);
                for (int i = 0; i < relation.size() - 1; i++) {
                    if (relation.get(i) != null) {
                        for (int j = 0; j < relation.get(i).size(); j += 5) {
                            Pointer p = new Pointer((float)relation.get(i).get(j).getX(),
                                (float)relation.get(i).get(j).getY(), biome);
                            model.getElements().get(type).putPointer(p);
                        }
                    } else
                        continue;
                }
            }
        }
    }

    private void addAmenity(ElementType type) {
        Amenity amenity;
        Pointer p;
        PolygonApprox polygonApprox;
        switch (type) {
            case BAR:
                amenity = new Amenity(longitude * longitudeFactor, -latitude, name);
                p = new Pointer(longitude * longitudeFactor, -latitude, amenity);
                bars.add(p);
                break;
            case NIGHT_CLUB:
                amenity = new Amenity(longitude * longitudeFactor, -latitude, name);
                p = new Pointer(longitude * longitudeFactor, -latitude, amenity);
                nightClubs.add(p);
                break;
            case FAST_FOOD:
                amenity = new Amenity(longitude * longitudeFactor, -latitude, name);
                p = new Pointer(longitude * longitudeFactor, -latitude, amenity);
                fastFoods.add(p);
                break;
            case HOSPITAL:
                polygonApprox = new PolygonApprox(way);
                amenity = new Amenity(polygonApprox.getCenterX(), polygonApprox.getCenterY(), name);
                p = new Pointer(polygonApprox.getCenterX(), polygonApprox.getCenterY(), amenity);
                model.getElements().get(ElementType.HOSPITAL).putPointer(p);
                break;
            case PLACE_OF_WORSHIP:
                polygonApprox = new PolygonApprox(way);
                amenity = new Amenity(polygonApprox.getCenterX(), polygonApprox.getCenterY(), name);
                p = new Pointer(polygonApprox.getCenterX(), polygonApprox.getCenterY(), amenity);
                model.getElements().get(ElementType.PLACE_OF_WORSHIP).putPointer(p);
                break;
            case PARKING_AMENITY:
                polygonApprox = new PolygonApprox(way);
                amenity = new Amenity(polygonApprox.getCenterX(), polygonApprox.getCenterY(), name);
                p = new Pointer(polygonApprox.getCenterX(), polygonApprox.getCenterY(), amenity);
                model.getElements().get(ElementType.PARKING_AMENITY).putPointer(p);
                break;
            case SPORT_AMENITY:
                polygonApprox = new PolygonApprox(way);
                amenity = new Amenity(polygonApprox.getCenterX(), polygonApprox.getCenterY(), name);
                p = new Pointer(polygonApprox.getCenterX(), polygonApprox.getCenterY(), amenity);
                model.getElements().get(ElementType.SPORT_AMENITY).putPointer(p);
                break;
            case RAILWAY_STATION:
                amenity = new Amenity(longitude * longitudeFactor, -latitude, name);
                p = new Pointer(longitude * longitudeFactor, -latitude, amenity);
                railwayStations.add(p);
                break;
            case RAILWAY_STATION_AREA:
                polygonApprox = new PolygonApprox(way);
                amenity = new Amenity(polygonApprox.getCenterX(), polygonApprox.getCenterY(), name);
                p = new Pointer(polygonApprox.getCenterX(), polygonApprox.getCenterY(), amenity);
                model.getElements().get(ElementType.RAILWAY_STATION_AREA).putPointer(p);
                break;
            case AIRPORT_AMENITY:
                polygonApprox = new PolygonApprox(way);
                amenity = new Amenity(polygonApprox.getCenterX(), polygonApprox.getCenterY(), name);
                p = new Pointer(polygonApprox.getCenterX(), polygonApprox.getCenterY(), amenity);
                model.getElements().get(ElementType.AIRPORT_AMENITY).putPointer(p);
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {

    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {

    }

    @Override
    public void skippedEntity(String name) throws SAXException {

    }
}
