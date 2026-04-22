# Forest Fire Alert System

A Java-based graph simulation for managing sensor networks and fire station alerts. The system models sensor nodes and fire stations as a weighted graph, using Prim's algorithm for minimum spanning tree construction and Dijkstra's algorithm for shortest-path routing.

---

## Overview

Nodes represent either temperature sensors or fire stations (identified by IDs prefixed with `PS`). Edges are automatically created between nodes within a configurable connection radius `d`, weighted by Euclidean distance. The program supports dynamic node management, temperature monitoring, and fire alert pathfinding.

---

## Project Structure

| File | Description |
|------|-------------|
| `Run.java` | Entry point and interactive menu |
| `Graph.java` | Core graph with adjacency lists and dynamic resizing |
| `Node.java` | Represents a sensor or fire station node |
| `Edge.java` | Weighted undirected edge between two nodes |
| `AdjacencyList.java` | Linked-list based adjacency list |
| `MinHeap.java` | Min-heap priority queue used by MST and Dijkstra |
| `MinimumSpanningTree.java` | Prim's algorithm with temperature broadcast |
| `Dijkstra.java` | Shortest path from an alarm node to a fire station |
| `RobinHoodHash.java` | Robin Hood open-addressing hash map for node indexing |
| `Position.java` | 2D coordinate with distance utilities |
| `FileParser.java` | Loads and saves node data from tab-delimited files |

---

## Getting Started

### Requirements

- Java 11 or higher

### Compilation

```bash
javac *.java
```

### Running

```bash
java Run <d> <filename>
```

- `<d>` — connection radius; nodes within this Euclidean distance are connected
- `<filename>` — path to a tab-delimited input file

**Example:**

```bash
java Run 50.0 nodes.txt
```

---

## Input File Format

Each line represents one node, with fields separated by tabs:

```
<ID>    (<x>,<y>)    <temperature>
```

**Example:**

```
PS1     (10.0,20.0)     30.0
S2      (15.0,25.0)     65.0
S3      (40.0,10.0)     72.5
```

- IDs starting with `PS` are treated as fire stations
- All other IDs are treated as sensors

---

## Menu Options

Once running, the program presents an interactive menu:

```
1. Calculate and print Minimum Spanning Tree (Prim)
2. Print Minimum Spanning Tree
3. Insert new node
4. Remove node
5. Change node temperature
6. Notify fire station of highest temperature
7. Notify fire station of potential fire (Pathfinding)
8. Exit
```

### Option Details

**1 — Calculate MST:** Runs Prim's algorithm from the first fire station found. Must be run before options 2 and 6.

**3 — Insert node:** Prompts for ID, coordinates, and temperature. Automatically connects the new node to all neighbours within radius `d` and updates the MST incrementally.

**4 — Remove node:** Removes a node and all its edges, then recomputes the MST from scratch.

**6 — Notify fire station of highest temperature:** Traverses the MST from a given fire station, finds the maximum temperature across all reachable nodes, and broadcasts it back to every node in the tree.

**7 — Pathfinding (Dijkstra):** Given an alarm node B and a fire station A, finds the shortest weighted path from B to A. Only runs if the source node's temperature is ≥ 50°C.

**8 — Exit:** Saves the current graph state back to the input file before exiting.

---

## Key Design Decisions

- **Robin Hood hashing** is used to map node IDs to their array indices in O(1) average time with low variance.
- **Graph resizing** doubles capacity when the load factor exceeds 90%, copying node and adjacency list arrays.
- **MST is maintained lazily** — incremental updates are attempted on node insertion; full recomputation is triggered on node removal.
- **Dijkstra skips nodes** whose source temperature is below 50°C, treating them as non-fire events.

---

## Limitations

- The graph is undirected; edges are shared between both endpoints' adjacency lists.
- The MST's `updateMST` method uses an approximation and may not always produce the optimal spanning tree after insertion. A full recomputation via option 1 is recommended after significant changes.
- No GUI; interaction is terminal-based only.