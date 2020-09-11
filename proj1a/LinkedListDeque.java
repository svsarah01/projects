public class LinkedListDeque<T> {
    private class DNode {
        private DNode prev;
        private T item;
        private DNode next;

        DNode(DNode p, T i, DNode n) {
            prev = p;
            item = i;
            next = n;
        }
    }
    private T recursiveHelper(DNode n, int index) {
        if (index == 0) {
            return n.item;
        }
        return recursiveHelper(n.next, index - 1);
    }

    private int size;
    private DNode sentinel;


    public boolean isEmpty() {
        return (size == 0);
    }

    public LinkedListDeque() {
        sentinel = new DNode(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(T item) {
        if (size == 0) {
            sentinel.next = new DNode(sentinel, item, sentinel.prev);
            sentinel.prev = sentinel.next;
        } else {
            sentinel.next.prev = new DNode(sentinel, item, sentinel.next);
            sentinel.next = sentinel.next.prev;
        }
        size += 1;
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T first = sentinel.next.item;
        sentinel.next.item = null;
        sentinel.next.next.prev = sentinel;
        sentinel.next = sentinel.next.next;
        size -= 1;
        return first;
    }

    public void addLast(T item) {
        sentinel.prev.next = new DNode(sentinel.prev, item, sentinel);
        sentinel.prev = sentinel.prev.next;
        size += 1;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T last = sentinel.prev.item;
        sentinel.prev.item = null;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        size -= 1;
        return last;
    }

    public T get(int index) {

        DNode p = sentinel.next;

        for (int i = 0; i <= index; i = i + 1) {
            if (i == index) {
                return p.item;
            }
            p = p.next;
        }
        return null;
    }

    public T getRecursive(int index) {
        DNode p = sentinel.next;
        return recursiveHelper(p, index);
    }

    public void printDeque() {
        for (int i = 0; i < size; i = i + 1) {
            System.out.print(get(i) + " ");
        }
        System.out.println();
    }

}
