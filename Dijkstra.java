public class Dijkstra {
    AdjacencyList path;
    Graph graph;
    Node source;
    Node destination;
    Double totalDistance;
    int numberOfSteps;

    public Dijkstra(Graph graph, Node source, Node destination) {
        this.path = new AdjacencyList();
        this.graph = graph;
        this.source = source;
        this.destination = destination;
        this.totalDistance = 0.0;
        this.numberOfSteps = 0;
    }

    public AdjacencyList dijkstra() {
        if (source.getTemperature() < 50){
            System.out.println("The temperature of the node " + source.getID() + " is below 50 degrees. \n There is no fire.");
            return null;
        }
        int n = graph.getNodeCount();
        Node[] nodes = graph.getNodes();
        double[] distances = new double[n];
        int[] previous = new int[n];
        boolean[] visited = new boolean[n];
        RobinHoodHash index = graph.getIndex();
        int sourceIndex = index.find(source.getID());
        int destinationIndex = index.find(destination.getID());
        for(int i = 0; i < n; i++) {
            distances[i] = Double.POSITIVE_INFINITY;
            previous[i] = -1;
            visited[i] = false;
        }
        distances[sourceIndex] = 0.0;
        MinHeap heap = new MinHeap(n * n);
        heap.insertNode(source, 0.0);
        int count = 0;
        while(count < n && !heap.isEmpty()) {
            MinHeap.HeapNode heapNode = heap.extractMin();
            int u = index.find(heapNode.node.getID());
            if (visited[u]) 
                continue;
            visited[u] = true;
            AdjacencyList neighbors = graph.getAdjacencyList(nodes[u]);
            for (Edge edge : neighbors.toArray()) {
                Node neighbor = edge.getOtherNode(nodes[u]);
                int v = index.find(neighbor.getID());
                if (distances[v] > distances[u] + edge.getWeight()) {
                    distances[v] = distances[u] + edge.getWeight();
                    previous[v] = u;
                    heap.insertNode(neighbor, distances[v]);
                }
            }
            count++;
        }
        if (distances[destinationIndex] == Double.POSITIVE_INFINITY)
            return null; // No path exists
        int currentIndex = destinationIndex;
        while(previous[currentIndex] != -1){
            int prev = previous[currentIndex];
            Edge[] edges = graph.getAdjacencyList(nodes[currentIndex]).toArray();
            for(Edge e : edges){
                if(e.getOtherNode(nodes[currentIndex]).equals(nodes[prev])){
                    this.totalDistance += e.getWeight();
                    path.insert(e);
                    break;
                }
            }
            this.numberOfSteps++;
            currentIndex = prev;
        }
        this.path = this.path.reverse();
        return path;
    }

    @Override
    public String toString() {
        return path.toString();
    }

    public double gettotalDistance() {
        return this.totalDistance;
    }

    public int getNumberOfSteps() {
        return this.numberOfSteps;
    }
}