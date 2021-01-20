package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;

public class World {

    private static final int WIDTH = 90;
    private static final int HEIGHT = 40;
    private HashSet<Position> filled = new HashSet<>();
    private TETile[][] world;
    private Random RANDOM;
    private List<Room> connected = new LinkedList<>();
    private Position avatarLocation;
    private List<Position> roomFloor = new ArrayList<>();
    private int numOfRooms = 0;

    public static void main(String[] args) {
        World w = new World(1824155662461692906L);
        TETile[][] map = w.getWorld();
        World.drawWorld(map);
    }

    public World(long seed) {
        RANDOM = new Random(seed);

        // initialize tiles
        world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        //randomly generate rooms and hallways
        numOfRooms = RandomUtils.uniform(RANDOM, 8, 15);
        for (int i = 0; i < numOfRooms; i++) {
            createRoom(world);
        }
        //randomly generate an Avatar
        createAvatar();
    }

    public static void drawWorld(TETile[][] w) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT + 10);

        // draws the world to the screen
        ter.renderFrame(w);
    }

    private void fillIn(TETile[][] w, List<Position> positions, TETile t) {
        for (Position p : positions) {
            filled.add(p);
            w[p.getX()][p.getY()] = t;
        }
    }

    private void createAvatar() {
        Position avatarPos = randomRoomFloorPos();
        avatarLocation = avatarPos;
        world[avatarPos.getX()][avatarPos.getY()] = Tileset.AVATAR;
    }

    private void createOneHallway(TETile[][] w, Room r, TETile t) {
        Hallway h = new Hallway(t, r, world, RANDOM);
        List<Position> hallwayPos = h.getHallwayPositions();
        fillIn(w, hallwayPos, h.getTile());
        fillIn(w, h.setWallPositions(hallwayPos), Tileset.WALL);
    }

    private void createRoom(TETile[][] w) {
        // pseudorandomly create rooms
        int[] dimensions = randomDimensions();
        Position p = randomPosition(dimensions);
        if (p == null) {
            return;
        }
        Room r = new Room(randomTile(), p, dimensions);
        numOfRooms += 1;
        //add floor tiles to list
        roomFloor.addAll(r.getRoomPositions());
        // fill in floor
        fillIn(w, r.getRoomPositions(), randomTile());
        // fill in walls
        fillIn(w, r.getWallPositions(), Tileset.WALL);
        // create hallway connecting newly created room
        if (numOfRooms > 1) {
            createOneHallway(w, r, Tileset.FLOOR);
        }
        // add room to rooms hashmap
        connected.add(r);
    }


    private int[] randomDimensions() {
        int width = RandomUtils.uniform(RANDOM, 2, 15);
        int height = RandomUtils.uniform(RANDOM, 2, 15);
        return new int[]{width, height};
    }

    private Position randomRoomFloorPos() {
        int index = RandomUtils.uniform(RANDOM, 0, roomFloor.size() - 1);
        return roomFloor.get(index);
    }

    private boolean validRoom(Position p, int[] d) {
        int width = d[0];
        int height = d[1];
        if (p.getX() + width >= WIDTH || p.getY() - height <= 0) {
            return false;
        }
        Room potentialRoom = new Room(Tileset.FLOOR, p, d);
        for (Position n : potentialRoom.getWallPositions()) {
            if (filled.contains(n)) {
                return false;
            }
        }
        for (Position v : potentialRoom.getRoomPositions()) {
            if (filled.contains(v)) {
                return false;
            }
        } return true;
    }

    private TETile randomTile() {
        return Tileset.FLOOR;
//        int tileNum = RANDOM.nextInt(4);
//        switch (tileNum) {
//            case 0: return Tileset.SAND;
//            case 1: return Tileset.FLOWER;
//            case 2: return Tileset.GRASS;
//            case 3: return Tileset.MOUNTAIN;
//            default: return Tileset.FLOWER;
//        }
    }

    private Position randomPosition(int[] dimensions) {
        int x = RandomUtils.uniform(RANDOM, 5, WIDTH - 5);
        int y = RandomUtils.uniform(RANDOM, 5, HEIGHT - 5);
        Position p = new Position(x, y);
        for (int i = 0; i < 100; i++) {
            if (!validRoom(p, dimensions) || filled.contains(p)) {
                x = RandomUtils.uniform(RANDOM, 5, WIDTH - 5);
                y = RandomUtils.uniform(RANDOM, 5, HEIGHT - 5);
                p = new Position(x, y);
            }
        }
        if (!validRoom(p, dimensions) || filled.contains(p)) {
            return null;
        } else {
            return p;
        }
    }

    public TETile[][] getWorld() {
        return world;
    }

    public static int getWIDTH() {
        return WIDTH;
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }

    public Position getAvatar() {
        return avatarLocation;
    }

    public void setAvatarLocation(Position avatarLocation) {
        this.avatarLocation = avatarLocation;
    }
}


