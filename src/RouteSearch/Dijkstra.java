package RouteSearch;

import java.util.HashMap;
import Enums.TravelType;
import Model.Model;

/**
 * Class details:
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Niclas Hedam, nhed@itu.dk
 * @version 27-04-2017
 */

public class Dijkstra {
  private HashMap<Long, Double> distTo;
  private HashMap<Long, Edge> edgeTo;
  private long end;

  private IndexMinPQ<Double> pq; // priority queue of vertices
  private TravelType type;
  private GraphFactory graphFactory;

  /**
   * Computes a shortest-paths tree from the source vertex {@code s} to every
   * other vertex in the edge-weighted digraph {@code G}.
   *
   * @param  G the edge-weighted digraph
   * @param  s the source vertex
   * @throws IllegalArgumentException if an edge weight is negative
   * @throws IllegalArgumentException unless {@code 0 <= s < V}
   */
  public Dijkstra(Graph G, long start, long end, TravelType type) {
    distTo = new HashMap<Long, Double>();
    edgeTo = new HashMap<Long, Edge>();
    this.type = type;
    graphFactory = Model.getInstance().getGraphFactory();
    this.end = end;

    for (long v : G.getAdjacencyMap().keySet()) {
      distTo.put(v, Double.POSITIVE_INFINITY);
    }
    distTo.put(start, 0.0);

    // relax vertices in order of distance from s
    pq = new IndexMinPQ<Double>(G.getNumberOfNodes());
    pq.insert(graphFactory.getID(start), distTo.get(start));
    while (!pq.isEmpty()) {
      long v = graphFactory.getLong(pq.delMin());
      for (Edge e : G.adjacent(v)) {
        relax(e);
        System.out.println("Relaxing " + v + "; Distance = " + distTo.get(v));
      }
      if (v == end) {
        return;
      }
    }
  }

  // relax edge e and update pq if changed
  private void relax(Edge e) {
    long v = e.either(), w = e.other(e.either());
    if (distTo.get(w) > distTo.get(v) + e.weight(type)) {
      distTo.put(w, distTo.get(v) + e.weight(type));
      edgeTo.put(w, e);
      if (pq.contains(graphFactory.getID(w))) {
        pq.decreaseKey(graphFactory.getID(w), distTo.get(w));
      } else {
        pq.insert(graphFactory.getID(w), distTo.get(w));
      }
    }
  }

  /**
   * Returns the length of a shortest path from the source vertex {@code s} to
   * vertex {@code v}.
   * @param  v the destination vertex
   * @return the length of a shortest path from the source vertex {@code s} to
   * vertex {@code v};
   *         {@code Double.POSITIVE_INFINITY} if no such path
   * @throws IllegalArgumentException unless {@code 0 <= v < V}
   */
  public double distTo(long v) { return distTo.get(v); }

  /**
   * Returns true if there is a path from the source vertex {@code s} to vertex
   * {@code v}.
   *
   * @param  v the destination vertex
   * @return {@code true} if there is a path from the source vertex
   *         {@code s} to vertex {@code v}; {@code false} otherwise
   * @throws IllegalArgumentException unless {@code 0 <= v < V}
   */
  private boolean hasPathTo(long v) {
    return distTo.get(v) < Double.POSITIVE_INFINITY;
  }

  /**
   * Returns a shortest path from the source vertex {@code s} to vertex {@code
   * v}.
   *
   * @param  v the destination vertex
   * @return a shortest path from the source vertex {@code s} to vertex {@code
   * v} as an iterable of edges, and {@code null} if no such path
   * @throws IllegalArgumentException unless {@code 0 <= v < V}
   */
  public Iterable<Edge> path() {
    if (!hasPathTo(end))
      return null;
    Stack<Edge> path = new Stack<Edge>();
    for (Edge e = edgeTo.get(end); e != null; e = edgeTo.get(e.either())) {
      path.push(e);
    }
    return path;
  }
}
