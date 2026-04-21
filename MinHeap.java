public class MinHeap {
    private HeapNode[] heap;
    private int size;
    private int capacity;

    public MinHeap(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.heap = new HeapNode[capacity];
    }

    private int parent(int i)     { return (i - 1) / 2; }
    private int leftChild(int i)  { return 2 * i + 1; }
    private int rightChild(int i) { return 2 * i + 2; }
        
    private void swap(int i, int j) {
        HeapNode temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void insertNode(Node node, double weight) {
        heap[size] = new HeapNode(node, weight);
        size++;
        heapifyUp(size - 1);
    }

    private void heapifyUp(int index){
        while(index > 0 && heap[index].weight < heap[parent(index)].weight){
            swap(index, parent(index));
            index = parent(index);
        }
    }

    public HeapNode extractMin(){
        if(isEmpty()){
            return null;
        }
        HeapNode min = heap[0];
        heap[0] = heap[size - 1];
        size--;
        PercolateDown(0);
        return min;
    }

    private void PercolateDown(int index){
        int left = leftChild(index);
        int right = rightChild(index);
        int smallest = index;

        if(left < size && heap[left].weight < heap[smallest].weight)
            smallest = left;
        if(right < size && heap[right].weight < heap[smallest].weight)
            smallest = right;
        if(smallest != index){
            swap(index, smallest);
            PercolateDown(smallest);
        }
    }

    public static class HeapNode {
        Node node;
        double weight;
        
        public HeapNode(Node node, double weight) {
            this.node = node;
            this.weight = weight;
        }
    }
}