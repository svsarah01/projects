# Project 3 Prep

**For tessellating hexagons, one of the hardest parts is figuring out where to place each hexagon/how to easily place hexagons on screen in an algorithmic way.
After looking at your own implementation, consider the implementation provided near the end of the lab.
How did your implementation differ from the given one? What lessons can be learned from it?**

Answer: 
I had my point class as a subclass within my hexagon class but having it as a separate 
class seems to be more useful. Same with having a second Position constructor which takes in
a position instance to find the midleft and midright. Also, I manually had the for loops inside 
my createHexagon method rather than having a list of positions which is much more efficient and clean.

-----

**Can you think of an analogy between the process of tessellating hexagons and randomly generating a world using rooms and hallways?
What is the hexagon and what is the tesselation on the Project 3 side?**

Answer: The data structures we created and the thought process itself should
be pretty analogous since rooms and hallways are just rectangles and we just drew
hexagons. The tesselation would be connecting all the rooms & hallways.

-----
**If you were to start working on world generation, what kind of method would you think of writing first? 
Think back to the lab and the process used to eventually get to tessellating hexagons.**

Answer: The first thing I would work on is how to create rooms and then hallways and go 
from there. I would create a room class (kind of like the hexagon) and have a position 
class as well. Then I would try to randomly generate room sizes & then add these rooms 
to the larger world before connecting them with hallways.

-----
**What distinguishes a hallway from a room? How are they similar?**

Answer: Hallways are either 1 wide or 1 height. They are just like rooms but with different dimensions. 
They are different due to these dimension differences and also they are 
used to connect the randomly generated rooms. 
