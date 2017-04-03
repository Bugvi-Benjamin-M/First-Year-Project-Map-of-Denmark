package Model;

import java.awt.geom.Path2D;

/**
 * Created by Jakob on 06-03-2017.
 */
public class Road implements Element {
    private String name;
    private Path2D path;

    public Road(Path2D path, String name){
        this.name = name;
        this.path = path;
    }
    public Road(Path2D path){
        this(path, "");
    }

    public String getName() {
        return name;
    }

    public Path2D getPath(){
        return path;
    }
}
