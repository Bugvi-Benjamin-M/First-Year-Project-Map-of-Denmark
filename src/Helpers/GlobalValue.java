package Helpers;

import Controller.MainWindowController;

/**
 * Created by Búgvi Magnussen on 02-04-2017.
 */
public class GlobalValue {

    public static int getSearchFieldSize() {
        return (int) (MainWindowController.getInstance().getWindow().getFrame().getWidth() / 3.2);
    }

}
