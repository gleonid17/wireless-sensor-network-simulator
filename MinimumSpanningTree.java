
public class MinimumSpanningTree {

    Graph graph;
    MinHeap minHeap;
    boolean[] visited;
    int[] parent;
    double[] weights;
    double totalCost;
    double maxTempFound;

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

    private int getIndex(Node node) {
        if (node == null) {
            return -1;
        }
        int index = graph.getIndex().find(node.getID());
        if (index == -1) {
            throw new IllegalArgumentException("Node with ID " + node.getID() + " not found in graph.");
        }
        return index;
    }

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
            weights[i] = 0;
        }
        totalCost = 0.0;
        minHeap.insertNode(startNode, 0.0);
        while (!minHeap.isEmpty()) {
            MinHeap.HeapNode min = minHeap.extractMin();
            Node u = min.node;
            int uIndex = getIndex(u);

            if (visited[uIndex]) {
                continue;
            }
            visited[uIndex] = true;

            parent[uIndex] = getIndex(potentialParent[uIndex]);
            weights[uIndex] = min.weight;
            totalCost += min.weight;

            Edge[] edges = graph.getAdjacencyList(u).toArray();
            for (Edge e : edges) {
                Node w = e.getOtherNode(u);
                int wIndex = getIndex(w);
                if (!visited[wIndex] && (e.getWeight() < weights[wIndex] || weights[wIndex] == 0)) {
                    potentialParent[wIndex] = u;
                    weights[wIndex] = e.getWeight();
                    minHeap.insertNode(w, e.getWeight());
                }
            }
        }
    }

    public void updateIndex(int newIdx, int oldIdx) {
        for (int j = 0; j < parent.length; j++) {
            if (parent[j] == oldIdx) {
                parent[j] = newIdx;
            }
        }
        parent[newIdx] = parent[oldIdx];
        weights[newIdx] = weights[oldIdx];
        parent[oldIdx] = -1;
        weights[oldIdx] = 0;
    }

    public void clearIndex(int idx) {
        if (idx >= 0 && idx < parent.length) {
            parent[idx] = -1;
            weights[idx] = 0;
            visited[idx] = false;
        }
    }

    private int findMaxEdgeInPath(int uIdx, int vIdx) {
        boolean[] visitedPath = new boolean[parent.length];
        int current = uIdx;
        while (current != -1) {
            visitedPath[current] = true;
            current = parent[current];
        }
        double maxWeight = -1;
        int maxIndex = -1;
        current = vIdx;
        while (current != -1) {
            if (visitedPath[current]) {
                break;
            }
            if (weights[current] > maxWeight) {
                maxWeight = weights[current];
                maxIndex = current;
            }
            current = parent[current];
        }
        return maxIndex;
    }

    public void updateMST(Node newNode) {
        if (parent.length < graph.getNodeCount()) {
            expandCapacity();
        }
        int newIdx = getIndex(newNode);
        Node bestNeighbor = null;
        double minWeight = Double.MAX_VALUE;
        for (int i = 0; i < graph.getNodeCount(); i++) {
            Node n = graph.getNodes()[i];
            if (n != null && !n.equals(newNode)) {
                double d = Position.distance(newNode.getPosition(), n.getPosition());
                if (d < minWeight) {
                    minWeight = d;
                    bestNeighbor = n;
                }
            }
        }
        int neighborIdx = getIndex(bestNeighbor);
        parent[newIdx] = neighborIdx;
        weights[newIdx] = minWeight;
        totalCost += minWeight;
        for (int i = 0; i < graph.getNodeCount(); i++) {
            Node other = graph.getNodes()[i];
            if (other == null || other.equals(newNode)) {
                continue;
            }
            double d = Position.distance(newNode.getPosition(), other.getPosition());
            if (d >= minWeight) {
                continue;
            }
            int otherIdx = getIndex(other);
            int maxEdgeIdx = findMaxEdgeInPath(newIdx, otherIdx);
            if (maxEdgeIdx != -1 && weights[maxEdgeIdx] > d) {
                totalCost -= weights[maxEdgeIdx];
                parent[maxEdgeIdx] = newIdx;
                weights[maxEdgeIdx] = d;
                totalCost += d;
            }
        }
    }

    public double getTotalCost() {
        return totalCost;
    }

    public double findMaxTemperature(Node current) {
        double maxTemp = current.getTemperature();
        for (int i = 0; i < parent.length; i++) {
            if (parent[i] != -1 && graph.getNodes()[parent[i]].equals(current)) {
                double childMax = findMaxTemperature(graph.getNodes()[i]);
                if (childMax > maxTemp) {
                    maxTemp = childMax;
                }
            }
        }

        if (maxTemp > maxTempFound) {
            maxTempFound = maxTemp;
        }
        return maxTemp;
    }

    public void broadcastMaxTemperature(Node current, double maxTemp) {
        current.setTemperature(maxTemp);

        for (int i = 0; i < parent.length; i++) {
            if (parent[i] != -1 && graph.getNodes()[parent[i]].equals(current)) {
                broadcastMaxTemperature(graph.getNodes()[i], maxTemp);
            }
        }
    }

    public void maxTemperature(String startNodeID) {
        Node startNode = graph.getNode(startNodeID);
        maxTempFound = -1.0;

        System.out.println("=== Collection Phase ===");
        findMaxTemperature(startNode);
        System.out.println("Final Maximum Temperature: " + maxTempFound);

        System.out.println("=== Broadcasting Phase ===");
        broadcastMaxTemperature(startNode, maxTempFound);
    }

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
            newWeights[i] = 0;
            newVisited[i] = false;
        }
        parent = newParent;
        weights = newWeights;
        visited = newVisited;
    }
}
