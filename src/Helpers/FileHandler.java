package Helpers;

import Controller.CanvasController;
import Enums.FileType;
import Model.Model;
import OSM.OSMHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

/**
 * Created by Jakob on 06-03-2017.
 */
public class FileHandler {

    private static String pathStart;

    static {
        if (OSDetector.isWindows()) {
            pathStart = "file:";
        } else if (OSDetector.isMac()) {
            pathStart = "file://";
        } else {
            pathStart = "file://";
        }

    }

    public static void loadDefault(String fileName) throws FileNotFoundException {
        if(fileExists(fileName) && fileName.endsWith(".osm")) {
            InputStream filename = FileHandler.class.getResourceAsStream(fileName);
            FileHandler.loadOSM(new InputSource(filename));
        }else{
            throw new FileNotFoundException(fileName + " can not be found.");
        }
    }

    public static void loadZip(String fileName) throws FileNotFoundException{
        if(fileExists((fileName)) && fileName.endsWith((".zip"))) {
            ZipInputStream zip = new ZipInputStream(new BufferedInputStream(FileHandler.class.getResourceAsStream(fileName)));
            try {
                zip.getNextEntry();
            } catch (IOException e) {
                e.printStackTrace();
            }
            loadOSM(new InputSource(zip));
        }else{
            throw new FileNotFoundException(fileName + " can not be found.");
        }
    }

    public static boolean fileExists(String fileName){
        if(Model.class.getResourceAsStream(fileName) != null) {
            return true;
        }
        return false;
    }

    public static void load(String fileName) {
        if(fileName.endsWith(FileType.OSM.getExtension())) {
                loadOSM(new InputSource(pathStart + fileName));
        }
    }

    public static void loadOSM(InputSource inputSource) {
        try {
            Model.getInstance().clear();
            CanvasController.resetBounds();
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(OSMHandler.getInstance());
            reader.parse(inputSource);
            Model.getInstance().modelHasChanged();
            CanvasController.adjustToBounds();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
