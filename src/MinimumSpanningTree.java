/**
 * MinimumSpanningTree class implements Prim's algorithm to find the minimum spanning tree (MST) of a graph.
 * It also provides methods to update the MST when new nodes are added and to find and broadcast the maximum temperature in the MST.
 * 
 * @author Giorgos Leonidou
 * @author Andriani Mitsinga
 */

public class MinimumSpanningTree {
    Graph graph;
    MinHeap minHeap;
    boolean[] visited;
    int[] parent;
    double[] weights;
    double totalCost;
    double maxTempFound;

    /**
     * Constructs a MinimumSpanningTree with the specified graph.
     * 
     * @param graph The graph for which to find the minimum spanning tree.
     */
    public MinimumSpanningTree(Graph graph) {
        this.graph = graph;
        this.minHeap = new MinHeap(graph.getNodeCount() * graph.getNodeCount());
        this.visited = new boolean[graph.getNodeCount()];
        this.parent = new int[graph.getNodeCount()];
        java.util.Arrays.fill(this.parent, -1);
        this.weights = new double[graph.getNodeCount()];
        this.totalCost = 0.0;
        this.maxTempFound = -1.0;
    }

    /**
     * Returns the index of the specified node in the graph.
     * 
     * @param node The node for which to find the index.
     * @return The index of the node, or -1 if not found.
     */
    private int getIndex(Node node) {
        if (node == null)
            return -1;
        int index = graph.getIndex().find(node.getID());
        if (index == -1)
            throw new IllegalArgumentException("Node with ID " + node.getID() + " not found in graph.");
        return index;
    }

    /**
     * Implements Prim's algorithm to find the minimum spanning tree (MST) of the graph.
     * 
     * @param startNode The node to start the MST from.
     */
    public void Prim(Node startNode) {
        int n = graph.getNodeCount();
        this.minHeap = new MinHeap(n * n);
        this.visited = new boolean[n];
        this.parent = new int[n];
        this.weights = new double[n];
        Node[] potentialParent = new Node[n];
        this.totalCost = 0.0;
        for (int i = 0; i < graph.getNodeCount(); i++) {
            visited[i] = false;
            parent[i] = -1;
            weights[i] = Double.MAX_VALUE;
        }
        totalCost = 0.0;
        minHeap.insertNode(startNode, 0.0);
        while (!minHeap.isEmpty()) {
            MinHeap.HeapNode min = minHeap.extractMin();
            Node u = min.node;
            int uIndex = getIndex(u);
            if (visited[uIndex])
                continue;
            visited[uIndex] = true;
            parent[uIndex] = getIndex(potentialParent[uIndex]);
            weights[uIndex] = min.weight;
            totalCost += min.weight;
            Edge[] edges = graph.getAdjacencyList(u).toArray();
            for (Edge e : edges) {
                Node w = e.getOtherNode(u);
                int wIndex = getIndex(w);
                if (!visited[wIndex] && (e.getWeight() < weights[wIndex])) {
                    potentialParent[wIndex] = u;
                    weights[wIndex] = e.getWeight();
                    minHeap.insertNode(w, e.getWeight());
                }
            }
        }
    }

    /**
     * Updates the index of a node in the MST.
     * 
     * @param newIdx The new index.
     * @param oldIdx The old index.
     */
    public void updateIndex(int newIdx, int oldIdx) {
        for (int j = 0; j < parent.length; j++) {
            if (parent[j] == oldIdx)
                parent[j] = newIdx;
        }
        parent[newIdx] = parent[oldIdx];
        weights[newIdx] = weights[oldIdx];
        parent[oldIdx] = -1;
        weights[oldIdx] = 0;
    }

    /**
     * Clears the index of a node in the MST.
     * 
     * @param idx The index to clear.
     */
    public void clearIndex(int idx) {
        if (idx >= 0 && idx < parent.length) {
            parent[idx] = -1;
            weights[idx] = 0;
            visited[idx] = false;
        }
    }

    /**
     * Finds the maximum edge in the path between two nodes in the MST.
     * 
     * @param uIdx The index of the first node.
     * @param vIdx The index of the second node.
     * @return The index of the maximum edge, or -1 if not found.
     */
    private int findMaxEdgeInPath(int uIdx, int vIdx) {
        boolean[] visitedPath = new boolean[parent.length];
        int current = uIdx;
        while (current != -1) {
            visitedPath[current] = true;
            current = parent[current];
        }
        int lca = vIdx;
        while (lca != -1 && !visitedPath[lca])
            lca = parent[lca];
        double maxWeight = -1;
        int maxIndex = -1;
        current = uIdx;
        while (current != lca && current != -1) {
            if (weights[current] > maxWeight) {
                maxWeight = weights[current];
                maxIndex = current;
            }
            current = parent[current];
        }
        current = vIdx;
        while (current != lca && current != -1) {
            if (weights[current] > maxWeight) {
                maxWeight = weights[current];
                maxIndex = current;
            }
            current = parent[current];
        }
        return maxIndex;
    }

