package Controller;

import View.Window;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 05/04/2017
 */
public final class ToolMenuController extends Controller {

    private static ToolMenuController instance;

    private ToolMenuController(Window window) {
        super(window);
    }

    public ToolMenuController getInstance(Window window) {
        if(instance == null) {
            instance = new ToolMenuController(window);
        }
        return instance;
    }

}
