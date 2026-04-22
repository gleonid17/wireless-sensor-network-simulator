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
        this.minHeap = new MinHeap(graph.getNodeCount() * graph.getNodeCount() );
        this.visited = new boolean[graph.getNodeCount()];
        this.parent = new int[graph.getNodeCount()];
        java.util.Arrays.fill(this.parent, -1);
        this.weights = new double[graph.getNodeCount()];
        this.totalCost = 0.0;
        this.maxTempFound = -1.0;
    }

    private int getIndex(Node node) {
        if(node == null) 
            return -1;
        int index = graph.getIndex().get(node.getID());
        if (index == -1) {
            throw new IllegalArgumentException("Node with ID " + node.getID() + " not found in graph.");
        }
        return index;
    }

    public void Prim(Node startNode){ 
        int n = graph.getNodeCount();
        this.minHeap = new MinHeap(n * n);
        this.visited = new boolean[n];
        this.parent = new int[n];
        this.weights = new double[n];
        Node[] potentialParent = new Node[n];
        this.totalCost = 0.0;

        for(int i=0; i<graph.getNodeCount(); i++){
            visited[i] = false;
            parent[i] = -1;
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

            parent[uIndex] = getIndex(potentialParent[uIndex]);
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

    public void updateIndex(int newIdx, int oldIdx) {
        for(int j=0; j < parent.length; j++) {
            if(parent[j] == oldIdx) {
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
            
    public void updateMST(Node newNode) {
        Node bestNeighbor = null;
        double minD = Double.MAX_VALUE;
        
        for (int i = 0; i < graph.getNodeCount(); i++) {
            Node n = graph.getNodes()[i];
            if (n != null && !n.equals(newNode)) {
                double d = Position.distance(newNode.getPosition(), n.getPosition());
                if (d < minD) {
                    minD = d;
                    bestNeighbor = n;
                }
            }
        }
        int newIdx = getIndex(newNode);
        parent[newIdx] = getIndex(bestNeighbor);
        weights[newIdx] = minD;

        totalCost += minD;

        optimizeMST(newNode);
    }

    public double getTotalCost() {
        return totalCost;
    }
    private void optimizeMST(Node newNode) {
        Node maxWeightNode = null;
        double maxWeight = -1.0;
        Node current = newNode;

        while (current != null) {
            int idx = getIndex(current);
            if (parent[idx] != -1 && weights[idx] > maxWeight) {
                maxWeight = weights[idx];
                maxWeightNode = current;
            }
            
            current = graph.getNodes()[parent[idx]];
        }

        if (maxWeightNode != null) {
            int idx = getIndex(maxWeightNode);
            totalCost -= weights[idx];
            
            parent[idx] = -1;
            weights[idx] = 0;
            
            System.out.println("Βελτιστοποίηση: Αφαιρέθηκε η ακμή " + maxWeightNode.getID() + " με βάρος " + maxWeight);
        }
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

        System.out.println("=== ΦΑΣΗ ΣΥΛΛΟΓΗΣ ===");
        findMaxTemperature(startNode);
        System.out.println("Τελική μέγιστη θερμοκρασία: " + maxTempFound);
        
        System.out.println("=== ΦΑΣΗ ΕΝΗΜΕΡΩΣΗΣ ===");
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
}