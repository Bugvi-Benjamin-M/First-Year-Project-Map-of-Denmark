package Model.Coastlines;

import Enums.BoundType;
import Enums.OSMEnums.WayType;
import OSM.OSMNode;
import OSM.OSMWay;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.awt.*;
import java.awt.geom.Point2D;
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

    private static Map<Long, Point2D> idToNode;
    private static Map<Long, OSMWay> idToWay;
    private static Map<Point2D,OSMWay> coastlines;

    private OSMWay way;
    private WayType wayType;
    private float longFactor;

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
        float minLatitude, maxLatitude, minLongitude, maxLongitude;
        minLatitude = Float.parseFloat(attributes.getValue("minlat"));
        // System.out.println("minlat: "+minLatitude);
        maxLatitude = Float.parseFloat(attributes.getValue("maxlat"));
        // System.out.println("maxlat: "+maxLatitude);
        minLongitude = Float.parseFloat(attributes.getValue("minlon"));
        // System.out.println("minlon: "+minLongitude);
        maxLongitude = Float.parseFloat(attributes.getValue("maxlon"));
        // System.out.println("maxlon: "+maxLongitude);
        factory.addBound(BoundType.MIN_LONGITUDE,minLongitude);
        factory.addBound(BoundType.MAX_LONGITUDE,maxLongitude);
        factory.setLongitudeFactor(minLatitude,maxLatitude);
        longFactor = factory.getLongitudeFactor();
        factory.addBound(BoundType.MIN_LATITUDE,-minLatitude);
        factory.addBound(BoundType.MAX_LATITUDE,-maxLatitude);
    }

    private void handleNode(Attributes attributes) {
        long id = Long.parseLong(attributes.getValue("id"));
        float latitude = Float.parseFloat(attributes.getValue("lat"));
        float longitude = Float.parseFloat(attributes.getValue("lon"));
        idToNode.put(id,new Point2D.Float(longitude* longFactor,-latitude));
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

}
