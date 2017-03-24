package View;

import Enums.OSMEnums.WayType;
import Model.Element;
import Model.Model;
import Model.Road;
import OSM.OSMWay;
import Theme.Theme;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.EnumMap;


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
    }


    /**
     * Paints the MapCanvas with all the shapes that should be displayed.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setTransform(transform);
        drawRoads(g2D);
        g2D.setColor(Color.BLACK);
        Path2D boundary = new Path2D.Double();
        boundary.moveTo(Model.getInstance().getMinLongitude(), Model.getInstance().getMinLatitude());
        boundary.lineTo(Model.getInstance().getMaxLongitude(), Model.getInstance().getMinLatitude());
        boundary.lineTo(Model.getInstance().getMaxLongitude(), Model.getInstance().getMaxLatitude());
        boundary.lineTo(Model.getInstance().getMinLongitude(), Model.getInstance().getMaxLatitude());
        boundary.lineTo(Model.getInstance().getMinLongitude(), Model.getInstance().getMinLatitude());
        g2D.draw(boundary);
        ArrayList<Point2D> medianpoints = Model.getInstance().getMedianpoints();
        if(medianpoints != null) {
            for (Point2D median : medianpoints) {
                g2D.fill(new Ellipse2D.Double(median.getX(), median.getY(), 0.01f, 0.01f));
            }
        }
    }

    //TODO remember to implement properly
    private void drawRoads(Graphics2D g){
        g.setStroke(new BasicStroke(0.00001f));
        ArrayList<Element> roadelements = Model.getInstance().getBst().getSection(1.0,1.0);
        if(roadelements != null) {
            for (Element e : roadelements) {
                Road r = (Road) e;
                g.draw(r.getWay().toPath2D());
            }
        }


        /*java.util.List<Element> roads = wayElements.get(WayType.ROAD);
        for(Element element : roads){
            Road road = (Road) element;
            switch(road.getRoadType()){
                case SERVICE:
                    g.setColor(theme.getWaterColor());
                    g.setStroke(new BasicStroke(0.00001f));
                    OSMWay way = road.getWay();
                    g.draw(way.toPath2D());
                    break;
                case TERTIARY:
                    g.setColor(theme.getSandColor());
                    g.setStroke(new BasicStroke(0.00001f));
                    //g.draw(road.getPath());
                    break;
                case UNCLASSIFIED:
                    g.setColor(theme.getParkColor());
                    g.setStroke(new BasicStroke(0.00001f));
                    //g.draw(road.getPath());
                    break;
                case UNKNOWN:
                    g.setColor(theme.getWaterColor());
                    g.setStroke(new BasicStroke(0.00001f));
                    //g.draw(road.getPath());

            }

        }*/
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

    public void setWayElements(EnumMap wayElements){
        this.wayElements = wayElements;
    }
}
