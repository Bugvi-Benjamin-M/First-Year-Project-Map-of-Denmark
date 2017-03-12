package Helpers;

/**
 * Created by Búgvi Magnussen on 08-03-2017.
 */
public class OSDetector {

    private static boolean isWindows;
    private static boolean isMac;

    static {
        String os = System.getProperty("os.name");
        isWindows = os.toLowerCase().contains("windows");
        isMac = os.toLowerCase().contains("mac");
    }

    public static boolean isWindows() {
        return isWindows;
    }

    public static boolean isMac() {
        return isMac;
    }
}


