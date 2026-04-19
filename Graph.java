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

        for (int i = 0; i < capacity; i++) {
            adj[i] = new AdjacencyList();
        }
    }

    public int getNodeCount() {
        return nodeCount;
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
        if(nodeCount/capacity >= 0.9){
            resize();
        }
        nodes[nodeCount] = newNode;
        adj[nodeCount] = new AdjacencyList();

        for(int i=0; i<nodeCount; i++){
            if(Position.withinRange(newNode.getPosition(), nodes[i].getPosition(), d)){
                Edge newEdge = new Edge(newNode, nodes[i]);
                adj[nodeCount].insertEdge(newEdge);
                adj[i].insertEdge(newEdge);
            }
        }
        nodeCount++;
    }

    public void removeNode(Node removableNode){
        int index = index.get(removableNode.getId());
        if(index == -1){
            return;
        }
        AdjacencyList.NodeList current = adj[index].getEdges();
        while(current != null){
            Node neighbor = current.edge.getOtherNode(nodes[i]);
            int neighborIndex = index.get(neighbor.getID());
            adj[neighborIndex].remove(current.edge);
            current = current.next;
        }          

        nodes[index] = nodes[nodeCount-1];
        adj[index] = adj[nodeCount-1];
        index.remove(removableNode.getId());
        if (index != nodeCount - 1) {
            index.update(nodes[i].getID(), i);
        }
        nodes[nodeCount-1] = null;
        adj[nodeCount-1] = new AdjacencyList();
        nodeCount--;
    }

    public Node getNode(int id){
        int index = index.get(id);
        if(index == -1){
            return null;
        }
        return nodes[index];
    }

    public AdjacencyList getAdjacencyList(int id){
        int index = index.get(id);
        if(index == -1){
            return null;
        }
        return adj[index];
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