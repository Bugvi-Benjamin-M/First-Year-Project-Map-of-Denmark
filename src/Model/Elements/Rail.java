package Model.Elements;

import Helpers.Shapes.PolygonApprox;

public class Rail extends Element {
    private boolean isInTunnel;

    /**
     * Constructs a new rail
     */
    public Rail(PolygonApprox polygon, boolean isInTunnel){
        super(polygon);
        this.isInTunnel = isInTunnel;
    }

    /**
     * Gets the shape of the rail line
     */
    public PolygonApprox getShape() { return (PolygonApprox)super.getShape(); }

    /**
     * Whether the railways is in a tunnel
     */
    public boolean isInTunnel() {
        return isInTunnel;
    }
}
