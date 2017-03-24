package View;

import Enums.OSMEnums.WayType;
import Model.Coastlines.Coastline;
import Model.Element;
import Model.Model;
import Model.Road;
import Theme.Theme;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.*;
import java.util.List;


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
    private AffineTransform transform;
    private EnumMap<WayType, java.util.List<Element>> wayElements;
    private java.util.List<Path2D> coastlines;
    private Theme theme;

    /**
     * The base Constructor for the MapCanvas.
     * @param dimension The dimension of the component
     */
    public MapCanvas(Dimension dimension, Theme theme) {
        transform = new AffineTransform();
        this.theme = theme;
        this.dimension = dimension;
        setPreferredSize(this.dimension);
        this.setBackground(theme.getWaterColor());
    }

    /**
     * Paints the MapCanvas with all the shapes that should be displayed.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setTransform(transform);

        drawCoastlines(g2D);

        drawRoads(g2D);

        drawBoundaries(g2D);
    }

    private void drawCoastlines(Graphics2D g) {
        if (coastlines == null) throw new RuntimeException("Coastlines has not been set!");
        g.setColor(theme.getBackgroundColor());
        for (Path2D path: coastlines) {
            g.fill(path);
        }
    }

    //TODO remember to implement properly
    private void drawRoads(Graphics2D g){
        java.util.List<Element> roads = wayElements.get(WayType.ROAD);
        for(Element element : roads){
            Road road = (Road) element;
            switch(road.getRoadType()){
                case HIGHWAY:
                    g.setColor(theme.getHighwayRoadColor());
                    g.setStroke(new BasicStroke(0.00008f));
                    g.draw(road.getPath());
                    break;
                case PRIMARY:
                    g.setColor(theme.getPrimaryRoadColor());
                    g.setStroke(new BasicStroke(0.00008f));
                    g.draw(road.getPath());
                    break;
                case SECONDARY:
                    g.setColor(theme.getSecondaryRoadColor());
                    g.setStroke(new BasicStroke(0.00004f));
                    g.draw(road.getPath());
                    break;
                case TERTIARY:
                    g.setColor(theme.getTertiaryRoadColor());
                    g.setStroke(new BasicStroke(0.00002f));
                    g.draw(road.getPath());
                    break;
                case UNCLASSIFIED:
                    g.setColor(theme.getTertiaryRoadColor());
                    g.setStroke(new BasicStroke(0.00002f));
                    g.draw(road.getPath());
                    break;

            }

        }
    }

    private void drawBoundaries(Graphics2D g2D) {
        g2D.setColor(Color.BLACK);
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

    /**
     * Lets other objects add an EnumMap to the MapCanvas
     * @param wayElements
     */
    public void setWayElements(EnumMap wayElements){
        this.wayElements = wayElements;
    }

    public void setCoastlines(List<Path2D> coastlines) {
        this.coastlines = coastlines;
    }
}
