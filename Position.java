public class Position {
    private final double x;
    private final double y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public static double distance(Position a, Position b) {
        double xCalculation = Math.pow((a.getX() - b.getX()), 2);
        double yCalculation = Math.pow((a.getY() - b.getY()), 2);
        return Math.sqrt(xCalculation + yCalculation);
    }

    public static boolean withinRange(Position a, Position b, double d) {
        return distance(a, b) <= d;
    }

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

    //Didn't implement toString, left for later
    @Override
    public String toString() {
        return "";
    }
}
    
    