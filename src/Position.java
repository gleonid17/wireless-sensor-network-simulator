/**
 * Position class represents the coordinates of a node in the sensor network.
 * It provides methods to calculate the distance between two positions and to check if they are within a certain range.
 * 
 * @author Giorgos Leonidou
 * @author Andriani Mitsinga
 */

public class Position {
    private final double x;
    private final double y;

    /**
     * Constructs a Position with the specified coordinates.
     * 
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x-coordinate of the position.
     * 
     * @return The x-coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of the position.
     * 
     * @return The y-coordinate.
     */
    public double getY() {
        return y;
    }

    /**
     * Calculates the Euclidean distance between two positions.
     * 
     * @param a The first position.
     * @param b The second position.
     * @return The distance between the two positions.
     */
    public static double distance(Position a, Position b) {
        double xCalculation = Math.pow((a.getX() - b.getX()), 2);
        double yCalculation = Math.pow((a.getY() - b.getY()), 2);
        return Math.sqrt(xCalculation + yCalculation);
    }

    /**
     * Checks if two positions are within a certain range.
     * 
     * @param a The first position.
     * @param b The second position.
     * @param d The maximum distance.
     * @return true if the positions are within the range, false otherwise.
     */
    public static boolean withinRange(Position a, Position b, double d) {
        return distance(a, b) < d;
    }

    /**
     * Checks if this position is equal to another object.
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

        Position other = (Position) obj;
        return Double.compare(this.x, other.x) == 0 && Double.compare(this.y, other.y) == 0;
    }

    /**
     * Returns a string representation of the position.
     * 
     * @return A string representing the position.
     */
    @Override
    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }
}