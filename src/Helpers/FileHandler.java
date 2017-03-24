package Helpers;

import Controller.CanvasController;
import Enums.FileType;
import Model.Coastlines.CoastlineFactory;
import Model.Coastlines.CoastlineHandler;
import Model.Model;
import OSM.OSMHandler;
import View.PopupWindow;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jakob on 06-03-2017.
 */
public class FileHandler {

    private static String pathStart = OSDetector.getPathPrefix();

    public static void loadDefault(String fileName) throws FileNotFoundException {
        if(fileExists(fileName)) {
            InputStream filename = FileHandler.class.getResourceAsStream(fileName);
            FileHandler.loadOSM(new InputSource(filename));
        }else{
            throw new FileNotFoundException(fileName + " does not exist.");
        }
    }

    public static boolean fileExists(String fileName){
        if(Model.class.getResourceAsStream(fileName) != null) {
            return true;
        }
        return false;
    }

    public static void fileChooserLoad(String fileName) {
        if(fileName.endsWith(FileType.OSM.getExtension())) {
                loadOSM(new InputSource(pathStart + fileName));
        } else {
            PopupWindow.errorBox(null, "Unsupported File Type. Please Select a New File!");
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
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public static CoastlineFactory loadCoastlines() {
        CoastlineHandler handler = CoastlineHandler.getInstance();
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(handler);
            // TODO: Add coastline.osm as a resource and reference it with InputSource
            InputSource source = new InputSource();
            reader.parse(source);
            return handler.getCoastlineFactory();
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
