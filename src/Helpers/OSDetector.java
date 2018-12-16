package Helpers;

import java.awt.event.KeyEvent;

public class OSDetector {

    /**
     * Returns the operating system
     */
    public static String OS() { return System.getProperty("os.name"); }

    /**
     * Returns whether the current system is a Windows based System
     */
    public static boolean isWindows()
    {
        return OSDetector.OS().toLowerCase().contains("windows");
    }

    /**
     * Returns whether the current system is a Mac based system
     */
    public static boolean isMac()
    {
        return OSDetector.OS().toLowerCase().contains("mac");
    }

    /**
     * Returns the default secondary shortcut key.
     * Mac: Command, Windows: CTRL
     */
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

    /**
     * Returns the operating system's temporary path
     */
    public static String getTemporaryPath()
    {
        if (OSDetector.isWindows()) {
            return System.getProperty("java.io.tmpdir") + "\\";
        } else if (OSDetector.isMac()) {
            return "/tmp/";
        } else {
            return "/tmp/";
        }
    }

    /**
     * Returns the operating systems default file prefix.
     */
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
