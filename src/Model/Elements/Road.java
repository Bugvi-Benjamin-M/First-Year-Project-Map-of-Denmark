package Model.Elements;

import Model.Elements.Element;
import Model.Model;

import java.awt.geom.Path2D;

/**
 * Created by Jakob on 06-03-2017.
 */
public class Road extends Element {
    private String name;

    public Road(Path2D path, String name){
        super(path);
        this.name = name;
    }
    public Road(Path2D path){
        this(path, "");
    }

    public String getName() {
        return name;
    }

}
