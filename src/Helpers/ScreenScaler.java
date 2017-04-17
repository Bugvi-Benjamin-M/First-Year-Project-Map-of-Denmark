package Helpers;

import java.awt.*;

/**
 * Created by Búgvi Magnussen on 15-04-2017.
 */
public class ScreenScaler {

    private static final Dimension screenSize;
    private static final double screenWidth;
    private static final double screenHeight;

    private static final double widthScaleFactor;

    private static final double heightScaleFactor;

    static {
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = screenSize.getWidth();
        screenHeight = screenSize.getHeight();

        widthScaleFactor = screenWidth / 1920;
        heightScaleFactor = screenHeight / 1080;

    }
    public static void print() {
        System.out.println(screenWidth);
    }
}
