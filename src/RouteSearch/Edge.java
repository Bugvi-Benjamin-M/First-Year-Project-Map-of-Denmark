package RouteSearch;

import Enums.TravelType;

/**
 * Class details:
 *
 * Each Edge object should only fill approx. 32 bytes:
 *  12 bytes overhead + 4 byte for int + 4 byte for int + 4 byte for int +
 *  4 byte for float + 4 bytes for Enum reference = 32 bytes
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 27-04-2017
 */
public class Edge implements Comparable<Edge>{

    private final int from;
    private final int to;
    private final int speed;
    private final float length;
    private final TravelType travel;

    /**
     *
     * @param from
     * @param to
     * @param speed
     * @param length
     * @param type
     */
    public Edge(int from, int to, int speed, float length, TravelType type) {
        if (from < 0) throw new IllegalArgumentException("node index must be nonnegative");
        if (to < 0) throw new IllegalArgumentException("node index must be nonnegative");
        if (Double.isNaN(length)) throw new IllegalArgumentException("length is NaN");
        else if (length < 0) throw new IllegalArgumentException("length must be nonnegative");
        this.from = from;
        this.to = to;
        this.speed = speed;
        this.length = length;
        this.travel = type;
    }

    /**
     * Returns the weight of this edge, e.g. the consumption of travelling down this edge
     */
    public double weight() {
        // TODO: Update to be able to calculate based on time needed to travel this edge
        return length;
    }

    /**
     * Returns either one of the two nodes (quite often the start node)
     */
    public int either() {
        return from;
    }

    /**
     * Returns the reference to the other node than the one given
     * @param node One of the two nodes in this Edge
     */
    public int other(int node) {
        if      (node == from)  return to;
        else if (node == to)    return from;
        else throw new IllegalArgumentException("Not an endpoint");
    }

    /**
     * Compares the length of this edge to another edge
     * @param that The other Edge object
     * @return If that Edge is smaller then -1, otherwise if greater than +1, and if same then 0
     */
    @Override
    public int compareTo(Edge that) {
        return Double.compare(this.length, that.length);
    }

    /**
     * Returns a string representation of this Edge
     */
    @Override
    public String toString() {
        return String.format("%d-%d %.5f (%d) - travel: %e",from,to,length,speed,travel);
    }
}
