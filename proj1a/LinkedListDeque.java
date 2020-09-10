public class LinkedListDeque<T> {
    private class DNode {
        public DNode prev;
        public T item;
        public DNode next;

        public DNode(DNode p, T i, DNode n) {
            prev = p;
            item = i;
            next = n;
//            not sure if prev is correct but we are doing placeholders until i sort shit out
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

//    am i supposed to implement this using size? or checking sentinel.next? or does having a sentinel mean it isn't empty to begin w/?
    public boolean isEmpty() {
        return (size == 0);
    }

    public LinkedListDeque() {
        sentinel = new DNode(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

//    public LinkedListDeque(T item) {
//        sentinel = new DNode(sentinel, null, sentinel);
////        i should check if ^ meets the 61b style guide, pls sarah remember this
//        sentinel.next = new DNode(sentinel, item, sentinel.prev);
//        sentinel.prev = sentinel.next;
//        size = 1;
//    }

    public int size() {
        return size;
    }

    public void addFirst(T item) {
        if (size == 0) {
            sentinel.next = new DNode(sentinel, item, sentinel.prev);
            sentinel.prev = sentinel.next;
//            i want to call linkedlistdeque^ but idk how to
        }
        else {
            sentinel.next = new DNode(sentinel, item, sentinel.next);
        }
        size += 1;
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T first = sentinel.next.item;
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

//        if (index == 0) {
//            return sentinel.next.item;
//        } i need to make a method in DNode to do this implementation ???
//        return sentinel.next.getRecursive(index - 1);
    }

    public void printDeque() {

        for (int i = 0; i < size; i = i + 1) {
            System.out.print(get(i) + " ");
        }
        System.out.println();

//        DNode p = sentinel.next;
//
//        while (p != sentinel) {
//            System.out.print(p.item);
//            p = p.next;
//        }
//        System.out.println();
//
//        same issue as the one above... saying there is no method for printDeque in DNode...
//        but although it is a DNode it also is a LinkedListDeque & therefore should have this behavior
//        System.out.print(sentinel.next.item);
//        sentinel.next.printDeque();
    }

}
