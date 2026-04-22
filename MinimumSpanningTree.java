public class MinimumSpanningTree {
    Graph graph;
    MinHeap minHeap;
    boolean[] visited;
    Node[] parent;     
    double[] weights;   
    double totalCost;
    double maxTempFound; 

    public MinimumSpanningTree(Graph graph) {
        this.graph = graph;
        this.minHeap = new MinHeap(graph.getNodeCount() * graph.getNodeCount() );
        this.visited = new boolean[graph.getNodeCount()];
        this.parent = new Node[graph.getNodeCount()];
        this.weights = new double[graph.getNodeCount()];
        this.totalCost = 0.0;
        this.maxTempFound = -1.0;
    }

    private int getIndex(Node node) {
         return graph.getIndex().get(node.getID()); 
    }

    public void Prim(Node startNode){ 
        int n = graph.getNodeCount();
        this.minHeap = new MinHeap(n * n);
        this.visited = new boolean[n];
        this.parent = new Node[n];
        this.weights = new double[n];
        Node[] potentialParent = new Node[n];
        this.totalCost = 0.0;

        for(int i=0; i<graph.getNodeCount(); i++){
            visited[i] = false;
            parent[i] = null;
            weights[i] = 0;
        }
        totalCost = 0.0;
        minHeap.insertNode(startNode, 0.0);           
        while(!minHeap.isEmpty()) {
            MinHeap.HeapNode min = minHeap.extractMin();
            Node u = min.node;
            int uIndex = getIndex(u);

            if (visited[uIndex]) continue;
            visited[uIndex] = true;

            parent[uIndex] = potentialParent[uIndex];
            weights[uIndex] = min.weight;
            totalCost += min.weight;

            Edge[] edges = graph.getAdjacencyList(u).toArray();
            for (Edge e : edges) {
                Node w = e.getOtherNode(u);
                int wIndex = getIndex(w);
                if (!visited[wIndex]) {
                    potentialParent[wIndex] = u;
                    weights[wIndex] = e.getWeight();
                    minHeap.insertNode(w, e.getWeight());
                }
            }
        }
    }

    public void updateMST(Node newNode) {
        double minweight = Double.MAX_VALUE;
        Edge bestEdge = null;
        Node bestNeighbor = null;

        Edge[] edges = graph.getAdjacencyList(newNode).toArray();

        for (Edge e : edges) {
            double w = e.getWeight();
            if (w < minweight) {
                minweight = w;
                bestEdge = e;
                bestNeighbor = e.getOtherNode(newNode);
            }
        }
        if (bestNeighbor != null) {
            int index = getIndex(newNode);
            parent[index] = bestNeighbor;
            weights[index] = minweight;
            totalCost += minweight;
        }

        for (Edge e : edges) {
            if (e != bestEdge) { 
                optimizeMST(newNode, e.getOtherNode(newNode), e.getWeight());
            }
        }
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void optimizeMST(Node newNode, Node neighbor, double newWeight) {
        Node current = neighbor;
        double maxWeight = -1.0;
        Node maxNode = null;

        while (current != null) {
            int currentIndex = getIndex(current);
            if (weights[currentIndex] > maxWeight) {
                maxWeight = weights[currentIndex];
                maxNode = current;
            }
            current = parent[currentIndex];
        }

        if (newWeight < maxWeight && maxNode != null) {
            System.out.printf("Improvement: %s--%s [%.2f] replaces %s--%s [%.2f]%n", 
                newNode.getID(), neighbor.getID(), newWeight, maxNode.getID(), 
                parent[getIndex(maxNode)] != null ? parent[getIndex(maxNode)].getID() : "root", maxWeight);
            
            totalCost += newWeight - maxWeight;

            for(int i = 0; i < parent.length; i++) {
                if(parent[i] == maxNode) {
                    parent[i] = newNode;
                }
            }
            int newNodeIndex = getIndex(newNode);
            parent[newNodeIndex] = neighbor;
            weights[newNodeIndex] = newWeight;

            int maxNodeIndex = getIndex(maxNode);
            weights[maxNodeIndex] = newWeight;

            int maxID = getIndex(maxNode);
            parent[maxID] = null;
            weights[maxID] = 0.0;
        }
    }

    public void findMaxTemperature(Node current) {
        for (int i = 0; i < parent.length; i++) {
            if (parent[i] == current) {
                findMaxTemperature(graph.getNodes()[i]);
            }
        }

         if (current.getTemperature() > maxTempFound) {
            maxTempFound = current.getTemperature();
        }
        
        if (parent[getIndex(current)] != null) {
            Node p = parent[getIndex(current)];
            System.out.println("Message: " + current.getID() + " -> " + p.getID() + " [MaxTemp=" + current.getTemperature() + "]");
        }
    }

    private void broadcastRequest(Node current) {
        for (int i = 0; i < parent.length; i++) {
            if (parent[i] == current && graph.getNodes()[i] != null) {
                Node child = graph.getNodes()[i];
                System.out.println("Message: " + current.getID() + " -> " + child.getID() 
                    + " [Request MaxTemp]");
                broadcastRequest(child);
            }
        }
    }

    public void maxTemperature(String startNodeID) {
        Node startNode = graph.getNode(startNodeID);
        maxTempFound = -1.0;

         System.out.println("=== REQUEST PROPAGATION PHASE ===");
            broadcastRequest(startNode);
        
         System.out.println("\n=== RESPONSE COLLECTION PHASE ===");
         findMaxTemperature(startNode);

         System.out.println("\n=== RESULT ===");
         System.out.println("Maximum Temperature found: " + maxTempFound + "°C");
    }

    public void printMST() {
        System.out.println("Updating MST View...");
        
        Node[] allNodes = graph.getNodes();
        
        for (int i = 0; i < parent.length; i++) {
            if (parent[i] != null && i < allNodes.length && allNodes[i] != null) {
                String pID = parent[i].getID();
                String nID = allNodes[i].getID();
                String wStr = String.format("%.2f", weights[i]);
                
                System.out.println("Adding Edge: " + pID + " -- " + nID + " [" + wStr + "]");
            }
        }
        System.out.println("\nNew Total MST Cost: " + String.format("%.2f", totalCost));
    }
}