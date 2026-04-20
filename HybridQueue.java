public class HybridQueue {
    private class HybridNode {
        Node[] data;
        int first;
        int last;
        HybridNode next;

        public HybridNode(int capacity) {
            this.data = new Node[capacity];
            this.first = 0;
            this.last = 0;
            this.next = null;
        }

        public boolean isEmpty() {
            return this.first == this.last;
        }

        public boolean isFull() {
            return this.last == this.data.length;
        }
    }

    private HybridNode frontNode;
    private HybridNode rearNode;
    private int capacity;
    private int totalSize;

    public HybridQueue(int capacity) {
        this.frontNode = new HybridNode(capacity);
        this.rearNode = this.frontNode;
        this.capacity = capacity;
        this.totalSize = 0;
    }

    public void enqueue(Node value) {
        if (!rearNode.isFull()) {
            rearNode.data[rearNode.last++] = value;
        } else {
            rearNode.next = new HybridNode(capacity);
            rearNode = rearNode.next;
            rearNode.data[rearNode.last++] = value;
        }
        totalSize++;
    }

    public Node dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        Node value = frontNode.data[frontNode.first];
        frontNode.data[frontNode.first] = null; // avoid memory leak
        frontNode.first++;
        totalSize--;
        if (frontNode.isEmpty()) {
            if (frontNode.next != null) {
                frontNode = frontNode.next;
            } else {
                frontNode.first = 0;
                frontNode.last = 0;
                rearNode = frontNode;
            }
        }
        return value;
    }

    public Node front() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        return frontNode.data[frontNode.first];
    }

    public boolean isEmpty() {
        return totalSize == 0;
    }

    public int size() {
        return totalSize;
    }
}