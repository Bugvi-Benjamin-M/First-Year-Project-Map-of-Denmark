package Helpers;

import Controller.CanvasController;
import Enums.BoundType;
import Enums.FileType;
import Enums.OSMEnums.WayType;
import Enums.OSMEnums.ElementType;
import KDtree.KDTree;
import Model.Coastlines.CoastlineFactory;
import Model.Coastlines.CoastlineHandler;
import Model.Model;
import OSM.OSMHandler;
import View.PopupWindow;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.*;
import java.util.EnumMap;
import java.util.zip.ZipInputStream;

/**
 * Created by Jakob on 06-03-2017.
 */
public class FileHandler {

    private static String pathStart = OSDetector.getPathPrefix();


    public static void loadResource(String fileName, boolean isLoadingFromStart) {
        try {
            if (fileExists(fileName) && fileName.endsWith(FileType.OSM.getExtension())) {
                if(isLoadingFromStart){
                    OSMHandler.getInstance().parseDefault(true);
                    FileHandler.loadOSM(new InputSource(FileHandler.class.getResourceAsStream(fileName)));
                }else {
                    OSMHandler.getInstance().parseDefault(false);
                    FileHandler.loadOSM(new InputSource(pathStart + fileName));
                    CanvasController.adjustToDynamicBounds();
                }
            } else if (fileExists(fileName) && fileName.endsWith(FileType.ZIP.getExtension())) {
                ZipInputStream zip;
                if(isLoadingFromStart){
                    OSMHandler.getInstance().parseDefault(true);
                    zip = new ZipInputStream(new BufferedInputStream(FileHandler.class.getResourceAsStream(fileName)));
                }else {
                    OSMHandler.getInstance().parseDefault(false);
                    zip = new ZipInputStream(new FileInputStream(fileName));
                }
                try {
                    zip.getNextEntry();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                loadOSM(new InputSource(zip));
                if(!isLoadingFromStart) CanvasController.adjustToDynamicBounds();
            } else if (fileExists(fileName) || fileName.endsWith(FileType.BIN.getExtension())) {
                loadBin(fileName, isLoadingFromStart);
            } else {
                PopupWindow.errorBox(null, "Unsupported File Type. Please Select a New File!");
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }


    public static boolean fileExists(String fileName){
        if(Model.class.getResourceAsStream(fileName) != null)return true;
        else if(new InputSource(pathStart + fileName) != null) return true;
        return false;
    }

    public static void loadBin(String filename, Boolean isLoadingFromStart){
        try {
            ObjectInputStream in;
            if(isLoadingFromStart){
                in = new ObjectInputStream(new BufferedInputStream(FileHandler.class.getResourceAsStream(filename)));
            }else {
                in = new ObjectInputStream(new FileInputStream(filename));
            }
            long time = -System.nanoTime();
            Model.getInstance().setElements((EnumMap<ElementType, KDTree>) in.readObject());
            if(isLoadingFromStart) {
                Model.getInstance().setBound(BoundType.MIN_LONGITUDE, in.readFloat());
                Model.getInstance().setBound(BoundType.MAX_LONGITUDE, in.readFloat());
                Model.getInstance().setBound(BoundType.MIN_LATITUDE, in.readFloat());
                Model.getInstance().setBound(BoundType.MAX_LATITUDE, in.readFloat());
            }else{
                Model.getInstance().setDynamicBound(BoundType.MIN_LONGITUDE, in.readFloat());
                Model.getInstance().setDynamicBound(BoundType.MAX_LONGITUDE, in.readFloat());
                Model.getInstance().setDynamicBound(BoundType.MIN_LATITUDE, in.readFloat());
                Model.getInstance().setDynamicBound(BoundType.MAX_LATITUDE, in.readFloat());
            }
            CanvasController.getInstance().getMapCanvas().setElements(Model.getInstance().getElements());
            time += System.nanoTime();
            if(!isLoadingFromStart) {
                CanvasController.adjustToDynamicBounds();
            }
            System.out.printf("Object deserialization: %f s\n", time / 1000000 / 1000d);
            Model.getInstance().modelHasChanged();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public static void saveBin(String fileName, boolean dynamic) {
        //File f = new File(fileName);
        //if(f.exists()) f.delete();
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(Model.getInstance().getElements());
            out.writeFloat(Model.getInstance().getMinLongitude(dynamic));
            out.writeFloat(Model.getInstance().getMaxLongitude(dynamic));
            out.writeFloat(Model.getInstance().getMinLatitude(dynamic));
            out.writeFloat(Model.getInstance().getMaxLatitude(dynamic));
            System.out.println("DONE");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveTreeStructure(String fileName){
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(Model.getInstance().getElements());
            System.out.println("DONE");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadTreeStructure(String fileName){
        try(ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(FileHandler.class.getResourceAsStream(fileName)));){
            Model.getInstance().setElements((EnumMap<ElementType, KDTree>) in.readObject());
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public static void fileChooserSave(String fileName){
        saveBin(fileName, true);
    }

    public static void fileChooserLoad(String fileName) {
        loadResource(fileName, false);
    }

    public static void loadOSM(InputSource inputSource) {
        try {
            Model.getInstance().clear();
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(OSMHandler.getInstance());
            reader.parse(inputSource);
            Model.getInstance().modelHasChanged();
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public static CoastlineFactory loadCoastlines() {
        CoastlineHandler handler = CoastlineHandler.getInstance();
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(handler);
            InputStream stream = FileHandler.class.getResourceAsStream("/coastlines.zip");
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
