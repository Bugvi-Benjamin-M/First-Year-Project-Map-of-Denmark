package Helpers;

import OSM.OSMHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;

/**
 * Created by Jakob on 06-03-2017.
 */
public class File {

    public static void load(String fileName) {
        if(fileName.endsWith(".osm")) {
            loadOSM(new InputSource(fileName));
        }
    }

    private static void loadOSM(InputSource inputSource) {
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(OSMHandler.getInstance());
            reader.parse(inputSource);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
