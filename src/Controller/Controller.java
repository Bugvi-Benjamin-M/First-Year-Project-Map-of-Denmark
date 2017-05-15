package Controller;

import View.Window;


public abstract class Controller {

    protected Window window;

    public Controller()
    {
        //
    }

    public void specifyWindow(Window window) { this.window = window; }
}
