package Controller;

import Model.Coastlines.Coastline;
import Model.Coastlines.CoastlineFactory;
import View.Window;
import Model.Model;

import java.awt.geom.Path2D;
import java.util.List;
import Helpers.FileHandler;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 24-03-2017.
 * @project BFST
 */
public final class CoastlineController extends Controller {

    private static CoastlineController instance;
    private static CoastlineFactory factory;
    private static Model model;

    private CoastlineController(Window window) {
        super(window);
        model = Model.getInstance();
        loadCoastlines();
    }

    public static CoastlineController getInstance() {
        if (instance == null) {
            instance = new CoastlineController(null);
        }
        return instance;
    }

    private void loadCoastlines() {
        factory = FileHandler.loadCoastlines();
    }

    public List<Path2D> getCoastlinePaths() {
        checkIfFactoryIsNull();
        return factory.getCoastlinePolygons();
    }

    private void checkIfFactoryIsNull() {
        if (factory == null) throw new RuntimeException("Factory has not been set.");
    }
}
