package Model.Elements;


import java.awt.*;


public abstract class Element extends SuperElement {

    private Shape shape;

    /**
     * Constructs a new generic element
     */
    public Element(Shape shape) { this.shape = shape; }

    /**
     * Returns the shape of the generic element
     */
    public Shape getShape() { return shape; }

}
