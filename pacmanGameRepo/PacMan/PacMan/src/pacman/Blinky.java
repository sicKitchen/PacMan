package pacman;

import jig.Entity;

class Blinky extends Entity {
	public float X;
	public float Y;
	public int grid_x;
	public int grid_y;
	// Direction can be l, r, u, d
	private char lastDirection;

	public Blinky(final float x, final float y, int cell_x, int cell_y) {
		super(x, y);
		X = x;
		Y = y;
		grid_x = cell_x;
		grid_y = cell_y;
	}
	
	public char getLastDirection(){
		return lastDirection;
	}
	
	public void setDirection(char heading){
		lastDirection = heading;
	}
	
	// Update grid looks at the last known direction of ghost and updates what tile they occupy accordingly.
	// Also will not let ghosts enter teleport tiles.
	public void updateGrid(){
		if(lastDirection == 'r') {
			// before me move right, check if the adjacent right tile is a teleport. Ghosts cant go in teleports.
			if(Map.grid[grid_x + 1][grid_y].isTele()) {
				// Dont want to go into teleport, go other way
				this.grid_x--;
				lastDirection = 'l';
			}else this.grid_x++;
		}
		if(lastDirection == 'l') {
			if(Map.grid[grid_x - 1][grid_y].isTele()) {
				// Dont want to go into teleport, go other way
				this.grid_x++;
				lastDirection = 'r';
			}else this.grid_x--;
		}
		if(lastDirection == 'u') {
			this.grid_y--;
		}
		if(lastDirection == 'd') {
			this.grid_y++;
		}
	}
	
	public void setPosition(final float x, final float y) {
		X = x;
		Y = y;
	}
}