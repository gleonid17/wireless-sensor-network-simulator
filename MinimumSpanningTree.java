public class MinimumSpanningTree {
    Graph graph;
    MinHeap minHeap;
    boolean[]visited;
    int closest[];
    double dist[];

    public MinimumSpanningTree(Graph graph) {
        this.graph = graph;
        this.minHeap = new MinHeap(graph.getNodeCount());
        this.visited = new boolean[graph.getNodeCount()];
        this.closest = new int[graph.getNodeCount()];
        this.dist = new double[graph.getNodeCount()];
    }

    public void Prim(Node startNode){ 
        for(int i=0; i<graph.getNodeCount(); i++){
            dist[i] = -1;
            closest[i] = 0;
            visited[i] = false;
        }
        minHeap.insertNode(startNode, 0);
        while(!minHeap.isEmpty()) {

        }
    }
}