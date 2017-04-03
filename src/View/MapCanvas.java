package View;

import Enums.OSMEnums.WayType;
import Enums.ZoomLevel;
import Helpers.ThemeHelper;
import Helpers.Utilities.DebugWindow;
import KDtree.KDTree;
import Main.Main;
import Model.Elements.Element;
import Model.Model;
import Model.Elements.Road;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;


/**
 * Class details:
 * The MapCanvas is a visual component, which purpose is to display the
 * elements (roads, points, structure, etc.) of the model. The MapCanvas
 * is able to be zoomed to and from as well as panned around upon.
 *
 * @author Andreas Blanke, blan@itu.dk
 * @author Niclas Hedam, nhed@itu.dk
 * @author Nikolaj Bläser, nibl@itu.dk
 * @author Búgvi Magnussen, buma@itu.dk
 * @author Jakob Mollerup, jmol@itu.dk
 * @version 06-03-2017
 */
public class MapCanvas extends View {

    private Dimension dimension;
    private AffineTransform transform;
    private java.util.List<Path2D> coastlines;
    private HashSet<Element> currentSection;
    private Point2D currentPoint;
    private Rectangle2D currentRectangle;
    private EnumMap<WayType, KDTree> elements;
    private boolean antiAliasing;

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
        antiAliasing = false;
        grabFocus();
    }

    public void setBackgroundColor() {
        setBackground(ThemeHelper.color("water"));
    }

    public void toggleAntiAliasing(boolean status) {
        antiAliasing = status;
    }

    private void setCurrentRectangle() {
        Rectangle2D rectangle = getVisibleRect();
        Point2D point = toModelCoords(new Point2D.Double(10, 10));
        Point2D factor = toModelCoords(new Point2D.Double(rectangle.getWidth()-10, rectangle.getHeight()-10));
        double xBounds = factor.getX() - point.getX();
        double yBounds = factor.getY() - point.getY();
        currentRectangle = new Rectangle2D.Double(point.getX(), point.getY(), xBounds, yBounds);
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

        setCurrentRectangle();

        drawCoastlines(g2D);

        g2D.setColor(Color.black);
        g2D.setStroke(new BasicStroke(0.00001f));
        g2D.draw(currentRectangle);

        if (Main.didTheProgramLoadDefault()) {
            drawRoads(g2D);
        }

        //drawBoundaries(g2D);

        Main.FPS_COUNTER.interrupt();
        DebugWindow.getInstance();
    }

    private void drawCoastlines(Graphics2D g) {
        g.setColor(ThemeHelper.color("background"));
        for (Path2D path: coastlines) {
            g.fill(path);
        }
    }

    //TODO tænk over rækkefølgen elementerne bliver tegnet i (Jakob Nikolaj)
    private void drawRoads(Graphics2D g){
        switch(ZoomLevel.getZoomLevel()){
            case LEVEL_0:
                drawLevelZero(g);
                drawLevelThree(g);
                break;
            case LEVEL_1:
                drawLevelThree(g);
                break;
            case LEVEL_2:
                drawLevelThree(g);
                break;
            case LEVEL_3:
                drawLevelThree(g);
                break;
        }
    }

    private void drawLevelZero(Graphics2D g){
        setCurrentSection(WayType.SERVICE_ROAD);
        for (Element element : currentSection) {
            g.setColor(ThemeHelper.color("border"));
            g.setStroke(new BasicStroke(0.00001f));
            g.draw(element.getShape());
        }
    }

    private void drawLevelOne(Graphics2D g){

    }

    private void drawLevelTwo(Graphics2D g){
    }

    private void drawLevelThree(Graphics2D g){
        setCurrentSection(WayType.PRIMARY_ROAD);
        for (Element element : currentSection) {
            g.setColor(ThemeHelper.color("border"));
            g.setStroke(new BasicStroke(0.00001f));
            g.draw(element.getShape());
        }
    }

    private void setCurrentSection(WayType wayType){
        currentSection = elements.get(wayType).getManyElements(
                (float) currentRectangle.getMinX(),
                (float) currentRectangle.getMinY(),
                (float) currentRectangle.getMaxX(),
                (float) currentRectangle.getMaxY());
    }

    private void drawBoundaries(Graphics2D g2D) {
        g2D.setColor(ThemeHelper.color("boundary"));
        Path2D boundary = new Path2D.Float();
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
        DebugWindow.getInstance().setZoomLabel();
        DebugWindow.getInstance().setZoomFactorLabel();
        DebugWindow.getInstance().setFPSLabel();
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
        DebugWindow.getInstance().setFPSLabel();
        repaint();
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

    public void setElements(EnumMap<WayType, KDTree> map) {
        elements = map;
    }

    public void setCurrentSection(HashSet<Element> currentSection) {
        this.currentSection = currentSection;
    }

    public void setCurrentPoint(Point2D currentPoint) {
        this.currentPoint = currentPoint;
    }

}
