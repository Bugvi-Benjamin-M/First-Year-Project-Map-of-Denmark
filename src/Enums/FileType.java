package Enums;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 13/03/2017
 */
public enum FileType {
    OSM,
    ZIP;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
    public String getExtension() {
        return "." + toString();
    }
}
