package OSM;

import Enums.BoundType;
import Enums.OSMEnums.AmenityType;
import Enums.OSMEnums.ElementType;
import Enums.OSMEnums.RoadType;
import Helpers.LongToPointMap;
import Helpers.Shapes.MultiPolygonApprox;
import Helpers.Shapes.PolygonApprox;
import KDtree.NodeGenerator;
import KDtree.Pointer;
import Model.Addresses.TenarySearchTrie;
import Model.Addresses.Value;
import Model.Elements.*;
import Model.Model;
import RouteSearch.RoadGraph;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import java.awt.geom.Point2D;
import java.util.*;

/**
 * Created by Jakob on 06-03-2017.
 */
public final class OSMHandler implements ContentHandler {
    private static OSMHandler handler;
    private NodeGenerator nodeGenerator;

    private LongToPointMap idToNode;
    private Map<Long, OSMWay> idToWay;
    //private Map<Long,OSMWayRef> idToRefWay;
    private OSMWay way;
    //private OSMWayRef refWay;
    // private List<OSMWayRef> refRelation;
    private OSMRelation relation;
    // private OSMNode node;
    private ElementType elementType;
    private RoadType roadType;
    private AmenityType place;
    private String name;
    private float longitudeFactor;
    private Model model;
    private int loadednodes, loadedRelations, loadedWays;
    private boolean isDefaultMode;
    private boolean isInitialized;
    private float latitude;
    private float longitude;
    private ArrayList<Pointer> amenities;
    private boolean isArea = false;
    private boolean isOneWay = false;
    private boolean isWalkingAllowed = false;
    private boolean isCycleAllowed = false;
    private boolean isVehicleAllowed = false;
    private int maxSpeed;
    private boolean isWikiDataAvalible;
    private boolean isInTunnel;
    private ArrayList<Road> roads;

    private String roadName;
    private String roadNumber;
    private String zipCode;
    private String cityName;

    private boolean isAddress;
    private int indexCounter = 1;
    private static final int precision = 3;

    private OSMHandler() {
        idToNode = new LongToPointMap(22);
        idToWay = new HashMap<>();
        //idToRefWay = new HashMap<>();
        model = Model.getInstance();
        model.setTst(new TenarySearchTrie());
        nodeGenerator = new NodeGenerator();
        amenities = new ArrayList<>();
        roads = new ArrayList<>();
    }

    public void parseDefault(Boolean mode){
        isDefaultMode = mode;
    }

