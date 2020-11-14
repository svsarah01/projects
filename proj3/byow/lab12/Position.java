package byow.lab12;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(Position reference, int xOff, int yOff) {
        this(reference.getX() + xOff, reference.getY() + yOff);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
