package Controller;

import Helpers.HelperFunctions;
import Helpers.ThemeHelper;
import View.DistanceScalerView;

import java.awt.geom.Point2D;

/**
 * The CanvasExtrasController handles all elements that should be
 * displayed on top of the canvas, but not directly interacting
 * with the canvas.
 */
public class CanvasExtrasController extends Controller {

    private static CanvasExtrasController instance;
    private DistanceScalerView distanceScaler;

    private CanvasExtrasController() {
        super();
    }

    /**
     * Lazy singleton getinstance method
     */
    public static CanvasExtrasController getInstance() {
        if (instance == null) {
            instance = new CanvasExtrasController();
        }
        return instance;
    }

    /**
     * Sets up extra elements on top of the canvas
     */
    public void setupExtras() {
        distanceScaler = new DistanceScalerView();
        distanceScaler.setVisible(true);
    }

    /**
     * Returns the DistanceScaler view component
     */
    public DistanceScalerView getDistanceScaller() {
        return distanceScaler;
    }

    /**
     * Updates the displayed distance on the distance scaler
     * (Found by taking the distance between the upper-leftmost corner and
     * a point approx. 100 pixels to the right of it and converting those points
     * into model coordinates such that a distance could be calculated)
     */
    public void updateDistance() {
        Point2D start = new Point2D.Float(0,0);
        Point2D to = new Point2D.Float(distanceScaler.getWidth(),0);
        start = CanvasController.getInstance().getMapCanvas().toModelCoords(start);
        to = CanvasController.getInstance().getMapCanvas().toModelCoords(to);
        double distance = HelperFunctions.distanceInMeters(start,to);
        distanceScaler.setDistance(distance);
        distanceScaler.repaint();
    }

    /**
     * Updates the theme of the visual components
     */
    public void themeHasChanged() {
        distanceScaler.themeChanged();
    }
}
