# Implementation Report: Dynamic MST Management in a Graph

## 1. Introduction
This document describes the strategy for managing the Minimum Spanning Tree (MST) in a dynamic network of nodes. The main challenge was maintaining the correctness of the MST after node deletions, without requiring a full recomputation of Prim’s algorithm, which would increase the system's complexity.

## 2. "Swap-and-Pop" Strategy
To delete a node, the technique of moving the last node into the position of the deleted one is used. This approach allows deletion in $O(1)$ time (after locating the node via the Hash Index).

## 3. Analysis of the updateIndex Method
The method `updateIndex(int newIdx, int oldIdx)` is the core of the dynamic update process.

### Functionality:
1. **Dependency Update:** It searches the `parent[]` array for all nodes that had the moved node (`oldIdx`) as their parent and updates their index to the new position (`newIdx`).
2. **Data Transfer:** It copies the `weight` and `parent` from the old position to the new position.
3. **Cleanup:** It marks the old position as empty (`parent = -1`, `weight = 0`), preventing "orphaned" data.

This method allows the MST to "recognize" that a node has changed its position in the graph array, maintaining tree consistency without requiring reconstruction.

## 4. Error Handling & Correctness
To ensure stability, the following were implemented:
* **Null Check:** Before any call to `mstInstance`, a check ensures the object is initialized, avoiding `NullPointerException`.
* **Order of Operations:** The MST update occurs **before** swapping data in the graph’s `nodes[]` array. This ensures that information about the movement is available before the old index is lost.
* **Cleanup (ClearIndex):** A cleanup method was added to prevent leftover data in unused array positions.

## 5. Complexity
* **Node Deletion:** $O(V)$ in the worst case (due to searching/updating children in the MST), which is significantly more efficient than full recomputation $O(E \log V)$ using Prim’s algorithm.
* **Storage:** $O(V)$ for the `parent` and `weight` arrays.

## 6. Conclusions
The implementation achieves a balance between performance and correctness. Using index-based arrays instead of object references (`Node references`) proved to be the most suitable solution for handling dynamic changes in network size.