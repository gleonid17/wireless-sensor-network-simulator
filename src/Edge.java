/**
 * Edge class represents an edge in the graph, connecting two nodes with a weight.
 * The weight typically represents the distance or cost between the nodes.
 * 
 * @author Giorgos Leonidou
 * @author Andriani Mitsinga
 */

public class Edge {
    private Node sourceNode;
    private Node destinationNode;
    private double weight;

    /**
     * Constructs a new Edge with the specified source and destination nodes.
     * The weight is automatically calculated as the distance between the nodes.
     *
     * @param sourceNode The source node of the edge.
     * @param destinationNode The destination node of the edge.
     */
    public Edge(Node sourceNode, Node destinationNode) {
        this.sourceNode = sourceNode;
        this.destinationNode = destinationNode;
        this.weight = Position.distance(sourceNode.getPosition(), destinationNode.getPosition());
    }

    /**
     * Returns the source node of the edge.
     *
     * @return The source node.
     */
    public Node getSourceNode() {
        return sourceNode;
    }

    /**
     * Returns the destination node of the edge.
     *
     * @return The destination node.
     */
    public Node getDestinationNode() {
        return destinationNode;
    }

    /**
     * Returns the weight of the edge.
     *
     * @return The weight.
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Returns the other node in the edge, given one of the nodes.
     *
     * @param node The node for which to find the other node.
     * @return The other node in the edge.
     * @throws IllegalArgumentException if the given node is not part of this edge.
     */
    public Node getOtherNode(Node node) throws IllegalArgumentException {
        if (node.equals(sourceNode)) 
            return this.destinationNode;
        else if (node.equals(destinationNode))
            return this.sourceNode;
        else
            throw new IllegalArgumentException("Given node is not part of this edge");
    }

    /**
     * Compares this edge with another edge based on their weights.
     *
     * @param other The other edge to compare with.
     * @return A negative integer, zero, or a positive integer as this edge's weight is less than, equal to, or greater than the other edge's weight.
     */
    public int compareTo(Edge other) {
        return Double.compare(this.weight, other.weight);
    }

    /**
     * Checks if this edge is equal to another object.
     *
     * @param obj The object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) 
            return true;
        if (obj == null) 
            return false;
        if (getClass() != obj.getClass())
            return false;
        Edge other = (Edge) obj;
        return (this.sourceNode.equals(other.sourceNode) && this.destinationNode.equals(other.destinationNode)) || (this.sourceNode.equals(other.destinationNode) && this.destinationNode.equals(other.sourceNode));
    }

    /**
     * Returns a string representation of the edge.
     *
     * @return A string representing the edge.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(sourceNode.toString()).append(" --> ").append(destinationNode.toString()).append("[").append(this.weight).append("]");
        return sb.toString();
    }
}