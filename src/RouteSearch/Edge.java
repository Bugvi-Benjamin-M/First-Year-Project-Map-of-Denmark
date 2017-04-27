package RouteSearch;

/**
 * Class details:
 *
 * Each Edge object should only fill approx. 32 bytes:
 *  12 bytes overhead + 4 byte for int + 4 byte for int +
 *  4 byte for int + 8 byte for double = 32 bytes
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 27-04-2017
 */
public class Edge implements Comparable<Edge>{

    private final int from;
    private final int to;
    private final int speed;
    private final double length;

    public Edge(int from, int to, int speed, double length) {
        if (from < 0) throw new IllegalArgumentException("node index must be nonnegative");
        if (to < 0) throw new IllegalArgumentException("node index must be nonnegative");
        if (Double.isNaN(length)) throw new IllegalArgumentException("length is NaN");
        else if (length < 0) throw new IllegalArgumentException("length must be nonnegative");
        this.from = from;
        this.to = to;
        this.speed = speed;
        this.length = length;
    }

    public double weight() {
        return length;
    }

    public int either() {
        return from;
    }

    public int other(int node) {
        if      (node == from)  return to;
        else if (node == to)    return from;
        else throw new IllegalArgumentException("Not an endpoint");
    }

    @Override
    public int compareTo(Edge that) {
        return Double.compare(this.length, that.length);
    }

    @Override
    public String toString() {
        return String.format("%d-%d %.5f (%d)",from,to,length,speed);
    }
}
