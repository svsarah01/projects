public class ArrayDeque<T> {
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 4;
        nextLast = 5;
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    public void addFirst(T item) {
        if (size == items.length) {
//            resize(something arbitrary)
        }
        items[nextFirst] = item;
        size += 1;
        nextFirst -= 1;
        if (nextFirst == 0) {
            nextFirst = items.length - 1;
        }
    }

    public T removeFirst() {
        T first = items[nextFirst + 1];
        size -= 1;
        nextFirst += 1;
        return first;
    }

    public void addLast(T item) {
        if (size == items.length) {
//            resize(something arbitrary)
        }
        items[nextLast] = item;
        size += 1;
        nextLast += 1;
        if (nextLast == items.length) {
            nextLast = 0;
        }
    }

    public T removeLast() {
        T last = items[nextLast - 1];
        size -= 1;
        nextLast -= 1;
        return last;
    }

    public T get(int index) {
        int realIndex = nextFirst + 1 + index;
        if (realIndex >=  items.length) {
            realIndex = realIndex - items.length;
            return items[realIndex];
        }
        return items[nextFirst + 1 + index];
//        this isn't really accurate once nextFirst becomes the end of an array
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        for (int i = 0; i <= size; i = i + 1) {
            System.out.print(get(i));
        }
        System.out.println();
    }


}
