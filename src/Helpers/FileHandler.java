package Helpers;

import Enums.BoundType;
import Enums.FileType;
import Enums.OSMEnums.ElementType;
import Exceptions.FileWasNotFoundException;
import KDtree.KDTree;
import Model.Addresses.TenarySearchTrie;
import Model.Coastlines.CoastlineFactory;
import Model.Coastlines.CoastlineHandler;
import Model.Elements.POI;
import Model.Elements.RoadEdge;
import Model.Model;
import OSM.OSMHandler;
import RouteSearch.RoadGraph;
import View.PopupWindow;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipInputStream;

import static Helpers.GlobalValue.DEBUG_MODE_ACTIVE;
import static Helpers.GlobalValue.DEFAULT_COASTLINE_FILE;

/**
 * The FileHandler class purpose is to handle saving and loading
 * to file.
 */
public class FileHandler {

    private static String pathStart = OSDetector.getPathPrefix();

    /**
     * Loads a resource file
     * @param fileName The path of the file
     * @param isLoadingFromStart Whether the program is starting up or not
     * @throws FileNotFoundException if file not present
     * @throws FileWasNotFoundException if file not present
     */
    public static void loadResource(String fileName, boolean isLoadingFromStart)
            throws FileNotFoundException, FileWasNotFoundException
    {
        try {
            // Loading OSM files...
            if (fileExists(fileName) && fileName.endsWith(FileType.OSM.getExtension())) {
                if (isLoadingFromStart) {
                    OSMHandler.getInstance().parseDefault(true);
                    FileHandler.loadOSM(
                        new InputSource(FileHandler.class.getResourceAsStream(fileName)));
                } else {
                    OSMHandler.getInstance().parseDefault(false);
                    OSMHandler.getInstance().setIsInitialized(false);
                    FileHandler.loadOSM(new InputSource(pathStart + fileName));
                }
            // Loading Zip files...
            } else if (fileExists(fileName) && fileName.endsWith(FileType.ZIP.getExtension())) {
                ZipInputStream zip;
                if (isLoadingFromStart) {
                    OSMHandler.getInstance().parseDefault(true);
                    zip = new ZipInputStream(new BufferedInputStream(
                        FileHandler.class.getResourceAsStream(fileName)));
                } else {
                    OSMHandler.getInstance().parseDefault(false);
                    OSMHandler.getInstance().setIsInitialized(false);
                    zip = new ZipInputStream(new BufferedInputStream(new FileInputStream(fileName)));
                }
                try {
                    zip.getNextEntry();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                loadOSM(new InputSource(zip));
            // Loads binary files...
            } else if (fileExists(fileName) || fileName.endsWith(FileType.BIN.getExtension())) {
                loadBin(fileName, isLoadingFromStart);
            // If not file type was not recognized or is unsupported...
            } else {
                PopupWindow.errorBox(
                    null, "Unsupported File Type. Please Select a New File!");
            }
        // If the file was not found
        } catch (FileNotFoundException | FileWasNotFoundException e) {
            throw new FileWasNotFoundException("File Not Found");
        }
    }

    /**
     * Tries to load the default binary file from the resources folder
     * @param isLoadingFromStart Whether the program is starting up
     * @throws FileNotFoundException If the file was not found
     * @throws FileWasNotFoundException if the file was not found
     */
    public static void loadDefaultResource(boolean isLoadingFromStart) throws FileNotFoundException, FileWasNotFoundException {
        try {
            try {
                long startTime = System.currentTimeMillis();
                if (!DEBUG_MODE_ACTIVE) {       // if active only loads coastlines
                    try {
                        // tries loading the default binary file
                        FileHandler.loadBin(GlobalValue.DEFAULT_BIN_RESOURCE, isLoadingFromStart);
                    } catch (FileWasNotFoundException e) {
                        // tries loading the default osm resource file
                        FileHandler.loadResource(GlobalValue.DEFAULT_RESOURCE, isLoadingFromStart);
                    }
                }
                long loadtime = System.currentTimeMillis() - startTime;
                System.out.println("Resource load time: " +
                        HelperFunctions.convertMillitimeToTime(loadtime));
                if (DEBUG_MODE_ACTIVE)
                    throw new FileWasNotFoundException("");
            } catch (FileWasNotFoundException e) {
                throw new FileWasNotFoundException(
                    "Program was not able to load default resource."
                    + "\nLoading from coastlines file instead.");
            }
            GlobalValue.setDidProgramLoadDefault(true);
        } catch (FileWasNotFoundException e) {
            // if neither the default binary file or the default osm file was
            // present then just load from binary file
            PopupWindow.warningBox(null, e.getMessage());
            Model.getInstance().loadFromCoastlines();
            GlobalValue.setDidProgramLoadDefault(false);
        }
    }

    /**
     * Checks if a given file actually exists
     */
    public static boolean fileExists(String fileName)
    {
        if (Model.class.getResourceAsStream(fileName) != null)
            return true;
        else if (new InputSource(pathStart + fileName) != null)
            return true;
        return false;
    }

    /**
     * Tries loading from binary
     * @param filename The path of the file
     * @param isLoadingFromStart Whether the program is starting up
     * @throws FileWasNotFoundException if the file was not found
     */
    public static void loadBin(String filename, Boolean isLoadingFromStart)
        throws FileWasNotFoundException
    {
        try {
            ObjectInputStream in;
            if (isLoadingFromStart) {   // default file loader
                in = new ObjectInputStream(new BufferedInputStream(
                    FileHandler.class.getResourceAsStream(filename)));
            } else {                    // standard file loader
                try {
                    in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)));
                }catch(FileNotFoundException e){
                    in = new ObjectInputStream(new BufferedInputStream(
                            FileHandler.class.getResourceAsStream(filename)));
                }
            }
            long time = System.nanoTime();
            // Sets the EnumMap for the KDTrees up in the model
            Model.getInstance().setElements(
                (EnumMap<ElementType, KDTree>)in.readObject());

