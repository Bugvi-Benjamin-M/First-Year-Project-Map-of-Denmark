package KDtree;

import Model.Elements.Element;
import Model.Elements.SuperElement;

/**
 * Created by Jakob on 30-03-2017.
 */
public class Pointer extends Point {
    private SuperElement element;

    public Pointer(float x, float y, SuperElement element)
    {
        super(x, y);
        this.element = element;
    }

    public SuperElement getElement() { return element; }
}
