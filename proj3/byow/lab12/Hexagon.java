package byow.lab12;

import byow.TileEngine.TETile;

import java.util.ArrayList;
import java.util.List;

public class Hexagon {
    private TETile tile;
    private int side;
    private Position topLeft;
    private Position midLeft;
    private Position midRight;

    public Hexagon(TETile tile, Position topLeft, int side) {
        this.tile = tile;
        this.topLeft = topLeft;
        this.side = side;
        this.midLeft = new Position(topLeft, -1 * (side - 1), -1 * (side - 1));
        this.midRight = new Position(midLeft, side - 1, 0);
    }

    private int getRowLength(int i) {
        if (i < this.side) {
            return this.side + i * 2;
        }
        else {
            return this.side + (this.side - 1) * 2 - (i % this.side) * 2;
        }
    }

    private int getRowStart(int i) {
        if (i < this.side) {
            return this.topLeft.getX() - i;
        }
        else {
            return this.topLeft.getX() - (this.side - 1) + (i % this.side);
        }
    }

    private int getHexHeight() {
        return side * 2;
    }

    public List<Position> getHexPositions() {
        List<Position> result = new ArrayList<>();
        int height = getHexHeight();
        for (int i = 0; i < height; i ++) {
            int rowStart = getRowStart(i);
            int rowLength = getRowLength(i);
            for (int j = 0; i < rowLength; i++) {
                result.add(new Position(rowStart + j, topLeft.getY() - i));
            }
        }
        return result;
    }

    public TETile getTile() {
        return tile;
    }

    public int getSide() {
        return side;
    }

    public Position getTopLeft() {
        return topLeft;
    }

    public Position getMidLeft() {
        return midLeft;
    }

    public Position getMidRight() {
        return midRight;
    }
}
