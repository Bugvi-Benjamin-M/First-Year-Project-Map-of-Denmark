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


public class FileHandler {

    private static String pathStart = OSDetector.getPathPrefix();

    public static void loadResource(String fileName, boolean isLoadingFromStart) throws FileNotFoundException, FileWasNotFoundException
    {
        try {
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
            } else if (fileExists(fileName) || fileName.endsWith(FileType.BIN.getExtension())) {
                loadBin(fileName, isLoadingFromStart);
            } else {
                PopupWindow.errorBox(
                    null, "Unsupported File Type. Please Select a New File!");
            }
        } catch (FileNotFoundException | FileWasNotFoundException e) {
            throw new FileWasNotFoundException("File Not Found");
        }
    }

    public static void loadDefaultResource(boolean isLoadingFromStart) throws FileNotFoundException, FileWasNotFoundException {
        try {
            try {
                long startTime = System.currentTimeMillis();
                if (!DEBUG_MODE_ACTIVE) {
                    try {
                        FileHandler.loadBin(GlobalValue.DEFAULT_BIN_RESOURCE, isLoadingFromStart);
                    } catch (FileWasNotFoundException e) {
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
                    "Program was not able to load default resource \"" + GlobalValue.DEFAULT_RESOURCE + "\""
                    + "\nLoading from coastlines instead.");
            }
            GlobalValue.setDidProgramLoadDefault(true);
        } catch (FileWasNotFoundException e) {
            PopupWindow.warningBox(null, e.getMessage());
            Model.getInstance().loadFromCoastlines();
            GlobalValue.setDidProgramLoadDefault(false);
        }
    }

    public static boolean fileExists(String fileName)
    {
        if (Model.class.getResourceAsStream(fileName) != null)
            return true;
        else if (new InputSource(pathStart + fileName) != null)
            return true;
        return false;
    }

    public static void loadBin(String filename, Boolean isLoadingFromStart)
        throws FileWasNotFoundException
    {
        try {
            ObjectInputStream in;
            if (isLoadingFromStart) {
                in = new ObjectInputStream(new BufferedInputStream(
                    FileHandler.class.getResourceAsStream(filename)));
            } else {
                try {
                    in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)));
                }catch(FileNotFoundException e){
                    in = new ObjectInputStream(new BufferedInputStream(
                            FileHandler.class.getResourceAsStream(filename)));
                }
            }
            long time = System.nanoTime();
            Model.getInstance().setElements(
                (EnumMap<ElementType, KDTree>)in.readObject());

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
            Model.getInstance().setLongitudeFactor(in.readFloat());
            Model.getInstance().setTst((TenarySearchTrie)in.readObject());
            Model.getInstance().setCityToIndexMap((HashMap<String, Integer>) in.readObject());
            Model.getInstance().setIndexToCityMap((HashMap<Integer, String>) in.readObject());
            Model.getInstance().setPointsOfInterest((ArrayList<POI>) in.readObject());
            //List<RoadEdge> edges = (List<RoadEdge>) in.readObject();
            //Model.getInstance().setGraph((RoadGraph) in.readObject());
            time = System.nanoTime() - time;
            System.out.println("Object deserialization: "+
                    HelperFunctions.convertNanotimeToTime(time));
            Model.getInstance().createGraph(Model.getInstance().getElements(Enums.OSMEnums.ElementType.HIGHWAY).getAllSections());
            Model.getInstance().modelHasChanged();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new FileWasNotFoundException("Failed loading bin file.");
        }
    }

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
            //out.writeObject(Model.getInstance().getGraphFactory().getEdges());
            //out.writeObject(Model.getInstance().getGraph());
            time = System.nanoTime() - time;
            System.out.println("Save time: "+
                    HelperFunctions.convertNanotimeToTime(time));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void fileChooserSave(String fileName)
    {
        saveBin(fileName, true);
    }

    public static void fileChooserLoad(String fileName) throws FileNotFoundException, FileWasNotFoundException
    {
        loadResource(fileName, false);
    }

    public static void loadOSM(InputSource inputSource) throws FileWasNotFoundException, FileNotFoundException
    {
        try {
            Model.getInstance().clear();
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(OSMHandler.getInstance());
            reader.parse(inputSource);
            Model.getInstance().modelHasChanged();
        } catch (SAXException | IOException e) {
            throw new FileWasNotFoundException("OSM File not Found");
        }
    }

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
