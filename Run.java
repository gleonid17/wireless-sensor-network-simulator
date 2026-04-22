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
        System.out.println("Συνολικός Αρ. κόμβων | Αρ. Σταθμών | Αρ. Αισθητήρων | Απόσταση δημιουργίας συνδέσεων (d=" + d + ")");
        System.out.println(totalNodes + " | " + fireStations + " | " + sensors + " | d=" + d);
        MinimumSpanningTree mst;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== ΜΕΝΟΥ ===");
            System.out.println("1. Υπολογισμός και εκτύπωση ελάχιστου γεννητορικού δένδρου (Prim)");
            System.out.println("2. Εκτύπωση ελάχιστου γεννητορικού δένδρου");
            System.out.println("3. Εισαγωγή νέου κόμβου");
            System.out.println("4. Αποχώρηση κόμβου");
            System.out.println("5. Αλλαγή θερμοκρασίας κόμβου");
            System.out.println("6. Ενημέρωση πυροσβεστικού κέντρου για υψηλότερη θερμοκρασία");
            System.out.println("7. Ενημέρωση πυροσβεστικού κέντρου για πιθανή πυρκαγιά");
            System.out.println("8. Έξοδος");
            System.out.print("Επιλογή: ");
            int choice = Integer.parseInt(scanner.nextLine().trim());
            switch (choice) {
                case 1:
                    Node root = graph.getFirstFireStation();
                    mst = new MinimumSpanningTree(graph);
                    mst.Prim(root);
                    mst.printMST();
                    break;
                case 2:
                    if (mst == null) {
                        System.out.println("Δεν έχει υπολογιστεί MST. Εκτελέστε πρώτα την επιλογή 1.");
                        break;
                    }
                    mst.printMST();
                    break;
                case 3:
                    System.out.print("Ταυτότητα κόμβου: ");
                    String newId = scanner.nextLine().trim();
                    System.out.print("Συντεταγμένες x: ");
                    double newX = Double.parseDouble(scanner.nextLine().trim());
                    System.out.print("Συντεταγμένες y: ");
                    double newY = Double.parseDouble(scanner.nextLine().trim());
                    System.out.print("Θερμοκρασία: ");
                    double newTemp = Double.parseDouble(scanner.nextLine().trim());
                    Node newNode = new Node(newId, newX, newY, newTemp);
                    System.out.println("Εισαγωγή κόμβου " + newId + " στη θέση (" + newX + ", " + newY + ") με θερμοκρασία " + newTemp + "°C...");
                    graph.addNode(newNode);
                    if (mst != null) mst.updateMST(newNode);
                    break;
                case 4:
                    System.out.print("Ταυτότητα κόμβου: ");
                    String removeId = scanner.nextLine().trim();
                    Node removeNode = graph.getNode(removeId);
                    if (removeNode == null) {
                        System.out.println("Κόμβος δεν βρέθηκε.");
                        break;
                    }
                    graph.removeNode(removeNode);
                    Node newRoot = graph.getFirstFireStation();
                    mst = new MinimumSpanningTree(graph);
                    mst.Prim(newRoot);
                    break;
                case 5:
                    System.out.print("Ταυτότητα κόμβου: ");
                    String tempId = scanner.nextLine().trim();
                    System.out.print("Νέα θερμοκρασία: ");
                    double newTemperature = Double.parseDouble(scanner.nextLine().trim());
                    Node tempNode = graph.getNode(tempId);
                    if (tempNode == null) {
                        System.out.println("Κόμβος δεν βρέθηκε.");
                        break;
                    }
                    tempNode.setTemperature(newTemperature);
                    System.out.println("Θερμοκρασία κόμβου " + tempId + " ενημερώθηκε σε " + newTemperature + "°C.");
                    break;
                case 6:
                    System.out.print("Ταυτότητα πυροσβεστικού κέντρου: ");
                    String psId = scanner.nextLine().trim();
                    Node psNode = graph.getNode(psId);
                    if (psNode == null || !psNode.isFireStation()) {
                        System.out.println("Μη έγκυρο πυροσβεστικό κέντρο.");
                        break;
                    }
                    if (mst == null) {
                        System.out.println("Δεν έχει υπολογιστεί MST. Εκτελέστε πρώτα την επιλογή 1.");
                        break;
                    }
                    mst.maxTemperature(psNode);
                    break;
                case 7:
                    System.out.print("Ταυτότητα πυροσβεστικού κέντρου (Α): ");
                    String fireStationId = scanner.nextLine().trim();
                    System.out.print("Ταυτότητα κόμβου συναγερμού (Β): ");
                    String alarmNodeId = scanner.nextLine().trim();
                    Node fireStation = graph.getNode(fireStationId);
                    Node alarmNode = graph.getNode(alarmNodeId);
                    if (fireStation == null || alarmNode == null) {
                        System.out.println("Μη έγκυροι κόμβοι.");
                        break;
                    }
                    Dijkstra dijkstra = new Dijkstra(graph, alarmNode, fireStation);
                    AdjacencyList path = dijkstra.dijkstra();
                    if (path == null) {
                        System.out.println("Δεν υπάρχει μονοπάτι.");
                        break;
                    }
                    dijkstra.toString();
                    break;
                case 8:
                    FileParser.saveToFile(filename, graph);
                    System.out.println("Αποθήκευση και έξοδος...");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Μη έγκυρη επιλογή.");
            }
        }
    }
}