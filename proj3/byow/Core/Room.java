package byow.Core;

import byow.TileEngine.TETile;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private TETile tile;
    private Position topLeft;
    private Position topRight;
    private Position bottomLeft;
    private Position bottomRight;
    private int width;
    private int height;
    //maybe keep a list of all existing rooms during world generation
    //overlap method that rejects any room that overlaps

    public Room(TETile t, Position p, int[] dimensions) {
        tile = t;
        topLeft = p;
        width = dimensions[0];
        height = dimensions[1];
        topRight = new Position(p.getX() + (width - 1), p.getY());
        bottomLeft = new Position(p.getX(), p.getY() - (height - 1));
        bottomRight = new Position(p.getX() + (width - 1), p.getY() - (height - 1));
    }

    public List<Position> getRoomPositions() {
        List<Position> result = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                result.add(new Position(topLeft.getX() + j, topLeft.getY() - i));
            }
        }
        return result;
    }

    public List<Position> getWallPositions() {
        List<Position> result = new ArrayList<>();
        /* top & bottom walls */
        int startingCol = topLeft.getX() - 1;
        int topRow = topLeft.getY() + 1;
        int bottomRow = bottomLeft.getY() - 1;
        for (int i = 0; i < width + 2; i++) {
            result.add(new Position(startingCol + i, topRow));
            result.add(new Position(startingCol + i, bottomRow));
        }
        /* side walls */
        int leftCol = topLeft.getX() - 1;
        int rightCol = topRight.getX() + 1;
        int startingRow = topLeft.getY();
        for (int i = 0; i < height; i++) {
            result.add(new Position(leftCol, startingRow - i));
            result.add(new Position(rightCol, startingRow - i));
        }
        return result;
    }

    public List<Position> getPositions() {
        List<Position> result = new ArrayList<>();
        /* add room positions */
        result.addAll(getRoomPositions());
        /* add wall positions */
        result.addAll(getWallPositions());
        return result;
    }

    //new
    public List<Position> getwallCorners() {
        List<Position> result = new ArrayList<>();
        //leftTop corner
        result.add(new Position(topLeft.getX() - 1, topLeft.getY() + 1));
        //rightTop corner
        result.add(new Position(topRight.getX() + 1, topRight.getY() + 1));
        //leftBottom corner
        result.add(new Position(bottomLeft.getX() - 1, bottomLeft.getY() - 1));
        //rightBottom corner
        result.add(new Position(bottomRight.getX() + 1, bottomRight.getY() - 1));
        return result;
    }

    public TETile getTile() {
        return tile;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Position getTopLeft() {
        return topLeft;
    }

    public Position getTopRight() {
        return topRight;
    }

    public Position getBottomLeft() {
        return bottomLeft;
    }

    public Position getBottomRight() {
        return bottomRight;
    }
}
