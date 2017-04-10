package View;

import Enums.BoundType;
import Enums.OSMEnums.WayType;
import Enums.ZoomLevel;
import Helpers.ThemeHelper;
import Helpers.Utilities.DebugWindow;
import KDtree.KDTree;
import Main.Main;
import Model.Elements.CityName;
import Model.Elements.Element;
import Model.Model;

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

    private AffineTransform transform;
    private java.util.List<Path2D> coastlines;
    private HashSet<Element> currentSection;
    private Point2D currentPoint;
    private Rectangle2D currentRectangle;
    private EnumMap<WayType, KDTree> elements;
    private boolean antiAliasing;

    /**
     * The base Constructor for the MapCanvas.
     */
    public MapCanvas() {
        transform = new AffineTransform();
        setBackgroundColor();
        coastlines = new ArrayList<>();
        antiAliasing = false;
        setPreferredSize(new Dimension(800, 600));
        grabFocus();
    }

    public void setBackgroundColor() {
        setBackground(ThemeHelper.color("water"));
    }

    public void toggleAntiAliasing(boolean status) {
        antiAliasing = status;
        repaint();
    }


    private void setCurrentRectangle() {
        Rectangle2D rectangle = getVisibleRect();

        Point2D point = toModelCoords(new Point2D.Double(10, 10));
        Point2D factor = toModelCoords(new Point2D.Double(rectangle.getWidth()-10, rectangle.getHeight()-10));
        double xBounds = factor.getX() - point.getX();
        double yBounds = factor.getY() - point.getY();
        currentRectangle = new Rectangle2D.Double(point.getX(), point.getY(), xBounds, yBounds);
        Model model = Model.getInstance();
        model.setCameraBound(BoundType.MIN_LONGITUDE, (float) point.getX());
        model.setCameraBound(BoundType.MAX_LONGITUDE, (float) factor.getX());
        model.setCameraBound(BoundType.MAX_LATITUDE, (float) point.getY());
        model.setCameraBound(BoundType.MIN_LATITUDE, (float) factor.getY());
        DebugWindow.getInstance().setCameraBoundsLabel();
    }


    /**
     * Paints the MapCanvas with all the shapes that should be displayed.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setTransform(transform);
        if(antiAliasing) g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        else g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        setBackgroundColor();
        // TODO: 10/04/2017 Remember to uncomment setCurrentRegtangle and g2.draw(currentRectangle)
        setCurrentRectangle();

        drawCoastlines(g2D);


        g2D.setColor(Color.black);
        g2D.setStroke(new BasicStroke(0.00001f));
        g2D.draw(currentRectangle);

        if (Main.didTheProgramLoadDefault()) {
            drawElements(g2D);
        }

        //drawBoundaries(g2D);

        Main.FPS_COUNTER.interrupt();
        DebugWindow.getInstance();
    }

    private void drawCoastlines(Graphics2D g) {
        coastlines = Model.getInstance().getCoastlines();
        g.setColor(ThemeHelper.color("background"));
        for (Path2D path: coastlines) {
            g.fill(path);
        }
    }

    //TODO tænk over rækkefølgen elementerne bliver tegnet i (Jakob Nikolaj)
    private void drawElements(Graphics2D g){
        switch(ZoomLevel.getZoomLevel()){
            case LEVEL_0:
                drawWater(g);
                drawTracks(g);
                drawSteps(g);
                drawFootways(g);
                drawBridleways(g);
                drawCycleways(g);
                drawPaths(g);
                drawRoads(g);
                break;
            case LEVEL_1:
                drawWater(g);
                drawUnclassifiedRoads(g);
                drawResidentialRoads(g);
                drawLivingStreets(g);
                drawServiceRoads(g);
                drawBusGuideways(g);
                drawEscapes(g);
                drawRaceways(g);
                drawPedestrianStreets(g);
                break;
            case LEVEL_2:
                drawWater(g);
                drawSecondaryRoads(g);
                drawSecondaryRoadLinks(g);
                drawTertiaryRoads(g);
                drawTertiaryRoadLinks(g);
                break;
            case LEVEL_3:
                drawWater(g);
                drawMotorways(g);
                drawMotorwayLinks(g);
                drawPrimaryRoads(g);
                drawPrimaryRoadLinks(g);
                drawCityNames(g);
                break;
        }
    }

    private void setCurrentSection(WayType wayType){
        currentSection = elements.get(wayType).getManyElements(
                (float) currentRectangle.getMinX(),
                (float) currentRectangle.getMinY(),
                (float) currentRectangle.getMaxX(),
                (float) currentRectangle.getMaxY());
    }
/*
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
    }*/


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

    //Draw Roads Methods
    private void drawMotorways(Graphics2D g){
        setCurrentSection(WayType.MOTORWAY);
        for (Element element : currentSection) {
            g.setColor(ThemeHelper.color("motorway"));
            g.setStroke(new BasicStroke(0.00003f));
            g.draw(element.getShape());
        }
    }
    private void drawMotorwayLinks(Graphics2D g){
        setCurrentSection(WayType.MOTORWAY_LINK);
        for (Element element : currentSection) {
            g.setColor(ThemeHelper.color("motorway"));
            g.setStroke(new BasicStroke(0.00002f));
            g.draw(element.getShape());
        }
    }
    private void drawTrunkRoads(Graphics2D g) {
        setCurrentSection(WayType.TRUNK_ROAD);
        for (Element element : currentSection) {
            g.setColor(ThemeHelper.color("trunkRoad"));
            g.setStroke(new BasicStroke(0.00002f));
            g.draw(element.getShape());
        }
    }
    private void drawTrunkRoadLinks(Graphics2D g){
        setCurrentSection(WayType.TRUNK_ROAD_LINK);
        for (Element element : currentSection) {
            g.setColor(ThemeHelper.color("trunkRoad"));
            g.setStroke(new BasicStroke(0.00002f));
            g.draw(element.getShape());
        }
    }
    private void drawPrimaryRoads(Graphics2D g){
        setCurrentSection(WayType.PRIMARY_ROAD);
        for (Element element : currentSection) {
            g.setColor(ThemeHelper.color("primaryRoad"));
            g.setStroke(new BasicStroke(0.00001f));
            g.draw(element.getShape());
        }
    }
    private void drawPrimaryRoadLinks(Graphics2D g){
        setCurrentSection(WayType.PRIMARY_ROAD_LINK);
        for (Element element : currentSection) {
            g.setColor(ThemeHelper.color("primaryRoad"));
            g.setStroke(new BasicStroke(0.00001f));
            g.draw(element.getShape());
        }
    }
    private void drawSecondaryRoads(Graphics2D g){
        setCurrentSection(WayType.SECONDARY_ROAD);
        for (Element element : currentSection) {
            g.setColor(ThemeHelper.color("secondaryRoad"));
            g.setStroke(new BasicStroke(0.00001f));
            g.draw(element.getShape());
        }
    }
    private void drawSecondaryRoadLinks(Graphics2D g){
        setCurrentSection(WayType.SECONDARY_ROAD_LINK);
        for (Element element : currentSection) {
            g.setColor(ThemeHelper.color("secondaryRoad"));
            g.setStroke(new BasicStroke(0.00001f));
            g.draw(element.getShape());
        }
    }
    private void drawTertiaryRoads(Graphics2D g){
        setCurrentSection(WayType.TERTIARY_ROAD);
        for (Element element : currentSection) {
            g.setColor(ThemeHelper.color("tertiaryRoad"));
            g.setStroke(new BasicStroke(0.00001f));
            g.draw(element.getShape());
        }
    }
    private void drawTertiaryRoadLinks(Graphics2D g){
        setCurrentSection(WayType.TERTIARY_ROAD_LINK);
        for (Element element : currentSection) {
            g.setColor(ThemeHelper.color("tertiaryRoad"));
            g.setStroke(new BasicStroke(0.00001f));
            g.draw(element.getShape());
        }
    }
    private void drawUnclassifiedRoads(Graphics2D g){
        setCurrentSection(WayType.UNCLASSIFIED_ROAD);
        for (Element element : currentSection) {
            g.setColor(ThemeHelper.color("unclassifiedRoad"));
            g.setStroke(new BasicStroke(0.00001f));
            g.draw(element.getShape());
        }
    }
    private void drawResidentialRoads(Graphics2D g){
        setCurrentSection(WayType.RESIDENTIAL_ROAD);
        for (Element element : currentSection) {
            g.setColor(ThemeHelper.color("residentialRoad"));
            g.setStroke(new BasicStroke(0.00001f));
            g.draw(element.getShape());
        }
    }
    private void drawLivingStreets(Graphics2D g){
        setCurrentSection(WayType.LIVING_STREET);
        for (Element element : currentSection) {
            g.setColor(ThemeHelper.color("livingStreet"));
            g.setStroke(new BasicStroke(0.00001f));
            g.draw(element.getShape());
        }
    }
    private void drawServiceRoads(Graphics2D g){
        setCurrentSection(WayType.SERVICE_ROAD);
        for (Element element : currentSection) {
            g.setColor(ThemeHelper.color("serviceRoad"));
            g.setStroke(new BasicStroke(0.00001f));
            g.draw(element.getShape());
        }
    }
    private void drawBusGuideways(Graphics2D g){
        setCurrentSection(WayType.BUS_GUIDEWAY);
        for (Element element : currentSection) {
            g.setColor(ThemeHelper.color("BusGuideway"));
            g.setStroke(new BasicStroke(0.00001f));
            g.draw(element.getShape());
        }
    }
    private void drawEscapes(Graphics2D g){
        setCurrentSection(WayType.ESCAPE);
        for (Element element : currentSection) {
            g.setColor(ThemeHelper.color("escape"));
            g.setStroke(new BasicStroke(0.00001f));
            g.draw(element.getShape());
        }
    }
    private void drawRaceways(Graphics2D g){
        setCurrentSection(WayType.RACEWAY);
        for (Element element : currentSection) {
            g.setColor(ThemeHelper.color("raceway"));
            g.setStroke(new BasicStroke(0.00001f));
            g.draw(element.getShape());
        }
    }
    private void drawPedestrianStreets(Graphics2D g){
        setCurrentSection(WayType.PEDESTRIAN_STERET);
        for (Element element : currentSection) {
            g.setColor(ThemeHelper.color("pedestrianStreet"));
            g.setStroke(new BasicStroke(0.00001f));
            g.draw(element.getShape());
        }
    }
    private void drawTracks(Graphics2D g){
        setCurrentSection(WayType.TRACK);
        for (Element element : currentSection) {
            g.setColor(ThemeHelper.color("track"));
            g.setStroke(new BasicStroke(0.00001f));
            g.draw(element.getShape());
        }
    }
    private void drawSteps(Graphics2D g){
        setCurrentSection(WayType.STEPS);
        for (Element element : currentSection) {
            g.setColor(ThemeHelper.color("steps"));
            g.setStroke(new BasicStroke(0.00001f));
            g.draw(element.getShape());
        }
    }
    private void drawFootways(Graphics2D g){
        setCurrentSection(WayType.FOOTWAY);
        for (Element element : currentSection) {
            g.setColor(ThemeHelper.color("footway"));
            g.setStroke(new BasicStroke(0.00001f));
            g.draw(element.getShape());
        }
    }
    private void drawBridleways(Graphics2D g){
        setCurrentSection(WayType.BRIDLEWAY);
        for (Element element : currentSection) {
            g.setColor(ThemeHelper.color("bridleway"));
            g.setStroke(new BasicStroke(0.00001f));
            g.draw(element.getShape());
        }
    }
    private void drawCycleways(Graphics2D g){
        setCurrentSection(WayType.CYCLEWAY);
        for (Element element : currentSection) {
            g.setColor(ThemeHelper.color("cycleway"));
            g.setStroke(new BasicStroke(0.00001f));
            g.draw(element.getShape());
        }
    }
    private void drawPaths(Graphics2D g){
        setCurrentSection(WayType.PATH);
        for (Element element : currentSection) {
            g.setColor(ThemeHelper.color("path"));
            g.setStroke(new BasicStroke(0.00001f));
            g.draw(element.getShape());
        }
    }
    private void drawRoads(Graphics2D g){
        setCurrentSection(WayType.ROAD);
        for (Element element : currentSection) {
            g.setColor(ThemeHelper.color("road"));
            g.setStroke(new BasicStroke(0.00001f));
            g.draw(element.getShape());
        }
    }

    //Draw water
    private void drawWater(Graphics2D g){
        setCurrentSection(WayType.WATER);
        for (Element element : currentSection) {
            g.setColor(ThemeHelper.color("water"));
            g.setStroke(new BasicStroke(0.00001f));
            g.fill(element.getShape());
        }
    }

    //Draw City Names
    private void drawCityNames(Graphics2D g){
        setCurrentSection(WayType.CITY_NAME);
        for(Element element : currentSection){
            CityName cityName = (CityName) element;
            g.setColor(ThemeHelper.color("border"));  //TODO CHOOSE NAME COLOR
            Font font = new Font("Arial", Font.BOLD, 12);
            FontMetrics fm = g.getFontMetrics(font);
            Rectangle2D visibleRect = currentRectangle;
            float xScale = (float) (visibleRect.getWidth() / fm.stringWidth("København"));
            float yScale = (float) (visibleRect.getHeight() / fm.getHeight());

            float scale = 0f;
            if (xScale > yScale) {
                scale = yScale / 12;
            } else {
                scale = xScale / 12;
            }

            g.setFont(font.deriveFont(AffineTransform.getScaleInstance(scale, scale)));

            g.drawString(cityName.getName(), cityName.getX(), cityName.getY());
            System.out.println(xScale + ", " + yScale + ", scale used: " + scale);
        }
    }


}
