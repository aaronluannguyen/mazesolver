package misc.graphs;

import datastructures.concrete.ArrayDisjointSet;
import datastructures.concrete.ArrayHeap;
import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.DoubleLinkedList;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;
import datastructures.interfaces.ISet;
import misc.Searcher;
import misc.exceptions.NoPathExistsException;

/**
 * Represents an undirected, weighted graph, possibly containing self-loops, parallel edges,
 * and unconnected components.
 *
 * Note: This class is not meant to be a full-featured way of representing a graph.
 * We stick with supporting just a few, core set of operations needed for the
 * remainder of the project.
 */
public class Graph<V, E extends Edge<V> & Comparable<E>> {
    // NOTE 1:
    //
    // Feel free to add as many fields, private helper methods, and private
    // inner classes as you want.
    //
    // And of course, as always, you may also use any of the data structures
    // and algorithms we've implemented so far.
    //
    // Note: If you plan on adding a new class, please be sure to make it a private
    // static inner class contained within this file. Our testing infrastructure
    // works by copying specific files from your project to ours, and if you
    // add new files, they won't be copied and your code will not compile.
    //
    //
    // NOTE 2:
    //
    // You may notice that the generic types of Graph are a little bit more
    // complicated then usual.
    //
    // This class uses two generic parameters: V and E.
    //
    // - 'V' is the type of the vertices in the graph. The vertices can be
    //   any type the client wants -- there are no restrictions.
    //
    // - 'E' is the type of the edges in the graph. We've contrained Graph
    //   so that E *must* always be an instance of Edge<V> AND Comparable<E>.
    //
    //   What this means is that if you have an object of type E, you can use
    //   any of the methods from both the Edge interface and from the Comparable
    //   interface
    //
    // If you have any additional questions about generics, or run into issues while
    // working with them, please ask ASAP either on Piazza or during office hours.
    //
    // Working with generics is really not the focus of this class, so if you
    // get stuck, let us know we'll try and help you get unstuck as best as we can.
    
    private IDictionary<V, ISet<E>> graph;
    private int totalEdges;
    private IList<E> graphEdges;
    private IList<V> graphVertices;

    /**
     * Constructs a new graph based on the given vertices and edges.
     *
     * @throws IllegalArgumentException  if any of the edges have a negative weight
     * @throws IllegalArgumentException  if one of the edges connects to a vertex not
     *                                   present in the 'vertices' list
     */
    public Graph(IList<V> vertices, IList<E> edges) {
        this.graph = new ChainedHashDictionary<V, ISet<E>>();
        this.totalEdges = edges.size();
        this.graphVertices = vertices;
        
        for (E edge : edges) {
            if (edge.getWeight() < 0) {
                throw new IllegalArgumentException();
            }
            
            if (!vertices.contains(edge.getVertex1()) || !vertices.contains(edge.getVertex2())) {
                throw new IllegalArgumentException();
            }
            
            updateGraph(edge.getVertex1(), edge);
            updateGraph(edge.getVertex2(), edge);
        }
        
        this.graphEdges = edges;
    }
    
    private void updateGraph(V vertex, E edge) {
        if (!this.graph.containsKey(vertex)) {
            ISet<E> vertexEdges = new ChainedHashSet<E>();
            vertexEdges.add(edge);
            this.graph.put(vertex, vertexEdges);
        } else {
            this.graph.get(vertex).add(edge);
        }
    }

    /**
     * Sometimes, we store vertices and edges as sets instead of lists, so we
     * provide this extra constructor to make converting between the two more
     * convenient.
     */
    public Graph(ISet<V> vertices, ISet<E> edges) {
        // You do not need to modify this method.
        this(setToList(vertices), setToList(edges));
    }

    // You shouldn't need to call this helper method -- it only needs to be used
    // in the constructor above.
    private static <T> IList<T> setToList(ISet<T> set) {
        IList<T> output = new DoubleLinkedList<>();
        for (T item : set) {
            output.add(item);
        }
        return output;
    }

    /**
     * Returns the number of vertices contained within this graph.
     */
    public int numVertices() {
        return this.graph.size();
    }

    /**
     * Returns the number of edges contained within this graph.
     */
    public int numEdges() {
        return this.totalEdges;
    }

