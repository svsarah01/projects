package bearmaps.proj2ab;

import java.util.HashMap;
import java.util.NoSuchElementException;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    private class PriorityNode {
        private T item;
        private double priority;
        private int index;

        PriorityNode(T i, double p, int x) {
            item = i;
            priority = p;
            index = x;
        }

        int getIndex() {
            return this.index;
        }

        T getItem() {
            return this.item;
        }

        Double getPriority() {
            return this.priority;
        }

        void setIndex(int x) {
            this.index = x;
        }

        void setPriority(double p) {
            this.priority = p;
        }

        boolean smaller(PriorityNode other) {
            if (other == null) {
                return true;
            }
            return this.getPriority() < other.getPriority();
        }
    }

    private PriorityNode[] items;
    private HashMap<T, PriorityNode> itemMap;
    private int size;
    private int end;

    public ArrayHeapMinPQ() {
        items = new ArrayHeapMinPQ.PriorityNode[10];
        size = 0;
        end = 1;
        itemMap = new HashMap<>();
    }

    private PriorityNode leftChild(PriorityNode node) {
        if (node == null || node.getIndex() * 2 > items.length - 1) {
            return null;
        }
        return items[node.getIndex() * 2];
    }

    private PriorityNode rightChild(PriorityNode node) {
        if (node == null || node.getIndex() * 2 > items.length - 1) {
            return null;
        }
        return items[(node.getIndex() * 2) + 1];
    }

    private PriorityNode parent(PriorityNode node) {
        return items[node.getIndex() / 2];
    }

    private void resize(int capacity) {
        if (capacity < 2) {
            capacity = 2;
        }
        PriorityNode[] temp = new ArrayHeapMinPQ.PriorityNode[capacity];
        for (int i = 0; i <= size; i++) {
            temp[i] = items[i];
        }
        items = temp;
    }

    private void swap(PriorityNode n1, PriorityNode n2) {
        if (n1.equals(n2)) {
            return;
        }
        int n1Index = n1.getIndex();
        int n2Index = n2.getIndex();
        PriorityNode temp = n1;
        items[n1Index] = n2;
        items[n2Index] = temp;
        n1.setIndex(n2Index);
        n2.setIndex(n1Index);
    }

    private void swim(PriorityNode node) {
        if (parent(node) == null) {
            return;
        }
        if (node.smaller(parent(node))) {
            swap(node, parent(node));
            swim(node);
        }
        return;
    }

    private void sink(PriorityNode node) {
        if (leftChild(node) == null && rightChild(node) == null) {
            return;
        }
        if (node.smaller(leftChild(node)) && node.smaller(rightChild(node))) {
            return;
        }
        if (leftChild(node).smaller(rightChild(node)) && leftChild(node).smaller(node)) {
            swap(node, leftChild(node));
            sink(node);
        } else if (rightChild(node).smaller(leftChild(node)) && rightChild(node).smaller(node)) {
            swap(node, rightChild(node));
            sink(node);
        }
        return;
    }

    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException();
        }
        if (size == items.length - 1) {
            resize(items.length * 2);
        }
        PriorityNode newNode = new PriorityNode(item, priority, end);
        items[end] = newNode;
        swim(newNode);
        itemMap.put(item, newNode);
        size += 1;
        end += 1;
    }

    @Override
    public boolean contains(T item) {
        return itemMap.containsKey(item);
    }

    @Override
    public T getSmallest() {
        if (items[1] == null || size < 1) {
            throw new NoSuchElementException();
        }
        return items[1].getItem();
    }

    @Override
    public T removeSmallest() {
        T smallest = getSmallest();
        if (size / (items.length * 1.0) <= 0.25) {
            resize(items.length / 2);
        }
        swap(items[1], items[size]);
        items[size] = null;
        sink(items[1]);
        size -= 1;
        end -= 1;
        itemMap.remove(smallest);
        return smallest;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException();
        }
        PriorityNode node = itemMap.get(item);
        boolean swim = priority < node.getPriority();
        node.setPriority(priority);
        if (swim) {
            swim(node);
        } else {
            sink(node);
        }
    }
}

