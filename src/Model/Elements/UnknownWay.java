package Model.Elements;

import Model.Elements.Element;
import Model.Model;

import java.awt.geom.Path2D;

/**
 * Created by Nik on 10/03/17.
 */
public class UnknownWay extends Element {
    private Path2D path;

    public UnknownWay(Path2D path){
        super(path);
        this.path = path;
    }

    public Path2D getPath() {
        return path;
    }
}
