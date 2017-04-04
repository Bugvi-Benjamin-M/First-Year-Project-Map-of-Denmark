package Helpers;

import Controller.MainWindowController;

/**
 * Created by Búgvi Magnussen on 02-04-2017.
 */
public class GlobalValue {

    public static int getSearchFieldLargeSize() {
        return (int) (MainWindowController.getInstance().getWindow().getFrame().getWidth() / 3.2);
    }

    public static int getSearchFieldSmallSize() {
        return (int) (MainWindowController.getInstance().getWindow().getFrame().getWidth() / 1.7);
    }

}
