package Model;

import Enums.*;

import java.awt.geom.Path2D;

/**
 * Created by Nik on 10/03/17.
 */
public class UnknownWay implements Element{
    private Path2D path;
    private DrawType drawType;

    public UnknownWay(Path2D path){
        this.path = path;
        drawType = DrawType.DRAW;
    }

    public Path2D getPath() {
        return path;
    }

    @Override
    public DrawType getDrawType() {
        return drawType;
    }
}
