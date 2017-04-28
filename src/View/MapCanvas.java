package View;

import Enums.BoundType;
import Enums.OSMEnums.ElementType;
import Enums.ZoomLevel;
import Helpers.GlobalValue;
import Helpers.Shapes.PolygonApprox;
import Helpers.ThemeHelper;
import Helpers.Utilities.DebugWindow;
import KDtree.KDTree;
import Main.Main;
import Model.Elements.*;
import Model.Model;

import java.awt.*;
import java.awt.geom.*;
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

    private AffineTransform transform  = new AffineTransform();
    private HashSet<Element> currentSection;
    private Point2D currentPoint;
    private Rectangle2D currentRectangle;
    private EnumMap<ElementType, KDTree> elements;
    private boolean antiAliasing;
    private Point2D.Float locationMarker;

    public MapCanvas() {
        transform = new AffineTransform();
        setBackgroundColor();
        grabFocus();
    }

    public void setBackgroundColor() {
        setBackground(ThemeHelper.color("water"));
    }

    public void toggleAntiAliasing(boolean status) {
        antiAliasing = status;
        repaint();
    }

    public void setCurrentRectangle()
    {
        Rectangle2D rectangle = getVisibleRect();
        rectangle.setRect(rectangle.getX(),
            rectangle.getY() + GlobalValue.getToolbarHeight(),
            rectangle.getWidth(), rectangle.getHeight());
        Point2D point = toModelCoords(new Point2D.Double(0, GlobalValue.getToolbarHeight()));
        Point2D factor = toModelCoords(
            new Point2D.Double(rectangle.getWidth(), rectangle.getHeight()));
        double xBounds = factor.getX() - point.getX();
        double yBounds = factor.getY() - point.getY();
        currentRectangle = new Rectangle2D.Double(point.getX(), point.getY(), xBounds, yBounds);
        Model model = Model.getInstance();
        model.setCameraBound(BoundType.MIN_LONGITUDE, (float)point.getX());
        model.setCameraBound(BoundType.MAX_LONGITUDE, (float)factor.getX());
        model.setCameraBound(BoundType.MAX_LATITUDE, (float)point.getY());
        model.setCameraBound(BoundType.MIN_LATITUDE, (float)factor.getY());
        DebugWindow.getInstance().setCameraBoundsLabel();
    }

    /**
   * Paints the MapCanvas with all the shapes that should be displayed.
   */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D)g;
        g2D.setTransform(transform);
        if (antiAliasing) g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        else g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        drawBackground(g2D);
        setCurrentRectangle();

        drawCoastlines(g2D);
        if (GlobalValue.getDidProgramLoadDefault()) drawElements(g2D);

        g2D.setColor(Color.black);
        g2D.setStroke(new BasicStroke(0.00001f));
        g2D.draw(currentRectangle);

        drawLocationMarker(g2D);
        drawBoundaries(g2D);
        Main.FPS_COUNTER.interrupt();
        DebugWindow.getInstance().setFPSLabel();
    }

    private void drawLocationMarker(Graphics2D g){
        if(locationMarker != null) {
            Point2D start = toModelCoords(new Point2D.Float(0f, 0f));
            Point2D blue = toModelCoords(new Point2D.Float(16f, 0f));
            Point2D white = toModelCoords(new Point2D.Float(20f, 0f));
            float boundsblue = (float)blue.getX() - (float) start.getX();
            float boundswhite = (float)white.getX() - (float) start.getX();


            g.setColor(Color.white);
            g.fill(getEllipseFromCenter(locationMarker.getX(), locationMarker.getY(), boundswhite, boundswhite));
            g.setColor(Color.blue);
            g.fill(getEllipseFromCenter(locationMarker.getX(), locationMarker.getY(), boundsblue, boundsblue));
        }
    }

    private Ellipse2D getEllipseFromCenter(double x, double y, double width, double height)
    {
        double newX = x - width / 2.0;
        double newY = y - height / 2.0;

        Ellipse2D ellipse = new Ellipse2D.Double(newX, newY, width, height);

        return ellipse;
    }

    private void drawBackground(Graphics2D g)
    {
        g.setColor(ThemeHelper.color("water"));
        Path2D boundary = new Path2D.Float();
        Model model = Model.getInstance();
        boundary.moveTo(model.getMinLongitude(false), model.getMinLatitude(false));
        boundary.lineTo(model.getMaxLongitude(false), model.getMinLatitude(false));
        boundary.lineTo(model.getMaxLongitude(false), model.getMaxLatitude(false));
        boundary.lineTo(model.getMinLongitude(false), model.getMaxLatitude(false));
        boundary.lineTo(model.getMinLongitude(false), model.getMinLatitude(false));
        g.fill(boundary);
    }

    private void drawCoastlines(Graphics2D g)
    {
        java.util.List<Path2D> coastlines = Model.getInstance().getCoastlines();
        g.setColor(ThemeHelper.color("background"));
        for (Path2D path : coastlines) {
            g.fill(path);
        }
        // Creates outline
        if (GlobalValue.getMarkCoastlines()) {
            g.setStroke(new BasicStroke(Float.MIN_VALUE));
            g.setColor(Color.black);
            for (Path2D path : coastlines) {
                g.draw(path);
            }
        }
    }

    // TODO tænk over rækkefølgen elementerne bliver tegnet i (Jakob Nikolaj)
    private void drawElements(Graphics2D g)
    {
        switch (ZoomLevel.getZoomLevel()) {
        case LEVEL_0:
            drawCommonLand(g, ThemeHelper.color("commonland"),0.000001);
            drawPark(g, ThemeHelper.color("park"), 0.000001);
            drawBeach(g, ThemeHelper.color("beach"),0.000001);
            drawForest(g, ThemeHelper.color("forest"), 0.000001);
            drawGrassland(g, ThemeHelper.color("grassland"), 0.000001);
            drawGrass(g, ThemeHelper.color("grass"), 0.000001);
            drawFarmland(g, ThemeHelper.color("farmland"), 0.000001);
            drawMeadow(g, ThemeHelper.color("meadow"), 0.000001);
            drawHeath(g, ThemeHelper.color("heath"), 0.000001);
            drawPlayground(g, ThemeHelper.color("playground"),0.000004f);
            drawSportsPitch(g, ThemeHelper.color("sportspitch"),0.000001);
            drawSportsTrack(g, ThemeHelper.color("sportstrack"),0.000001);
            drawWater(g, ThemeHelper.color("water"), 0.000001);
            drawWetland(g, ThemeHelper.color("wetland"), 0.000001);
            drawBridge(g, ThemeHelper.color("bridge"), 0.000004f);
            drawPier(g, ThemeHelper.color("bridge"), 0.000008f);
            drawParking(g, ThemeHelper.color("parking"), 0.000001);

            drawFootways(g, ThemeHelper.color("footway"), ThemeHelper.color("footwayArea"), 0.000004f);
            drawBridleways(g, ThemeHelper.color("bridleway"), 0.000004f);
            drawCycleways(g, ThemeHelper.color("cycleway"), 0.000004f);
            drawPaths(g, ThemeHelper.color("path"), 0.000004f);
            drawRoads(g, ThemeHelper.color("road"), 0.00004f);
            drawSteps(g, ThemeHelper.color("steps"), 0.000004f);
            drawTracks(g, ThemeHelper.color("track"), 0.00004f);
            drawRaceways(g, ThemeHelper.color("raceway"), 0.00007f);
            drawEscapes(g, ThemeHelper.color("escape"), 0.00002f);
            drawBusGuideways(g, ThemeHelper.color("busGuideway"), 0.00006f);

            // roads
            drawPedestrianStreets(g, ThemeHelper.color("pedestrianStreet"), 0.00004f);
            drawServiceRoads(g, ThemeHelper.color("serviceRoad"), 0.00004f);
            drawLivingStreets(g, ThemeHelper.color("livingStreet"), 0.00005f);
            drawResidentialRoads(g, ThemeHelper.color("residentialRoad"), 0.00005f);
            drawUnclassifiedRoads(g, ThemeHelper.color("unclassifiedRoad"), 0.00005f);
            drawTertiaryRoads(g, ThemeHelper.color("tertiaryRoad"), 0.00006f);
            drawTertiaryRoadLinks(g, ThemeHelper.color("tertiaryRoad"), 0.00006f);
            drawSecondaryRoads(g, ThemeHelper.color("secondaryRoad"), 0.00008f);
            drawSecondaryRoadLinks(g, ThemeHelper.color("secondaryRoad"), 0.00008f);
            drawPrimaryRoads(g, ThemeHelper.color("primaryRoad"), 0.00008f);
            drawPrimaryRoadLinks(g, ThemeHelper.color("primaryRoad"), 0.00008f);
            drawTrunkRoads(g, ThemeHelper.color("trunkRoad"), 0.00013f);
            drawTrunkRoadLinks(g, ThemeHelper.color("trunkRoad"), 0.0001f);
            drawMotorways(g, ThemeHelper.color("motorway"), 0.00016f);
            drawMotorwayLinks(g, ThemeHelper.color("motorway"), 0.00012f);
            drawRail(g, ThemeHelper.color("rail"), 0.00002f);

            drawBuilding(g, ThemeHelper.color("building"));

            drawRoadNames(g, ElementType.PEDESTRIAN_STREET);
            drawRoadNames(g, ElementType.SERVICE_ROAD);
            drawRoadNames(g, ElementType.LIVING_STREET);
            drawRoadNames(g, ElementType.RESIDENTIAL_ROAD);
            drawRoadNames(g, ElementType.UNCLASSIFIED_ROAD);
            drawRoadNames(g, ElementType.TERTIARY_ROAD);
            drawRoadNames(g, ElementType.SECONDARY_ROAD);
            drawRoadNames(g, ElementType.PRIMARY_ROAD);
            drawRoadNames(g, ElementType.TRUNK_ROAD);
            drawRoadNames(g, ElementType.MOTORWAY);

            // Amenities
            drawNight(g);
            drawRailwayStation(g);
            drawHospital(g);
            drawParkingAmenity(g);
            drawPlaceOfWorship(g);
            drawSportAmenity(g);
            break;
        case LEVEL_1:
            drawCommonLand(g, ThemeHelper.color("commonland"),0.00005);
            drawPark(g, ThemeHelper.color("park"), 0.00005);
            drawBeach(g, ThemeHelper.color("beach"),0.00005);
            drawForest(g, ThemeHelper.color("forest"), 0.00005);
            drawGrassland(g, ThemeHelper.color("grassland"), 0.00005);
            drawGrass(g, ThemeHelper.color("grass"), 0.00005);
            drawFarmland(g, ThemeHelper.color("farmland"), 0.00005);
            drawMeadow(g, ThemeHelper.color("meadow"), 0.00005);
            drawHeath(g, ThemeHelper.color("heath"), 0.00005);
            drawWater(g, ThemeHelper.color("water"), 0.00005);
            drawWetland(g, ThemeHelper.color("wetland"), 0.00005);
            drawBridge(g, ThemeHelper.color("bridge"), 0.000004f);
            drawParking(g, ThemeHelper.color("parking"), 0.000001);

            drawRaceways(g, ThemeHelper.color("raceway"), 0.00007f);
            drawEscapes(g, ThemeHelper.color("escape"), 0.00002f);
            drawBusGuideways(g, ThemeHelper.color("busGuideway"), 0.00006f);
            drawPedestrianStreets(g, ThemeHelper.color("pedestrianStreet"), 0.00006f);
            drawServiceRoads(g, ThemeHelper.color("serviceRoad"), 0.00006f);
            drawLivingStreets(g, ThemeHelper.color("livingStreet"), 0.00007f);
            drawResidentialRoads(g, ThemeHelper.color("residentialRoad"), 0.00007f);
            drawUnclassifiedRoads(g, ThemeHelper.color("unclassifiedRoad"), 0.00007f);
            drawTertiaryRoads(g, ThemeHelper.color("tertiaryRoad"), 0.0001f);
            drawTertiaryRoadLinks(g, ThemeHelper.color("tertiaryRoad"), 0.0001f);
            drawSecondaryRoads(g, ThemeHelper.color("secondaryRoad"), 0.0001f);
            drawSecondaryRoadLinks(g, ThemeHelper.color("secondaryRoad"), 0.0001f);
            drawPrimaryRoads(g, ThemeHelper.color("primaryRoad"), 0.0001f);
            drawPrimaryRoadLinks(g, ThemeHelper.color("primaryRoad"), 0.0001f);
            drawTrunkRoads(g, ThemeHelper.color("trunkRoad"), 0.00014f);
            drawTrunkRoadLinks(g, ThemeHelper.color("trunkRoad"), 0.00012f);
            drawMotorways(g, ThemeHelper.color("motorway"), 0.00018f);
            drawMotorwayLinks(g, ThemeHelper.color("motorway"), 0.00014f);

            drawRail(g, ThemeHelper.color("rail"), 0.00002f);

            drawCityNames(g, ElementType.HAMLET_NAME, 0.35f);
            drawCityNames(g, ElementType.SUBURB_NAME, 0.35f);
            drawCityNames(g, ElementType.QUARTER_NAME, 0.35f);
            drawCityNames(g, ElementType.NEIGHBOURHOOD_NAME, 0.35f);

            drawRoadNames(g, ElementType.PRIMARY_ROAD);
            drawRoadNames(g, ElementType.TRUNK_ROAD);
            drawRoadNames(g, ElementType.MOTORWAY);

            // Amenities
            drawHospital(g);
            drawRailwayStation(g);
            drawParkingAmenity(g);
            break;
        case LEVEL_2:
            drawCommonLand(g, ThemeHelper.color("commonland"),0.00008);
            drawPark(g, ThemeHelper.color("park"), 0.00008);
            drawBeach(g, ThemeHelper.color("beach"),0.00008);
            drawForest(g, ThemeHelper.color("forest"), 0.00008);
            drawGrassland(g, ThemeHelper.color("grassland"), 0.00008);
            drawGrass(g, ThemeHelper.color("grass"), 0.00008);
            drawFarmland(g, ThemeHelper.color("farmland"), 0.00008);
            drawMeadow(g, ThemeHelper.color("meadow"), 0.00008);
            drawHeath(g, ThemeHelper.color("heath"), 0.00008);
            drawWater(g, ThemeHelper.color("water"), 0.00008);
            drawWetland(g, ThemeHelper.color("wetland"), 0.00008);
            drawBridge(g, ThemeHelper.color("bridge"), 0.000004f);

            drawResidentialRoads(g, ThemeHelper.color("residentialRoad"), 0.00007f);
            drawUnclassifiedRoads(g, ThemeHelper.color("unclassifiedRoad"), 0.00007f);
            drawTertiaryRoads(g, ThemeHelper.color("tertiaryRoad"), 0.0001f);
            drawTertiaryRoadLinks(g, ThemeHelper.color("tertiaryRoad"), 0.0001f);
            drawSecondaryRoads(g, ThemeHelper.color("secondaryRoad"), 0.0001f);
            drawSecondaryRoadLinks(g, ThemeHelper.color("secondaryRoad"), 0.0001f);
            drawPrimaryRoads(g, ThemeHelper.color("primaryRoad"), 0.0001f);
            drawPrimaryRoadLinks(g, ThemeHelper.color("primaryRoad"), 0.0001f);
            drawTrunkRoads(g, ThemeHelper.color("trunkRoad"), 0.00014f);
            drawTrunkRoadLinks(g, ThemeHelper.color("trunkRoad"), 0.00012f);
            drawMotorways(g, ThemeHelper.color("motorway"), 0.00018f);
            drawMotorwayLinks(g, ThemeHelper.color("motorway"), 0.00014f);

            drawRail(g, ThemeHelper.color("rail"), 0.00002f);

            drawCityNames(g, ElementType.VILLAGE_NAME, 0.35f);
            drawCityNames(g, ElementType.HAMLET_NAME, 0.35f);
            drawCityNames(g, ElementType.SUBURB_NAME, 0.35f);
            drawCityNames(g, ElementType.QUARTER_NAME, 0.35f);
            drawCityNames(g, ElementType.NEIGHBOURHOOD_NAME, 0.35f);
            break;
        case LEVEL_3:
            drawCommonLand(g, ThemeHelper.color("commonland"),0.001);
            drawPark(g, ThemeHelper.color("park"), 0.001);
            drawBeach(g, ThemeHelper.color("beach"),0.001);
            drawForest(g, ThemeHelper.color("forest"), 0.001);
            drawGrassland(g, ThemeHelper.color("grassland"), 0.001);
            drawGrass(g, ThemeHelper.color("grass"), 0.001);
            drawFarmland(g, ThemeHelper.color("farmland"), 0.001);
            drawMeadow(g, ThemeHelper.color("meadow"), 0.001);
            drawHeath(g, ThemeHelper.color("heath"), 0.001);
            drawWater(g, ThemeHelper.color("water"), 0.001);
            drawWetland(g, ThemeHelper.color("wetland"), 0.001);

            drawTertiaryRoads(g, ThemeHelper.color("tertiaryRoad"), 0.0001f);
            drawTertiaryRoadLinks(g, ThemeHelper.color("tertiaryRoad"), 0.0001f);
            drawSecondaryRoads(g, ThemeHelper.color("secondaryRoad"), 0.0001f);
            drawSecondaryRoadLinks(g, ThemeHelper.color("secondaryRoad"), 0.0001f);
            drawPrimaryRoads(g, ThemeHelper.color("primaryRoad"), 0.0001f);
            drawPrimaryRoadLinks(g, ThemeHelper.color("primaryRoad"), 0.0001f);
            drawTrunkRoads(g, ThemeHelper.color("trunkRoad"), 0.00014f);
            drawTrunkRoadLinks(g, ThemeHelper.color("trunkRoad"), 0.00012f);
            drawMotorways(g, ThemeHelper.color("motorway"), 0.00018f);
            drawMotorwayLinks(g, ThemeHelper.color("motorway"), 0.00014f);

            drawRail(g, ThemeHelper.color("rail"), 0.00002f);

            drawCityNames(g, ElementType.VILLAGE_NAME, 0.35f);
            drawCityNames(g, ElementType.HAMLET_NAME, 0.35f);
            drawCityNames(g, ElementType.SUBURB_NAME, 0.35f);
            drawCityNames(g, ElementType.QUARTER_NAME, 0.35f);
            drawCityNames(g, ElementType.NEIGHBOURHOOD_NAME, 0.35f);
            break;
        case LEVEL_4:
            drawCommonLand(g, ThemeHelper.color("commonland"),0.001);
            drawPark(g, ThemeHelper.color("park"), 0.001);
            drawBeach(g, ThemeHelper.color("beach"),0.001);
            drawForest(g, ThemeHelper.color("forest"), 0.001);
            drawGrassland(g, ThemeHelper.color("grassland"), 0.001);
            drawGrass(g, ThemeHelper.color("grass"), 0.001);
            drawFarmland(g, ThemeHelper.color("farmland"), 0.001);
            drawMeadow(g, ThemeHelper.color("meadow"), 0.001);
            drawHeath(g, ThemeHelper.color("heath"), 0.001);
            drawWater(g, ThemeHelper.color("water"), 0.001);
            drawWetland(g, ThemeHelper.color("wetland"), 0.001);

            drawTertiaryRoads(g, ThemeHelper.color("tertiaryRoad"), 0.0001f);
            drawTertiaryRoadLinks(g, ThemeHelper.color("tertiaryRoad"), 0.0001f);
            drawSecondaryRoads(g, ThemeHelper.color("secondaryRoad"), 0.0001f);
            drawSecondaryRoadLinks(g, ThemeHelper.color("secondaryRoad"), 0.0001f);
            drawPrimaryRoads(g, ThemeHelper.color("primaryRoad"), 0.0001f);
            drawPrimaryRoadLinks(g, ThemeHelper.color("primaryRoad"), 0.0001f);
            drawTrunkRoads(g, ThemeHelper.color("trunkRoad"), 0.00014f);
            drawTrunkRoadLinks(g, ThemeHelper.color("trunkRoad"), 0.00012f);
            drawMotorways(g, ThemeHelper.color("motorway"), 0.00018f);
            drawMotorwayLinks(g, ThemeHelper.color("motorway"), 0.00014f);

            drawRail(g, ThemeHelper.color("rail"), 0.00002f);

            drawCityNames(g, ElementType.CITY_NAME, 0.8f);
            drawCityNames(g, ElementType.TOWN_NAME, 0.35f);
            drawCityNames(g, ElementType.VILLAGE_NAME, 0.35f);
            break;
        case LEVEL_5:
            drawCommonLand(g, ThemeHelper.color("commonland"),0.005);
            drawForest(g, ThemeHelper.color("forest"), 0.005);
            drawGrassland(g, ThemeHelper.color("grassland"), 0.005);
            drawWater(g, ThemeHelper.color("water"), 0.005);

            drawTertiaryRoads(g, ThemeHelper.color("tertiaryRoad"), 0.0001f);
            drawTertiaryRoadLinks(g, ThemeHelper.color("tertiaryRoad"), 0.0001f);
            drawSecondaryRoads(g, ThemeHelper.color("secondaryRoad"), 0.0001f);
            drawSecondaryRoadLinks(g, ThemeHelper.color("secondaryRoad"), 0.0001f);
            drawPrimaryRoads(g, ThemeHelper.color("primaryRoad"), 0.0001f);
            drawPrimaryRoadLinks(g, ThemeHelper.color("primaryRoad"), 0.0001f);
            drawTrunkRoads(g, ThemeHelper.color("trunkRoad"), 0.00014f);
            drawTrunkRoadLinks(g, ThemeHelper.color("trunkRoad"), 0.00012f);
            drawMotorways(g, ThemeHelper.color("motorway"), 0.00018f);
            drawMotorwayLinks(g, ThemeHelper.color("motorway"), 0.00014f);

            drawRail(g, ThemeHelper.color("rail"), 0.00002f);

            drawCityNames(g, ElementType.CITY_NAME, 0.8f);
            drawCityNames(g, ElementType.TOWN_NAME, 0.35f);
            break;
        case LEVEL_6:
            drawCommonLand(g, ThemeHelper.color("commonland"),0.03);
            drawForest(g, ThemeHelper.color("forest"), 0.03);
            drawGrassland(g, ThemeHelper.color("grassland"), 0.03);
            drawWater(g, ThemeHelper.color("water"), 0.03);

            drawPrimaryRoads(g, ThemeHelper.color("primaryRoad"), 0.0001f);
            drawPrimaryRoadLinks(g, ThemeHelper.color("primaryRoad"), 0.0001f);
            drawTrunkRoads(g, ThemeHelper.color("trunkRoad"), 0.00014f);
            drawTrunkRoadLinks(g, ThemeHelper.color("trunkRoad"), 0.00012f);
            drawMotorways(g, ThemeHelper.color("motorway"), 0.00018f);
            drawMotorwayLinks(g, ThemeHelper.color("motorway"), 0.00014f);
            drawCityNames(g, ElementType.CITY_NAME, 1f);
            break;
        }
    }

    private void setCurrentSection(ElementType elementType) {
        currentSection = elements.get(elementType)
                             .getManySections((float)currentRectangle.getMinX(),
                                 (float)currentRectangle.getMinY(),
                                 (float)currentRectangle.getMaxX(),
                                 (float)currentRectangle.getMaxY());
    }

    private void drawBoundaries(Graphics2D g2D) {
        g2D.setColor(ThemeHelper.color("boundary"));
        Path2D boundary = new Path2D.Float();
        Model model = Model.getInstance();
        boundary.moveTo(model.getMinLongitude(true), model.getMinLatitude(true));
        boundary.lineTo(model.getMaxLongitude(true), model.getMinLatitude(true));
        boundary.lineTo(model.getMaxLongitude(true), model.getMaxLatitude(true));
        boundary.lineTo(model.getMinLongitude(true), model.getMaxLatitude(true));
        boundary.lineTo(model.getMinLongitude(true), model.getMinLatitude(true));
        g2D.draw(boundary);
    }

    /**
   * Zooms in or out upon the elements on the MapCanvas depending on a given
   * factor.
   */
    public void zoom(double factor) {
        DebugWindow.getInstance().setZoomLabel();
        DebugWindow.getInstance().setZoomFactorLabel();
        transform.preConcatenate(AffineTransform.getScaleInstance(factor, factor));
        repaint();
    }

    /**
   * Resets the MapCanvas from being zoomed in or out and panned to one or
   * another position.
   */
    public void resetTransform() { transform.setToIdentity(); }

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

    public Point2D toModelCoords(Point2D mousePosition) {
        try {
            return transform.inverseTransform(mousePosition, null);
        } catch (NoninvertibleTransformException e) {
            throw new RuntimeException();
        }
    }

    public void setElements(EnumMap<ElementType, KDTree> map) { elements = map; }

    public void setCurrentSection(HashSet<Element> currentSection) {
        this.currentSection = currentSection;
    }

    public void setCurrentPoint(Point2D currentPoint) {
        this.currentPoint = currentPoint;
    }

    // Draw Roads Methods
    private void drawRail(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.RAIL);
        for (Element element : currentSection) {
            g.setColor(color);
            g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
            g.draw(element.getShape());
        }
    }

    // Draw Roads Methods
    private void drawMotorways(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.MOTORWAY);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        for (Element element : currentSection) {
            g.draw(element.getShape());
        }
    }
    private void drawMotorwayLinks(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.MOTORWAY_LINK);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        for (Element element : currentSection) {
            g.draw(element.getShape());
        }
    }
    private void drawTrunkRoads(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.TRUNK_ROAD);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        for (Element element : currentSection) {
            g.draw(element.getShape());
        }
    }
    private void drawTrunkRoadLinks(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.TRUNK_ROAD_LINK);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        for (Element element : currentSection) {
            g.draw(element.getShape());
        }
    }
    private void drawPrimaryRoads(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.PRIMARY_ROAD);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        for (Element element : currentSection) {
            g.draw(element.getShape());
        }
    }
    private void drawPrimaryRoadLinks(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.PRIMARY_ROAD_LINK);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        for (Element element : currentSection) {
            g.draw(element.getShape());
        }
    }
    private void drawSecondaryRoads(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.SECONDARY_ROAD);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        for (Element element : currentSection) {
            g.draw(element.getShape());
        }
    }
    private void drawSecondaryRoadLinks(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.SECONDARY_ROAD_LINK);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        for (Element element : currentSection) {
            g.draw(element.getShape());
        }
    }
    private void drawTertiaryRoads(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.TERTIARY_ROAD);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        for (Element element : currentSection) {
            g.draw(element.getShape());
        }
    }
    private void drawTertiaryRoadLinks(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.TERTIARY_ROAD_LINK);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        for (Element element : currentSection) {
            g.draw(element.getShape());
        }
    }
    private void drawUnclassifiedRoads(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.UNCLASSIFIED_ROAD);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        for (Element element : currentSection) {
            g.draw(element.getShape());
        }
    }
    private void drawResidentialRoads(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.RESIDENTIAL_ROAD);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        for (Element element : currentSection) {
            g.draw(element.getShape());
        }
    }
    private void drawLivingStreets(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.LIVING_STREET);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        for (Element element : currentSection) {
            g.draw(element.getShape());
        }
    }
    private void drawServiceRoads(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.SERVICE_ROAD);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        for (Element element : currentSection) {
            g.draw(element.getShape());
        }
    }
    private void drawBusGuideways(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.BUS_GUIDEWAY);
        g.setColor(color);
        g.setStroke(new BasicStroke(width));
        for (Element element : currentSection) {
            g.draw(element.getShape());
        }
    }
    private void drawEscapes(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.ESCAPE);
        g.setColor(color);
        g.setStroke(new BasicStroke(width));
        for (Element element : currentSection) {
            g.draw(element.getShape());
        }
    }
    private void drawRaceways(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.RACEWAY);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        for (Element element : currentSection) {
            g.draw(element.getShape());
        }
    }
    private void drawPedestrianStreets(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.PEDESTRIAN_STREET);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        for (Element element : currentSection) {
            Road road = (Road)element;
            if (road.isArea()) g.fill(element.getShape());
            else g.draw(element.getShape());
        }
    }
    private void drawTracks(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.TRACK);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        for (Element element : currentSection) {
            g.draw(element.getShape());
        }
    }
    private void drawSteps(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.STEPS);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] { 0.00001f }, 0.0f));
        for (Element element : currentSection) {
            g.draw(element.getShape());
        }
    }
    private void drawFootways(Graphics2D g, Color color, Color areaColor, float width) {
        setCurrentSection(ElementType.FOOTWAY);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] { 0.00001f }, 0.0f));
        for (Element element : currentSection) {
            Road r = (Road)element;
            if (r.isArea()) {
                g.setColor(areaColor);
                g.fill(element.getShape());
            } else {
                g.setColor(color);
                g.draw(element.getShape());
            }
        }
    }
    private void drawBridge(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.BRIDGE);
        g.setColor(color);
        g.setStroke(new BasicStroke(width));
        for (Element element : currentSection) {
            ManMade manMade = (ManMade)element;
            if (manMade.isArea()) g.fill(element.getShape());
            else g.draw(element.getShape());
        }
    }
    private void drawPier(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.PIER);
        g.setColor(color);
        g.setStroke(new BasicStroke(width));
        for (Element element : currentSection) {
            g.draw(element.getShape());
        }
    }
    private void drawBridleways(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.BRIDLEWAY);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] { 0.00001f }, 0.0f));
        for (Element element : currentSection) {
            g.draw(element.getShape());
        }
    }
    private void drawCycleways(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.CYCLEWAY);
        g.setColor(color);
        g.setStroke(new BasicStroke(width));
        for (Element element : currentSection) {
            g.draw(element.getShape());
        }
    }
    private void drawPaths(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.PATH);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] { 0.00001f }, 0.0f));
        for (Element element : currentSection) {
            g.draw(element.getShape());
        }
    }
    private void drawRoads(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.ROAD);
        g.setColor(color);
        g.setStroke(new BasicStroke(width));
        for (Element element : currentSection) {
            g.draw(element.getShape());
        }
    }

    private void drawPark(Graphics2D g, Color color, Double minSizeToBeSignificant) {
        setCurrentSection(ElementType.PARK);
        g.setColor(color);
        for (Element element : currentSection) {
            Biome biome = (Biome)element;
            float size = biome.getShape().getSize();
            if (size > minSizeToBeSignificant) {
                g.fill(biome.getShape());
            }
        }
    }

    private void drawPlayground(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.PLAYGROUND);
        g.setColor(color);
        g.setStroke(new BasicStroke(width));
        for (Element element : currentSection) {
            g.fill(element.getShape());
        }
    }

    private void drawWetland(Graphics2D g, Color color, Double minSizeToBeSignificant) {
        setCurrentSection(ElementType.WETLAND);
        g.setColor(color);
        for (Element element : currentSection) {
            Biome biome = (Biome)element;
            float size = biome.getShape().getSize();
            if (size > minSizeToBeSignificant) {
                g.fill(biome.getShape());
            }
        }
    }

    private void drawSportsPitch(Graphics2D g, Color color, Double minSizeToBeSignificant) {
        setCurrentSection(ElementType.SPORTSPITCH);
        g.setColor(color);
        for (Element element : currentSection) {
            Biome biome = (Biome)element;
            float size = biome.getShape().getSize();
            if (size > minSizeToBeSignificant) {
                g.fill(biome.getShape());
            }
        }
    }
    private void drawSportsTrack(Graphics2D g, Color color, Double minSizeToBeSignificant) {
        setCurrentSection(ElementType.SPORTSTRACK);
        g.setColor(color);
        for (Element element : currentSection) {
            Biome biome = (Biome)element;
            float size = biome.getShape().getSize();
            if (size > minSizeToBeSignificant) {
                g.fill(biome.getShape());
            }
        }
    }

    private void drawForest(Graphics2D g, Color color,
        Double minSizeToBeSignificant)
    {
        setCurrentSection(ElementType.FOREST);
        g.setColor(color);
        for (Element element : currentSection) {
            Biome biome = (Biome)element;
            float size = biome.getShape().getSize();
            if (size > minSizeToBeSignificant) {
                g.fill(biome.getShape());
            }
        }
    }

    private void drawGrassland(Graphics2D g, Color color, Double minSizeToBeSignificant) {
        setCurrentSection(ElementType.GRASSLAND);
        g.setColor(color);
        for (Element element : currentSection) {
            Biome biome = (Biome)element;
            float size = biome.getShape().getSize();
            if (size > minSizeToBeSignificant) {
                g.fill(biome.getShape());
            }
        }
    }

    private void drawGrass(Graphics2D g, Color color, Double minSizeToBeSignificant) {
        setCurrentSection(ElementType.GRASS);
        g.setColor(color);
        for (Element element : currentSection) {
            Biome biome = (Biome)element;
            float size = biome.getShape().getSize();
            if (size > minSizeToBeSignificant) {
                g.fill(biome.getShape());
            }
        }
    }

    private void drawMeadow(Graphics2D g, Color color, Double minSizeToBeSignificant) {
        setCurrentSection(ElementType.MEADOW);
        g.setColor(color);
        for (Element element : currentSection) {
            Biome biome = (Biome)element;
            float size = biome.getShape().getSize();
            if (size > minSizeToBeSignificant) {
                g.fill(biome.getShape());
            }
        }
    }

    private void drawFarmland(Graphics2D g, Color color,
        Double minSizeToBeSignificant)
    {
        setCurrentSection(ElementType.FARMLAND);
        g.setColor(color);
        for (Element element : currentSection) {
            Biome biome = (Biome)element;
            float size = biome.getShape().getSize();
            if (size > minSizeToBeSignificant) {
                g.fill(biome.getShape());
            }
        }
    }

    private void drawHeath(Graphics2D g, Color color,
        Double minSizeToBeSignificant)
    {
        setCurrentSection(ElementType.HEATH);
        g.setColor(color);
        for (Element element : currentSection) {
            Biome biome = (Biome)element;
            float size = biome.getShape().getSize();
            if (size > minSizeToBeSignificant) {
                g.fill(biome.getShape());
            }
        }
    }

    private void drawWater(Graphics2D g, Color color,
        Double minSizeToBeSignificant)
    {
        setCurrentSection(ElementType.WATER);
        g.setColor(color);
        g.setStroke(new BasicStroke(0.00001f));
        for (Element element : currentSection) {
            Biome biome = (Biome)element;
            float size = biome.getShape().getSize();
            if (size > minSizeToBeSignificant) {
                g.fill(biome.getShape());
            }
        }
    }

    private void drawBeach(Graphics2D g, Color color, Double minSizeToBeSignificant) {
        setCurrentSection(ElementType.BEACH);
        g.setColor(color);
        g.setStroke(new BasicStroke(0.00001f));
        for (Element element : currentSection) {
            Biome biome = (Biome)element;
            float size = biome.getShape().getSize();
            if (size > minSizeToBeSignificant) {
                g.fill(biome.getShape());
            }
        }
    }
    private void drawCommonLand(Graphics2D g, Color color, Double minSizeToBeSignificant) {
        setCurrentSection(ElementType.COMMON_LAND);
        g.setColor(color);
        g.setStroke(new BasicStroke(0.00001f));
        for (Element element : currentSection) {
            Biome biome = (Biome)element;
            float size = biome.getShape().getSize();
            if (size > minSizeToBeSignificant) {
                g.fill(biome.getShape());
            }
        }
    }
    private void drawParking(Graphics2D g, Color color, Double minSizeToBeSignificant) {
        setCurrentSection(ElementType.PARKING);
        g.setColor(color);
        g.setStroke(new BasicStroke(0.00001f));
        for (Element element : currentSection) {
            Biome biome = (Biome)element;
            float size = biome.getShape().getSize();
            if (size > minSizeToBeSignificant) {
                g.fill(biome.getShape());
            }
        }
    }


    private void drawBuilding(Graphics2D g, Color color)
    {
        setCurrentSection(ElementType.BUILDING);
        Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .9f);
        g.setComposite(c);
        g.setColor(color);
        g.setStroke(new BasicStroke(0.00001f));
        for (Element element : currentSection) {
            g.fill(element.getShape());
        }
        c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
        g.setComposite(c);
    }

    private void drawRoadNames(Graphics2D g, ElementType type) {
        setCurrentSection(type);

        // Scalefactor
        float scaleFactor = 0.1f * 397.522f * (float)(Math.pow(ZoomLevel.getZoomFactor(), -2.43114f));

        // Font
        Font font = new Font("Times New Roman", Font.PLAIN, 12);

        // Transparency
        Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .7f); // TODO want transparency for road names ?
        g.setComposite(c);

        // Color
        g.setColor(ThemeHelper.color("roadName"));

        for (Element element : currentSection) {
            Road road = (Road)element;
            g.setFont(font.deriveFont(AffineTransform.getScaleInstance(scaleFactor, scaleFactor)));
            PolygonApprox polygon = (PolygonApprox)road.getShape();

            // The polygon coords
            float[] coords = polygon.getCoords();
            if (coords.length < 4) break;
            float longestVectorX1 = coords[0];
            float longestVectorY1 = coords[1];
            float longestVectorX2 = coords[2];
            float longestVectorY2 = coords[3];

            // Coords to help finding the longest vector
            float x1;
            float y1;
            float x2;
            float y2;

            // Find the longest vector in the path
            for (int i = 2; i < coords.length; i += 2) {
                x1 = coords[i - 2];
                y1 = coords[i - 1];
                x2 = coords[i];
                y2 = coords[i + 1];

                //If longest vector update
                if ((vectorLength(longestVectorX2 - longestVectorX1,longestVectorY2 - longestVectorY1)) < (vectorLength(x2 - x1, y2 - y1))) {
                    longestVectorX1 = x1;
                    longestVectorY1 = y1;
                    longestVectorX2 = x2;
                    longestVectorY2 = y2;
                }
            }

            // Find the angle of the longest vector in the path
            double angle = vectorAngle(longestVectorX1, longestVectorY1,
                longestVectorX2, longestVectorY2);

            // The center of the longest vector
            float centerX = (longestVectorX2 + longestVectorX1) / 2;
            float centerY = (longestVectorY2 + longestVectorY1) / 2;

            // The length of the string (name)
            float stringLength = 0;
            for (int i = 0; i < road.getName().length(); i++) {
                char ch = road.getName().charAt(i);
                stringLength += getFontMetrics(font).charWidth(ch) * scaleFactor;
            }

            // Using trigonometry to find the appropiate start for the string
            float hyp = stringLength / 2;
            float hos = (float)Math.cos(angle) * hyp;
            float mod = (float)Math.sin(angle) * hyp;

            float drawFromX = centerX - hos;
            float drawFromY = centerY - mod;

            //Using teachings about triangles
            hyp = (float)0.0001 / 4;
            hos = (float)Math.cos(90 - angle) * hyp;
            mod = (float)Math.sin(90 - angle) * hyp;

            drawFromX = drawFromX - hos;
            drawFromY = drawFromY + mod;

            /*
             Safe Affine transform, rotate, shift etc. draw the name
             and set Affine Transform back to the state before
             drawing the name.
             */
            if (vectorLength(longestVectorX2 - longestVectorX1,longestVectorY2 - longestVectorY1) > stringLength) {
                AffineTransform old = g.getTransform();
                g.rotate(angle, drawFromX, drawFromY);
                drawString(road.getName(), g, drawFromX, drawFromY, font, scaleFactor,false);
                g.setTransform(old);
            }
        }
        // Transparency off
        c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
        g.setComposite(c);
    }
    private double vectorLength(float x, float y) {
        return Math.sqrt(x * x + y * y);
    }
    private double dotProduct(float x1, float y1, float x2, float y2) {
        double newX1 = (double)x1;
        double newY1 = (double)y1;
        double newX2 = (double)x2;
        double newY2 = (double)y2;
        return newX1 * newX2 + newY1 * newY2;
    }
    private double vectorAngle(float x1, float y1, float x2, float y2) {
        double cosAngle;
        double dotProduct = dotProduct((x2 - x1), (y2 - y1), 1, 0f);
        double vector1Length = vectorLength((x2 - x1), (y2 - y1));
        double vector2Length = vectorLength(1, 0f);
        cosAngle = dotProduct / (vector1Length * vector2Length);

        if ((y2 - y1) < 0 && (x2 - x1) > 0) return -Math.acos(cosAngle);
        if ((y2 - y1) < 0 && (x2 - x1) < 0) return Math.acos(-cosAngle);
        if ((y2 - y1) > 0 && (x2 - x1) > 0) return Math.acos(cosAngle);
        if ((y2 - y1) > 0 && (x2 - x1) < 0) return -Math.acos(-cosAngle);
        if ((x2 - x1) < 0) return Math.acos(-cosAngle);
        return Math.acos(cosAngle);
    }

    private void drawCityNames(Graphics2D g, ElementType type, float scaling) {
        setCurrentSection(type);
        if (ZoomLevel.getZoomFactor() > -60) {
            float scaleFactor;

            //Set the scalefactor such that the citynames have the desired size of the various zoom levels.
            if (ZoomLevel.getZoomFactor() >= 100) scaleFactor = scaling * 397.522f * (float)(Math.pow(ZoomLevel.getZoomFactor(), -2.43114f));
            else scaleFactor = 0.0054586004f;

            // Font
            Font font = new Font("Arial", Font.BOLD, 12);

            // Transparency
            Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .7f);
            g.setComposite(c);

            // Color
            g.setColor(ThemeHelper.color("cityName"));
            for (Element element : currentSection) {
                PlaceName placeName = (PlaceName)element;
                g.setFont(font.deriveFont(AffineTransform.getScaleInstance(scaleFactor, scaleFactor)));
                drawString(placeName.getName(), g, placeName.getX(), placeName.getY(), font, scaleFactor, true);
            }

            // Transparency off
            c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
            g.setComposite(c);
        }
    }

    /*
    Draw each character in the String, taken as a parameter, and then shift to the next letter.
    The shift will be the size of the latter just written.
     */
    private void drawString(String s, Graphics2D g, float x, float y, Font font, float scaleFactor, boolean isShiftedLeft) {
        if (s.length() > 0) {
            if (isShiftedLeft) x = x - ((getFontMetrics(font).charWidth(s.charAt(s.length() / 2)) * scaleFactor) * s.length() / 2);
            for (int i = 0; i < s.length(); i++) {
                char ch = s.charAt(i);
                g.drawString(ch + "", x, y);
                x += ((getFontMetrics(font).charWidth(ch))) * scaleFactor;
            }
        }
    }

    private void drawNight(Graphics2D g) {
        switch (ThemeHelper.getCurrentTheme()) {
        case "Night":
            if (ZoomLevel.getZoomFactor() >= 650) {
                float scaleFactor = 0.5f * (float)(Math.pow(ZoomLevel.getZoomFactor(), -2f));
                Font font = Helpers.FontAwesome.getFontAwesome();
                g.setFont(font.deriveFont(AffineTransform.getScaleInstance(scaleFactor, scaleFactor)));
                setCurrentSection(ElementType.BAR);
                for (Element element : currentSection) {
                    Amenity amenity = (Amenity)element;
                    g.setColor(ThemeHelper.color("barName"));
                    drawString("\uf000" + "", g, amenity.getX(), amenity.getY(), font, scaleFactor, false);
                }
                setCurrentSection(ElementType.NIGHT_CLUB);
                for (Element element : currentSection) {
                    Amenity amenity = (Amenity)element;
                    g.setColor(ThemeHelper.color("nightClubName"));
                    drawString("\uf001" + "", g, amenity.getX(), amenity.getY(), font, scaleFactor, false);
                }
                setCurrentSection(ElementType.FAST_FOOD);
                for (Element element : currentSection) {
                    Amenity amenity = (Amenity)element;
                    g.setColor(ThemeHelper.color("fastFoodName"));
                    drawString("\uf0f5" + "", g, amenity.getX(), amenity.getY(), font, scaleFactor, false);
                }
            }
        }
    }

    private void drawHospital(Graphics2D g) {
        float scaleFactor;
        scaleFactor = 1.7f * (float)(Math.pow(ZoomLevel.getZoomFactor(), -2f));
        setCurrentSection(ElementType.HOSPITAL);

        //Color and font
        g.setColor(ThemeHelper.color("hospital"));
        Font font = Helpers.FontAwesome.getFontAwesome();
        g.setFont(font.deriveFont(AffineTransform.getScaleInstance(scaleFactor, scaleFactor)));

        for (Element element : currentSection) {
            Amenity amenity = (Amenity)element;
            drawString("\uf0fe" + "", g, amenity.getX(), amenity.getY(), font, scaleFactor, true);
        }

        float deltay = ((getFontMetrics(font).getHeight()* scaleFactor)/2);

        //Font for drawing name
        font = new Font("Arial", Font.BOLD, 18);
        g.setFont(font.deriveFont(AffineTransform.getScaleInstance(scaleFactor, scaleFactor)));
        for (Element element : currentSection){
            Amenity amenity = (Amenity)element;

            String name = amenity.getName();
            float x = amenity.getX() - (getFontMetrics(font).stringWidth(name)* scaleFactor/2);

            drawString(name, g, x, amenity.getY()+deltay, font, scaleFactor, false);
        }
    }
    private void drawRailwayStation(Graphics2D g) {
        float scaleFactor;
        scaleFactor = 1.7f * (float)(Math.pow(ZoomLevel.getZoomFactor(), -2f));

        //Color and font
        g.setColor(ThemeHelper.color("railwayStation"));
        Font font = Helpers.FontAwesome.getFontAwesome();
        g.setFont(font.deriveFont(AffineTransform.getScaleInstance(scaleFactor, scaleFactor)));

        setCurrentSection(ElementType.RAILWAY_STATION);
        for (Element element : currentSection) {
            Amenity amenity = (Amenity)element;
            drawString("\uf238" + "", g, amenity.getX(), amenity.getY(), font, scaleFactor, true);
        }
        setCurrentSection(ElementType.RAILWAY_STATION_AREA);
        for (Element element : currentSection) {
            Amenity amenity = (Amenity)element;
            drawString("\uf238" + "", g, amenity.getX(), amenity.getY(), font, scaleFactor, true);
        }
    }
    private void drawPlaceOfWorship(Graphics2D g) {
        float scaleFactor;
        scaleFactor = 1.5f * (float)(Math.pow(ZoomLevel.getZoomFactor(), -2f));
        setCurrentSection(ElementType.PLACE_OF_WORSHIP);

        //Color and font
        g.setColor(ThemeHelper.color("placeOfWorship"));
        Font font = new Font("Wingdings", Font.PLAIN, 36);
        g.setFont(font.deriveFont(AffineTransform.getScaleInstance(scaleFactor, scaleFactor)));

        for (Element element : currentSection) {
            Amenity amenity = (Amenity)element;
            float y = amenity.getY();
            y += ((getFontMetrics(font).charWidth('\uf055')) / 2) * scaleFactor;
            drawString("\uf055" + "", g, amenity.getX(), y, font, scaleFactor, true);
        }
    }
    private void drawParkingAmenity(Graphics2D g) {
        float scaleFactor;
        scaleFactor = 1.5f * (float)(Math.pow(ZoomLevel.getZoomFactor(), -2f));
        setCurrentSection(ElementType.PARKING_AMENITY);

        // Transparency
        Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .2f);
        g.setComposite(c);

        //Color and font
        g.setColor(ThemeHelper.color("parkingAmenity"));
        Font font = new Font("Arial", Font.BOLD, 18);
        g.setFont(font.deriveFont(AffineTransform.getScaleInstance(scaleFactor, scaleFactor)));

        for (Element element : currentSection) {
            Amenity amenity = (Amenity)element;
            float y = amenity.getY();
            y += ((getFontMetrics(font).charWidth('P')) / 2) * scaleFactor;
            drawString("P", g, amenity.getX(), y, font, scaleFactor, true);
        }
        // Transparency off
        c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
        g.setComposite(c);
    }

    private void drawSportAmenity(Graphics2D g) {
        float scaleFactor;
        scaleFactor = 0.75f * (float)(Math.pow(ZoomLevel.getZoomFactor(), -2f));
        setCurrentSection(ElementType.SPORT_AMENITY);

        // Transparency
        Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .7f);
        g.setComposite(c);

        //Color and font
        g.setColor(ThemeHelper.color("sportAmenity"));
        Font font = Helpers.FontAwesome.getFontAwesome();
        g.setFont(font.deriveFont(AffineTransform.getScaleInstance(scaleFactor, scaleFactor)));

        for (Element element : currentSection) {
            Amenity amenity = (Amenity)element;
            float y = amenity.getY();
            y += ((getFontMetrics(font).charWidth('\uf1e3')) / 2) * scaleFactor;
            drawString("\uf1e3" + "", g, amenity.getX(), y, font, scaleFactor, true);
        }
        // Transparency off
        c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
        g.setComposite(c);
    }

    public void setLocationMarker(Point2D.Float locationMarker) {
        this.locationMarker = locationMarker;
    }

    public void panToPoint(Point2D point){

        Rectangle2D rectangle = getVisibleRect();
        Point2D midpoint = toModelCoords(new Point2D.Double(rectangle.getCenterX(), rectangle.getCenterY()));
        double xCenter = midpoint.getX();
        double yCenter = midpoint.getY();
        double pointX = point.getX();
        double pointY = point.getY();
        Point2D pointToMove;
        Point2D pointToStart;
        try {
            pointToMove = transform.createInverse().inverseTransform(point, null);
            pointToStart = transform.createInverse().inverseTransform(midpoint, null);
            pan(pointToStart.getX() - pointToMove.getX(), pointToStart.getY() - pointToMove.getY());
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }
        /*while((double)Math.round(xCenter * 1000d) / 1000d != (double)Math.round(pointX * 1000d) / 1000d && (double)Math.round(yCenter * 1000d) / 1000d != (double)Math.round(pointY * 1000d) / 1000d) {
            rectangle = getVisibleRect();
            midpoint = toModelCoords(new Point2D.Double(rectangle.getCenterX(), rectangle.getCenterY()));
            xCenter = midpoint.getX();
            yCenter = midpoint.getY();

            System.out.println(xCenter);
            System.out.println(pointX);

            pan(xCenter - pointX, yCenter - pointY);
            repaint();
        }*/
    }


}