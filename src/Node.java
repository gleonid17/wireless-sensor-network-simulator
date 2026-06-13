/**
 * Node class represents a node in the sensor network graph.
 * Each node has a unique ID, a position, and a temperature reading.
 * 
 * @author Giorgos Leonidou
 * @author Andriani Mitsinga
 */

public class Node {
    private final String id;
    private final Position position;
    private double temperature;
    private final boolean isFireStation; //determined by ID starting with "PS"

    /**
     * Constructs a Node with the specified ID, position, and temperature.
     * 
     * @param id The unique ID of the node.
     * @param x The x-coordinate of the node's position.
     * @param y The y-coordinate of the node's position.
     * @param temperature The temperature reading of the node.
     */
    public Node(String id, double x, double y, double temperature) {
        this.id = id;
        this.position = new Position(x, y);
        this.temperature = temperature;
        this.isFireStation = id.startsWith("PS");
    }

    /**
     * Returns the unique ID of the node.
     * 
     * @return The unique ID.
     */
    public String getID() {
        return id;
    }

    /**
     * Returns the position of the node.
     * 
     * @return The position.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Returns the temperature reading of the node.
     * 
     * @return The temperature.
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     * Sets the temperature reading of the node.
     * 
     * @param temperature The new temperature reading.
     */
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    /**
     * Checks if the node is a fire station.
     * 
     * @return true if the node is a fire station, false otherwise.
     */
    public boolean isFireStation() {
        return isFireStation;
    }

    /**
     * Checks if this node is equal to another object.
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
        Node other = (Node) obj;
        return this.id.equals(other.id) && this.position.equals(other.position);
    }
    
    /**
     * Returns a string representation of the node.
     * 
     * @return A string representing the node.
     */
    @Override
    public String toString() {
        return this.id;
    }
}