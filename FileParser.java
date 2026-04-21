import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.StringTokenizer;

public class FileParser {
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