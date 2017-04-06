package OSM;

import Enums.BoundType;
import Enums.OSMEnums.WayType;
import Helpers.LongToPointMap;
import KDtree.NodeGenerator;
import KDtree.Point;
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
    private WayType wayType;
    private String name;
    private boolean isCity;
    private float longitudeFactor;
    private Model model;
    private int loadednodes, loadedRelations, loadedWays;
    private boolean initialized;
    private float latitude;
    private float longitude;
    private ArrayList<Pointer> cityNames;

    private OSMHandler() {
        idToNode = new LongToPointMap(22);
        idToWay = new HashMap<>();
        model = Model.getInstance();
        nodeGenerator = new NodeGenerator();
        cityNames = new ArrayList<>();
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
                float avglat = minLatitude + (maxLatitude - minLatitude)/2;
                longitudeFactor = (float) Math.cos(avglat/180*Math.PI);
                minLongitude *= longitudeFactor;
                maxLongitude *= longitudeFactor;
                minLatitude = -minLatitude;
                maxLatitude = -maxLatitude;
                model.setBound(BoundType.MIN_LONGITUDE,minLongitude);
                model.setBound(BoundType.MAX_LONGITUDE,maxLongitude);
                model.setBound(BoundType.MIN_LATITUDE,minLatitude);
                model.setBound(BoundType.MAX_LATITUDE,maxLatitude);
                break;
            case "node":
                long id = Long.parseLong(atts.getValue("id"));
                latitude = Float.parseFloat(atts.getValue("lat"));
                longitude = Float.parseFloat(atts.getValue("lon"));
                idToNode.put(id, longitude * longitudeFactor, -latitude);
                nodeGenerator.addPoint(new Point2D.Float(longitude * longitudeFactor, -latitude));

                name = "";
                isCity = false;

                loadednodes++;
                if ((loadednodes & 0xFFFF) == 0) {
                    System.out.println("Numnodes: " + loadednodes);
                }
                break;
            case "relation":
                relation = new OSMRelation();
                wayType = WayType.UNKNOWN;
                loadedRelations++;
                if ((loadedRelations & 0xFF) == 0) {
                    System.out.println("Numrelations: " + loadedRelations);
                }
                break;
            case "way":
                if(!initialized){
                    nodeGenerator.initialise();
                    for (WayType type : WayType.values()) {
                        nodeGenerator.setupTree(model.getElements().get(type));
                    }
                    initialized = true;

                    for(Pointer p : cityNames){
                        model.getElements().get(WayType.CITYNAME).putPointer(p);
                    }
                }

                way = new OSMWay();
                id = Long.parseLong(atts.getValue("id"));
                wayType = WayType.UNKNOWN;
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
                        determineWater(v);
                        break;
                    case "place":
                        if(v.equals("city")) isCity = true;
                        break;
                }
                break;
            case "member":
                ref = Long.parseLong(atts.getValue("ref"));
                relation.add(idToWay.get(ref));
                break;
        }
    }

    private void determineWater(String value){
        wayType = wayType.UNKNOWN;
        switch(value){
            case "water":
                wayType = WayType.WATER;
                break;
        }
    }

    private void determineHighway(String value) {
        wayType = WayType.UNKNOWN;
        switch (value){
            case "motorway":
                wayType = WayType.MOTORWAY;
                break;
            case "motorway_link":
                wayType = WayType.MOTORWAY_LINK;
                break;
            case "trunk":
                wayType = WayType.TRUNK_ROAD;
                break;
            case "trunk_link":
                wayType = WayType.TRUNK_ROAD_LINK;
                break;
            case "primary":
                wayType = WayType.PRIMARY_ROAD;
                break;
            case "primary_link":
                wayType = WayType.PRIMARY_ROAD_LINK;
                break;
            case "secondary":
                wayType = WayType.SECONDARY_ROAD;
                break;
            case "seconday_link":
                wayType = WayType.SECONDARY_ROAD_LINK;
                break;
            case "tertiary":
                wayType = WayType.TERTIARY_ROAD;
                break;
            case "tertiary_link":
                wayType = WayType.TERTIARY_ROAD_LINK;
                break;
            case "unclassified":
                wayType = WayType.UNCLASSIFIED_ROAD;
                break;
            case "residential":
                wayType = WayType.RESIDENTIAL_ROAD;
                break;
            case "living_street":
                wayType = WayType.LIVING_STREET;
                break;
            case "service":
                wayType = WayType.SERVICE_ROAD;
                break;
            case "bus_guideway":
                wayType = WayType.BUS_GUIDEWAY;
                break;
            case "escape":
                wayType = WayType.ESCAPE;
                break;
            case "raceway":
                wayType = WayType.RACEWAY;
                break;
            case "pedestrian":
                wayType = WayType.PEDESTRIAN_STERET;
                break;
            case "track":
                wayType = WayType.TRACK;
                break;
            case "steps":
                wayType = WayType.STEPS;
                break;
            case "footway":
                wayType = WayType.FOOTWAY;
                break;
            case "bridleway":
                wayType = WayType.BRIDLEWAY;
                break;
            case "cycleway":
                wayType = WayType.CYCLEWAY;
                break;
            case "path":
                wayType = WayType.PATH;
                break;
            case "road":
                wayType = WayType.ROAD;
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName){
            case "node":
                if(isCity) {
                    addCityName();
                    System.out.println(name + " added.");
                }
                break;
            case "way":
                switch (wayType){
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
                        addRoad(wayType, false);
                        break;
                    case WATER:
                        addWater(wayType, false);
                        break;
                    case UNKNOWN:
                        //UnknownWay unknownWay = new UnknownWay(path);
                        //model.addWayElement(wayType, unknownWay);
                        break;
                } break;
            case "relation":
                switch(wayType){
                    case WATER:
                        addWater(wayType, true);
                }
                break;
        }
    }

    private void addCityName(){
        CityName cityName = new CityName(longitude * longitudeFactor, -latitude, name);
        Pointer p = new Pointer(longitude * longitudeFactor, -latitude, cityName);
        cityNames.add(p);
    }

    private void addRoad(WayType type, Boolean isRelation) {
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

    private void addWater(WayType type, Boolean isRelation) {
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
            for (int i = 0; i < relation.size(); i++) {
                for (int j = 0; j < relation.get(i).size(); j += 5) {
                    Pointer p = new Pointer((float) relation.get(i).get(j).getX(), (float) relation.get(i).get(j).getY(), water);
                    model.getElements().get(type).putPointer(p);
                }
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
