import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable, V> implements Map61B<K, V> {
    private int size;
    private Node root;

    private class Node {
        private K key;
        private V value;
        private Node left, right;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    public BSTMap() {
        root = null;
        size = 0;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public V get(K key) {
        return get(root, key);
    }

    private V get(Node n, K key) {
        if (key == null || n == null) {
            return null;
        }
        if (key.compareTo(n.key) < 0) {
            return get(n.left, key);
        }
        if (key.compareTo(n.key) > 0) {
            return get(n.right, key);
        }
        if (key.compareTo(n.key) == 0) {
            return n.value;
        } else {
            return null;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        if (size == 0) {
            root = new Node(key, value);
        }
        if (!(containsKey(key))) {
            put(root, key, value);
            size += 1;
        }
    }

    private Node put(Node n, K key, V value) {
        if (n == null) {
            return null;
        }
        if (key.compareTo(n.key) < 0) {
            n.left = put(n.left, key, value);
        }
        if (key.compareTo(n.key) > 0) {
            n.right = put(n.right, key, value);
        } else {
            n.value = value;
        }
        return n;
    }

    public void printInOrder() {
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }
}
