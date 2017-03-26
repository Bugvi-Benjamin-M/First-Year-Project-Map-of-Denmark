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

import java.io.*;
import java.util.zip.ZipInputStream;
import java.net.URL;

/**
 * Created by Jakob on 06-03-2017.
 */
public class FileHandler {

    private static String pathStart = OSDetector.getPathPrefix();

    public static void loadResource(String fileName) throws FileNotFoundException {
        if(fileExists(fileName) && fileName.endsWith(FileType.OSM.getExtension())) {
            InputStream filename = FileHandler.class.getResourceAsStream(fileName);
            FileHandler.loadOSM(new InputSource(filename));
        }else if(fileExists(fileName) && fileName.endsWith(FileType.ZIP.getExtension())){
            ZipInputStream zip = new ZipInputStream(new BufferedInputStream(FileHandler.class.getResourceAsStream(fileName)));
            try {
                zip.getNextEntry();
            } catch (IOException e) {
                e.printStackTrace();
            }
            loadOSM(new InputSource(zip));
        }
        else{
            throw new FileNotFoundException(fileName + " can not be found.");
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
        }else if(fileName.endsWith(FileType.ZIP.getExtension())){
            try {
                ZipInputStream zip = new ZipInputStream(new FileInputStream(fileName));
                zip.getNextEntry();
                loadOSM(new InputSource(zip));
            } catch (IOException e) {
                e.printStackTrace();
            }
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
