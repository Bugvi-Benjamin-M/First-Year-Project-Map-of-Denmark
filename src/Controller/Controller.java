package Controller;

import Helpers.ThemeHelper;
import View.Window;
import Theme.*;

/**
 * Created by Jakob on 06-03-2017.
 */
public abstract class Controller {

    protected static Window window;
    protected static Theme theme;


    public Controller(Window window) {
        if(window != null) this.window = window;
        this.theme = ThemeHelper.getTheme();
    }
}
