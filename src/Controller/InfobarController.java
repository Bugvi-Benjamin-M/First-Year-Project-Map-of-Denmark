package Controller;

import View.Infobar;
import View.Window;

import java.awt.*;

import static java.lang.Thread.sleep;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 06-03-2017.
 * @project BFST
 */
public class InfobarController extends Controller {

    private Window window;
    private Infobar infobar;

    public InfobarController(Window window) {
        this.window = window;
        infobar = new Infobar();
        this.window.addComponent(BorderLayout.LINE_START,infobar);
        toggleVisibility();
    }

    public void toggleVisibility() {
        infobar.toggleVisibility();
    }
}
