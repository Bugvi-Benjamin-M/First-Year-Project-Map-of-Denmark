package Helpers;

import Controller.MainWindowController;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
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

    public static int getWidthScaleFactor() {
        return (int) widthScaleFactor;
    }

    public static int getHeightScaleFactor() {
        return (int) heightScaleFactor;
    }

    public static int getToolbarWidth() {
        return (int) (100 * heightScaleFactor);
    }

    public static int getSearchFieldLargeSize() {
        return (int) ((MainWindowController.getInstance().getWindow().getFrame().getWidth() / 3.2) * widthScaleFactor);
    }

    public static int getSearchFieldSmallSize() {
        return (int) ((MainWindowController.getInstance().getWindow().getFrame().getWidth() / 2.7) * widthScaleFactor);
    }

    public static int getSearchFieldStartX() {
        return (int) (MainWindowController.getInstance().getWindow().getFrame().getWidth() / 2.909);
    }
}
