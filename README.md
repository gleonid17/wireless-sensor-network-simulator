# Wireless Sensor Network Simulator

A Java-based simulation of a wireless sensor network designed for environmental monitoring and forest fire detection.

The system models sensors and fire stations as nodes in a weighted undirected graph. Connections between nodes are established based on communication range, and graph algorithms are used to support routing, network maintenance, and emergency response operations.

Originally developed as a university Data Structures and Algorithms project and later refactored for portfolio presentation.



# Features

## Network Construction

- Creates a wireless sensor network from node data.
- Models sensors and fire stations as graph vertices.
- Automatically creates communication links based on Euclidean distance.
- Stores network topology using adjacency lists.

## Dynamic Network Management

- Add new sensor nodes.
- Remove existing nodes.
- Update node temperature readings.
- Recalculate network structures when topology changes.

## Minimum Spanning Tree (Prim's Algorithm)

- Constructs a Minimum Spanning Tree (MST) of the network.
- Minimizes communication cost between connected nodes.
- Supports dynamic updates when nodes are inserted or removed.

## Shortest Path Search (Dijkstra's Algorithm)

- Finds the shortest communication path between nodes.
- Calculates minimum transmission distance through the network.

## Temperature Monitoring

- Tracks temperature readings for every sensor.
- Propagates temperature information through the MST.
- Identifies the highest recorded temperature in the network.

## Fire Detection and Alerting

- Detects dangerous temperature levels.
- Sends alerts to the nearest fire station when temperatures exceed predefined thresholds.
- Simulates emergency response communication.



# Project Overview

The network consists of two types of nodes:

## Sensor Nodes

Regular monitoring devices that:

- Record temperature measurements
- Communicate with nearby sensors
- Participate in routing operations

## Fire Stations

Special nodes identified by IDs beginning with PS.

Fire stations act as emergency response centers and receive alerts from the network when abnormal temperatures are detected.





## Vertices

Each vertex represents:

- A sensor node
- A fire station

Every node stores:

- Unique ID
- Position (x, y)
- Temperature

## Edges

An edge exists between two nodes when:

```text
Distance(nodeA, nodeB) < d
```

where:

```text
d = communication range
```

The edge weight is the Euclidean distance between the two nodes.

### Distance Formula

```text
distance = √((x1 - x2)² + (y1 - y2)²)
```


# Algorithms

## Prim's Algorithm

Used to construct the Minimum Spanning Tree.

Purpose:

- Reduce total communication cost
- Maintain efficient connectivity
- Support temperature broadcasting

Complexity:

| Operation | Complexity |
|------------|------------|
| MST Construction | O(E log V) |



## Dijkstra's Algorithm

Used to find shortest communication paths.

Purpose:

- Routing between nodes
- Alert delivery
- Network navigation

Complexity:

| Operation | Complexity |
|------------|------------|
| Shortest Path | O(E log V) |



# Data Structures



## Graph

Stores the complete wireless sensor network.

Responsibilities:

- Node insertion
- Node removal
- Edge creation
- Connectivity management

File:

```text
Graph.java
```



## Adjacency List

Stores neighboring nodes for each vertex.

Used to represent graph connections efficiently.

File:

```text
AdjacencyList.java
```


## Minimum Spanning Tree

Maintains the MST generated using Prim's Algorithm.

Responsibilities:

- MST construction
- Dynamic updates
- Temperature propagation

File:

```text
MinimumSpanningTree.java
```





## Min Heap

Priority queue used by graph algorithms.

Responsibilities:

- Node prioritization
- Efficient minimum extraction

File:

```text
MinHeap.java
```


## Robin Hood Hash Table

Custom hash table implementation used for fast node lookups.

Advantages:

- Reduced clustering
- Fast searches
- Predictable probe lengths

File:

```text
RobinHoodHash.java
```


# Repository Structure

```text
.
├── src/
│   ├── Graph.java
│   ├── AdjacencyList.java
│   ├── Node.java
│   ├── Position.java
│   ├── Edge.java
│   ├── MinimumSpanningTree.java
│   ├── Dijkstra.java
│   ├── MinHeap.java
│   ├── RobinHoodHash.java
│   ├── Run.java
│   └── ...
│
├── sampleGraph.txt
├── README.md
├── LICENSE
└── .gitignore
```



# Input Format

The simulator loads nodes from a text file.

Example:

```text
PS1 0 0 20
S1 10 15 22
S2 18 25 24
S3 30 40 19
```

Where:

| Field | Description |
|---------|-------------|
| ID | Node identifier |
| X | X coordinate |
| Y | Y coordinate |
| Temperature | Current temperature |

Nodes whose IDs begin with:

```text
PS
```

are treated as fire stations.


## Requirements

### Java Version

This project requires:

```text
Java 8 or later

```

You can verify your installation by running:

```bash
java -version
javac -version

```

If both commands return version information, Java is correctly installed.

---
### Installing Java

#### Windows

1.  Download the latest Java Development Kit (JDK) from Oracle or OpenJDK.
    
2.  Run the installer.
    
3.  Open Command Prompt and verify the installation:
    

```cmd
java -version
javac -version

```

If the commands are not recognized, restart your terminal or ensure Java has been added to your system PATH.

----

#### macOS

Using Homebrew:

```bash
brew install openjdk

```

Verify the installation:

```bash
java -version
javac -version

```

Alternatively, Java can be installed through Oracle's official installer.

----

#### Ubuntu / Debian

```bash
sudo apt update
sudo apt install default-jdk

```

Verify:

```bash
java -version
javac -version

```
-------------

#### Fedora

```bash
sudo dnf install java-latest-openjdk-devel

```

Verify:

```bash
java -version
javac -version

```

-----
#### Arch Linux

```bash
sudo pacman -S jdk-openjdk

```

Verify:

```bash
java -version
javac -version
```



# Compilation

Compile all source files:

```bash
javac src/*.java
```

Windows PowerShell:

```powershell
javac src\*.java
```



# Running

Run the simulator:

```bash
java -cp src Run <d> <filename>
```

Example:

```bash
java -cp src Run 150 sampleGraph.txt
```

Where:

- `d` is the communication radius used to determine connectivity between nodes.
- `filename` is the input file containing the network definition.



# Example Operations

The simulator supports operations such as:

- Display network topology
- Build and display MST
- Find shortest path between nodes
- Add sensor nodes
- Remove nodes
- Update temperature values
- Report maximum temperature
- Trigger fire alerts



# Time Complexity Summary

| Operation | Complexity |
|------------|------------|
| Add Node | O(V) |
| Remove Node | O(V + E) |
| Find Node | O(1) average |
| Build MST | O(E log V) |
| Shortest Path | O(E log V) |
| Temperature Broadcast | O(V) |

Where:

- V = number of vertices
- E = number of edges



# Concepts Demonstrated

- Graph Theory
- Adjacency Lists
- Weighted Graphs
- Minimum Spanning Trees
- Prim's Algorithm
- Dijkstra's Algorithm
- Robin Hood Hashing
- Priority Queues
- Heaps
- Dynamic Graph Updates
- Wireless Sensor Networks
- Forest Fire Detection
- Object-Oriented Programming
- Java



# Notes

This repository was originally developed as part of a university project focused on graph-based network simulation and algorithm design.

The codebase has been cleaned and refactored for public release while preserving the original implementation of the underlying data structures and algorithms.



# Authors

George Leonidou  
Andriani Mitsinga



# License

# License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
