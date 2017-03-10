package Helpers;

import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 09/03/2017
 */
public final class Constant {

    private final static FileNameExtensionFilter osm = new FileNameExtensionFilter("OSM files", "osm");

    private final static FileNameExtensionFilter zip =  new FileNameExtensionFilter("ZIP files", "zip");


    public static FileNameExtensionFilter[] getFileNameExtensionFilters() {
        FileNameExtensionFilter[] filters = new FileNameExtensionFilter[] {osm, zip};

        return filters;
    }
}
