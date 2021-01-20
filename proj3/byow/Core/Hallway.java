package byow.Core;


import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Queue;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;


public class Hallway {
    private TETile tile;
    private Room start;
    private TETile[][] world;
    private Random RANDOM;

    public Hallway(TETile t, Room s, TETile[][] w, Random r) {
        this.tile = t;
        this.start = s;
        this.world = w;
        RANDOM = r;
    }

    public TETile getTile() {
        return tile;
    }

    public List<Position> getHallwayPositions() {
        Position first = getRandomWallPosition(start);
        List<Position> hallwayPos = buildHallway(first, start);
        return hallwayPos;
    }

    private List<Position> getNeighbors(Position p) {
        List<Position> neighbors = new ArrayList<>();
        Position leftChild = new Position(p.getX() - 1, p.getY());
        Position rightChild = new Position(p.getX() + 1, p.getY());
        Position topChild = new Position(p.getX(), p.getY() + 1);
        Position bottomChild = new Position(p.getX(), p.getY() - 1);
        neighbors.add(leftChild);
        neighbors.add(rightChild);
        neighbors.add(topChild);
        neighbors.add(bottomChild);
        return neighbors;
    }

    private List<Position> getDiagonalNeighbors(Position p) {
        List<Position> diagonalNeighbors = new ArrayList<>();
        Position topLeftChild = new Position(p.getX() - 1, p.getY() + 1);
        Position topRightChild = new Position(p.getX() + 1, p.getY() + 1);
        Position bottomLeftChild = new Position(p.getX() - 1, p.getY() - 1);
        Position bottomRightChild = new Position(p.getX() + 1, p.getY() - 1);
        diagonalNeighbors.add(topLeftChild);
        diagonalNeighbors.add(topRightChild);
        diagonalNeighbors.add(bottomLeftChild);
        diagonalNeighbors.add(bottomRightChild);
        return diagonalNeighbors;
    }

    public List<Position> setWallPositions(List<Position> hallway) {
        List<Position> result = new ArrayList<>();
        for (Position p: hallway) {
            List<Position> sideNeighbors = getNeighbors(p);
            List<Position> diagonalNeighbors = getDiagonalNeighbors(p);

            for (Position child: sideNeighbors) {
                if (world[child.getX()][child.getY()] == Tileset.NOTHING) {
                    world[child.getX()][child.getY()] = Tileset.WALL;
                    result.add(child);
                }
            }

            for (Position child: diagonalNeighbors) {
                if (world[child.getX()][child.getY()] == Tileset.NOTHING) {
                    world[child.getX()][child.getY()] = Tileset.WALL;
                    result.add(child);
                }
            }
        }
        return result;
    }

    //returns list of room wall positions without corners of room
    //can move this method to Room class
    private List<Position> getNoCornerWalls(Room r) {
        List<Position> wallPositions = r.getWallPositions();
        List<Position> wallCorners = r.getwallCorners();
        List<Position> wallNoCorners = new ArrayList<>();

        for (Position p : wallPositions) {
            wallNoCorners.add(p);
        }
        for (Position p : wallPositions) {
            for (Position c : wallCorners) {
                if (p.getX() == c.getX() && p.getY() == c.getY()) {
                    wallNoCorners.remove(p);
                }
            }
        }
        return wallNoCorners;
    }

    //random wall can not be one of the corners of the starting room bc will not be
    //new path generated will not be connected
    private Position getRandomWallPosition(Room r) {
        List<Position> noCWalls = getNoCornerWalls(r);
        int min = 0;
        int max = noCWalls.size() - 1;
        int random = RandomUtils.uniform(RANDOM, min, max);
        Position randomWall = noCWalls.get(random);

        //check if the selected wall is able to path correctly with walls at the edges of map
        //(needs to be 2 away from wall because path will extend one at least out from the room)
        if (randomWall.getX() > 1 && randomWall.getX() < World.getWIDTH() - 2) {
            if (randomWall.getY() > 1 && randomWall.getY() < World.getHEIGHT() - 2) {
                return randomWall;
            }
        }
        return getRandomWallPosition(r);
    }

    //checks for selected position to be in the world and 1 away from the border
    //(does not include the border b/c need to take into account the walls for each hallway)
    private Boolean isValidPos(Position p) {
        if (p.getX() > 0 && p.getX() < World.getWIDTH() - 1) {
            if (p.getY() > 0 && p.getY() < World.getHEIGHT() - 1) {
                return true;
            }
        }
        return false;
    }

    //all positions X left < val < X right and Y top > val > Y bottom are apart of current room,
    // which we don't want
    //need to account for positions not on the map/ make sure to not go off the map
    // (isValidPos method)
    private List<Position> neighbors(Position origin, Room r) {
        List<Position> validNeighbor = new ArrayList<>();
        List<Position> totalNeighbor = getNeighbors(origin);
        for (Position p : totalNeighbor) {
            if (isValidPos(p)) {
                if (r.getTopLeft().getX() - 1 <= p.getX()
                        && p.getX() <= r.getTopRight().getX() + 1) {
                    if (r.getTopLeft().getY() + 1 >= p.getY()
                            && r.getBottomLeft().getY() - 1 <= p.getY()) {
                        continue;
                    }
                }
                validNeighbor.add(p);
            }
        }
        return validNeighbor;
    }

    //Looks for a floor tile to connect to
    private boolean isGoal(Position p) {
        if (world[p.getX()][p.getY()] == Tileset.FLOOR) {
            return true;
        }
        return false;
    }

    //private class that helps keep track of weighted edges
    private class PosNode implements Comparable<PosNode> {
        private Position pos;
        private Integer val;

        PosNode(Position p, Integer v) {
            this.pos = p;
            this.val = v;
        }

        @Override
        public int compareTo(PosNode p) {
            return this.val - p.val;
        }

        public Position getPos() {
            return pos;
        }

        public Integer getVal() {
            return val;
        }
    }

    // Dijkstra's alg with weights of 1
    //"weights" are distance from start (just incremented by 1 for each tile further away)
    private List<Position> buildHallway(Position s, Room r) {
        Queue<PosNode> fringe = new PriorityQueue<>();
        HashMap<Position, Position> childParentMap = new HashMap<>();
        HashSet<Position> visitedPos = new HashSet<>();
        List<Position> result = new ArrayList<>();
        Position currPos = s;
        Integer currDist = 0;
        fringe.add(new PosNode(currPos, currDist));
        while (!fringe.isEmpty()) {
            currPos = fringe.peek().getPos();
            currDist = fringe.poll().getVal();
            if (isGoal(currPos)) {
                break;
            }
            List<Position> children = neighbors(currPos, r);
            for (Position c : children) {
                if (!visitedPos.contains(c)) {
                    fringe.add(new PosNode(c, currDist + 1));
                    visitedPos.add(c);
                    childParentMap.put(c, currPos);
                }
            }
        }
        //line below: skips the "goal position" that isn't apart of the hallway
//        currPos = childParentMap.get(currPos);
        while (!currPos.equals(s)) {
            result.add(0, currPos);
            currPos = childParentMap.get(currPos);
        }
        result.add(0, s);
        return result;
    }

}

