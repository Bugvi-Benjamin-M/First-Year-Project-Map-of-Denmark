package OSM;

import Enums.BoundType;
import Enums.OSMEnums.ElementType;
import Helpers.LongToPointMap;
import KDtree.NodeGenerator;
import KDtree.Pointer;
import Model.Model;
import Model.Elements.*;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jakob on 06-03-2017.
 */
public final class OSMHandler implements ContentHandler {
    private static OSMHandler handler;
    private NodeGenerator nodeGenerator;

    private LongToPointMap idToNode;
    private Map<Long, OSMWay> idToWay;
    private OSMWay way;
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

    private OSMHandler() {
        idToNode = new LongToPointMap(22);
        idToWay = new HashMap<>();
        model = Model.getInstance();
        nodeGenerator = new NodeGenerator();
        cityNames = new ArrayList<>();
        townNames  = new ArrayList<>();
        villageNames = new ArrayList<>();
        hamletNames = new ArrayList<>();
        suburbNames = new ArrayList<>();
        quarterNames = new ArrayList<>();
        neighbourhoodNames = new ArrayList<>();
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
    public static OSMHandler getInstance(){
        if(handler == null){
            handler = new OSMHandler();
        }
        return handler;
    }

    public static void resetInstance()
    {
        handler = null;
    }

    @Override
    public void setDocumentLocator(Locator locator) {

    }

    @Override
    public void startDocument() throws SAXException {

    }

    @Override
    public void endDocument() throws SAXException {

    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {

    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        switch (qName){
            case "bounds":
                float minLatitude, maxLatitude, minLongitude, maxLongitude;
                minLatitude = Float.parseFloat(atts.getValue("minlat"));
                maxLatitude = Float.parseFloat(atts.getValue("maxlat"));
                minLongitude = Float.parseFloat(atts.getValue("minlon"));
                maxLongitude = Float.parseFloat(atts.getValue("maxlon"));
                if(defaultMode == true) {
                float avglat = minLatitude + (maxLatitude - minLatitude) / 2;
                    longitudeFactor = (float) Math.cos(avglat / 180 * Math.PI);
                }else{
                    float avglat = model.getMinLatitude(false) + (model.getMaxLatitude(false) - model.getMinLatitude(false))/2;
                    longitudeFactor = (float) Math.cos(avglat / 180 * Math.PI);
                }
                minLongitude *= longitudeFactor;
                maxLongitude *= longitudeFactor;
                minLatitude = -minLatitude;
                maxLatitude = -maxLatitude;
                if(defaultMode == true) {
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

                if(defaultMode == true) {
                    nodeGenerator.addPoint(new Point2D.Float(longitude * longitudeFactor, -latitude));
                }

                name = "";
                place = ElementType.UNKNOWN;

                loadednodes++;
                if ((loadednodes & 0xFFFF) == 0) {
                    System.out.println("Numnodes: " + loadednodes);
                }
                break;
            case "relation":
                relation = new OSMRelation();
                elementType = ElementType.UNKNOWN;
                loadedRelations++;
                if ((loadedRelations & 0xFF) == 0) {
                    System.out.println("Numrelations: " + loadedRelations);
                }
                break;
            case "way":
                if(!initialized && defaultMode == true){
                    nodeGenerator.initialise();
                    for (ElementType type : ElementType.values()) {
                        nodeGenerator.setupTree(model.getElements().get(type));
                    }
                    initialized = true;

                    for(Pointer p : cityNames){
                        model.getElements().get(ElementType.CITY_NAME).putPointer(p);
                    }
                }

                way = new OSMWay();
                id = Long.parseLong(atts.getValue("id"));
                elementType = ElementType.UNKNOWN;
                idToWay.put(id, way);
                name = "";
                loadedWays++;
                if ((loadedWays & 0xFF) == 0) {
                    System.out.println("Numways: " + loadedWays);
                }
                break;
            case "nd":
                long ref = Long.parseLong(atts.getValue("ref"));
                way.add(idToNode.get(ref));
                break;
            case "tag":
                String k = atts.getValue("k");
                String v = atts.getValue("v");
                switch (k){
                    case "highway":
                        determineHighway(v);
                        break;
                    case "name":
                        name = v;
                        break;
                    case "natural":
                        determineNatural(v);
                        break;
                    case "place":
                        determinePlace(v);
                        break;
                }
                break;
            case "member":
                ref = Long.parseLong(atts.getValue("ref"));
                relation.add(idToWay.get(ref));
                break;
        }
    }

    private void determinePlace(String value){
        switch(value){
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

    private void determineNatural(String value){
        elementType = elementType.UNKNOWN;
        switch(value){
            case "water":
                elementType = ElementType.WATER;
                break;
        }
    }

    private void determineHighway(String value) {
        elementType = ElementType.UNKNOWN;
        switch (value){
            case "motorway":
                elementType = ElementType.MOTORWAY;
                break;
            case "motorway_link":
                elementType = ElementType.MOTORWAY_LINK;
                break;
            case "trunk":
                elementType = ElementType.TRUNK_ROAD;
                break;
            case "trunk_link":
                elementType = ElementType.TRUNK_ROAD_LINK;
                break;
            case "primary":
                elementType = ElementType.PRIMARY_ROAD;
                break;
            case "primary_link":
                elementType = ElementType.PRIMARY_ROAD_LINK;
                break;
            case "secondary":
                elementType = ElementType.SECONDARY_ROAD;
                break;
            case "seconday_link":
                elementType = ElementType.SECONDARY_ROAD_LINK;
                break;
            case "tertiary":
                elementType = ElementType.TERTIARY_ROAD;
                break;
            case "tertiary_link":
                elementType = ElementType.TERTIARY_ROAD_LINK;
                break;
            case "unclassified":
                elementType = ElementType.UNCLASSIFIED_ROAD;
                break;
            case "residential":
                elementType = ElementType.RESIDENTIAL_ROAD;
                break;
            case "living_street":
                elementType = ElementType.LIVING_STREET;
                break;
            case "service":
                elementType = ElementType.SERVICE_ROAD;
                break;
            case "bus_guideway":
                elementType = ElementType.BUS_GUIDEWAY;
                break;
            case "escape":
                elementType = ElementType.ESCAPE;
                break;
            case "raceway":
                elementType = ElementType.RACEWAY;
                break;
            case "pedestrian":
                elementType = ElementType.PEDESTRIAN_STERET;
                break;
            case "track":
                elementType = ElementType.TRACK;
                break;
            case "steps":
                elementType = ElementType.STEPS;
                break;
            case "footway":
                elementType = ElementType.FOOTWAY;
                break;
            case "bridleway":
                elementType = ElementType.BRIDLEWAY;
                break;
            case "cycleway":
                elementType = ElementType.CYCLEWAY;
                break;
            case "path":
                elementType = ElementType.PATH;
                break;
            case "road":
                elementType = ElementType.ROAD;
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
                        break;
                }
                break;
            case "way":
                switch (elementType){
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
                    case LIVING_STREET:
                    case SERVICE_ROAD:
                    case BUS_GUIDEWAY:
                    case ESCAPE:
                    case RACEWAY:
                    case PEDESTRIAN_STERET:
                    case TRACK:
                    case STEPS:
                    case FOOTWAY:
                    case BRIDLEWAY:
                    case CYCLEWAY:
                    case PATH:
                    case ROAD:
                        addRoad(elementType, false);
                        break;
                    case WATER:
                        addWater(elementType, false);
                        break;
                    case UNKNOWN:
                        //UnknownWay unknownWay = new UnknownWay(path);
                        //model.addWayElement(elementType, unknownWay);
                        break;
                } break;
            case "relation":
                switch(elementType){
                    case WATER:
                        addWater(elementType, true);
                        break;
                }
                break;
        }
    }

    private void addName(ElementType type){
        PlaceName placeName = new PlaceName(longitude * longitudeFactor, -latitude, name);
        Pointer p = new Pointer(longitude * longitudeFactor, -latitude, placeName);
        switch (type){
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

    private void addRoad(ElementType type, Boolean isRelation) {
        Path2D path;
        if(!isRelation) {
            path = way.toPath2D();
            Road road = new Road(path, name);
            for (int i = 0; i < way.size(); i+=5) {
                Pointer p = new Pointer((float) way.get(i).getX(), (float) way.get(i).getY(), road);
                model.getElements().get(type).putPointer(p);
            }
        }
        else {
            path = relation.toPath2D();
            Road road = new Road(path, name);
            for (int i = 0; i < relation.size(); i++){
                for (int j = 0; i < relation.get(i).size(); j+=5){
                    Pointer p = new Pointer((float) relation.get(i).get(0).getX(), (float) relation.get(i).get(0).getY(), road);
                    model.getElements().get(type).putPointer(p);
                }
            }
        }
        //System.out.println(name + " Added :)");
    }

    private void addWater(ElementType type, Boolean isRelation) {
        Path2D path;
        if (!isRelation) {
            path = way.toPath2D();
            Water water = new Water(path, name);
            for (int i = 0; i < way.size(); i += 5) {
                Pointer p = new Pointer((float) way.get(i).getX(), (float) way.get(i).getY(), water);
                model.getElements().get(type).putPointer(p);
            }
        } else {
            path = relation.toPath2D();
            Water water = new Water(path, name);
            for (int i = 0; i < relation.size()-1; i++) {
                if(relation.get(i) != null) {
                    for (int j = 0; j < relation.get(i).size(); j += 5) {
                        Pointer p = new Pointer((float) relation.get(i).get(j).getX(), (float) relation.get(i).get(j).getY(), water);
                        model.getElements().get(type).putPointer(p);
                    }
                }else continue;
            }
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
