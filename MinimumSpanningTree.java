public class MinimumSpanningTree {
    Graph graph;
    MinHeap minHeap;
    boolean[] visited;
    Node[] parent;      // αντί closest[]
    double[] weights;   // βάρος κάθε ακμής MST
    double totalCost;

    public MinimumSpanningTree(Graph graph) {
        this.graph = graph;
        this.minHeap = new MinHeap(graph.getNodeCount() * graph.getNodeCount() );
        this.visited = new boolean[graph.getNodeCount()];
        this.parent = new Node[graph.getNodeCount()];
        this.weights = new double[graph.getNodeCount()];
        this.totalCost = 0.0;
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
        this.totalCost = 0.0;

        for(int i=0; i<graph.getNodeCount(); i++){
            visited[i] = false;
            parent[i] = null;
            weights[i] = 0;
        }
        totalCost = 0.0;
        minHeap.insert(0, null, startNode);           
        while(!minHeap.isEmpty()) {
            MinHeap.HeapNode min = minHeap.extractMin();
            Node u = min.u;
            Node v = min.v;

            int vIndex = getIndex(v);
            if (visited[vIndex]) continue;
            visited[vIndex] = true;
            parent[vIndex] = u;
            weights[vIndex] = min.weight;
            totalCost += min.weight;
            Edge[] edges = graph.getAdjacencyList(v).toArray();
            for (Edge e : edges) {
                Node w = e.getOtherNode(v);
                int wIndex = getIndex(w);
                if (!visited[wIndex]) {
                    minHeap.insert(e.getWeight(), v, w);
                }
            }
        }
    }
}