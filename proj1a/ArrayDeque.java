public class ArrayDeque<T> {
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 0;
        nextLast = 1;
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    public void resize(int r) {
        T[] newArray = (T[]) new Object[r];
        for (int i = 0; i < size; i = i + 1) {
            newArray[i] = get(i);
        }
        items = newArray;
        nextFirst = items.length - 1;
        nextLast  = size;
    }

    public void addFirst(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextFirst] = item;
        size += 1;
        nextFirst -= 1;
        if (nextFirst < 0) {
            nextFirst = items.length - 1;
        }
    }

    public T removeFirst() {
        double rFactor = size/(items.length*1.0);
        if (rFactor < 0.25) {
            resize(items.length/2);
        }
        if (nextFirst == items.length - 1) {
            nextFirst = 0;
        } else {
            nextFirst += 1;
        }
        T first = get(nextFirst);
        items[nextFirst] = null;
        size -= 1;
        return first;
    }

    public void addLast(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextLast] = item;
        size += 1;
        nextLast += 1;
        if (nextLast == items.length) {
            nextLast = 0;
        }
    }

    public T removeLast() {
        double rFactor = size/(items.length*1.0);
        if (rFactor < 0.25) {
            resize(items.length/2);
        }
        if (nextLast == 0)  {
            nextLast = items.length-1;
        } else {
            nextLast -= 1;
        }
        T last = get(nextLast);
        items[nextLast] = null;
        size -= 1;
        return last;
    }

    public T get(int index) {
        int realIndex = nextFirst + 1 + index;
        if (realIndex >= items.length) {
            realIndex = realIndex - items.length;
            return items[realIndex];
        }
        return items[realIndex];
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        for (int i = 0; i < size; i = i + 1) {
            System.out.print(get(i) + " ");
        }
        System.out.println();
    }
}