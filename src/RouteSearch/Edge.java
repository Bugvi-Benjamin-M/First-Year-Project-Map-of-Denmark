package RouteSearch;

import Enums.TravelType;

import java.io.Serializable;

/**
 * Class details:
 *
 * Each Edge object should only fill approx. 32 bytes:
 *  12 bytes overhead + 4 byte for int + 4 byte for int + 4 byte for int +
 *  4 byte for float + 1 byte for byte value = 29 bytes (w/ 3 byte filler)
 *
 * @author Andreas Blanke, blan@itu.dk
 * @author Niclas Hedam, nhed@itu.dk
 * @version 27-04-2017
 */
public class Edge implements Comparable<Edge>, Serializable {

    private final long from;
    private final long to;
    private final int speed;
    private final float length;
    private final byte travelType;

    /**
     *
     * @param from
     * @param to
     * @param speed
     * @param length
     * @param type
     */
    public Edge(long from, long to, int speed, float length, byte type) {
        if (from < 0) throw new IllegalArgumentException("node index "+from+" must be nonnegative");
        if (to < 0) throw new IllegalArgumentException("node index "+to+" must be nonnegative");
        if (Double.isNaN(length)) throw new IllegalArgumentException("length is NaN");
        else if (length < 0) throw new IllegalArgumentException("length must be nonnegative");
        this.from = from;
        this.to = to;
        this.speed = speed;
        this.length = length;
        this.travelType = type;
    }

    /**
     * Returns the weight of this edge, e.g. the consumption of travelling down this edge
     */
    public double weight(TravelType type) {
        // TODO: Update to be able to calculate based on time needed to travel this edge
        if(type == TravelType.VEHICLE && !getTravelTypeName(this.travelType).contains("DRIVE")){
            return Double.POSITIVE_INFINITY;
        }
        if(type == TravelType.BICYCLE && !getTravelTypeName(this.travelType).contains("CYCLE")){
            return Double.POSITIVE_INFINITY;
        }
        if(type == TravelType.WALK && !getTravelTypeName(this.travelType).contains("WALK")){
            return Double.POSITIVE_INFINITY;
        }
        return length;
    }

    /**
     * Returns either one of the two nodes (quite often the start node)
     */
    public long either() {
        return from;
    }

    /**
     * Returns the reference to the other node than the one given
     * @param node One of the two nodes in this Edge
     */
    public long other(long node) {
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
        return String.format("%d-%d %.5f (%d) - travel: %s",from,to,length,speed,getTravelTypeName(travelType));
    }

    public static byte getTravelTypeValue(boolean canWalk, boolean canCycle, boolean canDrive, boolean isOneway) {
        byte travel = 0;
        if (canWalk) travel += 1;
        if (canCycle) travel += 2;
        if (canDrive) travel += 4;
        if (isOneway) travel += 8;
        return travel;
    }

    public static String getTravelTypeName(byte type) {
        switch (type) {
            case 1:
                return "WALK_ONLY";
            case 2:
                return "CYCLE_ONLY";
            case 3:
                return "WALK&CYCLE";
            case 4:
                return "DRIVE";
            case 5:
                return "DRIVE&WALK";
            case 6:
                return "DRIVE&CYCLE";
            case 7:
                return "ALL_ALLOWED";
            case 8:
                return "ONEWAY N/A";
            case 9:
                return "ONEWAYWALK";
            case 10:
                return "ONEWAYCYCLE";
            case 11:
                return "ONEWALKCYCLE";
            case 12:
                return "ONEWAYDRIVE";
            case 13:
                return "ONEDRIVEWALK";
            case 14:
                return "ONEDRIVECYCLE";
            case 15:
                return "ONEWAY_ALLALLOWED";
            default:
                return "N/A";
        }
    }

}
