package Model.Coastlines;

import Enums.FileType;
import Enums.OSMEnums.ElementType;
import Helpers.OSDetector;

import View.PopupWindow;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * Class details:
 * ContentHandler able to create files only containing complete
 * coastlines and their corresponding nodes. Incomplete or missing
 * segments of a coastline is not fixed by this class, but a fix is
 * being made by adding country borders as complete coastlines or
 * segments of coastline. An example is the border between Denmark
 * and Germany.
 *
 * USAGE:
 * - Start this class using your console
 * - Select an OSM or Zipped OSM file to load from
 * - Select a destination to save to
 * - DONE
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 20-03-2017
 */
public final class CoastlineFileGenerator implements ContentHandler {

    /* --- CONSTANTS DEFINED BY OSM --- */
    private static final String XML_INFO = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private static final String OSM_INFO = "<osm version=\"0.6\" "
        + "generator=\"CGImap 0.5.8 (12631 thorn-02.openstreetmap.org)\" "
        + "copyright=\"OpenStreetMap and contributors\" "
        + "attribution=\"http://www.openstreetmap.org/copyright\" "
        + "license=\"http://opendatacommons.org/licenses/odbl/1-0/\">";

    /* --- SINGLETON INSTANCE --- */
    private static CoastlineFileGenerator instance;

    /* --- COLLECTION FIELDS --- */
    private static Map<Long, NodeCarrier> idToNode;
    private static Map<Long, WayCarrier> idToWay;
    private static Map<Long, WayCarrier> coastlines;
    private static Map<Long, NodeCarrier> coastlineNodes;

    /* --- OTHER FIELDS --- */
    private WayCarrier way;
    private ElementType elementType;
    private boolean administrative_boundary, admin_level_nation, isMaritime;
    private static double minLatitude, maxLatitude, minLongitude, maxLongitude;
    private static long timer;

    /* --- DEBUGGING TOGGLE --- */
    private static final boolean DEBUGGING = false; // change this for more information

    /**
     * Constructor that initializes values and collections
     */
    private CoastlineFileGenerator()
    {
        idToNode = new HashMap<>();
        idToWay = new HashMap<>();
        coastlines = new HashMap<>();
        coastlineNodes = new HashMap<>();
        administrative_boundary = false;
        admin_level_nation = false;
    }

    /**
     * Returns the CoastlineFileGenerator instance
     */
    public static CoastlineFileGenerator getInstance()
    {
        if (instance == null) {
            instance = new CoastlineFileGenerator();
        }
        return instance;
    }

    /**
     * Client where from a osm file can be chosen and from that file generates
     * a new file containing only coastlines that also adds country borders as
     * either a coastline segment or an entire coastline. Note that this class
     * cannot handle if coastline segments is missing or unconnected.
     */
    public static void main(String[] args)
    {
        PopupWindow.infoBox(
            null,
            "The function of this program is to load an osm file and discover any "
                + "and all coastlines\n as well as any nation borders on land to create a rough outline "
                + "of the land. \n\nOn the next window please select an osm file.",
            "About this helper");
        JFileChooser fileChooser = PopupWindow.fileLoader(false, null);
        timer = System.currentTimeMillis();
        try {
            if (fileChooser == null)
                throw new Exception();
            String filename = fileChooser.getSelectedFile().getAbsolutePath();
            if (filename == null)
                throw new Exception();
            loadOSMFile(filename);
            saveToFile();
        } catch (Exception e) {
            PopupWindow.errorBox(null, "No file selected to open from!");
        }
    }

