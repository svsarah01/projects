# Build Your Own World Design Document

**Partner 1:** Sarah Varghese

**Partner 2:** Janette Jin

## Classes and Data Structures
Engine: Class the user interacts with
       
       Instance variables: 
       TERenderer ter = new TERenderer();
       int WIDTH - width of screen;
       int HEIGHT - height of screen;
       Font font - font of text

       List<Character> keys - list containing acceptable keys (WASD)
       HashMap<TETile, String> tileName - maps tile to string name
       World world - world that is generated

       int NOCHARACTERSLEFT = 0 - makes code more readable
       char NEWGAME = 'n' - makes code more readable
       char ENDOFSEED = 's' - makes code more readable
       char QUIT = 'q' - makes code more readable
       char LOAD = 'l' - makes code more readable
      

World: Generates a world with random rooms and hallways. 
       
       Instance variables: 
       int HEIGHT - height of world
       int WIDTH - width of world
       long SEED - seed passed in
       Random RANDOM - random instance with seed passed in
       Hashset<Position> filled - Hashset of all currently filled positions in world

Position: Represents x, y position in world.
      
      Instance variables:
      int x - x coordinate
      int y - y coordinate
Room: Represents a collection of Positions that make up a room and its walls.
      
      Instance variables:
      TETile tile - type of tile the floor will be made of
      Position topLeft - position of upper left corner of room (excludes wall)
      Position topRight - position of upper right corner of room (excludes wall)
      Position bottomLeft - position of lower left corner of room (excludes wall)
      Position bottomRight - position of lower right corner of room (excludes wall)
      int width - horizontal distance of room
      int height - vertical distance of room
      
Hallway: Represents a collection of Positions that make up a hallway and its walls.
      
      Instance Variables:
      Room start - room that hallway connects from
      Room end - room that the hallway connects to

## Algorithms
Engine:
      
      interactWithKeyBoard - Method used for exploring a fresh world, uses keys typed by player to create and navigate world. Calls on:
              drawMenuScreen - uses StdDraw to draw menu screen with options: new game, load, quit
              solicitChar - method that returns the char typed by the player
              replay - method that gets called when user types L (load) that generates previous world & replays through the players past actions
              drawHUD - draws the heads up display by passing in result of getMouseHover into tileMap & displays the tile the mouse is over
              getMouseHover - returns the position (x, y coordinate) of the mouse during that point in time
              moveAvatar - responsible for moving the avatar using helper methods (moveUP, moveLeft, moveRight, moveDown)
              initializeTileMap - adds all the tiles & their names to the tileMap
      interactWithString - Method that takes in a string as an input & generates the TETile[][] (world) corresponding to the input.
      

World:
      
      createRoom - Takes in TETile[][] world as input and creates a room in the world passed in, returns nothing. Calls on randomTile(), randomPosition(), and randomDimensions() to create a random room of random size. Uses fillIn helper method to change TETile of the positions of the room.
      fillIn - Takes in TETile[][] world, List<Position> positions, TETile t as inputs and reassigns the tiles in the world array, returns nothing. Iterates through given positions, assigning each position to the tile passed in.
      randomPosition - No input, returns viable unfilled Position. Randomly generates x, y within bounds of the world and recursively calls itself until the Position generated is not already in filled (Hashset). 
      randomTile - No input, returns either GRASS, FLOWER, FLOOR, SAND to fill in floor of room. 
      randomDimensions - No input, returns random dimensions of the room. (Have not figured out a way to do this yet)
      main - initializes the tile rendering engine and all the tiles
  
Position:
      
      getX - No input, returns x variable
      getY - No input, returns y variable
      
Room:
     
      getRoomPositions - No input, returns a list containing Positions of the room floor
      getWallPositions - No input, returns a list containing Positions of the room's walls
      
Hallway:
      
      getHallwayPositions - No input, returns a list containing Positions of the hallway floors (not been implemented yet)

## Persistence
