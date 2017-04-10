# PacMan Specs & Rules

## *Basics*
* If Pac-Man is captured by a ghost, a life is lost, the ghosts are returned to their pen, and a new
    Pac-Man is placed at the starting position before play continues. When the maze is cleared of all 
    dots, the board is reset, and a new round begins. If Pac-Man gets caught by a ghost when he has no 
    extra lives, the game is over.

## *Controls*
* 'Enter' to start the game
* 'Space' to start new life
* 'W', 'S', 'A', 'D' to move PacMan 

## *Sprites & Map*
* NOTE: This version of Pacman uses the dimmentions in ( )
* 2x the resolution for this implimentation  (Original × 2)
* Original Background map sprite size: 8px × 8px tiles (16px × 16px)
* Original sprite layer size: 16×16 pixels size (32 × 32 px)
* Original Video resolution: 224×288   (448 × 576 px)
* Grid : 28 tiles x 36 tiles     
    - 3 rows above map
    - 2 rows below map
* 244 total Dots on map
    - 240 PacDots
    - 4 PowerPills

## *Scoreing System*
| Name | Points | 
| ------ | ---- |
| Pac-Dot                           | 10 points |
| Power Pellet                      | 50 points |
| Cherry (not implemented yet)      | 100 points |
| Strawberry  (not implemented yet) | 300 points |
| Ghost (SCATTER)                   | 200 points | 


### Fruit (not implemented)
* Appear directly below the monster pen twice each round for additional points. 
* The first bonus fruit appears after 70 dots have been cleared from the maze. 
* The second one appears after 170 dots are cleared.
* The amount of time fruit stays on the screen before disappearing is always ten seconds. 

## *Ghost Movement*
### <span style="color:red"> Red ghost: _Blinky_ </span>
* Targets PacMan as his active target
* The red ghost starts outside of the ghost house

### <span style="color:#FF69B4"> Pink ghost: _Pinky_ </span>
* The pink ghost starts inside the ghost house, but always exits immediately,
     even in the first level.
* Pinky’s target tile in Chase mode is determined by looking at Pac-Man’s current position 
    and orientation, and selecting the location four tiles straight ahead of Pac- Man.

### <span style="color:#87CEFA"> Blue ghost: _Inky_ </span>
* Remains inside the ghost house for a short time on the first level, not joining the chase
    until Pac-Man has managed to consume at least 30 of the dots. 
* Inky actually uses both Pac-Man’s position as well as Blinky’s (the red ghost’s)
    position in his calculation. To locate Inky’s target, we first start by selecting the position of 
    Pac-Man. From there, imagine drawing a vector from Blinky’s position to this tile, and then doubling the 
    length of the vector. The tile that this new, extended vector ends on will be Inky’s actual target.

### <span style="color:#FFA500"> Orange ghost: _Clyde_ </span>
* The orange ghost, “Clyde”, is the last to leave the ghost house, and
    does not exit at all in the first level until over a third of the dots
    have been eaten.
* Whenever Clyde needs to determine his target tile, he first calculates his distance from Pac-Man. 
    If he is farther than eight tiles away, his targeting is identical to Blinky’s, using Pac-Man’s 
    current tile as his target. However, as soon as his distance to Pac-Man becomes less than eight 
    tiles, Clyde’s target is set to the same tile as his fixed one in Scatter mode, just outside the 
    bottom-left corner of the maze.

## *Ghost States*
Ghosts have three mutually-exclusive modes of behavior they can be in during play: 
    chase, scatter, and frightened. Each mode has a different objective/goal to be carried out:

* CHASE - A ghost's objective in chase mode is to find and capture Pac-Man by hunting him down 
    through the maze. Each ghost exhibits unique behavior when chasing Pac-Man, giving them their
    different personalities: Blinky (red) is very aggressive and hard to shake once he gets behind
    you, Pinky (pink) tends to get in front of you and cut you off, Inky (light blue) is the least 
    predictable of the bunch, and Clyde (orange) seems to do his own thing and stay out of the way.

* SCATTER - Ghosts enter SCATTER mode whenever Pac-Man eats one of the four energizers located 
    in the far corners of the maze. The ghosts will all turn dark blue (meaning they are vulnerable)
    and run to their respective corners of the map.

## *Rules*
* In all modes of behavior, the ghosts are prohibited from reversing their direction of travel. 
    As such, they can only choose between continuing on their current course or turning off to one 
    side or the other at the next intersection. Thus, once a ghost chooses which way to go at a maze 
    intersection, it has no option but to continue forward on that path until the next intersection 
    is reached.

* Ghosts are forced to reverse direction by the system anytime the mode changes from: chase-to-scatter. 
    Ghosts do not reverse direction when changing back from scattered to chase modes.

* Commonly referred to as the ghost house or monster pen, this cordoned-off area in the center of the 
    maze is the domain of the four ghosts and off-limits to Pac-Man.

* Whenever a level is completed or a life is lost, the ghosts are returned to their starting positions 
    in and around the ghost house before play continues-Blinky is always located just above and outside, 
    while the other three are placed inside: Inky on the left, Pinky in the middle, and Clyde on the right.

## Acknowledgments
Special thanks for PacMan research:
* [PacMan Dossier](http://www.gamasutra.com/view/feature/3938/the_pacman_dossier.php?print=1) - 
    http://www.gamasutra.com/view/feature/3938/the_pacman_dossier.php?print=1
* [Understanding Pac-Man Ghost Behavior](http://gameinternals.com/post/2072558330/understanding-pac-man-ghost-behavior) - 
    http://gameinternals.com/post/2072558330/understanding-pac-man-ghost-behavior