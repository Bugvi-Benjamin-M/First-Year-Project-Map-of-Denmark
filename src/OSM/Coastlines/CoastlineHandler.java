package OSM.Coastlines;

import Enums.OSMEnums.WayType;
import OSM.OSMNode;
import OSM.OSMWay;
import jdk.internal.org.xml.sax.Attributes;
import jdk.internal.org.xml.sax.ContentHandler;
import jdk.internal.org.xml.sax.Locator;
import jdk.internal.org.xml.sax.SAXException;

import java.util.HashMap;
import java.util.Map;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 20-03-2017.
 * @project BFST
 */
public final class CoastlineHandler implements ContentHandler {

    private static CoastlineHandler instance;

    private Map<Long, OSMNode> idToNode;
    private Map<Long, OSMWay> idToWay;

    private OSMWay way;
    private WayType wayType;

    private boolean administrative_boundary, admin_level_nation, isMaritime;

    private float minLatitude, maxLatitude, minLongitude, maxLongitude;
    private float longitudeFactor;

    private CoastlineHandler() {
        idToNode = new HashMap<>();
        idToWay = new HashMap<>();
        administrative_boundary = false;
        admin_level_nation = false;
    }

    public static CoastlineHandler getInstance() {
        if (instance == null) {
            instance = new CoastlineHandler();
        }
        return instance;
    }

    /**
     * Main method that takes an osm map as a parameter and finds all and any coastlines
     * @param args
     */
    public static void main(String[] args) {
        // TODO: Load an osm file and save to a new osm file ("coastlines.osm")
    }

    @Override
    public void setDocumentLocator(Locator locator) {

    }

    @Override
    public void startDocument() throws SAXException {

    }

    @Override
    public void endDocument() throws SAXException {
        // TODO: Go through added ways and merge those with same end points
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {

    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        switch (qName) {
            case "bounds":
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
                idToWay.put(id, way);

                wayType = WayType.UNKNOWN;
                administrative_boundary = false;
                admin_level_nation = false;
                isMaritime = false;
                break;
            case "nd":
                long ref = Long.parseLong(atts.getValue("ref"));
                way.add(idToNode.get(ref));
                break;
            case "tag":
                String k = atts.getValue("k");
                String v = atts.getValue("v");
                switch (k){
                    case "natural":
                        if (v.equals("coastline")) wayType = WayType.COASTLINE;
                        break;
                    case "boundary":
                        if (v.equals("administrative")) administrative_boundary = true;
                        break;
                    case "admin_level":
                        if (v.equals("2")) admin_level_nation = true;
                        break;
                    case "border_type":
                        if (v.equals("nation")) wayType = WayType.COUNTRY_BOUNDARY_LAND;
                        break;
                    case "maritime":
                        if (v.equals("yes")) isMaritime = true;
                        break;
                }
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("way")) {
            switch (wayType) {
                case COASTLINE:
                    // TODO: add all
                    break;
                case COUNTRY_BOUNDARY_LAND:
                    if (!isMaritime && administrative_boundary && admin_level_nation) {
                        // TODO: add boundary as coastline
                    }   // else ignore
                    break;
            }
        } // else ignore
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
