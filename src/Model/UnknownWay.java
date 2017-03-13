package Model;

import Enums.*;

import java.awt.geom.Path2D;

/**
 * Created by Nik on 10/03/17.
 */
public class UnknownWay extends Element{
    private Path2D path;

    public UnknownWay(Path2D path){
        this.path = path;
        drawType = DrawType.DRAW;
    }

    public Path2D getPath() {
        return path;
    }
}
