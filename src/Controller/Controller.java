package Controller;

import View.Window;

/**
 * Created by Jakob on 06-03-2017.
 */
public abstract class Controller {

    private static Window window;

    public Controller(Window window) {
        this.window = window;
    }


}
