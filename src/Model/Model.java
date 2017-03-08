package Model;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by Jakob on 06-03-2017.
 */
public final class Model extends Observable {
    private List<Road> roads;
    private List<Shape> unknown;
    private static Model model;
    private float minLatitude;
    private float maxLatitude;
    private float minLongitude;
    private float maxLongitude;


    private Model(){
        roads = new ArrayList<>();
        unknown = new ArrayList<>();
    }

    public static Model getInstance() {
        if(model == null) {
            model = new Model();
        }
        return model;
    }

    public void addRoad(Road road){
        roads.add(road);
        modelHasChanged();
    }

    public List<Shape> getRoads(){
        List<Shape> roads = new ArrayList<>();
        for(Road road : this.roads){
            roads.add(road.getPath());
        }
        return roads;
    }

    public void addUnknown(Shape shape){
        unknown.add(shape);
        modelHasChanged();
    }

    public List<Shape> getUnknown(){
        return unknown;
    }

    public void clear() {
        roads.clear();
        unknown.clear();
        modelHasChanged();
    }

    private void modelHasChanged(){
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
}
