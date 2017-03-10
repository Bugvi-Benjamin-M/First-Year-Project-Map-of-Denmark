package View;

import Model.Model;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.ArrayList;

/**
 * Class details:
 * The MapCanvas is a visual component, which purpose is to display the
 * elements (roads, points, structure, etc.) of the model. The MapCanvas
 * is able to be zoomed to and from as well as panned around upon.
 *
 * @author Andreas Blanke, blan@itu.dk
 * @author Niclas Hedam, nhed@itu.dk
 * @author Nikolaj Bl, nibl@itu.dk
 * @author Búgvi Magnussen, buma@itu.dk
 * @author Jacob Mollerup, jmol@itu.dk
 * @version 06-03-2017
 */
public class MapCanvas extends View {

    private Dimension dimension;
    private java.util.List<Shape> shapes;
    private AffineTransform transform;

    /**
     * The base Constructor for the MapCanvas.
     * @param dimension The dimension of the component
     */
    public MapCanvas(Dimension dimension) {
        transform = new AffineTransform();
        this.dimension = dimension;
        setPreferredSize(this.dimension);
        shapes = new ArrayList<>();
    }

    /**
     * Resets and clears the collection of shapes that is displayed on the MapCanvas.
     */
    public void resetShapes(){
        shapes.clear();
    }

    /**
     * Adds a collection of shapes to the existing MapCanvas collection.
     * @param shapes The new collection of shapes to be displayed
     */
    public void addShapes(java.util.List<Shape> shapes){
        this.shapes.addAll(shapes);
    }

    /**
     * Paints the MapCanvas with all the shapes that should be displayed.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setTransform(transform);
        g2D.setColor(Color.BLACK);
        g2D.setStroke(new BasicStroke(0.00001f));
        for(Shape shape : shapes) {
            g2D.draw(shape);
        }
        Path2D boundary = new Path2D.Double();
        boundary.moveTo(Model.getInstance().getMinLongitude(), Model.getInstance().getMinLatitude());
        boundary.lineTo(Model.getInstance().getMaxLongitude(), Model.getInstance().getMinLatitude());
        boundary.lineTo(Model.getInstance().getMaxLongitude(), Model.getInstance().getMaxLatitude());
        boundary.lineTo(Model.getInstance().getMinLongitude(), Model.getInstance().getMaxLatitude());
        boundary.lineTo(Model.getInstance().getMinLongitude(), Model.getInstance().getMinLatitude());
        g2D.draw(boundary);
    }

    /**
     * Zooms in or out upon the elements on the MapCanvas depending on a given factor.
     */
    public void zoom(double factor) {
        transform.preConcatenate(AffineTransform.getScaleInstance(factor, factor));
        repaint();
    }

    /**
     * Resets the MapCanvas from being zoomed in or out and panned to one or another position.
     */
    public void resetTransform(){
        transform.setToIdentity();
    }

    /**
     * Pans the MapCanvas to another position so other elements might be viewed.
     * @param dx The difference in x-coordinates between the new position to be
     *           centered and the current center point
     * @param dy The difference in y-coordinates between the new position to be
     *           centered and the current center point
     */
    public void pan(double dx, double dy) {
        transform.preConcatenate(AffineTransform.getTranslateInstance(dx, dy));
        repaint();
    }
}
