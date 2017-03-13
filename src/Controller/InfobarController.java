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
public class InfobarController extends Controller {

    private Infobar infobar;

    public InfobarController(Window window) {
        super(window);
        infobar = new Infobar();
        window.addComponent(BorderLayout.LINE_START,infobar);
        toggleVisibility();
    }

    public void toggleVisibility() {
        infobar.toggleVisibility();
    }
}
