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
        

    public Edge remove(Edge removalEdge){
        if(isEmpty()){
            return null;
        }if (head.edge.equals(removalEdge)) {
            head = head.next;
            return removalEdge;
        }
        NodeList current = head;
        while (current.next != null) {
            if (current.next.edge.equals(removalEdge)) {
                current.next = current.next.next; 
                return removalEdge;
            }
            current = current.next;
        }
        return null; 
    }

    private class NodeList {
        public Edge edge; ;
        public NodeList next;

        public NodeList(Edge edge) {
            this.edge = edge;
            this.next = null;
        }
    }
}