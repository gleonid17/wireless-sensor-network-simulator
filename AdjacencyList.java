public class AdjacencyList {
    public NodeList head;

    public AdjacencyList() {
        this.head = null;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public void insert(Edge newEdge){
        if(isEmpty()){
            head = new NodeList(newEdge);
        }
        else{
            NodeList temp = head;
            head = new NodeList(newEdge);
            head.next = temp;
        }
    }

    public NodeList getEdges(){
        return head;
    }
        

    public Edge remove(Edge removableEdge){
        if(isEmpty()){
            return null;
        }if (head.edge.equals(removableEdge)) {
            head = head.next;
            return removableEdge;
        }
        NodeList current = head;
        while (current.next != null) {
            if (current.next.edge.equals(removableEdge)) {
                current.next = current.next.next; 
                return removableEdge;
            }
            current = current.next;
        }
        return null; 
    }

    public boolean contains(Edge targetEdge) {
        NodeList current = head;
        while (current != null) {
            if (current.edge.equals(targetEdge)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public Edge[] toArray() {
        int count = 0;
        NodeList temp = head;
        while (temp != null) { count++; temp = temp.next; }
        
        Edge[] edges = new Edge[count];
        temp = head;
        for (int i = 0; i < count; i++) {
            edges[i] = temp.edge;
            temp = temp.next;
        }
        return edges;
    }

    public class NodeList {
        public Edge edge; ;
        public NodeList next;

        public NodeList(Edge edge) {
            this.edge = edge;
            this.next = null;
        }
    }
}