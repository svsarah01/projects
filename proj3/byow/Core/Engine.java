package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.io.*;
import java.util.HashMap;
import java.util.List;

public class Engine {
    public static final int WIDTH = 90;
    public static final int HEIGHT = 50;

    private Font font = new Font("Monaco", Font.BOLD, 30);
    private List<Character> keys = List.of('w', 'a', 's', 'd', 'W', 'A', 'S', 'D');
    private HashMap<TETile, String> tileName = new HashMap<>();
    private World world;
    private String name;

    private static final char NEWGAME = 'n';
    private static final char ENDOFSEED = 's';
    private static final char QUIT = 'q';
    private static final char LOAD = 'l';


    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() throws IOException {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.PINK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.enableDoubleBuffering();

        drawMenuScreen("", "");
//        StdDraw.pause(3000);
        char c = solicitChar();
        StdDraw.clear(Color.BLACK);
        TETile[][] frame = null;
        StringBuilder sb = new StringBuilder();
        if (c == NEWGAME) {
            drawMenuScreen("Input seed followed by 's'", "");
            while (c != ENDOFSEED) {
                c = solicitChar();
                if (Character.isDigit(c)) {
                    sb.append(c);
                }
                drawMenuScreen("Input seed followed by 's'", sb.toString());
            }
            // getting avatar name
            name = namingAvatar();
            System.out.println(name);

            // generating world
            Long seed = Long.parseLong(sb.toString());
            System.out.println(seed);
            world = new World(seed);
            frame = world.getWorld();
            World.drawWorld(frame);
        }
        if (c == LOAD) {
            name = namingAvatar();
            sb.append(replay());
        }
        while (true) {
            frame = world.getWorld();
            initializeTileNameMap();
            if (keys.contains(c)) {
                moveAvatar(c, world);
                sb.append(c);
                TERenderer ter = new TERenderer();
                ter.renderFrame(frame);
            }
            while (!StdDraw.hasNextKeyTyped()) {
                drawHUD(frame);
            }
            if (c == ':') {
                c = StdDraw.nextKeyTyped();
                if (c == QUIT) {
                    System.out.println("should quit game");
                    FileWriter writer = new FileWriter("byow/Core/SavedWorld.txt");
                    System.out.println(sb.toString());
                    writer.write(sb.toString());
                    writer.close();
                    System.exit(0);
                }
            }
            c = StdDraw.nextKeyTyped();
        }
    }

    private String namingAvatar() {
        StringBuilder an = new StringBuilder();
        drawMenuScreen("Input avatar name followed by '#'", "");
        char c = '/';
        while (c != '#') {
            c = solicitChar();
            if (Character.isAlphabetic(c)) {
                an.append(c);
            }
            drawMenuScreen("Input avatar name followed by '#'", an.toString());
        }
        return an.toString();
    }

    private void initializeTileNameMap() {
        // adding tiles to tileName map
        tileName.put(Tileset.FLOOR, "floor");
        tileName.put(Tileset.FLOWER, "flower");
        tileName.put(Tileset.GRASS, "grass");
        tileName.put(Tileset.MOUNTAIN, "mountain");
        tileName.put(Tileset.SAND, "sand");
        tileName.put(Tileset.TREE, "tree");
        tileName.put(Tileset.WATER, "water");
        tileName.put(Tileset.WALL, "wall");
        tileName.put(Tileset.NOTHING, "the unknown");
        tileName.put(Tileset.AVATAR, name);
    }

    private void drawHUD(TETile[][] f) {
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.line(0, HEIGHT - 10, WIDTH, HEIGHT - 10);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.filledRectangle(WIDTH / 2, HEIGHT - 5, WIDTH / 2, 5);
        StdDraw.setPenColor(Color.WHITE);
        TETile t = getMouseHover(f);
        String tile = tileName.get(t);
        StdDraw.text(10, HEIGHT - 5, tile);
        StdDraw.text(30, HEIGHT - 5, "name: " + name);
        StdDraw.show();
    }

    private TETile getMouseHover(TETile[][] f) {
        int x = (int) Math.round(StdDraw.mouseX());
        int y = (int) Math.round(StdDraw.mouseY());
        if (x > WIDTH - 1 || x < 1 || y > HEIGHT - 11 || y < 1) {
            return Tileset.NOTHING;
        }
        return f[x][y];
    }

