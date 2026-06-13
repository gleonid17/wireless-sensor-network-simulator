/**
 * AdjacencyList class represents the list of edges connected to a specific node in a graph.
 * It provides methods to insert, remove, and check for edges, as well as to convert the list to an array and reverse it.
 * The class also includes a nested NodeList class to represent each edge in the list.
 * 
 * @author Giorgos Leonidou
 * @author Andriani Mitsinga
 */

public class AdjacencyList {
    public NodeList head;
    private Node owner;

    /**
     * Constructs an empty adjacency list.
     */
    public AdjacencyList() {
        this.head = null;
        this.owner = null;
    }

    /**
     * Constructs an adjacency list with the specified owner node.
     *
     * @param owner The node that owns this adjacency list.
     */
    public AdjacencyList(Node owner) {
        this.head = null;
        this.owner = owner;
    }

    /**
     * Sets the owner node for this adjacency list.
     *
     * @param owner The node that owns this adjacency list.
     */
    public void setOwner(Node owner) {
        this.owner = owner;
    }

    /**
     * Checks if the adjacency list is empty.
     *
     * @return true if the list is empty, false otherwise.
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Inserts a new edge into the adjacency list.
     *
     * @param newEdge The edge to insert.
     */
    public void insert(Edge newEdge) {
        if (isEmpty())
            head = new NodeList(newEdge);
        else {
            NodeList temp = head;
            head = new NodeList(newEdge);
            head.next = temp;
        }
    }

    /**
     * Returns the list of edges.
     *
     * @return The list of edges.
     */
    public NodeList getEdges() {
        return head;
    }

    /**
     * Removes the specified edge from the adjacency list.
     *
     * @param removableEdge The edge to remove.
     * @return The removed edge, or null if not found.
     */
    public Edge remove(Edge removableEdge) {
        if (isEmpty())
            return null;
        if (head.edge.equals(removableEdge)) {
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

    /**
     * Checks if the adjacency list contains the specified edge.
     *
     * @param targetEdge The edge to search for.
     * @return true if the edge is found, false otherwise.
     */
    public boolean contains(Edge targetEdge) {
        NodeList current = head;
        while (current != null) {
            if (current.edge.equals(targetEdge))
                return true;
            current = current.next;
        }
        return false;
    }

    /**
     * Converts the adjacency list to an array of edges.
     *
     * @return An array containing all edges in the list.
     */
    public Edge[] toArray() {
        int count = 0;
        NodeList temp = head;
        while (temp != null) {
            count++;
            temp = temp.next;
        }
        Edge[] edges = new Edge[count];
        temp = head;
        for (int i = 0; i < count; i++) {
            edges[i] = temp.edge;
            temp = temp.next;
        }
        return edges;
    }

    /**
     * Reverses the adjacency list.
     *
     * @return A new adjacency list with the edges in reverse order.
     */
    public AdjacencyList reverse() {
        AdjacencyList reversed = new AdjacencyList(owner);
        NodeList current = head;
        while (current != null) {
            reversed.insert(current.edge);
            current = current.next;
        }
        return reversed;
    }

    /**
     * Returns a string representation of the adjacency list.
     *
     * @return A string representing the adjacency list.
     */
    @Override
    public String toString() {
        if (head == null || owner == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        NodeList temp = head;
        sb.append(owner);
        while (temp != null) {
            Node other = temp.edge.getOtherNode(owner);
            sb.append(" --> ")
              .append(other)
              .append(" [")
              .append(String.format("%.2f", temp.edge.getWeight()))
              .append("]");
            temp = temp.next;
        }
        return sb.toString();
    }

    /**
     * A node in the adjacency list.
     */
    public class NodeList {
        public Edge edge;
        public NodeList next;

        /**
         * Constructs a NodeList with the specified edge.
         *
         * @param edge The edge to store in this node.
         */
        public NodeList(Edge edge) {
            this.edge = edge;
            this.next = null;
        }
    }
}