package RouteSearch;

import java.util.*;

/**
 * Class details:
 * LongToIntMap is a data structure capable of linking a collection
 * of integers to a collection of longs. Note that deleting from
 * LongToIntMaps is not possible.
 *
 * The amortized time of inserting long values into the map is linear
 * while the worst case of getting ints from a value is also linear.
 *
 * The time required for retrieving array representation of the integer
 * or long values is always linear.
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 27-04-2017
 */
public class LongToIntMap {

    private final static int DEFAULT_SIZE = 10;

    private int[] ints;
    private long[] longs;
    private int N;

    /**
     * Creates a LongToIntMap with the initial size of 10
     */
    public LongToIntMap() {
        this(DEFAULT_SIZE);
    }

    /**
     * Creates a LongToIntMap with an initial size
     * @param size A specified initial size of the data structure
     */
    public LongToIntMap(int size) {
        if (size < 0) throw new IllegalArgumentException("size has to be a nonnegative number");
        ints = new int[size];
        longs = new long[size];
        N = 0;
    }

    /**
     * Inserts a value if it does not already exist in the map.
     * Might resize during insertion and therefore take more time to complete.
     * Amortized time: O(N)
     * @param value a long value to be inserted into the map
     */
    public void insert(long value) {
        int id = getInt(value);
        if (id != -1) throw new IllegalArgumentException("value is already associated with the id "+id);

        if (N == ints.length) {
            // resize arrays
            resize(N*2);
        }

        ints[N] = N;
        longs[N] = value;
        N++;
    }

    /**
     * Resizes the arrays containing keys and values
     * Runtime: O(N^3)
     * @param size the new size of the arrays
     */
    private void resize(int size) {
        if (size < N) throw new IllegalArgumentException("size has to be equal to or greater than "+N);
        int[] keyCarry = intsToArray();
        long[] valueCarry = longsToArray();
        ints = new int[size];
        longs = new long[size];
        for (int i = 0; i < N; i++) {
            ints[i] = keyCarry[i];
            longs[i] = valueCarry[i];
        }
    }

    /**
     * Returns a long value based on a int key
     * Runtime: O(constant)
     * @param key id for the long value
     */
    public long getLong(int key) {
        if (key < 0 || key > N) throw new ArrayIndexOutOfBoundsException("key has to be between 0 - "+N+": was "+key);
        return longs[key];
    }

    /**
     * Returns an int key based upon a long value and -1 if there is
     * no such value in the map
     * Worst case runtime: O(N)
     * @param value a long value contained in the map
     */
    public int getInt(long value) {
        for (int id = 0; id < N; id++) {
            if (value == longs[id]) return id;
        }
        return -1;  // not found value
    }

    /**
     * Creates a copy of the integer keys
     * Runtime: O(N)
     * @return integer array containing all the keys
     */
    public int[] intsToArray() {
        return Arrays.copyOf(ints,N);
    }

    /**
     * Creates a copy of the long values
     * Runtime: O(N)
     * @return long array containing all the values
     */
    public long[] longsToArray() {
        return Arrays.copyOf(longs,N);
    }
}
