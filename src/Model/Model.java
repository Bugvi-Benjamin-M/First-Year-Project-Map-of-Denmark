package Model;
import Enums.OSMEnums.NodeType;
import Enums.OSMEnums.RelationType;
import Enums.OSMEnums.WayType;
import Model.Coastlines.Coastline;
import Model.Coastlines.CoastlineFactory;

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

    private static Model instance;

    private float minLatitude, maxLatitude, minLongitude, maxLongitude;

    private Model(){
        wayElements = new EnumMap<>(WayType.class);
        for (WayType type : WayType.values()) {
            wayElements.put(type, new ArrayList<>());
        }
    }

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

    public void clear() {
        for(WayType type : WayType.values()){
            wayElements.get(type).clear();
        }
    }

    public void modelHasChanged(){
        setChanged();
        notifyObservers();
    }

    public void setBounds(float minLatitude, float maxLatitude, float minLongitude, float maxLongitude) {
        this.minLatitude = minLatitude;
        this.maxLatitude = maxLatitude;
        this.minLongitude = minLongitude;
        this.maxLongitude = maxLongitude;
    }

    public float getMinLatitude() {
        return minLatitude;
    }

    public float getMaxLatitude() {
        return maxLatitude;
    }

    public float getMinLongitude() {
        return minLongitude;
    }

    public float getMaxLongitude() {
        return maxLongitude;
    }

    public void resetInstance() {
        instance = null;
    }
}
