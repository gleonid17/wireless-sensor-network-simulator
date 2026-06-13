/**
 * Graph class representing a graph data structure with nodes and edges.
 * It uses an adjacency list representation and includes methods for adding and removing nodes,
 * as well as retrieving nodes and their adjacency lists. The graph also maintains a hash index for efficient node lookup.
 * 
 * @author Giorgos Leonidou 
 * @author Andriani Mitsinga
 */

public class Graph {
    private Node[] nodes;
    private AdjacencyList[] adj;
    private RobinHoodHash index;
    private int nodeCount;
    private int capacity;
    private double d;
    private MinimumSpanningTree mstInstance;

    /**
     * Constructs a new Graph with the specified range.
     * 
     * @param d The range within which nodes are considered adjacent.
     */
    public Graph(double d) {
        this.capacity = 7;
        this.adj = new AdjacencyList[capacity];
        this.nodeCount = 0;
        this.d = d;
        this.nodes = new Node[capacity];
        this.index = new RobinHoodHash(capacity);
        for (int i = 0; i < capacity; i++) {
            adj[i] = new AdjacencyList();
        }
    }

    /**
     * Sets the Minimum Spanning Tree instance for the graph.
     * 
     * @param mst The Minimum Spanning Tree instance.
     */
    public void setMST(MinimumSpanningTree mst) {
        this.mstInstance = mst;
    }

    /**
     * Returns the number of nodes in the graph.
     * 
     * @return The number of nodes.
     */
    public int getNodeCount() {
        return nodeCount;
    }

    /**
     * Returns the hash index for efficient node lookup.
     * 
     * @return The hash index.
     */
    public RobinHoodHash getIndex() {
        return index;
    }

    /**
     * Resizes the graph's internal arrays when the load factor exceeds the threshold.
     */
    private void resize() {
        int newCapacity = capacity * 2;
        Node[] newNodes = new Node[newCapacity];
        AdjacencyList[] newAdj = new AdjacencyList[newCapacity];
        for (int i = 0; i < nodeCount; i++) {
            newNodes[i] = nodes[i];
            newAdj[i] = adj[i];
        }
        for (int i = nodeCount; i < newCapacity; i++)
            newAdj[i] = new AdjacencyList();
        this.nodes = newNodes;
        this.adj = newAdj;
        this.capacity = newCapacity;
    }

    /**
     * Adds a new node to the graph.
     * 
     * @param newNode The node to add.
     */
    public void addNode(Node newNode) {
        if ((nodeCount + 1.0) / capacity >= 0.9) 
            resize();
        nodes[nodeCount] = newNode;
        adj[nodeCount] = new AdjacencyList(newNode);
        index.insert(newNode.getID(), nodeCount);
        for (int i = 0; i < nodeCount; i++)
            if (Position.withinRange(newNode.getPosition(), nodes[i].getPosition(), d)) {
                Edge newEdge = new Edge(newNode, nodes[i]);
                adj[nodeCount].insert(newEdge);
                adj[i].insert(newEdge);
            }
        nodeCount++;
    }

    /**
     * Removes a node from the graph.
     * 
     * @param removableNode The node to remove.
     */
    public void removeNode(Node removableNode) {
        int i = this.index.find(removableNode.getID());
        if (i == -1)
            return;
        Edge[] edges = adj[i].toArray();
        for (Edge e : edges) {
            Node neighbor = e.getOtherNode(nodes[i]);
            int neighborIndex = this.index.find(neighbor.getID());
            adj[neighborIndex].remove(e);
        }
        int lastIdx = nodeCount - 1;
        if (mstInstance != null && i != lastIdx)
            mstInstance.updateIndex(i, lastIdx);
        if (i != lastIdx) {
            nodes[i] = nodes[nodeCount - 1];
            adj[i] = adj[nodeCount - 1];
            adj[i].setOwner(nodes[i]);
            index.updateIndex(nodes[i].getID(), i);
        }
        index.remove(removableNode.getID());
        nodes[nodeCount - 1] = null;
        adj[nodeCount - 1] = new AdjacencyList();
        nodeCount--;
        if (mstInstance != null)
            mstInstance.clearIndex(lastIdx);
    }

    /**
     * Returns the node with the specified ID.
     * 
     * @param id The ID of the node to find.
     * @return The node with the specified ID, or null if not found.
     */
    public Node getNode(String id) {
        int i = this.index.find(id);
        if (i == -1)
            return null;
        return nodes[i];
    }

    /**
     * Returns the adjacency list for the specified node.
     * 
     * @param node The node for which to retrieve the adjacency list.
     * @return The adjacency list for the specified node, or null if not found.
     */
    public AdjacencyList getAdjacencyList(Node node) {
        int i = this.index.find(node.getID());
        if (i == -1)
            return null;
        return adj[i];
    }

    /**
     * Returns an array containing all nodes in the graph.
     * 
     * @return An array of all nodes.
     */
    public Node[] getNodes() {
        return nodes;
    }

    /**
     * Returns the first fire station node in the graph.
     * 
     * @return The first fire station node, or null if not found.
     */
    public Node getFirstFireStation() {
        for (int i = 0; i < nodeCount; i++)
            if (nodes[i].isFireStation())
                return nodes[i];
        return null;
    }
}