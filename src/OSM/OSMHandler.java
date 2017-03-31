package OSM;

import Enums.BoundType;
import Enums.OSMEnums.WayType;
import Enums.RoadType;
import Enums.ZoomLevel;
import Helpers.LongToPointMap;
import KDtree.NodeGenerator;
import KDtree.Pointer;
import Model.Model;
import Model.Road;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
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
    private RoadType roadType;
    private float longitudeFactor;
    private Model model;
    private int loadednodes, loadedRelations, loadedWays;
    private boolean initialized;

    private OSMHandler() {
        idToNode = new LongToPointMap(22);
        idToWay = new HashMap<>();
        model = Model.getInstance();
        nodeGenerator = new NodeGenerator();
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
                float latitude = Float.parseFloat(atts.getValue("lat"));
                float longitude = Float.parseFloat(atts.getValue("lon"));
                idToNode.put(id, longitude * longitudeFactor, -latitude);
                nodeGenerator.addPoint(new Point2D.Float(longitude * longitudeFactor, -latitude));
                loadednodes++;
                if ((loadednodes & 0xFFFF) == 0) {
                    System.out.println("Numnodes: " + loadednodes);
                }
                break;
            case "way":
                if(!initialized){
                    nodeGenerator.initialise();
                    for (ZoomLevel level : ZoomLevel.values()) {
                        nodeGenerator.setupTree(model.getRoads().get(level));
                    }
                    initialized = true;
                }

                way = new OSMWay();
                id = Long.parseLong(atts.getValue("id"));
                wayType = WayType.UNKNOWN;
                idToWay.put(id, way);
                loadedWays++;
                if ((loadedWays & 0xFFFF) == 0) {
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
                        wayType = WayType.ROAD;
                        determineHighway(v);
                        break;
                }
                break;
        }
    }

    private void determineHighway(String value) {
        roadType = roadType.UNKNOWN;
        switch (value){
            case "service":
                roadType = RoadType.SERVICE;
                break;
            case "primary":
                roadType = RoadType.PRIMARY;
                break;
            case "secondary":
                roadType = RoadType.SECONDARY;
                break;
            case "tertiary":
                roadType = RoadType.TERTIARY;
                break;
            case "unclassified":
                roadType = RoadType.UNCLASSIFIED;
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName){
            case "way":
                switch (wayType){
                    case ROAD:
                        switch (roadType) {
                            case SERVICE:
                                addRoad(ZoomLevel.LEVEL_0);
                                break;
                            case PRIMARY:
                                addRoad(ZoomLevel.LEVEL_3);
                                break;
                        } break;

                    case UNKNOWN:
                        //UnknownWay unknownWay = new UnknownWay(path);
                        //model.addWayElement(wayType, unknownWay);
                        break;
                } break;
        }
    }

    private void addRoad(ZoomLevel level) {
        Path2D path = way.toPath2D();
        Road road = new Road(roadType, path);
        for (int i = 0; i < way.size(); i++) {
            Pointer p = new Pointer((float) way.get(i).getX(), (float) way.get(i).getY(), road);
            model.getRoads().get(level).putPointer(p);
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