    /**
     * Returns the set of all edges that make up the minimum spanning tree of
     * this graph.
     *
     * If there exists multiple valid MSTs, return any one of them.
     *
     * Precondition: the graph does not contain any unconnected components.
     */
    public ISet<E> findMinimumSpanningTree() {
        IDisjointSet<V> mst = new ArrayDisjointSet<V>();
        ISet<E> result = new ChainedHashSet<E>();
        
        for (KVPair<V, ISet<E>> pair : this.graph) {
            mst.makeSet(pair.getKey());
        }
        
        IList<E> sortedEdges = Searcher.topKSort(this.totalEdges, this.graphEdges);
        for (E edge : sortedEdges) {
            V vertex1 = edge.getVertex1();
            V vertex2 = edge.getVertex2();
            if (mst.findSet(vertex1) != mst.findSet(vertex2)) {
                mst.union(vertex1, vertex2);
                result.add(edge);
            }
        }
        return result;
    }

    /**
     * Returns the edges that make up the shortest path from the start
     * to the end.
     *
     * The first edge in the output list should be the edge leading out
     * of the starting node; the last edge in the output list should be
     * the edge connecting to the end node.
     *
     * Return an empty list if the start and end vertices are the same.
     *
     * @throws NoPathExistsException  if there does not exist a path from the start to the end
     */
    public IList<E> findShortestPathBetween(V start, V end) {
        IList<E> result = new DoubleLinkedList<E>();
        IList<E> resultReversed = new DoubleLinkedList<E>();
        IDictionary<V, E> allPaths = new ChainedHashDictionary<V, E>();
        ISet<V> visited = new ChainedHashSet<V>();
        IDictionary<V, Double> vertexCosts = new ChainedHashDictionary<V, Double>();
        IPriorityQueue<VertexNode<V>> heap = new ArrayHeap<VertexNode<V>>();
        
        if (start == end) {
            return result;
        }
        
        if (this.graph.isEmpty() || !this.graph.containsKey(start) || !this.graph.containsKey(end)) {
            throw new NoPathExistsException();
        }
        
        for (V vertex : this.graphVertices) {
            vertexCosts.put(vertex, Double.POSITIVE_INFINITY);
        }
        vertexCosts.put(start, 0.0);
        
        findShortestPathHelper(start, 0.0, heap, visited, vertexCosts, allPaths);

        visited.add(start);
        
        while (!heap.isEmpty()) {
            VertexNode<V> currVertexNode = heap.removeMin();
            V currVertex = currVertexNode.getVertex();
            double cost = currVertexNode.getCost();
            
            if (!visited.contains(currVertex)) {
                visited.add(currVertex);
                findShortestPathHelper(currVertex, cost, heap, visited, vertexCosts, allPaths);
            }
        }
        
        if (vertexCosts.get(end) == Double.POSITIVE_INFINITY) {
            throw new NoPathExistsException();
        }
        
        V find = end;
        while (allPaths.get(find).getOtherVertex(find) != start) {
            E addEdge = allPaths.get(find);
            resultReversed.add(addEdge);
            find = addEdge.getOtherVertex(find);
        }
        
        resultReversed.add(allPaths.get(find));
        
        while (!resultReversed.isEmpty()) {
            result.add(resultReversed.remove());
        }
        
        return result;
    }
    
    private void findShortestPathHelper(V vertex, double cost, IPriorityQueue<VertexNode<V>> heap, 
            ISet<V> visited, IDictionary<V, Double> vertexCosts, IDictionary<V, E> allPaths) {
        
        for (E edge : this.graph.get(vertex)) {
            double newCost = cost + edge.getWeight();
            V newVertex = edge.getOtherVertex(vertex);
            
            heap.insert(new VertexNode<V>(newVertex, newCost));
            
            if (vertexCosts.get(newVertex) == Double.POSITIVE_INFINITY
                    || (!visited.contains(newVertex) && newCost < vertexCosts.get(newVertex))) {
                vertexCosts.put(newVertex, newCost);
                allPaths.put(edge.getOtherVertex(vertex), edge);
            }
        }
    }
    
    private static class VertexNode<V> implements Comparable<VertexNode<V>> {
        private V vertex;
        private double cost;
        
        public VertexNode(V vertex, double cost) {
            this.vertex = vertex;
            this.cost = cost;
        }
        
        public V getVertex() {
            return this.vertex;
        }
        
        public double getCost() {
            return this.cost;
        }
        
        public String toString() {
            return "" + this.vertex + " " + this.cost;
        }
        
        @Override
        public int compareTo(VertexNode<V> other) {
            return Double.compare(this.cost, other.cost);
        }
    }
}
