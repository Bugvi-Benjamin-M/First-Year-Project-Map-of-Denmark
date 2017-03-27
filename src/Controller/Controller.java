package Controller;

import View.Window;

/**
 * Created by Jakob on 06-03-2017.
 */
public abstract class Controller {

    protected static Window window;

    public Controller(Window window) {
        if(window != null) {
            this.window = window;
        }
    }

    public abstract void themeHasChanged();

    public abstract void toggleKeyBindings(boolean status);
}