    /**
     * Updates the minimum spanning tree with a new node.
     * 
     * @param newNode The new node to add to the MST.
     */
    public void updateMST(Node newNode) {
        if (parent.length < graph.getNodeCount())
            expandCapacity();
        int newIdx = getIndex(newNode);
        Edge[] edges = graph.getAdjacencyList(newNode).toArray();
        if (edges.length == 0)
            return;
        Node bestNeighbor = null;
        double minWeight = Double.MAX_VALUE;
        for (Edge edge : edges) {
            Node neighbor = edge.getOtherNode(newNode);
            if (edge.getWeight() < minWeight) {
                minWeight = edge.getWeight();
                bestNeighbor = neighbor;
            }
        }
        int neighborIdx = getIndex(bestNeighbor);
        parent[newIdx] = neighborIdx;
        weights[newIdx] = minWeight;
        totalCost += minWeight;
        for (Edge edge : edges) {
            Node other = edge.getOtherNode(newNode);
            int otherIdx = getIndex(other);
            double edgeWeight = edge.getWeight();
            int maxEdgeIdx = findMaxEdgeInPath(newIdx, otherIdx);
            if (maxEdgeIdx != -1 && weights[maxEdgeIdx] > edgeWeight) {
                totalCost -= weights[maxEdgeIdx];
                parent[maxEdgeIdx] = newIdx;
                weights[maxEdgeIdx] = edgeWeight;
                totalCost += edgeWeight;
            }
        }
    }

    /**
     * Returns the total cost of the minimum spanning tree.
     * 
     * @return The total cost.
     */
    public double getTotalCost() {
        return totalCost;
    }

    /**
     * Finds the maximum temperature in the subtree rooted at the specified node.
     * 
     * @param current The node to start the search from.
     * @return The maximum temperature in the subtree.
     */
    public double findMaxTemperature(Node current) {
        double maxTemp = current.getTemperature();

        for (int i = 0; i < parent.length; i++) {
            if (parent[i] != -1 && graph.getNodes()[parent[i]].equals(current)) {
                double childMax = findMaxTemperature(graph.getNodes()[i]);
                if (childMax > maxTemp)
                    maxTemp = childMax;
            }
        }
        if (maxTemp > maxTempFound)
            maxTempFound = maxTemp;
        return maxTemp;
    }

    /**
     * Broadcasts the maximum temperature to all nodes in the subtree rooted at the specified node.
     * 
     * @param current The node to start the broadcast from.
     * @param maxTemp The maximum temperature to broadcast.
     */
    public void broadcastMaxTemperature(Node current, double maxTemp) {
        current.setTemperature(maxTemp);
        for (int i = 0; i < parent.length; i++)
            if (parent[i] != -1 && graph.getNodes()[parent[i]].equals(current)) {
                broadcastMaxTemperature(graph.getNodes()[i], maxTemp);
        }
    }

    /**
     * Finds and broadcasts the maximum temperature in the sensor network.
     * 
     * @param startNodeID The ID of the node to start the search from.
     */
    public void maxTemperature(String startNodeID) {
        Node startNode = graph.getNode(startNodeID);
        maxTempFound = -1.0;
        System.out.println("=== Collection Phase ===");
        findMaxTemperature(startNode);
        System.out.println("Final Maximum Temperature: " + maxTempFound);
        System.out.println("=== Broadcasting Phase ===");
        broadcastMaxTemperature(startNode, maxTempFound);
    }

    /**
     * Prints the current minimum spanning tree.
     */
    public void printMST() {
        System.out.println("Updating MST View...");
        Node[] allNodes = graph.getNodes();
        for (int i = 0; i < parent.length; i++) {
            if (parent[i] != -1 && i < allNodes.length && allNodes[i] != null) {
                String pID = graph.getNodes()[parent[i]].getID();
                String nID = allNodes[i].getID();
                String wStr = String.format("%.2f", weights[i]);
                System.out.println("Adding Edge: " + pID + " -- " + nID + " [" + wStr + "]");
            }
        }
        System.out.println("\nNew Total MST Cost: " + String.format("%.2f", totalCost));
    }

    /**
     * Expands the capacity of the internal arrays to accommodate more nodes.
     */
    private void expandCapacity() {
        int newSize = graph.getNodeCount();
        int[] newParent = new int[newSize];
        double[] newWeights = new double[newSize];
        boolean[] newVisited = new boolean[newSize];
        for (int i = 0; i < parent.length; i++) {
            newParent[i] = parent[i];
            newWeights[i] = weights[i];
            newVisited[i] = visited[i];
        }
        for (int i = parent.length; i < newSize; i++) {
            newParent[i] = -1;
            newWeights[i] = Double.MAX_VALUE;
            newVisited[i] = false;
        }
        parent = newParent;
        weights = newWeights;
        visited = newVisited;
    }
}