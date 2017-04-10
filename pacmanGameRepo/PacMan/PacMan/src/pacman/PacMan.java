package pacman;

import jig.Entity;
import jig.Vector;

class PacMan extends Entity {

	boolean DEBUG_INKY = false;

	// X and Y coordinate in pixels
	public float X;             
	public float Y;
	
	// Hold the X and Y grid that PacMan lives in
	public int grid_x;
	public int grid_y;
	
	//  Holds Pinky's Target grid coordinate. Pinky targets 4 tiles 
	//  in front of PacMans last known direction. 
	public int pinkyTarget_x;
	public int pinkyTarget_y;
	
	// Holds Inky's Target grid coordinate. 
	public int inkyTarget_x;
	public int inkyTarget_y;
	
	// Holds last known direction PacMan was facing
	private char lastDirection;
	
	private int health = 3;

	public PacMan(final float x, final float y, int cell_x, int cell_y) {
		super(x, y);
		X = x;
		Y = y;
		grid_x = cell_x;
		grid_y = cell_y;
	}
	
	public void setPosition(final float x, final float y) {
		X = x;
		Y = y;
	}
	
	public void TakeHealth(){
		health--;
	}
	
	public int getHealth(){
		return health;
	}
	
	public boolean isDead(){
		if(health <= 0) return true;
		else return false;
	}
	
	public char getLastDirection(){
		return lastDirection;
	}
	
	public void setDirection(char heading){
		lastDirection = heading;
	}
	
	public void pinkys_target_x(){
		if (lastDirection == 'r'){
			// Grid in X direction can be 0-27; If PacMan is within the last 4 tiles from edge of grid,
			// then set target to last tile in grid.
			if (grid_x < 24)
				pinkyTarget_x = grid_x + 4;
			else pinkyTarget_x = 27;
		}
		if (lastDirection == 'l'){
			if (grid_x > 5)
				pinkyTarget_x = grid_x - 4;
			else pinkyTarget_x = 0;
		}
		// If pinky is going up and down then the X coordinate of grid doesn't change
		if (lastDirection == 'u'){
			pinkyTarget_x = grid_x;
		}	
		if (lastDirection == 'd'){
			pinkyTarget_x = grid_x;
		}
	}

	public void pinkys_target_y(){
		if (lastDirection == 'r'){
			pinkyTarget_y = grid_y;
		}
		if (lastDirection == 'l'){
			pinkyTarget_y = grid_y;
		}
		if (lastDirection == 'u'){
			pinkyTarget_y = grid_y - 4;
		}	
		if (lastDirection == 'd'){
			if (grid_y < 28)
				pinkyTarget_y = grid_y + 4;
			else pinkyTarget_y = 31;
		}
	}
	
	public void inkys_target(int blinky_pos_x, int blinky_pos_y){
			
			// First draw a vector from Blinky's position to PacMans position
			Vector Vec_Pac_To_Blinky = Map.grid[blinky_pos_x][blinky_pos_y].getTileMid().subtract(Map.grid[grid_x][grid_y].getTileMid());
			if (DEBUG_INKY) System.out.println("Vector from PacMan to Blinky: "+ Vec_Pac_To_Blinky);
			
			// now scale the vector by 2
			Vec_Pac_To_Blinky = Vec_Pac_To_Blinky.scale(2);
			if(DEBUG_INKY) System.out.println("Vector from PacMan to blinky after scale: "+ Vec_Pac_To_Blinky);
			
			// divide the vector by 16 to find how many tiles we need to offset PacMans location by
			int Vec_x = (int) Vec_Pac_To_Blinky.getX() / 16;
			int Vec_y = (int) Vec_Pac_To_Blinky.getY() / 16;
			if(DEBUG_INKY) System.out.println("Vector -> Grid coord x: "+ Vec_x);
			if(DEBUG_INKY) System.out.println("Vector -> Grid coord y: "+ Vec_y);
			
			// add the offset to PacMans location to find Inkys target tile
			inkyTarget_x = grid_x + Vec_x;
			inkyTarget_y = grid_y + Vec_y;
			if (DEBUG_INKY) System.out.println("inky target grid x: "+ inkyTarget_x);
			if (DEBUG_INKY) System.out.println("inky target grid y: "+ inkyTarget_y);
			
			// Dont want to overflow grid
			if (inkyTarget_x < 0) inkyTarget_x = 0;
			if (inkyTarget_x > 27) inkyTarget_x = 27;
			if (inkyTarget_y > 31) inkyTarget_y = 31;
			if (inkyTarget_y < 0) inkyTarget_y = 0;
	}
	
	// Checks if pacman is in the same tile as the ghost passed in
	public boolean sameTile(int other_x, int other_y){
		// Need to get the mid points of Pacman's Tile
		float Pac_x = Map.grid[grid_x][grid_y].getTileMid().getX();
		float Pac_y = Map.grid[grid_x][grid_y].getTileMid().getY();
		
		// Need to get mid points of ghost tile
		float Ghost_x = Map.grid[other_x][other_y].getTileMid().getX();
		float Ghost_y = Map.grid[other_x][other_y].getTileMid().getY();
		
		// Check if the coordinates are the same
		if (Pac_x == Ghost_x && Pac_y == Ghost_y){
			return true;
		}
		return false;
	}

}

