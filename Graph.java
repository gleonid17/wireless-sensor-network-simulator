public class Graph {
    private Node[] nodes;          // dynamic array, ξεκινά με capacity=7
    private AdjacencyList[] adj;   // ίδιο μέγεθος με nodes[]
    private RobinHoodHash index;   // id → θέση στο nodes[] για O(1) lookup
    private int nodeCount;         // τρέχων αριθμός κόμβων
    private int capacity;          // τρέχουσα χωρητικότητα (ξεκινά 7)
    private double d;              // μέγιστη απόσταση για ακμή

    
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

    public int getNodeCount() {
        return nodeCount;
    }

    public RobinHoodHash getIndex() {
        return index;
    }

    private void resize() {
        int newCapacity = capacity * 2;
        Node[] newNodes = new Node[newCapacity];
        AdjacencyList[] newAdj = new AdjacencyList[newCapacity];

        for (int i = 0; i < nodeCount; i++) {
            newNodes[i] = nodes[i];
            newAdj[i] = adj[i];
        }
        for (int i = nodeCount; i < newCapacity; i++) {
            newAdj[i] = new AdjacencyList();
        }

        this.nodes = newNodes;
        this.adj = newAdj;
        this.capacity = newCapacity;
    }

    public void addNode(Node newNode){
        if((nodeCount + 1.0)/capacity >= 0.9){
            resize();
        }
        nodes[nodeCount] = newNode;
        adj[nodeCount] = new AdjacencyList();
        index.put(newNode.getID(), nodeCount);

        for(int i=0; i<nodeCount; i++){
            if(Position.withinRange(newNode.getPosition(), nodes[i].getPosition(), d)){
                Edge newEdge = new Edge(newNode, nodes[i]);
                adj[nodeCount].insert(newEdge);
                adj[i].insert(newEdge);
            }
        }
        nodeCount++;
    }

    public void removeNode(Node removableNode){
        int i = this.index.get(removableNode.getID());
        if(i == -1){
            return;
        }
        Edge[] edges = adj[i].toArray();
        for(Edge e : edges){
            Node neighbor = e.getOtherNode(nodes[i]);
            int neighborIndex = this.index.get(neighbor.getID());
            adj[neighborIndex].remove(e);
        }          

        nodes[i] = nodes[nodeCount-1];
        adj[i] = adj[nodeCount-1];
        index.remove(removableNode.getID());
        if (i != nodeCount - 1) {
            index.update(nodes[i].getID(), i);
        }
        nodes[nodeCount-1] = null;
        adj[nodeCount-1] = new AdjacencyList();
        nodeCount--;
    }

    public Node getNode(String id){
        int i = this.index.get(id);
        if(i == -1){
            return null;
        }
        return nodes[i];
    }

    public AdjacencyList getAdjacencyList(Node node){
        int i = this.index.get(node.getID());
        if(i == -1){    
            return null;
        }
        return adj[i];
    }

    public Node[] getNodes(){
        return nodes;   
    }

    public Node getFirstFireStation(){
        for(int i=0; i<nodeCount; i++){
            if(nodes[i].isFireStation()){
                return nodes[i];
            }
        }
        return null;
    }
}