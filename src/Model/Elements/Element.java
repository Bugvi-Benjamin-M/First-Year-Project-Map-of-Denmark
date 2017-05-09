package Model.Elements;

import Model.Model;

import java.awt.*;
import java.io.Serializable;

/**
 * Created by Jakob on 06-03-2017.
 */
public abstract class Element extends SuperElement {

    private Shape shape;

    public Element(Shape shape) { this.shape = shape; }

    public Shape getShape() { return shape; }

    public void setShape(Shape shape) {this.shape = shape;}
}
