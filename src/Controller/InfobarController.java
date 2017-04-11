package Controller;

import View.Infobar;

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
        super();
    }

    public static InfobarController getInstance() {
        if(instance == null) {
            return new InfobarController();
        }
        return instance;
    }

    public void setupInfobar() {
        infobar = new Infobar();
        toggleVisibility();
    }

    public void toggleVisibility() {
        infobar.toggleVisibility();
    }

    public Infobar getInfobar() {
        return infobar;
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