    private String replay() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("byow/Core/SavedWorld.txt"));
        System.out.println(reader);
        String input = reader.readLine();
        System.out.println(input);
        String moves = "";
        for (int i = 0; i < input.length(); i++) {
            if (!Character.isDigit(input.charAt(i))) {
                Long seed = Long.parseLong(input.substring(0, i));
                world = new World(seed);
                World.drawWorld(world.getWorld());
                moves = input.substring(i);
                break;
            }
        }
        System.out.println(moves);
        for (int i = 0; i < moves.length(); i++) {
            if (keys.contains(moves.charAt(i))) {
                moveAvatar(moves.charAt(i), world);
                System.out.println(moves.charAt(i));
//                World.drawWorld(world.getWorld());
                TERenderer ter = new TERenderer();
                ter.renderFrame(world.getWorld());
            }
            StdDraw.pause(200); // pause so the moves don't happen instantaneously
        }
        System.out.println(moves);
        return input;
    }

    private char solicitChar() {
        while (!StdDraw.hasNextKeyTyped()) {
            continue;
        }
        return Character.toLowerCase(StdDraw.nextKeyTyped());
    }

    private void drawMenuScreen(String s, String t) {
        StdDraw.clear(Color.PINK);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "CS61B: THE GAME!");
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "New Game (N)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 2, "Load Game (L)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 5, "Quit (Q)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 8, s);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 11, t);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.show();
    }

    //move avatar using WASD keys
    private void moveAvatar(char move, World w) {
        TETile[][] map = w.getWorld();
        Position start = w.getAvatar();
        Position newLocation = w.getAvatar();
        if (move == 'w') {
            newLocation = moveUp(start);
        }
        if (move == 'a') {
            newLocation = moveLeft(start);
        }
        if (move == 's') {
            newLocation = moveDown(start);
        }
        if (move == 'd') {
            newLocation = moveRight(start);
        }
        if (map[newLocation.getX()][newLocation.getY()] == Tileset.WALL) {
            return;
        } else {
            map[start.getX()][start.getY()] = Tileset.FLOOR;
            map[newLocation.getX()][newLocation.getY()] = Tileset.AVATAR;
            w.setAvatarLocation(newLocation);
        }
    }

    private Position moveRight(Position start) {
        return new Position(start.getX() + 1, start.getY());
    }

    private Position moveDown(Position start) {
        return new Position(start.getX(), start.getY() - 1);
    }

    private Position moveLeft(Position start) {
        return new Position(start.getX() - 1, start.getY());
    }

    private Position moveUp(Position start) {
        return new Position(start.getX(), start.getY() + 1);
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        if (input.equals("")) {
            return null;
        }
        Character firstChar = Character.toLowerCase(input.charAt(0));
        StringBuilder seed = new StringBuilder();
        String moves = "";
        if (firstChar == LOAD) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader("byow/Core/SavedWorld.txt"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String history = null;
            try {
                history = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 1; i < history.length(); i++) {
                if (Character.isDigit(history.charAt(i))) {
                    seed.append(history.charAt(i));
                }
                if (history.charAt(i) == ENDOFSEED) {
                    moves += history.substring(i + 1, history.length() - 2);
                    break;
                }
            }
            if (input.length() > 1) {
                moves += input.substring(1);
            }
        }
        if (firstChar == NEWGAME) {
            for (int i = 1; i < input.length(); i++) {
                if (Character.isDigit(input.charAt(i))) {
                    seed.append(input.charAt(i));
                }
                if (Character.isAlphabetic(input.charAt(i))) {
                    moves += input.substring(i + 1);
                    break;
                }
            }
        }
        System.out.println("Moves: " + moves);
        System.out.println("Seed: " + seed);
        if (seed.toString().isEmpty()) {
            return null;
        }
        World w = new World(Long.parseLong(seed.toString()));
        for (int i = 0; i < moves.length(); i++) {
            if (moves.charAt(i) == ':' && moves.charAt(i + 1) == QUIT) {
                FileWriter writer = null;
                try {
                    writer = new FileWriter("byow/Core/SavedWorld.txt");
                    writer.write("n" + seed + "s" + moves);
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            moveAvatar(moves.charAt(i), w);
        }
        return w.getWorld();
    }

    public static void main(String[] args) throws IOException {
        // testing interactWithInputString, uncomment & run main
//        Engine e = new Engine();
//        TETile[][] w1 = e.interactWithInputString("n8702095859193238354sdaddawwawas:q");
//        Engine w = new Engine();
//        TETile[][] w2 = w.interactWithInputString("ldaddawwawaswasaasswadada:q");
//        Engine y = new Engine();
//        TETile[][] w4 = y.interactWithInputString("ladds");
//        Engine x = new Engine();
//        TETile[][] w3 = x.interactWithInputString("n8702095859193238354sdaddaw" +
//                "wawasdaddawwawaswasaasswadadaadds");
//        boolean b = true;
//        for (int i = 0; i < w4.length; i++) {
//            for (int j = 0; j < w3[1].length; j++) {
//                if (w4[i][j] != w3[i][j]) {
//                    b = false;
//                    break;
//                }
//            }
//        }
//        System.out.println(b);

        // testing interactWithKeyboard, uncomment & run main
//        Engine e = new Engine();
//        try {
//            e.interactWithKeyboard();
//        } catch (IOException ioException) {
//            ioException.printStackTrace();
//        }
    }
}

