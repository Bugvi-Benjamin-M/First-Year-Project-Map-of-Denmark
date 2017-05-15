package Model.Elements;


import java.awt.*;


public abstract class Element extends SuperElement {

    private Shape shape;

    public Element(Shape shape) { this.shape = shape; }

    public Shape getShape() { return shape; }

}