    public void setIsInitialized(boolean isInitialized){
        this.isInitialized = isInitialized;
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
        // idToRefWay = new HashMap<>();
        // refWay = null;
        idToWay = new HashMap<>();
        idToNode = new LongToPointMap(22);
        relation = null;
        //refRelation = null;
        nodeGenerator = new NodeGenerator();
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
            if (isDefaultMode) {
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
            if (isDefaultMode) {
                model.setBound(BoundType.MIN_LONGITUDE, minLongitude);
                model.setBound(BoundType.MAX_LONGITUDE, maxLongitude);
                model.setBound(BoundType.MIN_LATITUDE, minLatitude);
                model.setBound(BoundType.MAX_LATITUDE, maxLatitude);
            }
            model.setDynamicBound(BoundType.MIN_LONGITUDE, minLongitude);
            model.setDynamicBound(BoundType.MAX_LONGITUDE, maxLongitude);
            model.setDynamicBound(BoundType.MIN_LATITUDE, minLatitude);
            model.setDynamicBound(BoundType.MAX_LATITUDE, maxLatitude);
            model.setLongitudeFactor(longitudeFactor);
            break;
        case "node":
            long id = Long.parseLong(atts.getValue("id"));
            latitude = Float.parseFloat(atts.getValue("lat"));
            longitude = Float.parseFloat(atts.getValue("lon"));
            idToNode.put(id, longitude * longitudeFactor, -latitude);

            if (isDefaultMode) {
                nodeGenerator.addPoint(
                    new Point2D.Float(longitude * longitudeFactor, -latitude));
            }

            name = "";
            roadName = "";
            roadNumber = "";
            cityName = "";
            zipCode = "";
            isAddress = false;
            place = AmenityType.UNKNOWN;

            if (loadednodes % 100000 == 0) System.out.println("NumNodes: "+loadednodes);
            loadednodes++;
            break;
        case "relation":
            long relationID = Long.parseLong(atts.getValue("id"));
            name = "";
            isArea = false;
            isInTunnel = false;
            place = AmenityType.UNKNOWN;
            roadType = RoadType.UNKNOWN;
            elementType = ElementType.UNKNOWN;
            isVehicleAllowed = false;
            isWalkingAllowed = false;
            isCycleAllowed = false;
            maxSpeed = 0;
            relation = new OSMRelation(relationID);
            // refRelation = new ArrayList<>();

            if (loadedRelations % 100 == 0) System.out.println("NumRelations: "+loadedRelations);
            loadedRelations++;
            break;
        case "way":
            if (!isInitialized && isDefaultMode) {
                nodeGenerator.initialise();
                for (ElementType type : ElementType.values()) {
                    if(model.getElements().containsKey(type))
                    nodeGenerator.setupTree(model.getElements().get(type));
                }
            }
            if(!isInitialized){
                isInitialized = true;
                for(Pointer p : amenities){
                    model.getElements().get(ElementType.AMENITY).putPointer(p);
                }
                amenities = new ArrayList<>();
            }

            way = new OSMWay();
            // refWay = new OSMWayRef();
            id = Long.parseLong(atts.getValue("id"));
            elementType = ElementType.UNKNOWN;
            place = AmenityType.UNKNOWN;
            roadType = RoadType.UNKNOWN;
            idToWay.put(id, way);
            // idToRefWay.put(id,refWay);
            name = "";
            isInTunnel = false;
            isWikiDataAvalible = false;
            isArea = false;
            isVehicleAllowed = false;
            isWalkingAllowed = false;
            isCycleAllowed = false;
            maxSpeed = 0;
            isOneWay = false;

            if (loadedWays % 1000 == 0) System.out.println("NumWays: "+loadedWays);
            loadedWays++;
            break;
        case "nd":
            long ref = Long.parseLong(atts.getValue("ref"));
            way.add(idToNode.get(ref));
            //refWay.add(idToNode.get(ref),ref);
            break;
        case "tag":
            String k = atts.getValue("k");
            String v = atts.getValue("v");
            switch (k) {
                case "highway":
                    elementType = ElementType.HIGHWAY;
                    determineHighway(v);
                    break;
                case "railway":
                    determineRailway(v);
                    break;
                case "name":
                    name = v.intern();
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
                case "wikidata":
                    isWikiDataAvalible = true;
                    break;
                case "website": //Not the same as wikidata, but RUC dont have a wikidata-tag, but a website-tag
                    isWikiDataAvalible = true;
                    break;
                case "tunnel":
                    if(v.equals("yes")) isInTunnel = true;
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
            OSMWay way = idToWay.get(ref);
            if (way != null) {
                relation.add(way);
            }
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
                elementType = ElementType.GRASS;
                break;
            case "basin":
                elementType = ElementType.WATER;
                break;
            case "village_green":
                elementType = ElementType.COMMON_LAND;
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
            case "dog_park":
            case "garden":
                elementType = ElementType.PARK;
                break;
            case "common":
                elementType = ElementType.COMMON_LAND;
                break;
            case "pitch":
                elementType = ElementType.SPORTSPITCH;
                place = AmenityType.SPORT_AMENITY;
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
                place = AmenityType.RAILWAY_STATION;
                break;
        }
    }

    private void determineAmenity(String value) {
        switch (value) {
            case "bar":
            case "biergarten":
            case "pub":
                place = AmenityType.BAR;
                break;
            case "nightclub":
                place = AmenityType.NIGHT_CLUB;
                break;
            case "fast_food":
                place = AmenityType.FAST_FOOD;
                break;
            case "hospital":
                place = AmenityType.HOSPITAL;
                break;
            case "parking":
                place = AmenityType.PARKING_AMENITY;
                elementType = ElementType.PARKING;
                break;
            case "university":
                place = AmenityType.UNIVERSITY;
                break;
        }
    }

    private void determinePlace(String value) {
        switch (value) {
            case "city":
                place = AmenityType.CITY_NAME;
                break;
            case "town":
                place = AmenityType.TOWN_NAME;
                break;
            case "village":
                place = AmenityType.VILLAGE_NAME;
                break;
            case "hamlet":
                place = AmenityType.HAMLET_NAME;
                break;
            case "suburb":
                place = AmenityType.SUBURB_NAME;
                break;
            case "quarter":
                place = AmenityType.QUARTER_NAME;
                break;
            case "neighbourhood":
                place = AmenityType.NEIGHBOURHOOD_NAME;
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
                place = AmenityType.AIRPORT_AMENITY;
                break;
        }
    }

    private void determineHighway(String value) {
        switch (value) {
            case "motorway":
                roadType = RoadType.MOTORWAY;
                isVehicleAllowed = true;
                if (maxSpeed == 0) maxSpeed = 130;
                break;
            case "motorway_link":
                roadType = RoadType.MOTORWAY_LINK;
                isVehicleAllowed = true;
                if (maxSpeed == 0) maxSpeed = 130;
                break;
            case "trunk":
                roadType = RoadType.TRUNK_ROAD;
                isVehicleAllowed = true;
                if (maxSpeed == 0) maxSpeed = 90;
                break;
            case "trunk_link":
                roadType = RoadType.TRUNK_ROAD_LINK;
                isVehicleAllowed = true;
                if (maxSpeed == 0) maxSpeed = 90;
                break;
            case "primary":
                roadType = RoadType.PRIMARY_ROAD;
                isVehicleAllowed = true;
                isWalkingAllowed = true;
                isCycleAllowed = true;
                if (maxSpeed == 0) maxSpeed = 80;
                break;
            case "primary_link":
                roadType = RoadType.PRIMARY_ROAD_LINK;
                isVehicleAllowed = true;
                isWalkingAllowed = true;
                isCycleAllowed = true;
                if (maxSpeed == 0) maxSpeed = 80;
                break;
            case "secondary":
                roadType = RoadType.SECONDARY_ROAD;
                isVehicleAllowed = true;
                isWalkingAllowed = true;
                isCycleAllowed = true;
                if (maxSpeed == 0) maxSpeed = 80;
                break;
            case "seconday_link":
                roadType = RoadType.SECONDARY_ROAD_LINK;
                isVehicleAllowed = true;
                isWalkingAllowed = true;
                isCycleAllowed = true;
                if (maxSpeed == 0) maxSpeed = 80;
                break;
            case "tertiary":
                roadType = RoadType.TERTIARY_ROAD;
                isVehicleAllowed = true;
                isWalkingAllowed = true;
                isCycleAllowed = true;
                if (maxSpeed == 0) maxSpeed = 80;
                break;
            case "tertiary_link":
                roadType = RoadType.TERTIARY_ROAD_LINK;
                isVehicleAllowed = true;
                isWalkingAllowed = true;
                isCycleAllowed = true;
                if (maxSpeed == 0) maxSpeed = 80;
                break;
            case "unclassified":
                roadType = RoadType.UNCLASSIFIED_ROAD;
                isVehicleAllowed = true;
                isWalkingAllowed = true;
                isCycleAllowed = true;
                if (maxSpeed == 0) maxSpeed = 50;
                break;
            case "residential":
                roadType = RoadType.RESIDENTIAL_ROAD;
                isVehicleAllowed = true;
                isWalkingAllowed = true;
                isCycleAllowed = true;
                if (maxSpeed == 0) maxSpeed = 50;
                break;
            case "living_street":
                roadType = RoadType.LIVING_STREET;
                isVehicleAllowed = true;
                isWalkingAllowed = true;
                isCycleAllowed = true;
                if (maxSpeed == 0) maxSpeed = 50;
                break;
            case "service":
                roadType = RoadType.SERVICE_ROAD;
                isVehicleAllowed = true;
                isWalkingAllowed = true;
                isCycleAllowed = true;
                if (maxSpeed == 0) maxSpeed = 50;
                break;
            case "raceway":
                elementType = ElementType.AIRPORT_TAXIWAY;
                break;
            case "pedestrian":
                roadType = RoadType.PEDESTRIAN_STREET;
                isWalkingAllowed = true;
                break;
            case "track":
                roadType = RoadType.TRACK;
                isVehicleAllowed = true;
                if (maxSpeed == 0) maxSpeed = 50;
                break;
            case "steps":
                roadType = RoadType.STEPS;
                isWalkingAllowed = true;
                break;
            case "footway":
                roadType = RoadType.FOOTWAY;
                isWalkingAllowed = true;
                break;
            case "bridleway":
                roadType = RoadType.BRIDLEWAY;
                isWalkingAllowed = true;
                break;
            case "cycleway":
                roadType = RoadType.CYCLEWAY;
                isCycleAllowed = true;
                break;
            case "path":
                roadType = RoadType.PATH;
                isCycleAllowed = true;
                isWalkingAllowed = true;
                break;
            case "road":
                roadType = RoadType.ROAD;
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
                    case AIRPORT_AMENITY:
                        addAmenity(place, false, true);
                        break;
                }
                if(isAddress) {
                    String key = roadName + " " + roadNumber;
                    if(model.cityEntryExists(cityName + " " + zipCode)){
                        int indexOfCity = model.getCityToIndex(cityName + " " + zipCode);
                        model.getTst().put(key.intern(), new Value(indexOfCity,longitude * longitudeFactor, -latitude, false));
                    }else{
                        model.putCityToIndex(cityName + " " + zipCode, indexCounter);
                        model.getTst().put(key.intern(), new Value(indexCounter,longitude * longitudeFactor, -latitude, false));
                        indexCounter ++;
                    }
                }

            break;
        case "way":
            switch (elementType) {
                case HIGHWAY:
                    if (!isArea) {
                        addRoad(elementType, false, false, roadType);
                    } else {
                        addRoad(elementType, false, true, roadType);
                        isArea = false;
                    }
                    break;
                case RAIL:
                    addRail(false, isInTunnel);
                    break;
                case BUILDING:
                    addBuilding(elementType, false);
                    //if(place == AmenityType.RAILWAY_STATION)addAmenity(AmenityType.RAILWAY_STATION, false);
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
                    // We dont want to add unknown elements to the data model
                    break;
                }
                switch (place) {
                    case HOSPITAL:
                    case UNIVERSITY:
                    case SPORT_AMENITY:
                    case PARKING_AMENITY:
                    case RAILWAY_STATION:
                    case AIRPORT_AMENITY:
                        addAmenity(place, false, false);
                        break;
                }
                break;
            case "relation":
                switch (elementType) {
                    case HIGHWAY:
                        addRoad(elementType, true, true, roadType);
                        isArea = false;
                        break;
                    case RAIL:
                        addRail(true, isInTunnel);
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
                switch (place) {
                    case UNIVERSITY:
                        addAmenity(place, true, false);
                        break;
                }
                break;
        }
    }

    private void addRail(boolean isRelation, boolean isInTunnel){
        if (!isRelation) {
            PolygonApprox polygonApprox = new PolygonApprox(way);
            Rail rail;
            rail = new Rail(polygonApprox, isInTunnel);
            for (int i = 0; i < way.size(); i += precision){
                Pointer p = new Pointer((float)way.get(i).getX(), (float)way.get(i).getY(), rail);
                model.getElements().get(ElementType.RAIL).putPointer(p);
            }
        } else {
            MultiPolygonApprox multiPolygonApprox;
            multiPolygonApprox = new MultiPolygonApprox(relation);
            Rail rail = new Rail(multiPolygonApprox, isInTunnel);
            for (int i = 0; i < relation.size(); i++){
                if (relation.get(i) != null){
                    for (int j = 0; j < relation.get(i).size(); j += precision){
                        if (relation.get(i).size() > j){
                            Pointer p = new Pointer((float)relation.get(i).get(j).getX(), (float)relation.get(i).get(j).getY(), rail);
                            model.getElements().get(ElementType.RAIL).putPointer(p);
                        }
                    }
                }
            }
        }
    }

    private void addBuilding(ElementType type, boolean isRelation) {
        PolygonApprox polygonApprox;
        polygonApprox = new PolygonApprox(way);
        if (!isRelation) {
            Building building = new Building(polygonApprox);
            for (int i = 0; i < way.size(); i += precision) {
                Pointer p = new Pointer((float)way.get(i).getX(),
                        (float)way.get(i).getY(), building);
                model.getElements().get(type).putPointer(p);
            }
        } else {
            MultiPolygonApprox multiPolygonApprox;
            relation = OSMRelation.sortWays(relation);
            multiPolygonApprox = new MultiPolygonApprox(relation);
            Building building = new Building(multiPolygonApprox);
            for (int i = 0; i < relation.size(); i++) {
                if (relation.get(i) != null) {
                    for (int j = 0; j < relation.get(i).size(); j += precision) {
                        if (relation.get(i).size() > j) {
                            Pointer p = new Pointer((float)relation.get(i).get(j).getX(), (float)relation.get(i).get(j).getY(), building);
                            model.getElements().get(type).putPointer(p);
                        }
                    }
                }
            }
        }
    }

    private void addName(AmenityType type) {
        Amenity name = new Amenity(type, longitude * longitudeFactor, -latitude, this.name.intern());
        Pointer p = new Pointer(longitude * longitudeFactor, -latitude, name);
        switch (type) {
            case CITY_NAME:
            case TOWN_NAME:
            case VILLAGE_NAME:
            case HAMLET_NAME:
            case SUBURB_NAME:
            case QUARTER_NAME:
            case NEIGHBOURHOOD_NAME:
                amenities.add(p);
                break;
        }
    }
    private void addManMade(ElementType type, boolean isRelation, boolean area){
        if (!isRelation) {
            PolygonApprox polygonApprox = new PolygonApprox(way);
            ManMade manMade;
            manMade = new ManMade(polygonApprox, area);
            for (int i = 0; i < way.size(); i += precision){
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
                    for (int j = 0; j < relation.get(i).size(); j += precision){
                        if (relation.get(i).size() > j){
                            Pointer p = new Pointer((float)relation.get(i).get(j).getX(), (float)relation.get(i).get(j).getY(), manMade);
                            model.getElements().get(type).putPointer(p);
                        }
                    }
                }
            }
        }
    }

