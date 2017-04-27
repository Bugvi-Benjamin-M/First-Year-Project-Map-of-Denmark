package RouteSearch;

import java.util.LinkedList;
import java.util.Map;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 27-04-2017
 */
public class Graph {

    public static final String NEWLINE = System.getProperty("line.separator");

    private final int nNodes;
    private int nEdges;
    private LinkedList<Edge>[] adjacencyLists;
    private LongToIntMap refConverter;

    public Graph(int nNodes, LongToIntMap refConverter) {
        if (refConverter == null) throw new IllegalArgumentException("LongToIntMap must not be null");
        this.refConverter = refConverter;
        this.nNodes = nNodes;
        adjacencyLists = (LinkedList<Edge>[]) new LinkedList[nEdges];
        for (int n = 0; n < nNodes; n++) {
            adjacencyLists[n] = new LinkedList<Edge>();
        }
    }

    public int getNumberOfNodes() {
        return nNodes;
    }

    public int getNumberOfEdges() {
        return nEdges;
    }

    private void validateNode(int node) {
        if (node < 0 || node >= nNodes) {
            throw new IllegalArgumentException("node "+node+" is not between 0 and "+(nNodes-1));
        }
    }

    public void addEdge(Edge edge) {
        int v = edge.either();
        int w = edge.other(v);
        validateNode(v);
        validateNode(w);
        adjacencyLists[v].add(edge);
        adjacencyLists[w].add(edge);
        nEdges++;
    }

    public Iterable<Edge> adjacent(int node) {
        validateNode(node);
        return adjacencyLists[node];
    }

    public int degree(int node) {
        validateNode(node);
        return adjacencyLists[node].size();
    }

    public Iterable<Edge> edges() {
        LinkedList<Edge> list = new LinkedList<>();
        for (int n = 0; n < nNodes; n++) {
            int selfLoops = 0;
            for (Edge e: adjacent(n)) {
                if (e.other(n) > n) {
                    list.add(e);
                } // only add one copy of each self loop (selfloops will be consecutive)
                else if (e.other(n) == n) {
                    if (selfLoops % 2 == 0) list.add(e);
                    selfLoops++;
                }
                
            }
        }
        return list;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(nNodes+" "+nEdges + NEWLINE);
        for (int n = 0; n < nNodes; n++) {
            sb.append(n + ": ");
            for (Edge e: adjacencyLists[n]) {
                sb.append(e + " ");
            }
            sb.append(NEWLINE);
        }
        return sb.toString();
    }
}
