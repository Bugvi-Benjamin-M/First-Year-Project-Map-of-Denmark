package Model.Elements;

import java.awt.geom.Path2D;


public class UnknownWay extends Element {

    private Path2D path;

    /**
     * Constructs a new way, that are of an unknwon type
     */
    public UnknownWay(Path2D path) {
        super(path);
        this.path = path;
    }

    /**
     * Returns the way path.
     */
    public Path2D getPath() { return path; }
}
