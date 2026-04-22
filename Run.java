
import java.util.Scanner;

public class Run {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java Run <d> <filename>");
            System.exit(1);
        }
        double d = Double.parseDouble(args[0]);
        String filename = args[1];
        Graph graph = new Graph(d);
        FileParser.loadFromFile(filename, graph);
        int totalNodes = graph.getNodeCount();
        int fireStations = 0;
        int sensors = 0;
        for (int i = 0; i < totalNodes; i++) {
            if (graph.getNodes()[i].isFireStation()) {
                fireStations++;
            } else {
                sensors++;
            }
        }
        System.out.println("Total Nodes | Fire Stations | Sensors | Connection Radius (d=" + d + ")");
        System.out.println(totalNodes + " | " + fireStations + " | " + sensors + " | d=" + d);
        MinimumSpanningTree mst = null;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println();
            System.out.println("-----------------------------------------------------------------");
            System.out.println("\n=== MENU ===");
            System.out.println("1. Calculate and print Minimum Spanning Tree (Prim)");
            System.out.println("2. Print Minimum Spanning Tree");
            System.out.println("3. Insert new node");
            System.out.println("4. Remove node");
            System.out.println("5. Change node temperature");
            System.out.println("6. Notify fire station of highest temperature");
            System.out.println("7. Notify fire station of potential fire (Pathfinding)");
            System.out.println("8. Exit");
            System.out.print("Selection: ");
            System.out.println();
            System.out.println();
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number between 1 and 8.");
                continue;
            }
            switch (choice) {
                case 1:
                    try {
                        Node root = graph.getFirstFireStation();
                        mst = new MinimumSpanningTree(graph);
                        graph.setMST(mst);
                        mst.Prim(root);
                        mst.printMST();
                    } catch (Exception e) {
                        System.out.println("Error calculating MST: " + e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        if (mst == null) {
                            System.out.println("MST has not been calculated. Please select option 1 first.");
                            break;
                        }
                        mst.printMST();
                    } catch (Exception e) {
                        System.out.println("Error printing MST: " + e.getMessage());
                    }
                    break;
                case 3:
                    try {
                        System.out.print("Node ID: ");
                        String newId = scanner.nextLine().trim();
                        System.out.print("X coordinate: ");
                        double newX = Double.parseDouble(scanner.nextLine().trim());
                        System.out.print("Y coordinate: ");
                        double newY = Double.parseDouble(scanner.nextLine().trim());
                        System.out.print("Temperature: ");
                        double newTemp = Double.parseDouble(scanner.nextLine().trim());
                        Node newNode = new Node(newId, newX, newY, newTemp);
                        System.out.println("Inserting node " + newId + " at (" + newX + ", " + newY + ") with temperature " + newTemp + "°C...");
                        graph.addNode(newNode);
                        if (mst != null) {
                            mst.updateMST(newNode);
                        }
                        System.out.println("Node " + newId + " added successfully!");
                    } catch (Exception e) {
                        System.out.println("Error inserting node: " + e.getMessage());
                    }
                    break;
                case 4:
                    try {
                        System.out.print("Node ID to remove: ");
                        String removeId = scanner.nextLine().trim();
                        Node removeNode = graph.getNode(removeId);
                        if (removeNode == null) {
                            System.out.println("Node not found.");
                            break;
                        }
                        graph.removeNode(removeNode);
                        System.out.println("Node " + removeId + " has been removed.");
                        Node newRoot = graph.getFirstFireStation();
                        mst = new MinimumSpanningTree(graph);
                        graph.setMST(mst);
                        mst.Prim(newRoot);
                    } catch (Exception e) {
                        System.out.println("Error removing node: " + e.getMessage());
                    }
                    break;
                case 5:
                    try {
                        System.out.print("Node ID: ");
                        String tempId = scanner.nextLine().trim();
                        System.out.print("New temperature: ");
                        double newTemperature = Double.parseDouble(scanner.nextLine().trim());
                        Node tempNode = graph.getNode(tempId);
                        if (tempNode == null) {
                            System.out.println("Node not found.");
                            break;
                        }
                        tempNode.setTemperature(newTemperature);
                        System.out.println("Temperature of node " + tempId + " updated to " + newTemperature + "°C.");
                    } catch (Exception e) {
                        System.out.println("Error changing temperature: " + e.getMessage());
                    }
                    break;
                case 6:
                    try {
                        System.out.print("Fire station ID: ");
                        String psId = scanner.nextLine().trim();
                        Node psNode = graph.getNode(psId);
                        if (psNode == null || !psNode.isFireStation()) {
                            System.out.println("Invalid fire station ID.");
                            break;
                        }
                        if (mst == null) {
                            System.out.println("MST has not been calculated. Please select option 1 first.");
                            break;
                        }
                        mst.maxTemperature(psNode.getID());
                    } catch (Exception e) {
                        System.out.println("Error notifying fire station: " + e.getMessage());
                    }
                    break;
                case 7:
                    try {
                        System.out.print("Fire station ID (A): ");
                        String fireStationId = scanner.nextLine().trim();
                        System.out.print("Alarm node ID (B): ");
                        String alarmNodeId = scanner.nextLine().trim();
                        Node fireStation = graph.getNode(fireStationId);
                        Node alarmNode = graph.getNode(alarmNodeId);
                        if (fireStation == null || alarmNode == null) {
                            System.out.println("Invalid nodes provided.");
                            break;
                        }
                        Dijkstra dijkstra = new Dijkstra(graph, alarmNode, fireStation);
                        AdjacencyList path = dijkstra.dijkstra();
                        if (path == null) {
                            System.out.println("No path exists between the selected nodes.");
                            break;
                        }
                        System.out.println("FIRE ALERT!");
                        System.out.println("Node        : " + alarmNode.getID());
                        System.out.println("Temperature : " + alarmNode.getTemperature() + "°C (Threshold: 50°C)");
                        System.out.println("Calculating shortest path using Dijkstra...\n");
                        System.out.println("Shortest Path from " + alarmNode.getID() + " to " + fireStation.getID() + ":");
                        System.out.println("--------------------------------------");
                        System.out.println(dijkstra.toString());
                        System.out.println("\nTotal Distance : " + String.format("%.2f", dijkstra.gettotalDistance()));
                        System.out.println("Number of Steps: " + dijkstra.getNumberOfSteps());
                    } catch (Exception e) {
                        System.out.println("Error during pathfinding: " + e.getMessage());
                    }
                    break;
                case 8:
                    try {
                        FileParser.saveToFile(filename, graph);
                        System.out.println("Saving data and exiting...");
                        scanner.close();
                        System.exit(0);
                    } catch (Exception e) {
                        System.out.println("Error saving file: " + e.getMessage());
                        System.exit(1);
                    }
                default:
                    System.out.println("Invalid selection. Please try again.");
            }
        }
    }
}
