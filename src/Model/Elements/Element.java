package Model.Elements;

import Model.Model;

import java.awt.*;

/**
 * Created by Jakob on 06-03-2017.
 */
public abstract class Element {

    private Shape shape;

    Element(Shape shape){
        this.shape = shape;
    }

    public Shape getShape(){
        return shape;
    }

}
