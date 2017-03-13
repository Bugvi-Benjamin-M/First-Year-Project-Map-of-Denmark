package Helpers;

import Controller.CanvasController;
import Enums.FileType;
import Model.Model;
import OSM.OSMHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.io.InputStream;

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

    public static void loadDefault(){
        InputStream filename = FileHandler.class.getResourceAsStream("/defaultosm.osm");
        FileHandler.loadOSM(new InputSource(filename));
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
