package Model;
import Enums.BoundType;
import Enums.OSMEnums.NodeType;
import Enums.OSMEnums.RelationType;
import Enums.OSMEnums.WayType;
import Enums.ZoomLevel;
import Helpers.Utilities.DebugWindow;
import KDtree.KDTree;
import Model.Coastlines.CoastlineFactory;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.*;

/**
 * Created by Jakob on 06-03-2017.
 */
public final class Model extends Observable {

    private EnumMap<WayType, List<Element>> wayElements;
    private EnumMap<NodeType, List<Element>> nodeElements;
    private EnumMap<RelationType, List<Element>> relationElements;

    private EnumMap<ZoomLevel, KDTree> roads;

    private static Model instance;
    private ArrayList<Point2D> medianpoints = new ArrayList<>();

    private CoastlineFactory coastlineFactory;
    private ZoomLevel zoom_level;

    private EnumMap<BoundType, Float> bounds;

    private Model(){
        bounds = new EnumMap<>(BoundType.class);
        for (BoundType type: BoundType.values()) {
            bounds.put(type,0.0f);
        }
        wayElements = new EnumMap<>(WayType.class);
        for (WayType type : WayType.values()) {
            wayElements.put(type, new ArrayList<>());
        }

        roads = new EnumMap<>(ZoomLevel.class);
        for(ZoomLevel level : ZoomLevel.values()) {
            roads.put(level, new KDTree());
        }
        //Todo remember to clean up the constructor
        zoom_level = ZoomLevel.LEVEL_3;
        coastlineFactory = Helpers.FileHandler.loadCoastlines();
    }

    public void changeZoomLevel(double zoom_factor) {
        ZoomLevel.setZoomFactor(zoom_factor);
        zoom_level = ZoomLevel.getZoomLevel();
        DebugWindow.getInstance().setZoomLabel();
        DebugWindow.getInstance().setZoomFactorLabel();
    }

    public ZoomLevel getZoomLevel() {return zoom_level;}

    public static Model getInstance() {
        if(instance == null) {
            instance = new Model();
        }
        return instance;
    }

    public EnumMap<ZoomLevel, KDTree> getRoads() {
        return roads;
    }

    public void addWayElement(WayType type, Element element){
        wayElements.get(type).add(element);
    }

    public EnumMap<WayType, List<Element>> getWayElements(){
        return wayElements;
    }

    public List<Path2D> getCoastlines() {
        return coastlineFactory.getCoastlinePolygons();
    }

    public void loadFromCoastlines() {
        coastlineFactory.setLongitudeFactor();
        float lonfactor = coastlineFactory.getLongitudeFactor();
        this.setBound(BoundType.MIN_LONGITUDE, coastlineFactory.getBound(BoundType.MIN_LONGITUDE)*lonfactor);
        this.setBound(BoundType.MAX_LONGITUDE, coastlineFactory.getBound(BoundType.MAX_LONGITUDE)*lonfactor);
        this.setBound(BoundType.MIN_LATITUDE, coastlineFactory.getBound(BoundType.MIN_LATITUDE));
        this.setBound(BoundType.MAX_LATITUDE, coastlineFactory.getBound(BoundType.MAX_LATITUDE));
        DebugWindow.getInstance().setLongitudeLabel();
        DebugWindow.getInstance().setLatitudeLabel();
    }

    public void clear() {
        for(WayType type : WayType.values()){
            wayElements.get(type).clear();
        }
    }

    public void modelHasChanged(){
        setChanged();
        notifyObservers();
    }

    public void setBound(BoundType type, float value) {
        bounds.put(type,value);
    }

    public float getMinLatitude() {
        return bounds.get(BoundType.MIN_LATITUDE);
    }

    public float getMaxLatitude() {
        return bounds.get(BoundType.MAX_LATITUDE);
    }

    public float getMinLongitude() {
        return bounds.get(BoundType.MIN_LONGITUDE);
    }

    public float getMaxLongitude() {
        return bounds.get(BoundType.MAX_LONGITUDE);
    }

    public ArrayList<Point2D> getMedianpoints() {
        return medianpoints;
    }

    public void addMedianPoints(Double longitude,Double latitude){
        medianpoints.add(new Point2D.Double(longitude, latitude));
    }

    public void resetInstance() {
        instance = null;
    }
}
