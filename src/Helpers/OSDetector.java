package Helpers;

import java.awt.event.KeyEvent;

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

    public static int getActivationKey() {
        if(isWindows()) {
            return KeyEvent.CTRL_DOWN_MASK;
        } else if(isMac()) {
            return KeyEvent.META_DOWN_MASK;
        } else {
            return KeyEvent.CTRL_DOWN_MASK;
        }

    }

    public static String getPathPrefix() {
        if (OSDetector.isWindows()) {
            return "file:";
        } else if (OSDetector.isMac()) {
            return "file://";
        } else {
            return "file://";
        }
    }
}
