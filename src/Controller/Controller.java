package Controller;

import View.Window;

/**
 * The superclass of all controllers.
 */
public abstract class Controller {

    protected Window window;

    /**
     * Constructor.
     */
    public Controller()
    {
        //
    }

    /**
     * Specifies the window the controller manipulates.
     * @param window the window the controller belongs to.
     */
    public void specifyWindow(Window window) { this.window = window; }
}