            // Sets up the bounds for the model
            if (isLoadingFromStart) {
                Model.getInstance().setBound(BoundType.MIN_LONGITUDE, in.readFloat());
                Model.getInstance().setBound(BoundType.MAX_LONGITUDE, in.readFloat());
                Model.getInstance().setBound(BoundType.MIN_LATITUDE, in.readFloat());
                Model.getInstance().setBound(BoundType.MAX_LATITUDE, in.readFloat());
            } else {
                Model.getInstance().setDynamicBound(BoundType.MIN_LONGITUDE,
                    in.readFloat());
                Model.getInstance().setDynamicBound(BoundType.MAX_LONGITUDE,
                    in.readFloat());
                Model.getInstance().setDynamicBound(BoundType.MIN_LATITUDE,
                    in.readFloat());
                Model.getInstance().setDynamicBound(BoundType.MAX_LATITUDE,
                    in.readFloat());
            }
            // Sets the longitude factor of the model
            Model.getInstance().setLongitudeFactor(in.readFloat());
            // Sets the Tenary Search Trie in the model to the object loaded
            Model.getInstance().setTst((TenarySearchTrie)in.readObject());
            // Sets the HashMap for the Cities up
            Model.getInstance().setCityToIndexMap((HashMap<String, Integer>) in.readObject());
            Model.getInstance().setIndexToCityMap((HashMap<Integer, String>) in.readObject());
            // Sets the collection of Points of interest up in the model
            Model.getInstance().setPointsOfInterest((ArrayList<POI>) in.readObject());
            time = System.nanoTime() - time;
            System.out.println("Object deserialization: "+
                    HelperFunctions.convertNanotimeToTime(time));
            // Creates the graph from the loaded roads
            Model.getInstance().createGraph(Model.getInstance().getElements(Enums.OSMEnums.ElementType.HIGHWAY).getAllSections());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new FileWasNotFoundException("Failed loading bin file.");
        }
    }

    /**
     * Tries saving the model to a binary file
     * @param fileName the path of the file
     * @param dynamic whether the dynamic or static bounds has to be saved
     */
    public static void saveBin(String fileName, boolean dynamic)
    {
        long time = System.nanoTime();
        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)))) {
            out.writeObject(Model.getInstance().getElements());
            out.writeFloat(Model.getInstance().getMinLongitude(dynamic));
            out.writeFloat(Model.getInstance().getMaxLongitude(dynamic));
            out.writeFloat(Model.getInstance().getMinLatitude(dynamic));
            out.writeFloat(Model.getInstance().getMaxLatitude(dynamic));
            out.writeFloat(Model.getInstance().getLongitudeFactor());
            out.writeObject(Model.getInstance().getTst());
            out.writeObject(Model.getInstance().getCityToIndexMap());
            out.writeObject(Model.getInstance().getIndexToCityMap());
            out.writeObject(Model.getInstance().getPointsOfInterest());
            time = System.nanoTime() - time;
            System.out.println("Save time: "+
                    HelperFunctions.convertNanotimeToTime(time));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saving from the FileChooser
     * @param fileName the file path to save to
     * @see PopupWindow fileSaver
     */
    public static void fileChooserSave(String fileName)
    {
        saveBin(fileName, true);
    }

    /**
     * Loading from the FileChooser
     * @param fileName the path of the file to be loaded
     * @throws FileNotFoundException if file was not found
     * @throws FileWasNotFoundException if file was not found
     * @see PopupWindow fileLoader
     */
    public static void fileChooserLoad(String fileName) throws FileNotFoundException, FileWasNotFoundException
    {
        loadResource(fileName, false);
    }

    /**
     * Tries to load from a osm file
     * @param inputSource a given input source of data
     * @throws FileWasNotFoundException if file was not found
     * @throws FileNotFoundException if file was not found
     */
    public static void loadOSM(InputSource inputSource) throws FileWasNotFoundException, FileNotFoundException
    {
        try {
            Model.getInstance().clear();
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(OSMHandler.getInstance());
            reader.parse(inputSource);
        } catch (SAXException | IOException e) {
            throw new FileWasNotFoundException("OSM File not Found");
        }
    }

    /**
     * Tries to load from the coastline file
     * @return a CoastlineFactory object containing all coastlines
     */
    public static CoastlineFactory loadCoastlines()
    {
        CoastlineHandler handler = CoastlineHandler.getInstance();
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(handler);
            InputStream stream = FileHandler.class.getResourceAsStream(DEFAULT_COASTLINE_FILE);
            ZipInputStream zip = new ZipInputStream(new BufferedInputStream(stream));
            zip.getNextEntry();
            InputSource source = new InputSource(zip);
            reader.parse(source);
            return handler.getCoastlineFactory();
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
