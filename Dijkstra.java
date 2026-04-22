
public class Dijkstra {

    AdjacencyList path;
    Graph graph;
    Node source;
    Node destination;
    Double totalDistance;
    int numberOfSteps;
    private Node[] pathFrom;
    private int pathSize;

    public Dijkstra(Graph graph, Node source, Node destination) {
        this.path = new AdjacencyList();
        this.graph = graph;
        this.source = source;
        this.destination = destination;
        this.totalDistance = 0.0;
        this.numberOfSteps = 0;
    }

    public AdjacencyList dijkstra() {
        if (source.getTemperature() < 50) {
            System.out.println("No fire detected.");
            System.out.println("Node        : " + source.getID());
            System.out.println("Temperature : " + source.getTemperature() + "°C (Threshold: 50°C)");
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
        for (int i = 0; i < n; i++) {
            distances[i] = Double.POSITIVE_INFINITY;
            previous[i] = -1;
            visited[i] = false;
        }
        distances[sourceIndex] = 0.0;
        MinHeap heap = new MinHeap(n * n);
        heap.insertNode(source, 0.0);
        while (!heap.isEmpty()) {
            MinHeap.HeapNode heapNode = heap.extractMin();
            int u = index.find(heapNode.node.getID());
            if (visited[u]) {
                continue;
            }
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
        }
        if (distances[destinationIndex] == Double.POSITIVE_INFINITY) {
            return null; // No path exists
        }
        int steps = 0;
        int cur = destinationIndex;
        while (previous[cur] != -1) {
            steps++;
            cur = previous[cur];
        }
        numberOfSteps = steps;
        Node[] orderedNodes = new Node[steps + 1];
        cur = destinationIndex;
        for (int i = steps; i >= 0; i--) {
            orderedNodes[i] = nodes[cur];
            if (previous[cur] != -1) {
                cur = previous[cur];
            }
        }
        for (int i = 0; i < steps; i++) {
            Edge edge = new Edge(orderedNodes[i], orderedNodes[i + 1]);
            path.insert(edge);
            totalDistance += edge.getWeight();
        }
        this.path = this.path.reverse();
        return path;
    }

    @Override
    public String toString() {
        if (path == null || path.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        AdjacencyList.NodeList temp = path.head;
        Node prev = temp.edge.getSourceNode();
        sb.append(prev);
        while (temp != null) {
            Node next = temp.edge.getOtherNode(prev);
            sb.append(" --> ")
                    .append(next)
                    .append(" [")
                    .append(String.format("%.2f", temp.edge.getWeight()))
                    .append("]");
            prev = next;
            temp = temp.next;
        }
        return sb.toString();
    }

    public double gettotalDistance() {
        return this.totalDistance;
    }

    public int getNumberOfSteps() {
        return this.numberOfSteps;
    }
}
