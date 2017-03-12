package OSM;

import Enums.RoadType;
import Enums.WayType;
import Model.*;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.awt.geom.Path2D;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jakob on 06-03-2017.
 */
public final class OSMHandler implements ContentHandler {
    private static OSMHandler handler;

    private Map<Long, OSMNode> idToNode;
    private Map<Long, OSMWay> idToWay;
    private OSMWay way;
    private OSMRelation relation;
    private OSMNode node;
    private WayType wayType;
    private RoadType roadType;
    private float longitudeFactor;
    private Model model;

    private OSMHandler() {
        idToNode = new HashMap<>();
        idToWay = new HashMap<>();
        model = Model.getInstance();
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
                model.setBounds(minLatitude, maxLatitude, minLongitude, maxLongitude);
                break;
            case "node":
                long id = Long.parseLong(atts.getValue("id"));
                float latitude = Float.parseFloat(atts.getValue("lat"));
                float longitude = Float.parseFloat(atts.getValue("lon"));
                idToNode.put(id, new OSMNode(longitude* longitudeFactor, -latitude));
                break;
            case "way":
                way = new OSMWay();
                id = Long.parseLong(atts.getValue("id"));
                wayType = WayType.UNKNOWN;
                idToWay.put(id, way);
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
                        switch (v){
                            case "service":
                                roadType = RoadType.SERVICE;
                                break;
                            case "tertiary":
                                roadType = RoadType.TERTIARY;
                                break;
                            case "unclassified":
                                roadType = RoadType.UNCLASSIFIED;
                                break;
                        }
                        break;
                }
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName){
            case "way":
                Path2D path = way.toPath2D();
                switch (wayType){
                    case ROAD:
                        Road road = new Road(roadType, path);
                        model.addWayElement(wayType, road);
                        break;
                    case UNKNOWN:
                        UnknownWay unknownWay = new UnknownWay(path);
                        model.addWayElement(wayType, unknownWay);
                        break;
                }
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
