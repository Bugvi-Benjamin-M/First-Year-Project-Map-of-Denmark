package Helpers;

import Enums.FileType;
import Exceptions.FileWasNotFoundException;
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

/**
 * Created by Jakob on 06-03-2017.
 */
public class FileHandler {

    private static String pathStart = OSDetector.getPathPrefix();

    public static void loadResource(String fileName) throws FileWasNotFoundException {
        if (fileExists(fileName) && fileName.endsWith(FileType.OSM.getExtension())) {
            InputStream filename = FileHandler.class.getResourceAsStream(fileName);
            FileHandler.loadOSM(new InputSource(filename));
        } else if (fileExists(fileName) && fileName.endsWith(FileType.ZIP.getExtension())) {
            ZipInputStream zip = new ZipInputStream(new BufferedInputStream(FileHandler.class.getResourceAsStream(fileName)));
            try {
                zip.getNextEntry();
            } catch (IOException e) {
                e.printStackTrace();
            }
            loadOSM(new InputSource(zip));
        } else if(fileExists(fileName) && fileName.endsWith(FileType.BIN.getExtension())){
            loadBin(fileName);
        }else{
            throw new FileWasNotFoundException(fileName + " can not be found.");
        }
    }

    public static boolean fileExists(String fileName){
        if(Model.class.getResourceAsStream(fileName) != null) {
            return true;
        }
        return false;
    }

    public static void loadBin(String filename){
        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(FileHandler.class.getResourceAsStream(filename)))) {
            long time = -System.nanoTime();
            //Model.getInstance().setBst((BST) in.readObject());
            //Model.getInstance().setBounds(in.readFloat(), in.readFloat(), in.readFloat(), in.readFloat());
            time += System.nanoTime();
            System.out.printf("Object deserialization: %f s\n", time / 1000000 / 1000d);
            Model.getInstance().modelHasChanged();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
