public class Dijkstra {
    public static AdjacencyList dijkstra(Graph graph, Node source, Node destination) {
        if (destination.getTemperature() < 50){
            System.out.println("Η θερμοκρασία του σταθμού " + destination.getID() + " είναι κάτω από 50 βαθμούς. \n Δεν υπάρχει πυρκαγία");
            return null;
        }
        int n = graph.getNodeCount();
        Node[] nodes = graph.getNodes();
        double[] distances = new double[n];
        int[] previous = new int[n];
        boolean[] visited = new boolean[n];
        RobinHoodHash index = graph.getIndex();
        int sourceIndex = index.get(source.getID());
        int destinationIndex = index.get(destination.getID());
        for(int i = 0; i < n; i++) {
            distances[i] = Double.POSITIVE_INFINITY;
            previous[i] = -1;
            visited[i] = false;
        }
        distances[sourceIndex] = 0.0;
        MinHeap heap = new MinHeap(n);
        heap.insertNode(source, 0.0);
        int count = 0;
        while(count < n && !heap.isEmpty()) {
            MinHeap.HeapNode heapNode = heap.extractMin();
            int u = index.get(heapNode.node.getID());
            if (visited[u]) 
                continue;
            visited[u] = true;
            AdjacencyList neighbors = graph.getAdjacencyList(nodes[u]);
            for (Edge edge : neighbors.toArray()) {
                Node neighbor = edge.getOtherNode(nodes[u]);
                int v = index.get(neighbor.getID());
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
        AdjacencyList path = new AdjacencyList();
        int currentIndex = destinationIndex;
        while (previous[currentIndex] != -1) {
            path.insert(new Edge(nodes[previous[currentIndex]], nodes[currentIndex]));
            currentIndex = previous[currentIndex];
        }
        return path;
    }
}