    /**
     * Loads either an OSM file or a zipped OSM file
     * @param fileName The filename to load from
     */
    private static void loadOSMFile(String fileName)
    {
        String pathStart = OSDetector.getPathPrefix();

        System.out.println("Loading from file: \"" + fileName + "\"\n");

        if (fileName.endsWith(FileType.OSM.getExtension())) {
            loadOSM(new InputSource(pathStart + fileName));
        } else if (fileName.endsWith(FileType.ZIP.getExtension())) {
            try {
                ZipInputStream zip = new ZipInputStream(new FileInputStream(fileName));
                try {
                    zip.getNextEntry();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                loadOSM(new InputSource(zip));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Loading time: " + ((System.currentTimeMillis() - timer) / 1000) + " s");

        PopupWindow.infoBox(
            null,
            "The following file has been loaded:\n\"" + fileName + "\"\n into"
                + " the helper which contains " + coastlines.size() + " coastlines.\n\n"
                + "On the next window please select a location and a name for the new osm file.",
            "File loaded");
    }

    /**
     * Loads from a OSM file from a InputSource
     */
    private static void loadOSM(InputSource inputSource)
    {
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(getInstance());
            reader.parse(inputSource);
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the collection of coastlines to a new osm file
     */
    private static void saveToFile()
    {
        // @see testCoastlines.osm for inspiration
        Charset charset = Charset.forName("UTF-8"); // unicode encoding

        JFileChooser fileChooser = PopupWindow.fileSaver(false, null);
        timer = System.currentTimeMillis();
        if (fileChooser != null) {
            // retrieves path
            File selectedFile = fileChooser.getSelectedFile();
            String file = selectedFile.getAbsolutePath();
            Path path = Paths.get(file);

            System.out.println("Saving data to file \"" + file + "\"\n");

            try (BufferedWriter writer = Files.newBufferedWriter(path, charset)) {
                // header
                writer.write(XML_INFO);
                writer.newLine();
                writer.write(OSM_INFO);
                writer.newLine();

                // write bounds
                StringBuilder boundsBuilder = new StringBuilder();
                boundsBuilder.append("<bounds minlat=\"");
                boundsBuilder.append(minLatitude);
                boundsBuilder.append("\" minlon=\"");
                boundsBuilder.append(minLongitude);
                boundsBuilder.append("\" maxlat=\"");
                boundsBuilder.append(maxLatitude);
                boundsBuilder.append("\" maxlon=\"");
                boundsBuilder.append(maxLongitude);
                boundsBuilder.append("\"/>");
                writer.write(boundsBuilder.toString());
                writer.newLine();
                writer.newLine();

                // write nodes
                for (NodeCarrier node : coastlineNodes.values()) {
                    writer.write(node.toString());
                    writer.newLine();
                }
                writer.newLine();

                // write ways
                for (WayCarrier way : coastlines.values()) {
                    writer.write(way.toString());
                    writer.newLine();
                    if (DEBUGGING)
                        System.out.println(way.getInfo());
                }
                writer.newLine();

                // footer
                writer.write("</osm>");

                PopupWindow.infoBox(null, "Coastlines has been saved to \"" + file + "\"...\nProgram will now terminate",
                    "File saved...");
            } catch (IOException x) {
                System.err.format("IOException: %s%n", x);
            }

            System.out.println("\n... writing complete ... closing file");
        } else {
            PopupWindow.errorBox(null, "No file selected!");
        }

        System.out.println("Saving time: " + ((System.currentTimeMillis() - timer) / 1000) + " s");
    }

    /**
     * The beginning of an osm element
     */
    @Override
    public void startElement(String uri, String localName, String qName,
        Attributes atts) throws SAXException
    {
        switch (qName) {
        case "bounds":
            minLatitude = Float.parseFloat(atts.getValue("minlat"));
            maxLatitude = Float.parseFloat(atts.getValue("maxlat"));
            minLongitude = Float.parseFloat(atts.getValue("minlon"));
            maxLongitude = Float.parseFloat(atts.getValue("maxlon"));
            break;
        case "node":
            long id = Long.parseLong(atts.getValue("id"));
            double latitude = Double.parseDouble(atts.getValue("lat"));
            double longitude = Double.parseDouble(atts.getValue("lon"));
            NodeCarrier node = new NodeCarrier(id, longitude, latitude);
            idToNode.put(id, node); // see OSMHandler for converting
            break;
        case "way":
            id = Long.parseLong(atts.getValue("id"));
            way = new WayCarrier(id);
            idToWay.put(id, way);
            elementType = ElementType.UNKNOWN;
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
            switch (k) {
            case "natural":
                if (v.equals(Coastline.OSM_IDENTIFIER)) {
                    elementType = ElementType.COASTLINE;
                }
                break;
            case "boundary":
                if (v.equals("administrative"))
                    administrative_boundary = true;
                if (DEBUGGING)
                    System.out.println("\"" + v + "\" equals boundary administrative " + v.equals("administrative"));
                break;
            case "admin_level":
                if (v.equals("2"))
                    admin_level_nation = true;
                if (DEBUGGING)
                    System.out.println("\"" + v + "\" equals admin level 2 " + v.equals("2"));
                break;
            case "border_type":
                if (v.equals("nation"))
                    elementType = ElementType.COUNTRY_BOUNDARY_LAND;
                if (DEBUGGING)
                    System.out.println("\"" + v + "\" equals border type nation " + v.equals("nation"));
                break;
            case "maritime":
                if (v.equals("yes"))
                    isMaritime = true;
                if (DEBUGGING)
                    System.out.println("\"" + v + "\" equals maritime yes " + v.equals("yes"));
                break;
            }
            break;
        }
    }

    /**
     * The ending of an osm element
     */
    @Override
    public void endElement(String uri, String localName, String qName)
        throws SAXException
    {
        if (qName.equals("way")) {
            switch (elementType) {
            case COASTLINE:
                // coastLineFix();
                addWay(way);
                break;
            case COUNTRY_BOUNDARY_LAND:
                if (DEBUGGING)
                    System.out.println("maritime: " + isMaritime + " boundary: " + administrative_boundary + " admin level: " + admin_level_nation);
                if (!isMaritime && administrative_boundary && admin_level_nation) {
                    // coastLineFix();
                    addWay(way);
                } // else ignore
                break;
            }
        } // else ignore
    }

    /**
     * Adds an osm way to the collection of coastlines
     */
    private void addWay(WayCarrier way)
    {
        if (DEBUGGING)
            System.out.println("Added way... Total: " + coastlines.size());
        way.putCoastlineTag();
        coastlines.put(way.ref, way);
        coastlineNodes.putAll(way.getMap());
    }

    /* ---------- IGNORE METHODS UNTIL LINE 382 ---------- */

    @Override
    public void setDocumentLocator(Locator locator) {}

    @Override
    public void startDocument() throws SAXException {}

    @Override
    public void endDocument() throws SAXException {}

    @Override
    public void startPrefixMapping(String prefix, String uri)
        throws SAXException {}

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {}

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException
    {
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length)
        throws SAXException {}

    @Override
    public void processingInstruction(String target, String data)
        throws SAXException {}

    @Override
    public void skippedEntity(String name) throws SAXException {}

    /* ---- INTERNAL CLASSES - DO NOT USE OUTSIDE THIS CLASS ---- */

    /**
     * Object that can carry information about an OSM Node
     */
    public static class NodeCarrier {
        long ref;
        double lat, lon;

        /**
         * Constructor for Nodes
         */
        public NodeCarrier(long ref, double longitude, double latitude)
        {
            this.ref = ref;
            lon = longitude;
            lat = latitude;
        }

        /**
         * Returns a OSM format of this object
         */
        @Override
        public String toString()
        {
            return "<node id=\"" + ref + "\" "
                + "lat=\"" + lat + "\" lon=\"" + lon + "\"/>";
        }
    }

    /**
     * Object that can carry information about an OSM Way
     */
    public static class WayCarrier extends ArrayList<NodeCarrier> {
        private long ref;
        private Map<String, String> tags;

        /**
         * Constructor for Ways
         */
        public WayCarrier(long ref)
        {
            this.ref = ref;
            tags = new HashMap<>();
        }

        /**
         * Returns a OSM format of this object
         */
        @Override
        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            sb.append("<way id=\"");
            sb.append(ref);
            sb.append("\">");

            // add node references
            for (NodeCarrier node : this) {
                sb.append("<nd ref=\"");
                sb.append(node.ref);
                sb.append("\"/>");
            }

            // add tags
            for (String key : tags.keySet()) {
                sb.append("<tag k=\"");
                sb.append(key);
                sb.append("\" v=\"");
                sb.append(tags.get(key));
                sb.append("\"/>");
            }

            // end
            sb.append("</way>");
            return sb.toString();
        }

        /**
         * Returns a Map of Nodes with all references
         */
        private Map<Long, NodeCarrier> getMap()
        {
            Map<Long, NodeCarrier> map = new HashMap<>();
            for (NodeCarrier node : this) {
                map.put(node.ref, node);
            }
            return map;
        }

        /**
         * Puts an tag with coastline on this object
         */
        private void putCoastlineTag() { tags.put("natural", "coastline"); }

        /**
         * Returns a large amount of info about this way
         */
        private String getInfo()
        {
            StringBuilder sb = new StringBuilder();
            sb.append("Way: ");
            sb.append(ref);
            sb.append(" contains ");
            sb.append(this.size());
            sb.append(" number of elements.\n Nodes (ref): ");
            for (NodeCarrier node : this) {
                sb.append("\n ... ");
                sb.append(node.ref);
            }
            sb.append("\n Tags:");
            if (tags.isEmpty())
                sb.append("N/A");
            for (String key : tags.keySet()) {
                sb.append("\n ... k=\"");
                sb.append(key);
                sb.append("\" v=\"");
                sb.append(tags.get(key));
                sb.append("\"");
            }
            return sb.toString();
        }
    }

    /**
     * Resets instance to be used again without interference
     */
    public void resetInstance() { instance = null; }
}
