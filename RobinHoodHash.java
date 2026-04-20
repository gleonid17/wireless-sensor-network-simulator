public class RobinHoodHash {

    private NodeEntry[] table;
    
    private int maxProbeLength;

    private int capacity;

    private int size;

   
    public RobinHoodHash() {
        this(7);
    }

   
    public RobinHoodHash(int capacity) {
        size = 0;
        this.capacity = capacity;
        maxProbeLength = 0;
        table = new NodeEntry[capacity];
    }


    public void put(String id, int index) {
        if (id == null) 
            return;
        if ((size + 1) / (float) capacity >= 0.9)
            rehash();
        int hashIndex = hash(id);
        insertHelper(new NodeEntry(id, index), hashIndex, 0);
        size++;
    }

    private void insertHelper(NodeEntry entry, int index, int probeLength) {
        if (table[index] == null || !table[index].isOccupied()) {
            table[index] = entry;

            if (probeLength > maxProbeLength) {
                maxProbeLength = probeLength;
            }
            return;
        }

        // Check whether to swap elements
        int key = hash(table[index].id);
        int existingProbeLength = index - key;

        if (existingProbeLength < 0)
            existingProbeLength = capacity - key + index;

        if (probeLength <= existingProbeLength) {
            // Insert the same edge at the next index
            insertHelper(entry, (index + 1) % capacity, probeLength + 1);
        } else {
            // Swap elements and insert temp at next index
            NodeEntry temp = table[index];
            table[index] = entry;

            if (probeLength > maxProbeLength) {
                maxProbeLength = probeLength;
            }

            insertHelper(temp, (index + 1) % capacity, existingProbeLength + 1);
        }
    }

    public int get(String id) {
        int key = hash(id);
        for (int i = 0; i <= maxProbeLength; i++) {
            int index = (key + i) % capacity;
            if (table[index] == null || !table[index].occupied)
                return -1;
            if (table[index].id.equals(id))
                return table[index].index;
        }
        return -1;
    }

    
    public void rehash() {
        int newCapacity = capacity * 2;
        RobinHoodHash ht = new RobinHoodHash(newCapacity);
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null && table[i].occupied)
                ht.put(table[i].id, table[i].index);
        }
        copy(ht);
    }

    private void copy(RobinHoodHash ht) {
        this.capacity = ht.capacity;
        this.maxProbeLength = ht.maxProbeLength;
        this.size = ht.size;

        this.table = new NodeEntry[ht.capacity];

        for (int i = 0; i < ht.capacity; i++) {
            this.table[i] = ht.table[i];
        }
    }

    private int hash(String id) {
        int sum = 0;
        for (int i = 0; i < id.length(); i++)
            sum += id.charAt(i);
        return sum % capacity;
    }

    public void remove(String id) {
        int key = hash(id);
        for (int i = 0; i <= maxProbeLength; i++) {
            int index = (key + i) % capacity;
            if (table[index] == null) return;
            if (table[index].id.equals(id)) {
                table[index].occupied = false;
                size--;
                return;
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < capacity; i++) {
            if (table[i] == null || !table[i].isOccupied())
                sb.append("_ ");
            else
                sb.append(table[i].id + ' ');

        }

        return sb.toString();
    }

    public void update(String id, int newIndex) {
        int key = hash(id);
        for (int i = 0; i <= maxProbeLength; i++) {
            int index = (key + i) % capacity;
            if (table[index] == null) return;
            if (table[index].id.equals(id)) {
                table[index].index = newIndex;
                return;
            }
        }
    }

    private class NodeEntry {
        public String id;
        public int index;
        public boolean occupied;
        
        public NodeEntry(String id, int index) {
            this.id = id;
            this.index = index;
            this.occupied = true;
        }

        public boolean isOccupied() {
            return occupied;
        }
    }

}