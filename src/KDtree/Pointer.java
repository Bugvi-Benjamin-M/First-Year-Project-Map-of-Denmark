package KDtree;

import Model.Elements.SuperElement;


public class Pointer extends Point {
    private SuperElement element;

    public Pointer(float x, float y, SuperElement element)
    {
        super(x, y);
        this.element = element;
    }

    public SuperElement getElement() { return element; }
}
