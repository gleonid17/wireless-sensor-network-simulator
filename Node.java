public class Node {
    private final String id;
    private final Position position;
    private double temperature;
    private final boolean isFireStation; //determined by ID starting with "PS"

    public Node(String id, double x, double y, double temperature) {
        this.id = id;
        this.position = new Position(x, y);
        this.temperature = temperature;
        this.isFireStation = id.startsWith("PS");
    }

    public String getID() {
        return id;
    }

    public Position getPosition() {
        return position;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public boolean isFireStation() {
        return isFireStation;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) 
            return true;
        if (obj == null) 
            return false;
        if (getClass() != obj.getClass())
            return false;
        Node other = (Node) obj;
        return this.id.equals(other.id) && this.position.equals(other.position) && Double.compare(this.temperature, other.temperature) == 0 && Boolean.compare(this.isFireStation, other.isFireStation) == 0;
    }

    //Didn't implement toString, left for later
    @Override
    public String toString() {
        return this.id;
    }
}