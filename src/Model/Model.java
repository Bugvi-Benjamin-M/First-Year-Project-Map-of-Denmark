package Model;
import Enums.BoundType;
import Enums.OSMEnums.NodeType;
import Enums.OSMEnums.RelationType;
import Enums.OSMEnums.WayType;
import Enums.ZoomLevel;
import KDtree.KDTree;
import Model.Coastlines.CoastlineFactory;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Observable;

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

    private BST bst;
    private CoastlineFactory coastlineFactory;

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
        bst = new BST();
        coastlineFactory = Helpers.FileHandler.loadCoastlines();
    }

    public BST getBst(){
        return bst;
    }

    public CoastlineFactory getCoastlineFactory() {return coastlineFactory;}

    public static Model getInstance() {
        if(instance == null) {
            instance = new Model();
        }
        return instance;
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

    @Deprecated
    public void setBounds(float minLatitude, float maxLatitude, float minLongitude, float maxLongitude) {
        bounds.put(BoundType.MIN_LONGITUDE,minLongitude);
        bounds.put(BoundType.MAX_LONGITUDE,maxLongitude);
        bounds.put(BoundType.MIN_LATITUDE,minLatitude);
        bounds.put(BoundType.MAX_LATITUDE,maxLatitude);
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
