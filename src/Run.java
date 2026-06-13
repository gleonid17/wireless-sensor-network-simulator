import java.util.Scanner;

/**
 * Run class is the main entry point for the sensor network simulator.
 */
public class Run {
    /**
     * Main method to run the sensor network simulator.
     * 
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java Run <d> <filename>");
            System.exit(1);
        }
        double d = Double.parseDouble(args[0]);
        String filename = args[1];
        Graph graph = new Graph(d);
        FileParser.loadFromFile(filename, graph);
        printStartupSummary(graph, d);
        MinimumSpanningTree mst = null;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMenu();
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                printError("Invalid input. Please enter a number between 1 and 8.");
                continue;
            }
            switch (choice) {
                case 1:
                    try {
                        printSection("Minimum Spanning Tree");
                        Node root = graph.getFirstFireStation();
                        mst = new MinimumSpanningTree(graph);
                        graph.setMST(mst);
                        mst.Prim(root);
                        mst.printMST();
                    } catch (Exception e) {
                        printError("Error calculating MST: " + e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        printSection("Stored Minimum Spanning Tree");
                        if (mst == null) {
                            printWarning("MST has not been calculated. Please select option 1 first.");
                            break;
                        }
                        mst.printMST();
                    } catch (Exception e) {
                        printError("Error printing MST: " + e.getMessage());
                    }
                    break;
                case 3:
                    try {
                        printSection("Insert New Node");
                        System.out.print("Node ID: ");
                        String newId = scanner.nextLine().trim();
                        System.out.print("X coordinate: ");
                        double newX = Double.parseDouble(scanner.nextLine().trim());
                        System.out.print("Y coordinate: ");
                        double newY = Double.parseDouble(scanner.nextLine().trim());
                        System.out.print("Temperature: ");
                        double newTemp = Double.parseDouble(scanner.nextLine().trim());
                        Node newNode = new Node(newId, newX, newY, newTemp);
                        System.out.println();
                        System.out.println("Inserting node " + newId + " at (" + newX + ", " + newY + ") with temperature " + newTemp + "°C...");
                        graph.addNode(newNode);
                        if (mst != null) {
                            System.out.println("Updating MST...");
                            mst.updateMST(newNode);
                        }
                        printSuccess("Node " + newId + " added successfully!");
                    } catch (Exception e) {
                        printError("Error inserting node: " + e.getMessage());
                    }
                    break;
                case 4:
                    try {
                        printSection("Remove Node");
                        System.out.print("Node ID to remove: ");
                        String removeId = scanner.nextLine().trim();
                        Node removeNode = graph.getNode(removeId);
                        if (removeNode == null) {
                            printWarning("Node not found.");
                            break;
                        }
                        graph.removeNode(removeNode);
                        printSuccess("Node " + removeId + " has been removed.");
                        Node newRoot = graph.getFirstFireStation();
                        mst = new MinimumSpanningTree(graph);
                        graph.setMST(mst);
                        mst.Prim(newRoot);
                        System.out.println("MST recalculated after node removal.");
                    } catch (Exception e) {
                        printError("Error removing node: " + e.getMessage());
                    }
                    break;
                case 5:
                    try {
                        printSection("Change Node Temperature");
                        System.out.print("Node ID: ");
                        String tempId = scanner.nextLine().trim();
                        System.out.print("New temperature: ");
                        double newTemperature = Double.parseDouble(scanner.nextLine().trim());
                        Node tempNode = graph.getNode(tempId);
                        if (tempNode == null) {
                            printWarning("Node not found.");
                            break;
                        }
                        tempNode.setTemperature(newTemperature);
                        printSuccess("Temperature of node " + tempId + " updated to " + newTemperature + "°C.");
                    } catch (Exception e) {
                        printError("Error changing temperature: " + e.getMessage());
                    }
                    break;
                case 6:
                    try {
                        printSection("Highest Temperature Notification");
                        System.out.print("Fire station ID: ");
                        String psId = scanner.nextLine().trim();
                        Node psNode = graph.getNode(psId);
                        if (psNode == null || !psNode.isFireStation()) {
                            printWarning("Invalid fire station ID.");
                            break;
                        }
                        if (mst == null) {
                            printWarning("MST has not been calculated. Please select option 1 first.");
                            break;
                        }
                        mst.maxTemperature(psNode.getID());
                    } catch (Exception e) {
                        printError("Error notifying fire station: " + e.getMessage());
                    }
                    break;
                case 7:
                    try {
                        printSection("Fire Alert Pathfinding");
                        System.out.print("Fire station ID (A): ");
                        String fireStationId = scanner.nextLine().trim();
                        System.out.print("Alarm node ID (B): ");
                        String alarmNodeId = scanner.nextLine().trim();
                        Node fireStation = graph.getNode(fireStationId);
                        Node alarmNode = graph.getNode(alarmNodeId);
                        if (fireStation == null || alarmNode == null) {
                            printWarning("Invalid nodes provided.");
                            break;
                        }
                        Dijkstra dijkstra = new Dijkstra(graph, alarmNode, fireStation);
                        AdjacencyList path = dijkstra.dijkstra();
                        if (path == null) {
                            if (alarmNode.getTemperature() < 50) {
                                printWarning("No fire detected at node " + alarmNode.getID() + ". Temperature: " + alarmNode.getTemperature() + "°C (Threshold: 50°C)");
                            } else {
                                printWarning("No path exists between the selected nodes.");
                            }
                            break;
                        }
                        System.out.println("FIRE ALERT");
                        System.out.println("----------");
                        System.out.println("Node        : " + alarmNode.getID());
                        System.out.println("Temperature : " + alarmNode.getTemperature() + "°C (Threshold: 50°C)");
                        System.out.println();
                        System.out.println("Calculating shortest path using Dijkstra...");
                        System.out.println();
                        System.out.println("Shortest path from " + alarmNode.getID() + " to " + fireStation.getID() + ":");
                        System.out.println("--------------------------------------------------");
                        System.out.println(dijkstra.toString());
                        System.out.println();
                        System.out.println("Total Distance : " + String.format("%.2f", dijkstra.gettotalDistance()));
                        System.out.println("Number of Steps: " + dijkstra.getNumberOfSteps());
                    } catch (Exception e) {
                        printError("Error during pathfinding: " + e.getMessage());
                    }
                    break;
                case 8:
                    try {
                        printSection("Exit");
                        FileParser.saveToFile(filename, graph);
                        printSuccess("Data saved successfully.");
                        System.out.println("Exiting program...");
                        scanner.close();
                        System.exit(0);
                    } catch (Exception e) {
                        printError("Error saving file: " + e.getMessage());
                        System.exit(1);
                    }
                    break;
                default:
                    printWarning("Invalid selection. Please try again.");
            }
        }
    }

    private static void printStartupSummary(Graph graph, double d) {
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
        System.out.println();
        System.out.println("============================================================");
        System.out.println("        Wireless Sensor Network Simulator");
        System.out.println("============================================================");
        System.out.println("Total Nodes       : " + totalNodes);
        System.out.println("Fire Stations     : " + fireStations);
        System.out.println("Sensors           : " + sensors);
        System.out.println("Connection Radius : d = " + d);
        System.out.println("============================================================");
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("------------------------------------------------------------");
        System.out.println("MENU");
        System.out.println("------------------------------------------------------------");
        System.out.println("1. Calculate and print Minimum Spanning Tree (Prim)");
        System.out.println("2. Print stored Minimum Spanning Tree");
        System.out.println("3. Insert new node");
        System.out.println("4. Remove node");
        System.out.println("5. Change node temperature");
        System.out.println("6. Notify fire station of highest temperature");
        System.out.println("7. Notify fire station of potential fire");
        System.out.println("8. Save and exit");
        System.out.println("------------------------------------------------------------");
        System.out.print("Enter selection (1-8): ");
    }

    private static void printSection(String title) {
        System.out.println();
        System.out.println("============================================================");
        System.out.println(title);
        System.out.println("============================================================");
    }

    private static void printSuccess(String message) {
        System.out.println("[OK] " + message);
    }

    private static void printWarning(String message) {
        System.out.println("[!] " + message);
    }

    private static void printError(String message) {
        System.out.println("[ERROR] " + message);
    }
}