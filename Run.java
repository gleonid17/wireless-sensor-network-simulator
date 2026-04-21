public class Run {
    public static void main(String[] args) {
        double d = Double.parseDouble(args[0]);
        String filename = args[1];
        Graph graph = new Graph(d);
        FileParser.loadFromFile(filename, graph);
        
    }
}