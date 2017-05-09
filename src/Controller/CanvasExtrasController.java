package Controller;

import Helpers.HelperFunctions;
import Helpers.ThemeHelper;
import View.DistanceScallerView;

import java.awt.geom.Point2D;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 09-05-2017
 */
public class CanvasExtrasController extends Controller {

    private static CanvasExtrasController instance;
    private DistanceScallerView distanceScaller;

    private CanvasExtrasController() {
        super();
    }

    public static CanvasExtrasController getInstance() {
        if (instance == null) {
            instance = new CanvasExtrasController();
        }
        return instance;
    }

    public void setupExtras() {
        distanceScaller = new DistanceScallerView();
        distanceScaller.setVisible(true);
    }

    public DistanceScallerView getDistanceScaller() {
        return distanceScaller;
    }

    public void updateDistance() {
        Point2D start = new Point2D.Float(0,0);
        Point2D to = new Point2D.Float(distanceScaller.getWidth(),0);
        start = CanvasController.getInstance().getMapCanvas().toModelCoords(start);
        to = CanvasController.getInstance().getMapCanvas().toModelCoords(to);
        double distance = HelperFunctions.distanceInMeters(start,to);
        distanceScaller.setDistance(distance);
        distanceScaller.repaint();
    }

    public void themeHasChanged() {
        distanceScaller.themeChanged();
    }
}
