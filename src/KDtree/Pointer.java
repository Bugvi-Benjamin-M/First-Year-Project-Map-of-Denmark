package KDtree;

import Model.Elements.Element;

/**
 * Created by Jakob on 30-03-2017.
 */
public class Pointer extends Point {
    private Element element;

    public Pointer(float x, float y, Element element)
    {
        super(x, y);
        this.element = element;
    }

    public Element getElement() { return element; }
}
