package Controller;

import Helpers.ThemeHelper;
import View.Window;

/**
 * Created by Jakob on 06-03-2017.
 */
public abstract class Controller {

    protected static Window window;
    protected static ThemeHelper themeHelper;


    public Controller(Window window) {
        this.window = window;
        themeHelper = ThemeHelper.getInstance();
    }

    public abstract void themeHasChanged();

    public abstract void toggleKeyBindings(boolean status);
}
