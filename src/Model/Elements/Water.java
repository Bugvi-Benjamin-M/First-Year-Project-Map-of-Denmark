package Model.Elements;

import java.awt.geom.Path2D;

/**
 * Created by Nik on 04/04/17.
 */
public class Water extends Element{
    private String name;

    public Water(Path2D path, String name){
        super(path);
        this.name = name;
    }
    public Water(Path2D path){
        this(path, "");
    }

    public String getName() {
        return name;
    }
}
