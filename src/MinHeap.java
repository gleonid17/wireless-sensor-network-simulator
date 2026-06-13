/**
 * MinHeap class implements a minimum heap data structure to efficiently manage nodes based on their weights.
 * 
 * @author Giorgos Leonidou
 * @author Andriani Mitsinga
 */

public class MinHeap {
    private HeapNode[] heap;
    private int size;
    private int capacity;

    /**
     * Constructs a new MinHeap with the specified capacity.
     * 
     * @param capacity The maximum number of nodes the heap can hold.
     */
    public MinHeap(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.heap = new HeapNode[capacity];
    }

    /**
     * Returns the index of the parent node of the node at the specified index.
     * 
     * @param i The index of the node.
     * @return The index of the parent node.
     */
    private int parent(int i) { 
        return (i - 1) / 2; 
    }

    /**
     * Returns the index of the left child of the node at the specified index.
     * 
     * @param i The index of the node.
     * @return The index of the left child node.
     */
    private int leftChild(int i) { 
        return 2 * i + 1; 
    }

    /**
     * Returns the index of the right child of the node at the specified index.
     * 
     * @param i The index of the node.
     * @return The index of the right child node.
     */
    private int rightChild(int i) { 
        return 2 * i + 2; 
    }
    
    /**
     * Swaps the nodes at the specified indices in the heap.
     * 
     * @param i The index of the first node.
     * @param j The index of the second node.
     */
    private void swap(int i, int j) {
        HeapNode temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    /**
     * Checks if the heap is empty.
     * 
     * @return true if the heap is empty, false otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Inserts a new node into the heap with the specified weight.
     * 
     * @param node The node to insert.
     * @param weight The weight of the node.
     */
    public void insertNode(Node node, double weight) {
        heap[size] = new HeapNode(node, weight);
        size++;
        percolateUp(size - 1);
    }

    /**
     * Maintains the heap property by moving the node at the specified index up the heap.
     * 
     * @param index The index of the node to heapify up.
     */
    private void percolateUp(int index){
        while(index > 0 && heap[index].weight < heap[parent(index)].weight){
            swap(index, parent(index));
            index = parent(index);
        }
    }

    /**
     * Extracts the minimum node from the heap.
     * 
     * @return The minimum node, or null if the heap is empty.
     */
    public HeapNode extractMin(){
        if(isEmpty())
            return null;
        HeapNode min = heap[0];
        heap[0] = heap[size - 1];
        size--;
        percolateDown(0);
        return min;
    }

    /**
     * Maintains the heap property by moving the node at the specified index down the heap.
     * 
     * @param index The index of the node to heapify down.
     */
    private void percolateDown(int index){
        int left = leftChild(index);
        int right = rightChild(index);
        int smallest = index;
        if(left < size && heap[left].weight < heap[smallest].weight)
            smallest = left;
        if(right < size && heap[right].weight < heap[smallest].weight)
            smallest = right;
        if(smallest != index){
            swap(index, smallest);
            percolateDown(smallest);
        }
    }

    /**
     * Represents a node in the heap.
     */
    public static class HeapNode {
        Node node;
        double weight;
        
        /**
         * Constructs a HeapNode with the specified node and weight.
         * 
         * @param node The node to store in the heap.
         * @param weight The weight of the node.
         */
        public HeapNode(Node node, double weight) {
            this.node = node;
            this.weight = weight;
        }
    }
}