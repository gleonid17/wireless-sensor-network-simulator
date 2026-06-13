/**
 * RobinHoodHash class implements a hash table using the Robin Hood hashing algorithm.
 * It provides methods for inserting, finding, removing, and updating entries in the hash table.
 * The hash table automatically resizes when the load factor exceeds a certain threshold.
 * 
 * @author Giorgos Leonidou
 * @author Andriani Mitsinga
 */

class RobinHoodHash {
    /**
     * HashEntry class represents an entry in the hash table, containing the key (id), 
     * the index in the graph, the probe sequence length (psl), and a flag to indicate if the entry is empty.
     */
    private class HashEntry {
        String id;
        int indexInGraph;
        int psl;
        boolean isEmpty;

        /**
         * Default constructor initializes an empty hash entry with psl set to 0.
         */
        public HashEntry() { 
            isEmpty = true; 
            psl = 0; 
        }

        /**
         * Constructor for creating a hash entry with a key, index in the graph, and probe sequence length.
         * 
         * @param id The key for the hash entry.
         * @param indexInGraph The index in the graph.
         * @param psl The probe sequence length.
         */
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

    /**
     * Constructor for creating a RobinHoodHash instance with a specified capacity.
     * 
     * @param capacity The initial capacity of the hash table.
     */
    public RobinHoodHash(int capacity) {
        this.capacity = capacity;
        table = new HashEntry[capacity];
        for (int i = 0; i < capacity; i++) table[i] = new HashEntry();
        size = 0;
        maxPSL = 0;
    }

    /**
     * Computes the hash value for a given key.
     * 
     * @param key The key for which to compute the hash.
     * @return The hash value.
     */
    private long computeHash(String key) {
        long hash = 0xcbf29ce484222325L;
        long prime = 0x100000001b3L;
        for (int i = 0; i < key.length(); i++) {
            hash ^= key.charAt(i);
            hash *= prime;
        }
        return hash;
    }

    /**
     * Computes the hash index for a given key.
     * 
     * @param key The key for which to compute the hash index.
     * @return The hash index.
     */
    private int hash(String key) {
        return (int)(Math.abs(computeHash(key)) % capacity);
    }

    /**
     * Inserts a key-value pair into the hash table.
     * 
     * @param key The key to insert.
     * @param indexInGraph The index in the graph.
     */
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

    /**
     * Finds the index in the graph for a given key.
     * 
     * @param key The key to search for.
     * @return The index in the graph if the key is found, otherwise -1.
     */
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

    /**
     * Removes a key-value pair from the hash table.
     * 
     * @param key The key to remove.
     */
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

    /**
     * Shifts elements to fill the gap left by a deleted entry.
     * 
     * @param pos The position of the deleted entry.
     */
    private void shiftDelete(int pos) {
        int next = (pos + 1) % capacity;
        while (!table[next].isEmpty && table[next].psl > 0) {
            table[pos] = new HashEntry(table[next].id, table[next].indexInGraph, table[next].psl - 1);
            pos = next;
            next = (next + 1) % capacity;
        }
        table[pos] = new HashEntry();
    }

    /**
     * Updates the index in the graph for a given key.
     * 
     * @param key The key for which to update the index.
     * @param newIndexInGraph The new index in the graph.
     */
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
    
    /**
     * Rehashes the hash table when the load factor exceeds the threshold.
     */
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