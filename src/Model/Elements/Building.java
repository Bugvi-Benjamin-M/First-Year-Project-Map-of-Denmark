package Model.Elements;

import java.awt.*;
import java.awt.geom.Path2D;

/**
 * Created by Nik on 11/04/17.
 */
public class Building extends Element{
    private String name;

    public Building(Shape shape, String name){
        super(shape);
        this.name = name;
    }

    public Building(Shape shape){
        this(shape, "");
    }

    public String getName() {
        return name;
    }
}
