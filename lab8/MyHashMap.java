import java.util.*;

public class MyHashMap<K, V> implements Map61B<K, V> {
    private class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private double loadFactor;
    private int size;
    private ArrayList<Node>[] buckets;
    private HashSet<K> keys;

    private static final int DEFAULT_INIT_SIZE = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    public MyHashMap() {
        this(DEFAULT_INIT_SIZE, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initialSize, double loadFactor) {
        this.loadFactor = loadFactor;
        size = 0;
        buckets = (ArrayList<Node>[]) new ArrayList[initialSize];
        keys = new HashSet<>();
    }

    private int findBucket(K key, int numBuckets) {
        return Math.floorMod(key.hashCode(), numBuckets);
    }

    private int hashFunction(K key) {
        return findBucket(key, buckets.length);
    }

    private void resize(int capacity) {
        ArrayList<Node>[] temp = (ArrayList<Node>[]) new ArrayList[capacity];
        for (K key : keys) {
            int index = findBucket(key, capacity);
            if (temp[index] == null) {
                temp[index] = new ArrayList<>();
            }
            temp[index].add(getNode(key));
        }
        buckets = temp;
    }

    @Override
    public void clear() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        size = 0;
        buckets = (ArrayList<Node>[]) new ArrayList[DEFAULT_INIT_SIZE];
        keys = new HashSet<>();
    }

    @Override
    public boolean containsKey(K key) {
        return keys.contains(key);
    }

    private Node getNode(K key) {
        int hashCode = hashFunction(key);
        if (buckets[hashCode] != null) {
            for (Node n : buckets[hashCode]) {
                if (n != null && n.key.equals(key)) {
                    return n;
                }
            }
        } return null;
    }

    @Override
    public V get(K key) {
        Node n = getNode(key);
        if (n != null) {
            return n.value;
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        int hashCode = hashFunction(key);
        if (keys.contains(key)) {
            getNode(key).value = value;
        } else {
            if (size/ buckets.length > loadFactor) {
                resize(buckets.length * 2);
            }
            if (buckets[hashCode] == null) {
                buckets[hashCode] = new ArrayList<>();
            }
            buckets[hashCode].add(new Node(key, value));
            size += 1;
            keys.add(key);
        }
    }

    @Override
    public Set<K> keySet() {
        return keys;
    }

    @Override
    public Iterator<K> iterator() {
        return keys.iterator();
    }

    @Override
    public V remove(K key) {
        return null;
    }

    @Override
    public V remove(K key, V value) {
        return null;
    }
}
