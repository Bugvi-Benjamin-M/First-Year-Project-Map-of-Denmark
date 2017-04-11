package Model.Elements;

import java.awt.*;
import java.awt.geom.Path2D;

/**
 * Created by Nik on 04/04/17.
 */
public class Water extends Element{
    private String name;

    public Water(Shape shape, String name){
        super(shape);
        this.name = name;
    }
    public Water(Path2D shape){
        this(shape, "");
    }

    public String getName() {
        return name;
    }
}