    private void addRoad(ElementType type, boolean isRelation, boolean area, RoadType roadType)
    {
        Road road;
        if (!isRelation) {
            PolygonApprox polygonApprox = new PolygonApprox(way);
            if (!area) {
                road = new Road(polygonApprox, name.intern(), roadType);
            } else {
                road = new Road(polygonApprox, name.intern(), area, roadType);
            }
            road.setTravelByBikeAllowed(isCycleAllowed);
            road.setTravelByCarAllowed(isVehicleAllowed);
            road.setTravelByFootAllowed(isWalkingAllowed);
            road.setMaxSpeed(maxSpeed);
            road.setOneWay(isOneWay);
            //road.setWay(way);
            roads.add(road);
            for (int i = 0; i < way.size(); i += precision) {
                Pointer p = new Pointer((float)way.get(i).getX(), (float)way.get(i).getY(), road);
                model.getElements().get(type).putPointer(p);
                if(roadType == RoadType.MOTORWAY || roadType == RoadType.TRUNK_ROAD || roadType == RoadType.PRIMARY_ROAD){
                    model.getElements().get(ElementType.HIGHWAY_LEVEL_5_6).putPointer(p);
                }
            }
        } else {
            MultiPolygonApprox multiPolygonApprox = new MultiPolygonApprox(relation);
            if (!area) {
                road = new Road(multiPolygonApprox, name.intern(), roadType);
            } else {
                road = new Road(multiPolygonApprox, name.intern(), true, roadType);
            }
            road.setTravelByBikeAllowed(isCycleAllowed);
            road.setTravelByCarAllowed(isVehicleAllowed);
            road.setTravelByFootAllowed(isWalkingAllowed);
            road.setMaxSpeed(maxSpeed);
            road.setOneWay(isOneWay);
            //road.setRelation(relation);
            for (int i = 0; i < relation.size(); i++) {
                if (relation.get(i) != null) {
                    for (int j = 0; j < relation.get(i).size(); j += precision) {
                        if (relation.get(i).size() > j) {
                            Pointer p = new Pointer((float)relation.get(i).get(j).getX(), (float)relation.get(i).get(j).getY(), road);
                            model.getElements().get(type).putPointer(p);
                            if(roadType == RoadType.MOTORWAY || roadType == RoadType.TRUNK_ROAD || roadType == RoadType.PRIMARY_ROAD){
                                model.getElements().get(ElementType.HIGHWAY_LEVEL_5_6).putPointer(p);
                            }
                        }
                    }
                }
            }
        }
    }

