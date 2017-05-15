package Controller;

import Helpers.HelperFunctions;
import Helpers.ThemeHelper;
import View.DistanceScalerView;

import java.awt.geom.Point2D;


public class CanvasExtrasController extends Controller {

    private static CanvasExtrasController instance;
    private DistanceScalerView distanceScaler;

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
        distanceScaler = new DistanceScalerView();
        distanceScaler.setVisible(true);
    }

    public DistanceScalerView getDistanceScaller() {
        return distanceScaler;
    }

    public void updateDistance() {
        Point2D start = new Point2D.Float(0,0);
        Point2D to = new Point2D.Float(distanceScaler.getWidth(),0);
        start = CanvasController.getInstance().getMapCanvas().toModelCoords(start);
        to = CanvasController.getInstance().getMapCanvas().toModelCoords(to);
        double distance = HelperFunctions.distanceInMeters(start,to);
        distanceScaler.setDistance(distance);
        distanceScaler.repaint();
    }

    public void themeHasChanged() {
        distanceScaler.themeChanged();
    }
}
