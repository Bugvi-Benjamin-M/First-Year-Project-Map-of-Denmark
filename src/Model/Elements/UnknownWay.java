package Model.Elements;

import java.awt.geom.Path2D;


public class UnknownWay extends Element {

    private Path2D path;

    public UnknownWay(Path2D path) {
        super(path);
        this.path = path;
    }

    public Path2D getPath() { return path; }
}