    private void addBiome(ElementType type, boolean isRelation) {
            if (!isRelation) {
                PolygonApprox polygonApprox;
                polygonApprox = new PolygonApprox(way);
                Biome biome = new Biome(polygonApprox);
                for (int i = 0; i < way.size(); i += precision) {
                    Pointer p = new Pointer((float)way.get(i).getX(),
                        (float)way.get(i).getY(), biome);
                    model.getElements().get(type).putPointer(p);
                }
            } else {
                MultiPolygonApprox multiPolygonApprox;
                relation = OSMRelation.sortWays(relation);
                multiPolygonApprox = new MultiPolygonApprox(relation);
                Biome biome = new Biome(multiPolygonApprox);
                for (int i = 0; i < relation.size() - 1; i++) {
                    if (relation.get(i) != null) {
                        for (int j = 0; j < relation.get(i).size(); j += precision) {
                            Pointer p = new Pointer((float)relation.get(i).get(j).getX(),
                                (float)relation.get(i).get(j).getY(), biome);
                            model.getElements().get(type).putPointer(p);
                        }
                    }
                }
            }
    }

    private void addAmenity(AmenityType type, boolean isRelation, boolean isNode) {
        Amenity amenity;
        Pointer p;
        PolygonApprox polygonApprox;
        switch (type) {
            case BAR:
            case NIGHT_CLUB:
            case FAST_FOOD:
            case RAILWAY_STATION:
                if(isNode){
                    amenity = new Amenity(type,longitude * longitudeFactor, -latitude, name.intern());
                    if(!checkForNearbyAmenity(amenity, 0.001f)){
                        p = new Pointer(longitude * longitudeFactor, -latitude, amenity);
                        amenities.add(p);
                    }
                }
                else{
                    polygonApprox = new PolygonApprox(way);
                    amenity = new Amenity(type, polygonApprox.getCenterX(), polygonApprox.getCenterY(), name.intern());
                    if(!checkForNearbyAmenity(type, amenity, 0.001f)){
                        p = new Pointer(polygonApprox.getCenterX(), polygonApprox.getCenterY(), amenity);
                        model.getElements().get(ElementType.AMENITY).putPointer(p);
                    }
                }
                break;
            case PARKING_AMENITY:
            case SPORT_AMENITY:
            case HOSPITAL:
                polygonApprox = new PolygonApprox(way);
                amenity = new Amenity(type, polygonApprox.getCenterX(), polygonApprox.getCenterY(), name.intern());
                if(!checkForNearbyAmenity(type, amenity, 0.0005f)){
                    p = new Pointer(polygonApprox.getCenterX(), polygonApprox.getCenterY(), amenity);
                    model.getElements().get(ElementType.AMENITY).putPointer(p);
                }
                break;
            case UNIVERSITY:
                if (!isRelation){
                    polygonApprox = new PolygonApprox(way);
                    amenity = new Amenity(type, polygonApprox.getCenterX(), polygonApprox.getCenterY(), name.intern());
                    if(!checkForNearbyAmenity(type, amenity, 0.001f) && isWikiDataAvalible){
                        p = new Pointer(polygonApprox.getCenterX(), polygonApprox.getCenterY(), amenity);
                        model.getElements().get(ElementType.AMENITY).putPointer(p);
                    }
                }
                else {
                    /*
                     * Some univesities, such as in Aarhus, are relations and spread throughout a wider area.
                     */
                    if(relation.size() > 3){
                        polygonApprox = new PolygonApprox(relation.get(3));
                        amenity = new Amenity(type, polygonApprox.getCenterX(), polygonApprox.getCenterY(), name.intern());
                        if(!checkForNearbyAmenity(type, amenity, 0.001f) && isWikiDataAvalible){
                            p = new Pointer(polygonApprox.getCenterX(), polygonApprox.getCenterY(), amenity);
                            model.getElements().get(ElementType.AMENITY).putPointer(p);
                        }
                    }
                }
                break;
            case AIRPORT_AMENITY:
                if(way == null){
                    amenity = new Amenity(type, longitude * longitudeFactor, -latitude, name.intern());
                    p = new Pointer(longitude * longitudeFactor, -latitude, amenity);
                    amenities.add(p);
                }
                else{
                    polygonApprox = new PolygonApprox(way);
                    amenity = new Amenity(type, polygonApprox.getCenterX(), polygonApprox.getCenterY(), name.intern());
                    p = new Pointer(polygonApprox.getCenterX(), polygonApprox.getCenterY(), amenity);
                    model.getElements().get(ElementType.AMENITY).putPointer(p);
                }
                break;
        }
    }

