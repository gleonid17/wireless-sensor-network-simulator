public class Edge {
    private Node sourceNode;
    private Node destinationNode;
    private double weight;

    public Edge(Node sourceNode, Node destinationNode) {
        this.sourceNode = sourceNode;
        this.destinationNode = destinationNode;
        this.weight = Position.distance(sourceNode.getPosition(), destinationNode.getPosition());
    }

    public Node getSourceNode() {
        return sourceNode;
    }

    public Node getDestinationNode() {
        return destinationNode;
    }

    public double getWeight() {
        return weight;
    }

    public Node getOtherNode(Node node) throws IllegalArgumentException {
        if (node.equals(sourceNode)) 
            return this.destinationNode;
        else if (node.equals(destinationNode))
            return this.sourceNode;
        else
            throw new IllegalArgumentException("Given node is not part of this edge");
    }

    public int compareTo(Edge other) {
        return Double.compare(this.weight, other.weight);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) 
            return true;
        if (obj == null) 
            return false;
        if (getClass() != obj.getClass())
            return false;
        Edge other = (Edge) obj;
        return this.sourceNode.equals(other.sourceNode) && this.destinationNode.equals(other.destinationNode);
    }

    @Override
    public String toString() {
        return "";
    }
}