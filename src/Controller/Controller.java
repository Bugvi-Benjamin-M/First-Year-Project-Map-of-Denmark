package Controller;

import View.Window;

/**
 * Created by Jakob on 06-03-2017.
 */
public abstract class Controller {

    protected Window window;

    public Controller(Window window) {
            this.window = window;
    }

    protected void specifyWindow(Window window) {
        this.window = window;
    }
}