    /*
     * Check for nearby amenities in the KDTree with amenities.
     * @param AmenityType the kind of amenity which is checked for
     * @param the new amenity
     * @float the minimum distance to other amenities of the kind specified as the first parameter
     */
    private boolean checkForNearbyAmenity(AmenityType type, Amenity amenity, float bufferDistance){
        boolean isAmenityNear = false;
        HashSet<SuperElement> nearbyAmenities;
        float maxX = amenity.getX() + bufferDistance;
        float minX = amenity.getX() - bufferDistance;
        float maxY = amenity.getY() + bufferDistance;
        float minY = amenity.getY() - bufferDistance;

        nearbyAmenities = model.getElements().get(ElementType.AMENITY).getManySections(minX, minY, maxX, maxY);
        for(SuperElement element : nearbyAmenities){
            Amenity amenityInModel = (Amenity) element;
            if(amenityInModel.getAmenityType() == type){
                double dist = Math.sqrt((Math.pow((amenity.getX()-amenityInModel.getX()),2))+(Math.pow((amenity.getY()-amenityInModel.getY()),2)));
                if(dist < bufferDistance){
                    isAmenityNear = true;
                    break;
                }
            }
        }
        return isAmenityNear;
    }

    /*
     * Check for nearby amenity in the ArrayList. This have to be done while parsing OSMNodes,
     * as the ArrayList is emptied when OSMWays start to be parsed.
     */
    private boolean checkForNearbyAmenity(Amenity amenity, float bufferDistance){
        boolean isAmenityNear = false;
        for(Pointer pointer : amenities){
            Amenity pointerAmenity = (Amenity) pointer.getElement();
            if(amenity.getAmenityType() == pointerAmenity.getAmenityType()){
                float amenityX = amenity.getX();
                float amenityY = amenity.getY();
                float pointerX = pointerAmenity.getX();
                float pointerY = pointerAmenity.getY();

                double dist = Math.sqrt((Math.pow((amenityX-pointerX),2))+(Math.pow((amenityY-pointerY),2)));
                if(dist < bufferDistance){
                    isAmenityNear = true;
                }
            }
        }
        return isAmenityNear;
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
