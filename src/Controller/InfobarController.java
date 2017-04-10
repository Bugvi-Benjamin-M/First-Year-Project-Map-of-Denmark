package Controller;

import View.Infobar;

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

    private InfobarController() {
        super(MainWindowController.getInstance().getWindow());
        infobar = new Infobar();
        window.addBorderLayoutComponent(BorderLayout.LINE_START,infobar,true);
        toggleVisibility();
    }

    public static InfobarController getInstance() {
        if(instance == null) {
            return new InfobarController();
        }
        return instance;
    }

    public void toggleVisibility() {
        infobar.toggleVisibility();
    }

    public void resetInstance() {
        instance = null;
    }

    public void themeHasChanged() {
        //Todo fix
    }

    public void toggleKeyBindings(boolean status) {
        //Todo fix
    }
}
