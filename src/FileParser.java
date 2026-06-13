import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * FileParser class provides methods to load and save graph data from and to files.
 * 
 * @author Giorgos Leonidou
 * @author Andriani Mitsinga
 */

public class FileParser {
    /**
     * Loads graph data from a file.
     * 
     * @param filename The name of the file to load data from.
     * @param graph The graph to populate with loaded data.
     */
    public static void loadFromFile(String filename, Graph graph) {
        try {
            File file = new File(filename);
            Scanner in = new Scanner(file);
            StringBuilder sb = new StringBuilder();
            while (in.hasNextLine()) {
                sb.append(in.nextLine()).append("\n");
            }
            StringTokenizer tokenizer = new StringTokenizer(sb.toString(), "\t\n");
            while (tokenizer.hasMoreTokens()) {
                String id = tokenizer.nextToken();
                String position = tokenizer.nextToken();
                StringTokenizer positionTokenizer = new StringTokenizer(position, "(), ");
                double x = Double.parseDouble(positionTokenizer.nextToken());
                double y = Double.parseDouble(positionTokenizer.nextToken());
                String temperature = tokenizer.nextToken();
                double temp = Double.parseDouble(temperature);
                Node newNode = new Node(id, x, y, temp);
                graph.addNode(newNode);
            }
        } catch (Exception e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Saves graph data to a file.
     * 
     * @param filename The name of the file to save data to.
     * @param graph The graph containing the data to save.
     */
    public static void saveToFile(String filename, Graph graph) {
        try {
            FileWriter writer = new FileWriter(filename);
            Node[] nodes = graph.getNodes();
            for (int i = 0; i < graph.getNodeCount(); i++) {
                writer.write(nodes[i].getID() + "\t" + nodes[i].getPosition().toString() + "\t" + nodes[i].getTemperature() + "\n");
            }
            writer.close();
        } catch (Exception e) {
            System.err.println("Error writing file: " + e.getMessage());
            System.exit(1);
        }
    }
}