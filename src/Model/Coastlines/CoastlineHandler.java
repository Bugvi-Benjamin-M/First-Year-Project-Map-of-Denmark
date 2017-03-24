package Model.Coastlines;

import Enums.BoundType;
import Enums.OSMEnums.WayType;
import OSM.OSMNode;
import OSM.OSMWay;
import jdk.internal.org.xml.sax.Attributes;
import jdk.internal.org.xml.sax.ContentHandler;
import jdk.internal.org.xml.sax.Locator;
import jdk.internal.org.xml.sax.SAXException;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 23-03-2017.
 * @project BFST
 */
public class CoastlineHandler implements ContentHandler {

    private static CoastlineHandler instance;
    private static CoastlineFactory factory;

    private static Map<Long, OSMNode> idToNode;
    private static Map<Long, OSMWay> idToWay;
    private static Map<OSMNode,OSMWay> coastlines;

    private OSMWay way;
    private WayType wayType;

    private CoastlineHandler() {
        factory = new CoastlineFactory();
        idToNode = new HashMap<>();
        idToWay = new HashMap<>();
        coastlines = new HashMap<>();
    }

    public static CoastlineHandler getInstance() {
        if (instance == null) {
            instance = new CoastlineHandler();
        }
        return instance;
    }

    public static void resetInstance() {
        instance = null;
    }

    public CoastlineFactory getCoastlineFactory() {
        return factory;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        switch (qName) {
            case "bounds":
                handleBounds(atts);
                break;
            case "node":
                handleNode(atts);
                break;
            case "way":
                handleWay(atts);
                break;
            case "nd":
                handleWayNode(atts);
                break;
            case "tag":
                handleTag(atts);
                break;
        }
    }

    private void handleBounds(Attributes attributes) {
        double minLatitude, maxLatitude, minLongitude, maxLongitude;
        minLatitude = Double.parseDouble(attributes.getValue("minlat"));
        maxLatitude = Double.parseDouble(attributes.getValue("maxlat"));
        minLongitude = Double.parseDouble(attributes.getValue("minlon"));
        maxLongitude = Double.parseDouble(attributes.getValue("maxlon"));
        factory.addBound(BoundType.MIN_LATITUDE,-minLatitude);
        factory.addBound(BoundType.MAX_LATITUDE,-maxLatitude);
        factory.addBound(BoundType.MIN_LONGITUDE,minLongitude);
        factory.addBound(BoundType.MAX_LONGITUDE,maxLongitude);
    }

    private void handleNode(Attributes attributes) {
        long id = Long.parseLong(attributes.getValue("id"));
        float latitude = Float.parseFloat(attributes.getValue("lat"));
        float longitude = Float.parseFloat(attributes.getValue("lon"));
        idToNode.put(id,new OSMNode(longitude,-latitude));
    }

    private void handleWay(Attributes attributes) {
        way = new OSMWay();
        long id = Long.parseLong(attributes.getValue("id"));
        wayType = WayType.UNKNOWN;
        idToWay.put(id, way);
    }

    private void handleWayNode(Attributes attributes) {
        long ref = Long.parseLong(attributes.getValue("ref"));
        way.add(idToNode.get(ref));
    }

    private void handleTag(Attributes attributes) {
        String k = attributes.getValue("k");
        String v = attributes.getValue("v");
        switch (k) {
            case "natural":
                if (v.equals(Coastline.OSM_IDENTIFIER)) {
                    wayType = WayType.COASTLINE;
                }
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName) {
            case "way":
                switch (wayType){
                    case COASTLINE:
                        coastLineFix();
                        break;
                }
                break;
            case "osm":
                handleEndOfFile();
                break;
        }
    }

    private void coastLineFix() {
        OSMWay before = coastlines.remove(way.getFromNode());
        OSMWay after = coastlines.remove(way.getToNode());
        OSMWay merged = new OSMWay();
        if (before != null) {
            merged.addAll(before.subList(0, before.size()-1));
        }
        merged.addAll(way);
        if (after != null) {
            merged.addAll(after.subList(1, after.size()));
        }
        coastlines.put(merged.getFromNode(), merged);
        coastlines.put(merged.getToNode(), merged);
    }

    private void handleEndOfFile() {
        for (OSMWay way: coastlines.values()) {
            factory.insertCoastline(way);
        }
    }

    /* ---------------- IGNORE REST OF CLASS (NOT USED) ---------------- */
    @Override
    public void setDocumentLocator(Locator locator) {}

    @Override
    public void startDocument() throws SAXException {}

    @Override
    public void endDocument() throws SAXException {}

    @Override
    public void startPrefixMapping(String s, String s1) throws SAXException {}

    @Override
    public void endPrefixMapping(String s) throws SAXException {}

    @Override
    public void characters(char[] chars, int i, int i1) throws SAXException {}

    @Override
    public void ignorableWhitespace(char[] chars, int i, int i1) throws SAXException {}

    @Override
    public void processingInstruction(String s, String s1) throws SAXException {}

    @Override
    public void skippedEntity(String s) throws SAXException {}

}
