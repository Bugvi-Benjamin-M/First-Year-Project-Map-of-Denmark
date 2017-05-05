package Model;
import Enums.BoundType;
import Enums.OSMEnums.ElementType;
import Helpers.Utilities.DebugWindow;
import KDtree.*;
import Model.Addresses.TenarySearchTrie;
import Model.Coastlines.CoastlineFactory;
import Model.Elements.Road;
import RouteSearch.Graph;
import RouteSearch.GraphFactory;
import Model.Elements.POI;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.*;

/**
 * Created by Jakob on 06-03-2017.
 */
public final class Model extends Observable {

    private EnumMap<ElementType, KDTree> elements;

    private TenarySearchTrie tst;

    private static Model instance;
    private ArrayList<Point2D> medianpoints = new ArrayList<>();

    private CoastlineFactory coastlineFactory;
    private GraphFactory graphFactory;

    private EnumMap<BoundType, Float> bounds;
    private EnumMap<BoundType, Float> dynamicBounds;
    private EnumMap<BoundType, Float> camera_bounds;
    private HashMap<String, Integer> cityToIndex;
    private HashMap<Integer, String> indexToCity;
    private ArrayList<POI> pointsOfInterest;

    private Model()
    {
        bounds = new EnumMap<>(BoundType.class);
        dynamicBounds = new EnumMap<>(BoundType.class);
        camera_bounds = new EnumMap<>(BoundType.class);
        cityToIndex = new HashMap<>();
        indexToCity = new HashMap<>();
        for (BoundType type : BoundType.values()) {
            bounds.put(type, 0.0f);
            camera_bounds.put(type, 0.0f);
        }

        elements = new EnumMap<>(ElementType.class);
        for (ElementType type : ElementType.values()) {
            elements.put(type, new KDTree());
        }
        coastlineFactory = Helpers.FileHandler.loadCoastlines();
        pointsOfInterest = new ArrayList<>();
    }

    public static Model getInstance()
    {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }

    public GraphFactory getGraphFactory(){
        return this.graphFactory;
    }

    public Graph getGraph() {
        if (graphFactory == null) throw new NullPointerException("The graph has not been initialized");
        else return graphFactory.getGraph();
    }

    public void setGraph(Graph graph, List<Road> roads) {
        if (graph == null) throw new IllegalArgumentException("Graph object must not be null");
        if (graphFactory == null) {
            graphFactory = new GraphFactory(graph,roads);
        } else {
            graphFactory.setGraph(graph);
        }
    }

    public String getIndexToCity(int index){
        return indexToCity.get(index);
    }

    public int getCityToIndex(String cityName){
        return cityToIndex.get(cityName);
    }

    public boolean cityEntryExists(String cityName){
        return cityToIndex.containsKey(cityName);
    }

    public void putCityToIndex(String cityName, int i){
            cityToIndex.put(cityName, i);
            indexToCity.put(i, cityName);
    }

    public HashMap<String, Integer> getCityToIndexMap(){
        return cityToIndex;
    }

    public void setCityToIndexMap(HashMap<String, Integer> map){
        this.cityToIndex = map;
    }

    public HashMap<Integer, String> getIndexToCityMap(){
        return indexToCity;
    }

    public void setIndexToCityMap(HashMap<Integer, String> map){
        this.indexToCity = map;
    }

    public EnumMap<ElementType, KDTree> getElements() { return elements; }

    public KDTree getElements(ElementType type) {return elements.get(type);}

    public void setElements(EnumMap<ElementType, KDTree> elements)
    {
        this.elements = elements;
    }

    public void addWayElement(ElementType type, Pointer pointer)
    {
        elements.get(type).putPointer(pointer);
    }

    public List<Path2D> getCoastlines()
    {
        return coastlineFactory.getCoastlinePolygons();
    }

    public TenarySearchTrie getTst() { return tst; }

    public void setTst(TenarySearchTrie tst) { this.tst = tst; }

    public void loadFromCoastlines()
    {
        coastlineFactory.setLongitudeFactor();
        float lonfactor = coastlineFactory.getLongitudeFactor();
        this.setBound(BoundType.MIN_LONGITUDE,
            coastlineFactory.getBound(BoundType.MIN_LONGITUDE) * lonfactor);
        this.setBound(BoundType.MAX_LONGITUDE,
            coastlineFactory.getBound(BoundType.MAX_LONGITUDE) * lonfactor);
        this.setBound(BoundType.MIN_LATITUDE,
            coastlineFactory.getBound(BoundType.MIN_LATITUDE));
        this.setBound(BoundType.MAX_LATITUDE,
            coastlineFactory.getBound(BoundType.MAX_LATITUDE));
        DebugWindow.getInstance().setLongitudeLabel();
        DebugWindow.getInstance().setLatitudeLabel();
    }

    public void clear()
    {
        for (ElementType type : ElementType.values()) {
            KDTree tree = elements.get(type);
            if (tree != null) tree.clear();
        }
    }

    public void modelHasChanged()
    {
        setChanged();
        notifyObservers();
    }

    public void setBound(BoundType type, float value)
    {
        bounds.put(type, value);
        dynamicBounds.put(type, value);
    }

    public void setDynamicBound(BoundType type, float value)
    {
        dynamicBounds.put(type, value);
    }

    public void setCameraBound(BoundType type, float value)
    {
        camera_bounds.put(type, value);
    }

    public float getCameraBound(BoundType type)
    {
        return camera_bounds.get(type);
    }

    public float getMinLatitude(boolean dynamic)
    {
        if (dynamic)
            return dynamicBounds.get(BoundType.MIN_LATITUDE);
        else
            return bounds.get(BoundType.MIN_LATITUDE);
    }

    public float getMaxLatitude(boolean dynamic)
    {
        if (dynamic)
            return dynamicBounds.get(BoundType.MAX_LATITUDE);
        else
            return bounds.get(BoundType.MAX_LATITUDE);
    }

    public float getMinLongitude(boolean dynamic)
    {
        if (dynamic)
            return dynamicBounds.get(BoundType.MIN_LONGITUDE);
        else
            return bounds.get(BoundType.MIN_LONGITUDE);
    }

    public float getMaxLongitude(boolean dynamic)
    {
        if (dynamic)
            return dynamicBounds.get(BoundType.MAX_LONGITUDE);
        else
            return bounds.get(BoundType.MAX_LONGITUDE);
    }

    public ArrayList<Point2D> getMedianpoints() { return medianpoints; }

    public void addMedianPoints(Double longitude, Double latitude)
    {
        medianpoints.add(new Point2D.Double(longitude, latitude));
    }

    public void resetInstance() { instance = null; }

    public CoastlineFactory getCoastlineFactory() { return coastlineFactory; }

    public ArrayList<POI> getPointsOfInterest() {
        return pointsOfInterest;
    }

    public void setPointsOfInterest(ArrayList<POI> pointsOfInterest) {
        this.pointsOfInterest = pointsOfInterest;
    }

    public void addPOI(float x, float y, String description){
        pointsOfInterest.add(new POI(x, y, description));
    }

    public void removePOI(int index){
        pointsOfInterest.remove(index);
    }

    public void removeAllPOI(){
        pointsOfInterest.clear();
    }
}
