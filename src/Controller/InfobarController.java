package Controller;

import View.Infobar;
import View.Window;

import java.awt.*;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 06-03-2017.
 * @project BFST
 */
public final class InfobarController extends Controller {

    private Infobar infobar;
    private static InfobarController instance;

    private InfobarController(Window window) {
        super(window);
        infobar = new Infobar();
        window.addComponent(BorderLayout.LINE_START,infobar);
        toggleVisibility();
    }

    public static InfobarController getInstance(Window window) {
        if(instance == null) {
            return new InfobarController(window);
        }
        return instance;
    }

    public void toggleVisibility() {
        infobar.toggleVisibility();
    }

    public void resetInstance() {
        instance = null;
    }

    @Override
    public void themeHasChanged() {

    }
}
