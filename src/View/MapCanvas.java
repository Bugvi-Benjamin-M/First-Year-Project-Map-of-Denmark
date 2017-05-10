package View;

import Enums.OSMEnums.AmenityType;
import Enums.OSMEnums.ElementType;
import Enums.ZoomLevel;
import Helpers.GlobalValue;
import Helpers.Shapes.PolygonApprox;
import Helpers.ThemeHelper;
import Helpers.Utilities.DebugWindow;
import KDtree.KDTree;
import Main.Main;
import Model.Elements.*;
import OSM.OSMWay;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

/**
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
    private HashSet<SuperElement> currentSection;
    private Point2D currentPoint;
    private Rectangle2D currentRectangle;
    private EnumMap<ElementType, KDTree> elements;
    private boolean antiAliasing;
    private Point2D.Float locationMarker;
    private Point2D.Float toMarker;
    private Point2D.Float fromMarker;
    private ArrayList<POI> poiList;

    private List<Path2D> coastlines;
    private List<Shape> route;
    private boolean drawRoute;

    private float cameraMaxLon;
    private float cameraMinLon;
    private float cameraMaxLat;
    private float cameraMinLat;

    private float maxLon;
    private float minLon;
    private float maxLat;
    private float minLat;

    private float dynMaxLon;
    private float dynMinLon;
    private float dynMaxLat;
    private float dynMinLat;

    //default level of detals on zoom levels
    private static final double DEFAULT_LEVEL_0_MINIMUM_SIZE_TO_BE_SIGNISFICANT = 0.000001;
    private static final double DEFAULT_LEVEL_1_MINIMUM_SIZE_TO_BE_SIGNISFICANT = 0.00005;
    private static final double DEFAULT_LEVEL_2_MINIMUM_SIZE_TO_BE_SIGNISFICANT = 0.00008;
    private static final double DEFAULT_LEVEL_3_MINIMUM_SIZE_TO_BE_SIGNISFICANT = 0.001;
    private static final double DEFAULT_LEVEL_4_MINIMUM_SIZE_TO_BE_SIGNISFICANT = 0.001;
    private static final double DEFAULT_LEVEL_5_MINIMUM_SIZE_TO_BE_SIGNISFICANT = 0.005;
    private static final double DEFAULT_LEVEL_6_MINIMUM_SIZE_TO_BE_SIGNISFICANT = 0.03;

    public MapCanvas() {
        transform = new AffineTransform();
        poiList = new ArrayList<>();
        setBackgroundColor();
        grabFocus();
    }

    public void setBackgroundColor() {
        setBackground(ThemeHelper.color("water"));
    }

    public void toggleAntiAliasing(boolean isAntiAliasing) {
        antiAliasing = isAntiAliasing;
    }

    private void setCurrentRectangle() {
        Rectangle2D rectangle = getVisibleRect();
        rectangle.setRect(rectangle.getX(), rectangle.getY() + GlobalValue.getToolbarHeight(), rectangle.getWidth(), rectangle.getHeight());
        Point2D point = toModelCoords(new Point2D.Double(0, GlobalValue.getToolbarHeight()));
        Point2D factor = toModelCoords(new Point2D.Double(rectangle.getWidth(), rectangle.getHeight()));
        double xBounds = factor.getX() - point.getX();
        double yBounds = factor.getY() - point.getY();
        currentRectangle = new Rectangle2D.Double(point.getX() - 0.001, point.getY() - 0.001, xBounds + 0.001, yBounds + 0.001);
        cameraMinLon = (float) point.getX();
        cameraMaxLon = (float) factor.getX();
        cameraMaxLat = (float) point.getY();
        cameraMinLat = (float) factor.getY();
        DebugWindow.getInstance().setCameraBoundsLabel();
    }

    public void setCoastLines(List<Path2D> coastLines) {this.coastlines = coastLines;}

    public float getCameraMaxLon() {
        return cameraMaxLon;
    }

    public float getCameraMinLon() {
        return cameraMinLon;
    }

    public float getCameraMaxLat() {
        return cameraMaxLat;
    }

    public float getCameraMinLat() {
        return cameraMinLat;
    }

    public void setMaxLon(float maxLon) {
        this.maxLon = maxLon;
    }

    public void setMinLon(float minLon) {
        this.minLon = minLon;
    }

    public void setMaxLat(float maxLat) {
        this.maxLat = maxLat;
    }

    public void setMinLat(float minLat) {
        this.minLat = minLat;
    }

    public void setDynMaxLon(float dynMaxLon) {
        this.dynMaxLon = dynMaxLon;
    }

    public void setDynMinLon(float dynMinLon) {
        this.dynMinLon = dynMinLon;
    }

    public void setDynMaxLat(float dynMaxLat) {
        this.dynMaxLat = dynMaxLat;
    }

    public void setDynMinLat(float dynMinLat) {
        this.dynMinLat = dynMinLat;
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

        drawMarkers(g2D);
        drawPOI(g2D);
        drawBoundaries(g2D);
        drawRoute(g2D);
        Main.FPS_COUNTER.interrupt();
        DebugWindow.getInstance().setFPSLabel();
    }

    private void drawMarkers(Graphics2D g){
        Point2D start = toModelCoords(new Point2D.Float(0f, 0f));
        Point2D inner = toModelCoords(new Point2D.Float(16f, 0f));
        Point2D outer = toModelCoords(new Point2D.Float(20f, 0f));
        float boundsblue = (float)inner.getX() - (float) start.getX();
        float boundswhite = (float)outer.getX() - (float) start.getX();

        if(locationMarker != null) {
            g.setColor(Color.white);
            g.fill(getEllipseFromCenter(locationMarker.getX(), locationMarker.getY(), boundswhite, boundswhite));
            g.setColor(Color.black);
            g.fill(getEllipseFromCenter(locationMarker.getX(), locationMarker.getY(), boundsblue, boundsblue));
        }
        //Only draw the route if the journey planner window is open.
        if(drawRoute) {
            if (toMarker != null) {
                g.setColor(Color.white);
                g.fill(getEllipseFromCenter(toMarker.getX(), toMarker.getY(), boundswhite, boundswhite));
                g.setColor(Color.red);
                g.fill(getEllipseFromCenter(toMarker.getX(), toMarker.getY(), boundsblue, boundsblue));
            }

            if (fromMarker != null) {
                g.setColor(Color.white);
                g.fill(getEllipseFromCenter(fromMarker.getX(), fromMarker.getY(), boundswhite, boundswhite));
                g.setColor(Color.red);
                g.fill(getEllipseFromCenter(fromMarker.getX(), fromMarker.getY(), boundsblue, boundsblue));
            }
        }
    }

    public void toggleRouteVisualization(boolean isActive){
        this.drawRoute = isActive;
    }

    public void resetToAndFrom(){
        toMarker = null;
        fromMarker = null;
    }

    public void setLocationMarker(Point2D.Float locationMarker) {
        this.locationMarker = locationMarker;
    }

    public void setToMarker(Point2D.Float toMarker){
        this.toMarker = toMarker;
    }

    public void setFromMarker(Point2D.Float fromMarker){
        this.fromMarker = fromMarker;
    }

    private Ellipse2D getEllipseFromCenter(double x, double y, double width, double height) {
        double newX = x - width / 2.0;
        double newY = y - height / 2.0;

        Ellipse2D ellipse = new Ellipse2D.Double(newX, newY, width, height);
        return ellipse;
    }

    private void drawBackground(Graphics2D g) {
        g.setColor(ThemeHelper.color("water"));
        Path2D boundary = new Path2D.Float();
        boundary.moveTo(minLon, minLat);
        boundary.lineTo(maxLon, minLat);
        boundary.lineTo(maxLon, maxLat);
        boundary.lineTo(minLon, maxLat);
        boundary.lineTo(minLon, minLat);
        g.fill(boundary);
    }

    private void drawCoastlines(Graphics2D g) {
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

    private void drawRoute(Graphics2D g) {
        g.setColor(ThemeHelper.color("route"));
        g.setStroke(new BasicStroke(0.00020f));
        if (route != null) {
            for (Shape road: route) {
                g.draw(road);
            }
        }
    }

    public void setRoute(List<Road> route, List<Long> routeRefs) {
        this.route = new LinkedList<>();
        int counter = 0;
        for (int i = 1; i < routeRefs.size(); i++) {
            this.route.add(route.get(counter).getShapeSection(
                    routeRefs.get(i-1),routeRefs.get(i)));
            counter++;
        }
    }

    public void setRoute(List<RoadEdge> route) {
        this.route = new LinkedList<>();
        for (RoadEdge edge : route) {
            OSMWay way = new OSMWay();
            way.add(edge.getEither());
            way.add(edge.getOther(edge.getEither()));
            this.route.add(new PolygonApprox(way));
        }
    }

    private void drawElements(Graphics2D g) {
        switch (ZoomLevel.getZoomLevel()) {
        case LEVEL_0:
            drawBiomeArea(ElementType.COMMON_LAND, g, ThemeHelper.color("commonland"), DEFAULT_LEVEL_0_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.PARK, g, ThemeHelper.color("park"), DEFAULT_LEVEL_0_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.BEACH, g, ThemeHelper.color("beach"), DEFAULT_LEVEL_0_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.FOREST, g, ThemeHelper.color("forest"), DEFAULT_LEVEL_0_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.GRASSLAND, g, ThemeHelper.color("grassland"), DEFAULT_LEVEL_0_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.GRASS, g, ThemeHelper.color("grass"), DEFAULT_LEVEL_0_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.FARMLAND, g, ThemeHelper.color("farmland"), DEFAULT_LEVEL_0_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.MEADOW, g, ThemeHelper.color("meadow"), DEFAULT_LEVEL_0_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.HEATH, g, ThemeHelper.color("heath"), DEFAULT_LEVEL_0_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.PLAYGROUND, g, ThemeHelper.color("playground"), DEFAULT_LEVEL_0_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.SPORTSPITCH, g, ThemeHelper.color("sportspitch"), DEFAULT_LEVEL_0_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.SPORTSTRACK, g, ThemeHelper.color("sportstrack"), DEFAULT_LEVEL_0_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeWay(ElementType.HEDGE, g, ThemeHelper.color("forest"), DEFAULT_LEVEL_0_MINIMUM_SIZE_TO_BE_SIGNISFICANT, 0.00004f, BasicStroke.CAP_ROUND);
            drawBiomeArea(ElementType.WATER, g, ThemeHelper.color("water"), DEFAULT_LEVEL_0_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.WETLAND, g, ThemeHelper.color("wetland"), DEFAULT_LEVEL_0_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeWay(ElementType.RIVER, g, ThemeHelper.color("water"), DEFAULT_LEVEL_0_MINIMUM_SIZE_TO_BE_SIGNISFICANT, 0.00004f, BasicStroke.CAP_ROUND);
            drawBiomeWay(ElementType.DRAIN, g, ThemeHelper.color("water"), DEFAULT_LEVEL_0_MINIMUM_SIZE_TO_BE_SIGNISFICANT, 0.00002f, BasicStroke.CAP_ROUND);
            drawBridge(g, ThemeHelper.color("bridge"), 0.000004f);
            drawPier(g, ThemeHelper.color("bridge"), 0.000008f);
            drawBiomeArea(ElementType.PARKING, g, ThemeHelper.color("parking"), DEFAULT_LEVEL_0_MINIMUM_SIZE_TO_BE_SIGNISFICANT);

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
            drawBiomeWay(ElementType.AIRPORT_RUNWAY, g, ThemeHelper.color("airport"), DEFAULT_LEVEL_0_MINIMUM_SIZE_TO_BE_SIGNISFICANT,0.00018f, BasicStroke.CAP_BUTT);
            drawBiomeWay(ElementType.AIRPORT_TAXIWAY, g, ThemeHelper.color("airport"), DEFAULT_LEVEL_0_MINIMUM_SIZE_TO_BE_SIGNISFICANT,0.00006f, BasicStroke.CAP_BUTT);

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
            drawAmenity(AmenityType.BAR, g);
            drawAmenity(AmenityType.NIGHT_CLUB, g);
            drawAmenity(AmenityType.FAST_FOOD, g);
            drawAmenity(AmenityType.RAILWAY_STATION, g);
            drawAmenity(AmenityType.HOSPITAL, g);
            drawAmenity(AmenityType.UNIVERSITY, g);
            drawAmenity(AmenityType.AIRPORT_AMENITY, g);
            drawAmenity(AmenityType.PARKING_AMENITY, g);
            drawAmenity(AmenityType.PLACE_OF_WORSHIP, g);
            drawAmenity(AmenityType.SPORT_AMENITY, g);
            break;
        case LEVEL_1:
            drawBiomeArea(ElementType.COMMON_LAND, g, ThemeHelper.color("commonland"), DEFAULT_LEVEL_1_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.PARK, g, ThemeHelper.color("park"), DEFAULT_LEVEL_1_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.BEACH, g, ThemeHelper.color("beach"), DEFAULT_LEVEL_1_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.FOREST, g, ThemeHelper.color("forest"), DEFAULT_LEVEL_1_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.GRASSLAND, g, ThemeHelper.color("grassland"), DEFAULT_LEVEL_1_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.GRASS, g, ThemeHelper.color("grass"), DEFAULT_LEVEL_1_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.FARMLAND, g, ThemeHelper.color("farmland"), DEFAULT_LEVEL_1_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.MEADOW, g, ThemeHelper.color("meadow"), DEFAULT_LEVEL_1_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.HEATH, g, ThemeHelper.color("heath"), DEFAULT_LEVEL_1_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.PLAYGROUND, g, ThemeHelper.color("playground"), DEFAULT_LEVEL_1_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.SPORTSPITCH, g, ThemeHelper.color("sportspitch"), DEFAULT_LEVEL_1_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.SPORTSTRACK, g, ThemeHelper.color("sportstrack"), DEFAULT_LEVEL_1_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.WATER, g, ThemeHelper.color("water"), DEFAULT_LEVEL_1_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.WETLAND, g, ThemeHelper.color("wetland"), DEFAULT_LEVEL_1_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeWay(ElementType.RIVER, g, ThemeHelper.color("water"), DEFAULT_LEVEL_1_MINIMUM_SIZE_TO_BE_SIGNISFICANT, 0.00005f, BasicStroke.CAP_ROUND);
            drawBiomeWay(ElementType.DRAIN, g, ThemeHelper.color("water"), DEFAULT_LEVEL_1_MINIMUM_SIZE_TO_BE_SIGNISFICANT, 0.00003f, BasicStroke.CAP_ROUND);
            drawBridge(g, ThemeHelper.color("bridge"), 0.000004f);
            drawPier(g, ThemeHelper.color("bridge"), 0.000008f);
            drawBiomeArea(ElementType.PARKING, g, ThemeHelper.color("parking"), DEFAULT_LEVEL_1_MINIMUM_SIZE_TO_BE_SIGNISFICANT);

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
            drawBiomeWay(ElementType.AIRPORT_RUNWAY, g, ThemeHelper.color("airport"), DEFAULT_LEVEL_1_MINIMUM_SIZE_TO_BE_SIGNISFICANT,0.00020f, BasicStroke.CAP_BUTT);
            drawBiomeWay(ElementType.AIRPORT_TAXIWAY, g, ThemeHelper.color("airport"), DEFAULT_LEVEL_1_MINIMUM_SIZE_TO_BE_SIGNISFICANT,0.00007f, BasicStroke.CAP_BUTT);
            drawRail(g, ThemeHelper.color("rail"), 0.00002f);

            drawRoadNames(g, ElementType.PRIMARY_ROAD);
            drawRoadNames(g, ElementType.TRUNK_ROAD);
            drawRoadNames(g, ElementType.MOTORWAY);

            drawCityNames(g, AmenityType.HAMLET_NAME, 0.35f);
            drawCityNames(g, AmenityType.SUBURB_NAME, 0.35f);
            drawCityNames(g, AmenityType.QUARTER_NAME, 0.35f);
            drawCityNames(g, AmenityType.NEIGHBOURHOOD_NAME, 0.35f);

            // Amenities
            drawAmenity(AmenityType.RAILWAY_STATION, g);
            drawAmenity(AmenityType.HOSPITAL, g);
            drawAmenity(AmenityType.UNIVERSITY, g);
            drawAmenity(AmenityType.AIRPORT_AMENITY, g);
            drawAmenity(AmenityType.PARKING_AMENITY, g);
            drawAmenity(AmenityType.PLACE_OF_WORSHIP, g);
            drawAmenity(AmenityType.SPORT_AMENITY, g);
            break;
        case LEVEL_2:
            drawBiomeArea(ElementType.COMMON_LAND, g, ThemeHelper.color("commonland"), DEFAULT_LEVEL_2_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.PARK, g, ThemeHelper.color("park"), DEFAULT_LEVEL_2_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.BEACH, g, ThemeHelper.color("beach"), DEFAULT_LEVEL_2_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.FOREST, g, ThemeHelper.color("forest"), DEFAULT_LEVEL_2_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.GRASSLAND, g, ThemeHelper.color("grassland"), DEFAULT_LEVEL_2_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.GRASS, g, ThemeHelper.color("grass"), DEFAULT_LEVEL_2_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.FARMLAND, g, ThemeHelper.color("farmland"), DEFAULT_LEVEL_2_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.MEADOW, g, ThemeHelper.color("meadow"), DEFAULT_LEVEL_2_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.HEATH, g, ThemeHelper.color("heath"), DEFAULT_LEVEL_2_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.WATER, g, ThemeHelper.color("water"), DEFAULT_LEVEL_2_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.WETLAND, g, ThemeHelper.color("wetland"), DEFAULT_LEVEL_2_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeWay(ElementType.RIVER, g, ThemeHelper.color("water"), DEFAULT_LEVEL_2_MINIMUM_SIZE_TO_BE_SIGNISFICANT, 0.00005f, BasicStroke.CAP_ROUND);
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
            drawBiomeWay(ElementType.AIRPORT_RUNWAY, g, ThemeHelper.color("airport"), DEFAULT_LEVEL_2_MINIMUM_SIZE_TO_BE_SIGNISFICANT,0.00024f, BasicStroke.CAP_BUTT);
            drawBiomeWay(ElementType.AIRPORT_TAXIWAY, g, ThemeHelper.color("airport"), DEFAULT_LEVEL_2_MINIMUM_SIZE_TO_BE_SIGNISFICANT,0.00008f, BasicStroke.CAP_BUTT);
            drawRail(g, ThemeHelper.color("rail"), 0.00002f);

            drawCityNames(g, AmenityType.VILLAGE_NAME, 0.35f);
            drawCityNames(g, AmenityType.HAMLET_NAME, 0.35f);
            drawCityNames(g, AmenityType.SUBURB_NAME, 0.35f);
            drawCityNames(g, AmenityType.QUARTER_NAME, 0.35f);
            drawCityNames(g, AmenityType.NEIGHBOURHOOD_NAME, 0.35f);
            break;
        case LEVEL_3:
            drawBiomeArea(ElementType.COMMON_LAND, g, ThemeHelper.color("commonland"), DEFAULT_LEVEL_3_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.PARK, g, ThemeHelper.color("park"), DEFAULT_LEVEL_3_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.BEACH, g, ThemeHelper.color("beach"), DEFAULT_LEVEL_3_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.FOREST, g, ThemeHelper.color("forest"), DEFAULT_LEVEL_3_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.GRASSLAND, g, ThemeHelper.color("grassland"), DEFAULT_LEVEL_3_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.GRASS, g, ThemeHelper.color("grass"), DEFAULT_LEVEL_3_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.FARMLAND, g, ThemeHelper.color("farmland"), DEFAULT_LEVEL_3_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.MEADOW, g, ThemeHelper.color("meadow"), DEFAULT_LEVEL_3_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.HEATH, g, ThemeHelper.color("heath"), DEFAULT_LEVEL_3_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.WATER, g, ThemeHelper.color("water"), DEFAULT_LEVEL_3_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.WETLAND, g, ThemeHelper.color("wetland"), DEFAULT_LEVEL_3_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeWay(ElementType.RIVER, g, ThemeHelper.color("water"), DEFAULT_LEVEL_2_MINIMUM_SIZE_TO_BE_SIGNISFICANT, 0.00005f, BasicStroke.CAP_ROUND);
            drawBridge(g, ThemeHelper.color("bridge"), 0.000004f);

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
            drawBiomeWay(ElementType.AIRPORT_RUNWAY, g, ThemeHelper.color("airport"), DEFAULT_LEVEL_3_MINIMUM_SIZE_TO_BE_SIGNISFICANT,0.00028f, BasicStroke.CAP_BUTT);
            drawBiomeWay(ElementType.AIRPORT_TAXIWAY, g, ThemeHelper.color("airport"), DEFAULT_LEVEL_3_MINIMUM_SIZE_TO_BE_SIGNISFICANT,0.00009f, BasicStroke.CAP_BUTT);
            drawRail(g, ThemeHelper.color("rail"), 0.00002f);

            drawCityNames(g, AmenityType.VILLAGE_NAME, 0.35f);
            drawCityNames(g, AmenityType.HAMLET_NAME, 0.35f);
            drawCityNames(g, AmenityType.SUBURB_NAME, 0.35f);
            drawCityNames(g, AmenityType.QUARTER_NAME, 0.35f);
            drawCityNames(g, AmenityType.NEIGHBOURHOOD_NAME, 0.35f);
            break;
        case LEVEL_4:
            drawBiomeArea(ElementType.COMMON_LAND, g, ThemeHelper.color("commonland"), DEFAULT_LEVEL_4_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.PARK, g, ThemeHelper.color("park"), DEFAULT_LEVEL_4_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.BEACH, g, ThemeHelper.color("beach"), DEFAULT_LEVEL_4_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.FOREST, g, ThemeHelper.color("forest"), DEFAULT_LEVEL_4_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.GRASSLAND, g, ThemeHelper.color("grassland"), DEFAULT_LEVEL_4_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.GRASS, g, ThemeHelper.color("grass"), DEFAULT_LEVEL_4_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.FARMLAND, g, ThemeHelper.color("farmland"), DEFAULT_LEVEL_4_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.MEADOW, g, ThemeHelper.color("meadow"), DEFAULT_LEVEL_4_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.HEATH, g, ThemeHelper.color("heath"), DEFAULT_LEVEL_4_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.WATER, g, ThemeHelper.color("water"), DEFAULT_LEVEL_4_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.WETLAND, g, ThemeHelper.color("wetland"), DEFAULT_LEVEL_4_MINIMUM_SIZE_TO_BE_SIGNISFICANT);

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
            drawBiomeWay(ElementType.AIRPORT_RUNWAY, g, ThemeHelper.color("airport"), DEFAULT_LEVEL_4_MINIMUM_SIZE_TO_BE_SIGNISFICANT,0.00030f, BasicStroke.CAP_BUTT);
            drawBiomeWay(ElementType.AIRPORT_TAXIWAY, g, ThemeHelper.color("airport"), DEFAULT_LEVEL_4_MINIMUM_SIZE_TO_BE_SIGNISFICANT,0.00010f, BasicStroke.CAP_BUTT);

            drawRail(g, ThemeHelper.color("rail"), 0.00002f);

            drawCityNames(g, AmenityType.CITY_NAME, 0.8f);
            drawCityNames(g, AmenityType.TOWN_NAME, 0.35f);
            drawCityNames(g, AmenityType.VILLAGE_NAME, 0.35f);
            break;
        case LEVEL_5:
            drawBiomeArea(ElementType.COMMON_LAND, g, ThemeHelper.color("commonland"), DEFAULT_LEVEL_5_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.FOREST, g, ThemeHelper.color("forest"), DEFAULT_LEVEL_5_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.GRASSLAND, g, ThemeHelper.color("grassland"), DEFAULT_LEVEL_5_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.WATER, g, ThemeHelper.color("water"), DEFAULT_LEVEL_5_MINIMUM_SIZE_TO_BE_SIGNISFICANT);

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
            drawBiomeWay(ElementType.AIRPORT_RUNWAY, g, ThemeHelper.color("airport"), DEFAULT_LEVEL_3_MINIMUM_SIZE_TO_BE_SIGNISFICANT,0.00036f, BasicStroke.CAP_BUTT);
            drawRail(g, ThemeHelper.color("rail"), 0.00002f);

            drawCityNames(g, AmenityType.CITY_NAME, 0.8f);
            drawCityNames(g, AmenityType.TOWN_NAME, 0.35f);
            break;
        case LEVEL_6:
            drawBiomeArea(ElementType.COMMON_LAND, g, ThemeHelper.color("commonland"), DEFAULT_LEVEL_6_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.FOREST, g, ThemeHelper.color("forest"), DEFAULT_LEVEL_6_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.GRASSLAND, g, ThemeHelper.color("grassland"), DEFAULT_LEVEL_6_MINIMUM_SIZE_TO_BE_SIGNISFICANT);
            drawBiomeArea(ElementType.WATER, g, ThemeHelper.color("water"), DEFAULT_LEVEL_6_MINIMUM_SIZE_TO_BE_SIGNISFICANT);

            drawPrimaryRoads(g, ThemeHelper.color("primaryRoad"), 0.0001f);
            drawPrimaryRoadLinks(g, ThemeHelper.color("primaryRoad"), 0.0001f);
            drawTrunkRoads(g, ThemeHelper.color("trunkRoad"), 0.00014f);
            drawTrunkRoadLinks(g, ThemeHelper.color("trunkRoad"), 0.00012f);
            drawMotorways(g, ThemeHelper.color("motorway"), 0.00018f);
            drawMotorwayLinks(g, ThemeHelper.color("motorway"), 0.00014f);
            drawCityNames(g, AmenityType.CITY_NAME, 1f);
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
        g2D.setStroke(new BasicStroke(Float.MIN_VALUE));
        Path2D boundary = new Path2D.Float();
        boundary.moveTo(dynMinLon, dynMinLat);
        boundary.lineTo(dynMaxLon, dynMinLat);
        boundary.lineTo(dynMaxLon, dynMaxLat);
        boundary.lineTo(dynMinLon, dynMaxLat);
        boundary.lineTo(dynMinLon, dynMinLat);
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
    }

    public Point2D toModelCoords(Point2D mousePosition) {
        try {
            return transform.inverseTransform(mousePosition, null);
        } catch (NoninvertibleTransformException e) {
            throw new RuntimeException();
        }
    }

    public void setElements(EnumMap<ElementType, KDTree> map) { elements = map; }

    public void setCurrentSection(HashSet<SuperElement> currentSection) {
        this.currentSection = currentSection;
    }

    public void setCurrentPoint(Point2D currentPoint) {
        this.currentPoint = currentPoint;
    }

    // Draw Roads Methods
    private void drawMotorways(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.MOTORWAY);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        for (SuperElement element : currentSection) {
            Road road = (Road) element;
            g.draw(road.getShape());
        }
    }
    private void drawMotorwayLinks(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.MOTORWAY_LINK);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        for (SuperElement element : currentSection) {
            Road road = (Road) element;
            g.draw(road.getShape());
        }
    }
    private void drawTrunkRoads(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.TRUNK_ROAD);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        for (SuperElement element : currentSection) {
            Road road = (Road) element;
            g.draw(road.getShape());
        }
    }
    private void drawTrunkRoadLinks(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.TRUNK_ROAD_LINK);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        for (SuperElement element : currentSection) {
            Road road = (Road) element;
            g.draw(road.getShape());
        }
    }
    private void drawPrimaryRoads(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.PRIMARY_ROAD);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        for (SuperElement element : currentSection) {
            Road road = (Road) element;
            g.draw(road.getShape());
        }
    }
    private void drawPrimaryRoadLinks(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.PRIMARY_ROAD_LINK);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        for (SuperElement element : currentSection) {
            Road road = (Road) element;
            g.draw(road.getShape());
        }
    }
    private void drawSecondaryRoads(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.SECONDARY_ROAD);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        for (SuperElement element : currentSection) {
            Road road = (Road) element;
            g.draw(road.getShape());
        }
    }
    private void drawSecondaryRoadLinks(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.SECONDARY_ROAD_LINK);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        for (SuperElement element : currentSection) {
            Road road = (Road) element;
            g.draw(road.getShape());
        }
    }
    private void drawTertiaryRoads(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.TERTIARY_ROAD);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        for (SuperElement element : currentSection) {
            Road road = (Road) element;
            g.draw(road.getShape());
        }
    }
    private void drawTertiaryRoadLinks(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.TERTIARY_ROAD_LINK);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        for (SuperElement element : currentSection) {
            Road road = (Road) element;
            g.draw(road.getShape());
        }
    }
    private void drawUnclassifiedRoads(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.UNCLASSIFIED_ROAD);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        for (SuperElement element : currentSection) {
            Road road = (Road) element;
            g.draw(road.getShape());
        }
    }
    private void drawResidentialRoads(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.RESIDENTIAL_ROAD);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        for (SuperElement element : currentSection) {
            Road road = (Road) element;
            g.draw(road.getShape());
        }
    }
    private void drawLivingStreets(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.LIVING_STREET);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        for (SuperElement element : currentSection) {
            Road road = (Road) element;
            g.draw(road.getShape());
        }
    }
    private void drawServiceRoads(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.SERVICE_ROAD);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        for (SuperElement element : currentSection) {
            Road road = (Road) element;
            g.draw(road.getShape());
        }
    }
    private void drawBusGuideways(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.BUS_GUIDEWAY);
        g.setColor(color);
        g.setStroke(new BasicStroke(width));
        for (SuperElement element : currentSection) {
            Road road = (Road) element;
            g.draw(road.getShape());
        }
    }
    private void drawEscapes(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.ESCAPE);
        g.setColor(color);
        g.setStroke(new BasicStroke(width));
        for (SuperElement element : currentSection) {
            Road road = (Road) element;
            g.draw(road.getShape());
        }
    }
    private void drawRaceways(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.RACEWAY);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        for (SuperElement element : currentSection) {
            Road road = (Road) element;
            g.draw(road.getShape());
        }
    }
    private void drawPedestrianStreets(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.PEDESTRIAN_STREET);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        for (SuperElement element : currentSection) {
            Road road = (Road)element;
            if (road.isArea()) g.fill(road.getShape());
            else g.draw(road.getShape());
        }
    }
    private void drawTracks(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.TRACK);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        for (SuperElement element : currentSection) {
            Road road = (Road) element;
            g.draw(road.getShape());
        }
    }
    private void drawSteps(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.STEPS);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] { 0.00001f }, 0.0f));
        for (SuperElement element : currentSection) {
            Road road = (Road) element;
            g.draw(road.getShape());
        }
    }
    private void drawFootways(Graphics2D g, Color color, Color areaColor, float width) {
        setCurrentSection(ElementType.FOOTWAY);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] { 0.00001f }, 0.0f));
        for (SuperElement element : currentSection) {
            Road road = (Road)element;
            if (road.isArea()) {
                g.setColor(areaColor);
                g.fill(road.getShape());
            } else {
                g.setColor(color);
                g.draw(road.getShape());
            }
        }
    }
    private void drawBridge(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.BRIDGE);
        g.setColor(color);
        g.setStroke(new BasicStroke(width));
        for (SuperElement element : currentSection) {
            ManMade manMade = (ManMade)element;
            if (manMade.isArea()) g.fill(manMade.getShape());
            else g.draw(manMade.getShape());
        }
    }
    private void drawPier(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.PIER);
        g.setColor(color);
        g.setStroke(new BasicStroke(width));
        for (SuperElement element : currentSection) {
            ManMade manMade = (ManMade)element;
            g.draw(manMade.getShape());
        }
    }
    private void drawBridleways(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.BRIDLEWAY);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] { 0.00001f }, 0.0f));
        for (SuperElement element : currentSection) {
            Road road = (Road) element;
            g.draw(road.getShape());
        }
    }
    private void drawCycleways(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.CYCLEWAY);
        g.setColor(color);
        g.setStroke(new BasicStroke(width));
        for (SuperElement element : currentSection) {
            Road road = (Road) element;
            g.draw(road.getShape());
        }
    }
    private void drawPaths(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.PATH);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] { 0.00001f }, 0.0f));
        for (SuperElement element : currentSection) {
            Road road = (Road) element;
            g.draw(road.getShape());
        }
    }
    private void drawRoads(Graphics2D g, Color color, float width) {
        setCurrentSection(ElementType.ROAD);
        g.setColor(color);
        g.setStroke(new BasicStroke(width));
        for (SuperElement element : currentSection) {
            Road road = (Road) element;
            g.draw(road.getShape());
        }
    }

    /*
     *  Draw the biome elements, which are to be drawns as an area, ie. a filled shape.
     *  @param Element type, Graphics2D, Color
     *  @param double minSizeToBeSignificant - a double telling if the biome should be drawns
     */
    private void drawBiomeArea(ElementType elementType, Graphics2D g, Color color, Double minSizeToBeSignificant){
        setCurrentSection(elementType);
        g.setColor(color);
        g.setStroke(new BasicStroke(0.00001f));
        for (SuperElement element : currentSection){
            Biome biome = (Biome)element;
            float size = biome.getShape().getSize();
            if (size > minSizeToBeSignificant) g.fill(biome.getShape());
        }
    }

    /*
     *  Draw the biome elements, which are to be drawns as ways, ie. not a filled shape.
     *  @param Element type, Graphics2D, Color
     *  @param double minSizeToBeSignificant - a double telling if the biome should be drawns
     *  @param float strokeSize - the size of the stroke used to paint the way/path
     *  @param int lineEnd - an integer which represent what type of end the way/path should have: CAP_BUTT=0, CAP_ROUND=1, CAP_SQUARE=2
     */
    private void drawBiomeWay(ElementType elementType, Graphics2D g, Color color, double minSizeToBeSignificant, float strokeSize, int lineEnd){
        setCurrentSection(elementType);
        g.setColor(color);
        g.setStroke(new BasicStroke(strokeSize, lineEnd, BasicStroke.JOIN_BEVEL));
        for (SuperElement element : currentSection){
            Biome biome = (Biome)element;
            float size = biome.getShape().getSize();
            if (size > minSizeToBeSignificant) g.draw(biome.getShape());
        }
    }

    private void drawRail(Graphics2D g, Color color, float width){
        setCurrentSection(ElementType.RAIL);
        g.setColor(color);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));

        for (SuperElement element : currentSection){
            Rail rail = (Rail) element;
            if(rail.isInTunnel()){
                Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f);
                g.setComposite(c);
            }
            g.draw(rail.getShape());
            Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
            g.setComposite(c);
        }
    }

    private void drawBuilding(Graphics2D g, Color color) {
        setCurrentSection(ElementType.BUILDING);
        Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .9f);
        g.setComposite(c);
        g.setColor(color);
        g.setStroke(new BasicStroke(0.00001f));
        for (SuperElement element : currentSection) {
            Building building = (Building)element;
            g.fill(building.getShape());
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
        Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .7f);
        g.setComposite(c);

        // Color
        g.setColor(ThemeHelper.color("roadName"));

        for (SuperElement element : currentSection) {
            Road road = (Road)element;
            g.setFont(font.deriveFont(AffineTransform.getScaleInstance(scaleFactor, scaleFactor)));
            PolygonApprox polygon = road.getShape();

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
                if(!road.isArea())drawString(road.getName(), g, drawFromX, drawFromY, font, scaleFactor,false);
                g.setTransform(old);
            }
            if(road.isArea())drawString(road.getName(), g, road.getShape().getCenterX(), road.getShape().getCenterY(), font, scaleFactor,true);
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

    private void drawCityNames(Graphics2D g, AmenityType type, float scaling) {
        setCurrentSection(ElementType.AMENITY);
        for (SuperElement element : currentSection){
            Amenity amenity = (Amenity) element;
            if(amenity.getAmenityType() == type){
                if (ZoomLevel.getZoomFactor() > -60){
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

                    g.setFont(font.deriveFont(AffineTransform.getScaleInstance(scaleFactor, scaleFactor)));
                    drawString(amenity.getName(), g, amenity.getX(), amenity.getY(), font, scaleFactor, true);

                    // Transparency off
                    c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
                    g.setComposite(c);
                }
            }
        }
    }

    /*
    Draw each character in the String, taken as a parameter, and then shift to the next letter.
    The shift will be the size of the letter just written.
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

    private void drawAmenity(AmenityType type, Graphics2D g){
        switch (type){
            case HOSPITAL:
                drawHospital(g);
                break;
            case BAR:
            case NIGHT_CLUB:
            case FAST_FOOD:
                drawNight(type, g);
                break;
            case UNIVERSITY:
                drawUniversity(g);
                break;
            case RAILWAY_STATION:
                drawRailwayStation(g);
                break;
            case PLACE_OF_WORSHIP:
                drawPlaceOfWorship(g);
                break;
            case PARKING_AMENITY:
                drawParkingAmenity(g);
                break;
            case SPORT_AMENITY:
                drawSportAmenity(g);
                break;
            case AIRPORT_AMENITY:
                drawAirportAmenity(g);
                break;
            case CITY_NAME:
            case TOWN_NAME:
            case VILLAGE_NAME:
            case HAMLET_NAME:
            case SUBURB_NAME:
            case QUARTER_NAME:
            case NEIGHBOURHOOD_NAME:

        }
    }

    private void drawNight(AmenityType type, Graphics2D g) {
        if (ThemeHelper.getCurrentTheme() == "Night") {
            if (ZoomLevel.getZoomFactor() >= 650) {
                float scaleFactor = 0.5f * (float) (Math.pow(ZoomLevel.getZoomFactor(), -2f));
                Font font = Helpers.FontAwesome.getFontAwesome();
                g.setFont(font.deriveFont(AffineTransform.getScaleInstance(scaleFactor, scaleFactor)));
                setCurrentSection(ElementType.AMENITY);
                for (SuperElement element : currentSection) {
                    Amenity amenity = (Amenity) element;
                    if (amenity.getAmenityType() == AmenityType.BAR) {
                        g.setColor(ThemeHelper.color("barName"));
                        drawString("\uf000" + "", g, amenity.getX(), amenity.getY(), font, scaleFactor, true);
                    }
                    if (amenity.getAmenityType() == AmenityType.NIGHT_CLUB) {
                        g.setColor(ThemeHelper.color("nightClubName"));
                        drawString("\uf001" + "", g, amenity.getX(), amenity.getY(), font, scaleFactor, true);
                    }
                    if (amenity.getAmenityType() == AmenityType.FAST_FOOD) {
                        g.setColor(ThemeHelper.color("fastFoodName"));
                        drawString("\uf0f5" + "", g, amenity.getX(), amenity.getY(), font, scaleFactor, true);
                    }
                }
                //Drawing names
                float deltay = ((getFontMetrics(font).getHeight() * scaleFactor) / 2);

                //Font for drawing name
                font = new Font("Arial", Font.PLAIN, 20);
                g.setFont(font.deriveFont(AffineTransform.getScaleInstance(scaleFactor, scaleFactor)));

                for (SuperElement element : currentSection) {
                    Amenity amenity = (Amenity) element;
                    if (type == AmenityType.BAR) {
                        String name = amenity.getName();
                        float x = amenity.getX() - (getFontMetrics(font).stringWidth(name) * scaleFactor / 2);
                        drawString(name, g, x, amenity.getY() + deltay, font, scaleFactor, false);
                    }
                }
            }
        }
    }

    private void drawHospital(Graphics2D g) {
        float scaleFactor;
        scaleFactor = 1.7f * (float)(Math.pow(ZoomLevel.getZoomFactor(), -2f));
        setCurrentSection(ElementType.AMENITY);

        //Color and font
        g.setColor(ThemeHelper.color("hospital"));
        Font font = Helpers.FontAwesome.getFontAwesome();
        g.setFont(font.deriveFont(AffineTransform.getScaleInstance(scaleFactor, scaleFactor)));

        for (SuperElement element : currentSection) {
            Amenity amenity = (Amenity)element;
            if(amenity.getAmenityType() == AmenityType.HOSPITAL)
            drawString("\uf0fe" + "", g, amenity.getX(), amenity.getY(), font, scaleFactor, true);
        }

        float deltay = ((getFontMetrics(font).getHeight()* scaleFactor)/2);

        //Font for drawing name
        font = new Font("Arial", Font.ITALIC, 18);
        g.setFont(font.deriveFont(AffineTransform.getScaleInstance(scaleFactor, scaleFactor)));
        for (SuperElement element : currentSection){
            Amenity amenity = (Amenity)element;
            if(amenity.getAmenityType() == AmenityType.HOSPITAL){
                String name = amenity.getName();
                float x = amenity.getX() - (getFontMetrics(font).stringWidth(name)* scaleFactor/2);
                drawString(name, g, x, amenity.getY()+deltay, font, scaleFactor, false);
            }
        }
    }

    private void drawUniversity(Graphics2D g){
        float scaleFactor;
        scaleFactor = 1.7f * (float)(Math.pow(ZoomLevel.getZoomFactor(), -2f));
        setCurrentSection(ElementType.AMENITY);

        //Color and font
        g.setColor(ThemeHelper.color("university"));
        Font font = Helpers.FontAwesome.getFontAwesome();
        g.setFont(font.deriveFont(AffineTransform.getScaleInstance(scaleFactor, scaleFactor)));

        for (SuperElement element : currentSection) {
            Amenity amenity = (Amenity)element;
            if(amenity.getAmenityType() == AmenityType.UNIVERSITY)
            drawString("\uf19c" + "", g, amenity.getX(), amenity.getY(), font, scaleFactor, true);
        }

        float deltay = ((getFontMetrics(font).getHeight()* scaleFactor)/2);

        //Font for drawing name
        font = new Font("Arial", Font.ITALIC, 18);
        g.setFont(font.deriveFont(AffineTransform.getScaleInstance(scaleFactor, scaleFactor)));
        for (SuperElement element : currentSection){
            Amenity amenity = (Amenity)element;
            if(amenity.getAmenityType() == AmenityType.UNIVERSITY){
                String name = amenity.getName();
                float x = amenity.getX() - (getFontMetrics(font).stringWidth(name)* scaleFactor/2);
                drawString(name, g, x, amenity.getY()+deltay, font, scaleFactor, false);
            }
        }
    }

    private void drawRailwayStation(Graphics2D g) {
        float scaleFactor;
        scaleFactor = 1.7f * (float)(Math.pow(ZoomLevel.getZoomFactor(), -2f));

        //Color and font
        g.setColor(ThemeHelper.color("railwayStation"));
        Font font = Helpers.FontAwesome.getFontAwesome();
        g.setFont(font.deriveFont(AffineTransform.getScaleInstance(scaleFactor, scaleFactor)));
        setCurrentSection(ElementType.AMENITY);
        for (SuperElement element : currentSection){
            Amenity amenity = (Amenity)element;
            if(amenity.getAmenityType() == AmenityType.RAILWAY_STATION || amenity.getAmenityType() == AmenityType.RAILWAY_STATION_AREA)
            drawString("\uf238" + "", g, amenity.getX(), amenity.getY(), font, scaleFactor, true);
        }

        float deltay = ((getFontMetrics(font).getHeight()* scaleFactor)/2);

        //Font for drawing name
        font = new Font("Arial", Font.BOLD, 14);
        g.setFont(font.deriveFont(AffineTransform.getScaleInstance(scaleFactor, scaleFactor)));
        for (SuperElement element : currentSection) {
            Amenity amenity = (Amenity) element;
            if (amenity.getAmenityType() == AmenityType.RAILWAY_STATION || amenity.getAmenityType() == AmenityType.RAILWAY_STATION_AREA) {
                String name = amenity.getName();
                float x = amenity.getX() - (getFontMetrics(font).stringWidth(name) * scaleFactor / 2);
                drawString(name, g, x, amenity.getY() + deltay, font, scaleFactor, false);
            }
        }
    }

    private void drawPlaceOfWorship(Graphics2D g) {
        float scaleFactor;
        scaleFactor = 1.5f * (float)(Math.pow(ZoomLevel.getZoomFactor(), -2f));

        setCurrentSection(ElementType.AMENITY);

        //Color and font
        g.setColor(ThemeHelper.color("placeOfWorship"));
        Font font = new Font("Wingdings", Font.PLAIN, 36);
        g.setFont(font.deriveFont(AffineTransform.getScaleInstance(scaleFactor, scaleFactor)));

        for (SuperElement element : currentSection) {
            Amenity amenity = (Amenity)element;
            if(amenity.getAmenityType() == AmenityType.PLACE_OF_WORSHIP){
                float y = amenity.getY();
                y += ((getFontMetrics(font).charWidth('\uf055')) / 2) * scaleFactor;
                drawString("\uf055" + "", g, amenity.getX(), y, font, scaleFactor, true);
            }
        }
    }
    private void drawParkingAmenity(Graphics2D g) {
        float scaleFactor;
        scaleFactor = 1.5f * (float)(Math.pow(ZoomLevel.getZoomFactor(), -2f));
        setCurrentSection(ElementType.AMENITY);

        // Transparency
        Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .2f);
        g.setComposite(c);

        //Color and font
        g.setColor(ThemeHelper.color("parkingAmenity"));
        Font font = new Font("Arial", Font.BOLD, 18);
        g.setFont(font.deriveFont(AffineTransform.getScaleInstance(scaleFactor, scaleFactor)));

        for (SuperElement element : currentSection) {
            Amenity amenity = (Amenity)element;
            if(amenity.getAmenityType() == AmenityType.PARKING_AMENITY){
                float y = amenity.getY();
                y += ((getFontMetrics(font).charWidth('P')) / 2) * scaleFactor;
                drawString("P", g, amenity.getX(), y, font, scaleFactor, true);
            }
        }
        // Transparency off
        c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
        g.setComposite(c);
    }

    private void drawSportAmenity(Graphics2D g) {
        float scaleFactor;
        scaleFactor = 0.75f * (float)(Math.pow(ZoomLevel.getZoomFactor(), -2f));
        setCurrentSection(ElementType.AMENITY);

        // Transparency
        Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .7f);
        g.setComposite(c);

        //Color and font
        g.setColor(ThemeHelper.color("sportAmenity"));
        Font font = Helpers.FontAwesome.getFontAwesome();
        g.setFont(font.deriveFont(AffineTransform.getScaleInstance(scaleFactor, scaleFactor)));

        for (SuperElement element : currentSection) {
            Amenity amenity = (Amenity)element;
            if(amenity.getAmenityType() == AmenityType.SPORT_AMENITY){
                float y = amenity.getY();
                y += ((getFontMetrics(font).charWidth('\uf1e3')) / 2) * scaleFactor;
                drawString("\uf1e3" + "", g, amenity.getX(), y, font, scaleFactor, true);
            }
        }
        // Transparency off
        c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
        g.setComposite(c);
    }

    private void drawAirportAmenity(Graphics2D g) {
        float scaleFactor;
        scaleFactor = 3f * (float)(Math.pow(ZoomLevel.getZoomFactor(), -2f));
        setCurrentSection(ElementType.AMENITY);

        //Color and font
        g.setColor(ThemeHelper.color("airportAmenity"));
        Font font = Helpers.FontAwesome.getFontAwesome();
        g.setFont(font.deriveFont(AffineTransform.getScaleInstance(scaleFactor, scaleFactor)));

        for (SuperElement element : currentSection) {
            Amenity amenity = (Amenity)element;
            if(amenity.getAmenityType() == AmenityType.AIRPORT_AMENITY)
            drawString("\uf072" + "", g, amenity.getX(), amenity.getY(), font, scaleFactor, true);
        }

        float deltay = ((getFontMetrics(font).getHeight()* scaleFactor)/2);

        //Font for drawing name
        font = new Font("Arial", Font.ITALIC, 20);
        g.setFont(font.deriveFont(AffineTransform.getScaleInstance(scaleFactor, scaleFactor)));
        for (SuperElement element : currentSection){
            Amenity amenity = (Amenity)element;
            if(amenity.getAmenityType() == AmenityType.AIRPORT_AMENITY){
                String name = amenity.getName();
                float x = amenity.getX() - (getFontMetrics(font).stringWidth(name)* scaleFactor/2);
                drawString(name, g, x, amenity.getY()+deltay, font, scaleFactor, false);
            }
        }
    }


    public void panToPoint(Point2D point){
        Rectangle2D rectangle = getVisibleRect();
        Point2D midpoint = toModelCoords(new Point2D.Double(rectangle.getCenterX(), rectangle.getCenterY()));
        Point2D pointToMove;
        Point2D pointToStart;
        try {
            pointToMove = transform.createInverse().inverseTransform(point, null);
            pointToStart = transform.createInverse().inverseTransform(midpoint, null);
            pan(pointToStart.getX() - pointToMove.getX(), pointToStart.getY() - pointToMove.getY());
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }
    }

    public void setPOIs(ArrayList<POI> poiList){
        this.poiList = poiList;
    }

    private void drawPOI(Graphics2D g) {
        //Calculation of the scalefactor used to derive the font
        float scaleFactor;
        scaleFactor = 960000f*(float) (Math.pow(ZoomLevel.getZoomFactor(), -4.0f));
        if(ZoomLevel.getZoomFactor() < 400) //0.000000015625
            scaleFactor = 2400f*(float) (Math.pow(ZoomLevel.getZoomFactor(), -3.0f));
        if(ZoomLevel.getZoomFactor() < 200) //0.0003
            scaleFactor = 0.003f * (20f) / (float) ZoomLevel.getZoomFactor();
        if(ZoomLevel.getZoomFactor() < 50)
            scaleFactor = 0.0012f;

        //Color and font
        g.setColor(ThemeHelper.color("poi"));
        Font font = Helpers.FontAwesome.getFontAwesome();
        g.setFont(font.deriveFont(AffineTransform.getScaleInstance(scaleFactor, scaleFactor)));

        for (POI poi : poiList) {
            drawString("\uf276" + "", g, (float) poi.getX(), (float)poi.getY(), font, scaleFactor, true);
        }
    }
}