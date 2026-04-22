class RobinHoodHash {
    private class HashEntry {
        String id;
        int indexInGraph;
        int psl;
        boolean isEmpty;

        public HashEntry() { 
            isEmpty = true; 
            psl = 0; 
        }

        public HashEntry(String id, int indexInGraph, int psl) {
            this.id = id;
            this.indexInGraph = indexInGraph;
            this.psl = psl;
            this.isEmpty = false;
        }
    }
    private HashEntry[] table;
    private int size;
    private int capacity;
    private int maxPSL;

    public RobinHoodHash(int capacity) {
        this.capacity = capacity;
        table = new HashEntry[capacity];
        for (int i = 0; i < capacity; i++) table[i] = new HashEntry();
        size = 0;
        maxPSL = 0;
    }

    private long computeHash(String key) {
        long hash = 0xcbf29ce484222325L;
        long prime = 0x100000001b3L;
        for (int i = 0; i < key.length(); i++) {
            hash ^= key.charAt(i);
            hash *= prime;
        }
        return hash;
    }

    private int hash(String key) {
        return (int)(Math.abs(computeHash(key)) % capacity);
    }

    public void insert(String key, int indexInGraph) {
        if (size >= capacity * 0.9) rehash();
        HashEntry entry = new HashEntry(key, indexInGraph, 0);
        int pos = hash(key);
        while (true) {
            if (table[pos].isEmpty) {
                table[pos] = entry;
                size++;
                if (entry.psl > maxPSL) maxPSL = entry.psl;
                return;
            }
            if (entry.psl > table[pos].psl) {
                HashEntry temp = table[pos];
                table[pos] = entry;
                entry = temp;
            }
            pos = (pos + 1) % capacity;
            entry.psl++;
        }
    }

    public int find(String key) {
        int pos = hash(key);
        int psl = 0;
        while (!table[pos].isEmpty && psl <= maxPSL) {
            if (table[pos].id.equals(key)) return table[pos].indexInGraph;
            pos = (pos + 1) % capacity;
            psl++;
        }
        return -1;
    }

    public void remove(String key) {
        int pos = hash(key);
        int psl = 0;
        while (!table[pos].isEmpty && psl <= maxPSL) {
            if (table[pos].id.equals(key)) {
                shiftDelete(pos);
                size--;
                return;
            }
            pos = (pos + 1) % capacity;
            psl++;
        }
    }

    private void shiftDelete(int pos) {
        int next = (pos + 1) % capacity;
        while (!table[next].isEmpty && table[next].psl > 0) {
            table[pos] = new HashEntry(table[next].id, table[next].indexInGraph, table[next].psl - 1);
            pos = next;
            next = (next + 1) % capacity;
        }
        table[pos] = new HashEntry();
    }

    public void updateIndex(String key, int newIndexInGraph) {
        int pos = hash(key);
        int psl = 0;
        while (!table[pos].isEmpty && psl <= maxPSL) {
            if (table[pos].id.equals(key)) {
                table[pos].indexInGraph = newIndexInGraph;
                return;
            }
            pos = (pos + 1) % capacity;
            psl++;
        }
    }
    
    private void rehash() {
        HashEntry[] old = table;
        capacity *= 2;
        table = new HashEntry[capacity];
        for (int i = 0; i < capacity; i++) table[i] = new HashEntry();
        size = 0;
        maxPSL = 0;
        for (HashEntry e : old) if (!e.isEmpty) insert(e.id, e.indexInGraph);
    }
}