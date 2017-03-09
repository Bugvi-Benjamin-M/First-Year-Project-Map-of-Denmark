package Helpers;

import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 09/03/2017
 */
public final class Constant {

    private final static FileNameExtensionFilter[] FILE_NAME_EXTENSION_FILTERS = {new FileNameExtensionFilter("OSM files", "osm"), new FileNameExtensionFilter("ZIP files", "zip")};

    private final static double ZOOM_FACTOR = 0.9;

    private final static String TITLE = "OSM Map Viewer v0.1";

    public static FileNameExtensionFilter[] getFileNameExtensionFilters() {
        return FILE_NAME_EXTENSION_FILTERS;
    }

    public static double getZOOM_FACTOR() {
        return ZOOM_FACTOR;
    }

    public static String getTITLE() {
        return TITLE;
    }
}
