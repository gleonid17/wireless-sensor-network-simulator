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

    public void insertNode(Node node, double key) {
        heap[size] = new HeapNode(node, key);
        size++;
        heapifyUp(size - 1);
    }

    private void heapifyUp(int index){
        while(index > 0 && heap[index].key < heap[parent(index)].key){
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
        heapifyDown(0);
        return min;
    }

    private void heapifyDown(int index){
        int left = leftChild(index);
        int right = rightChild(index);
        int smallest = index;

        if(left < size && heap[left].key < heap[smallest].key)
            smallest = left;
        if(right < size && heap[right].key < heap[smallest].key)
            smallest = right;
        if(smallest != index){
            swap(index, smallest);
            heapifyDown(smallest);
        }
    }

    public static class HeapNode {
        Node node;
        double key;
        
        public HeapNode(Node node, double key) {
            this.node = node;
            this.key = key;
        }
    }
}