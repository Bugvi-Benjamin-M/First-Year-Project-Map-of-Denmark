package Helpers;

import java.awt.event.KeyEvent;

public class OSDetector {

    public static String OS() { return System.getProperty("os.name"); }

    public static boolean isWindows()
    {
        return OSDetector.OS().toLowerCase().contains("windows");
    }

    public static boolean isMac()
    {
        return OSDetector.OS().toLowerCase().contains("mac");
    }

    public static int getActivationKey()
    {
        if (isWindows()) {
            return KeyEvent.CTRL_DOWN_MASK;
        } else if (isMac()) {
            return KeyEvent.META_DOWN_MASK;
        } else {
            return KeyEvent.CTRL_DOWN_MASK;
        }
    }

    public static String getTemporaryPath()
    {
        if (OSDetector.isWindows()) {
            return "%USERPROFILE%\\AppData\\Local\\Temp\\";
        } else if (OSDetector.isMac()) {
            return "/tmp/";
        } else {
            return "/tmp/";
        }
    }

    public static String getPathPrefix()
    {
        if (OSDetector.isWindows()) {
            return "file:";
        } else if (OSDetector.isMac()) {
            return "file://";
        } else {
            return "file://";
        }
    }
}
