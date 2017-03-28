package View;

import Enums.OSMEnums.WayType;
import Helpers.ThemeHelper;
import Helpers.Utilities.DebugWindow;
import Helpers.Utilities.FPSCounter;
import Main.Main;
import Model.Element;
import Model.Model;
import Model.Road;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
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
    private java.util.List<Path2D> coastlines;
    private ArrayList<Element> currentSection;
    private Point2D currentPoint;
    private double zoom_value;

    /**
     * The base Constructor for the MapCanvas.
     * @param dimension The dimension of the component
     */
    public MapCanvas(Dimension dimension) {
        transform = new AffineTransform();
        this.dimension = dimension;
        setBackgroundColor();
        setPreferredSize(this.dimension);
        coastlines = new ArrayList<>();
        zoom_value = 0;
        DebugWindow.getInstance().hide();
    }

    public void setBackgroundColor() {
        setBackground(ThemeHelper.color("water"));
    }

    /**
     * Paints the MapCanvas with all the shapes that should be displayed.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setTransform(transform);
        setBackgroundColor();

        drawCoastlines(g2D);

        drawRoads(g2D);

        drawBoundaries(g2D);

        ArrayList<Point2D> medianpoints = Model.getInstance().getMedianpoints();
        if(medianpoints != null) {
            for (Point2D median : medianpoints) {
                g2D.fill(new Ellipse2D.Double(median.getX(), median.getY(), 0.01f, 0.01f));
            }
        }

        //Rectangle
        if(currentPoint != null){
            Rectangle2D rectangle = new Rectangle2D.Double(currentPoint.getX(), currentPoint.getY(), 0.3, 0.3);
            g2D.setStroke(new BasicStroke(0.0001f));
            g.setColor(ThemeHelper.color("boundary"));
            g2D.draw(rectangle);
        }

        Main.FPS_COUNTER.interrupt();
        DebugWindow.getInstance().setFPSLabel();
    }

    private void drawCoastlines(Graphics2D g) {
        g.setColor(ThemeHelper.color("background"));
        for (Path2D path: coastlines) {
            g.fill(path);
        }
    }

    private void drawRoads(Graphics2D g){
        g.setColor(ThemeHelper.color("highwayroad"));
        g.setStroke(new BasicStroke(0.00001f));
        if(currentSection != null) {
            for (Element e : currentSection) {
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

    private void drawBoundaries(Graphics2D g2D) {
        g2D.setColor(ThemeHelper.color("boundary"));
        Path2D boundary = new Path2D.Double();
        Model model = Model.getInstance();
        boundary.moveTo(model.getMinLongitude(), model.getMinLatitude());
        boundary.lineTo(model.getMaxLongitude(), model.getMinLatitude());
        boundary.lineTo(model.getMaxLongitude(), model.getMaxLatitude());
        boundary.lineTo(model.getMinLongitude(), model.getMaxLatitude());
        boundary.lineTo(model.getMinLongitude(), model.getMinLatitude());
        g2D.draw(boundary);
    }

    /**
     * Zooms in or out upon the elements on the MapCanvas depending on a given factor.
     */
    public void zoom(double factor) {
        zoom_value += factor;
        transform.preConcatenate(AffineTransform.getScaleInstance(factor, factor));
        repaint();
    }

    /**
     * Resets the MapCanvas from being zoomed in or out and panned to one or another position.
     */
    public void resetTransform(){
        zoom_value = 0;
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

    public void setCoastlines(java.util.List<Path2D> coastlines) {
        this.coastlines = coastlines;
    }

    public Point2D toModelCoords(Point2D mousePosition){
        try{
            return transform.inverseTransform(mousePosition, null);

        }catch(NoninvertibleTransformException e){
            throw new RuntimeException();
        }
    }

    public void setCurrentSection(ArrayList<Element> currentSection) {
        this.currentSection = currentSection;
    }

    public void setCurrentPoint(Point2D currentPoint) {
        this.currentPoint = currentPoint;
    }
}
