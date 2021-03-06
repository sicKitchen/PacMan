package pacman;

import jig.Entity;
import jig.ResourceManager;


class Map extends Entity {
	// For grid based map
	public int TILESIZE = 16;
	public static Tile[][] grid = new Tile[28][36]; // Map is 28 tiles width 36 height
	
	/*  KEY:
	 *  @ : Wall
	 *  - : unfilled grid map
	 *  • : Pac Dot
	 *  P : Power Pill
	 *  . : Part of path but no power dot
	 *  H : Ghost house
	 *  D : Door
	 *  L : Left teleport
	 *  R : Right teleport
	 *  I : Intersection tile holding a PacDot.
	 *  i : Intersection tile but not holding a PacDot or PowerPill
	 *  J : Intersection tile holding  PowerPill
	 */
	// Holds PacMan gameboard. Prob should move to a different file when I get a chance.
	private final char[] MAP_MASK = {                       
			'-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-',
			'-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-',
			'-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-',
			'@','@','@','@','@','@','@','@','@','@','@','@','@','@','@','@','@','@','@','@','@','@','@','@','@','@','@','@',
			'@','I','•','•','•','•','I','•','•','•','•','•','I','@','@','I','•','•','•','•','•','I','•','•','•','•','I','@',
			'@','•','@','@','@','@','•','@','@','@','@','@','•','@','@','•','@','@','@','@','@','•','@','@','@','@','•','@',
			'@','P','@','@','@','@','•','@','@','@','@','@','•','@','@','•','@','@','@','@','@','•','@','@','@','@','P','@',
			'@','•','@','@','@','@','•','@','@','@','@','@','•','@','@','•','@','@','@','@','@','•','@','@','@','@','•','@',
			'@','I','•','•','•','•','I','•','•','I','•','•','I','•','•','I','•','•','I','•','•','I','•','•','•','•','I','@',
			'@','•','@','@','@','@','•','@','@','•','@','@','@','@','@','@','@','@','•','@','@','•','@','@','@','@','•','@',
			'@','•','@','@','@','@','•','@','@','•','@','@','@','@','@','@','@','@','•','@','@','•','@','@','@','@','•','@',
			'@','I','•','•','•','•','I','@','@','I','•','•','I','@','@','I','•','•','I','@','@','I','•','•','•','•','I','@',
			'@','@','@','@','@','@','•','@','@','@','@','@','.','@','@','.','@','@','@','@','@','•','@','@','@','@','@','@',
			'-','-','-','-','-','@','•','@','@','@','@','@','.','@','@','.','@','@','@','@','@','•','@','-','-','-','-','-',
			'-','-','-','-','-','@','•','@','@','i','.','.','i','.','.','i','.','.','i','@','@','•','@','-','-','-','-','-',
			'-','-','-','-','-','@','•','@','@','.','H','H','H','D','D','H','H','H','.','@','@','•','@','-','-','-','-','-',
			'@','@','@','@','@','@','•','@','@','.','H','-','-','-','-','-','-','H','.','@','@','•','@','@','@','@','@','@',
			'L','.','.','.','.','.','I','.','.','i','H','-','-','-','-','-','-','H','i','.','.','I','.','.','.','.','.','R',
			'@','@','@','@','@','@','•','@','@','.','H','-','-','-','-','-','-','H','.','@','@','•','@','@','@','@','@','@',
			'-','-','-','-','-','@','•','@','@','.','H','H','H','H','H','H','H','H','.','@','@','•','@','-','-','-','-','-',
			'-','-','-','-','-','@','•','@','@','i','.','.','.','.','.','.','.','.','i','@','@','•','@','-','-','-','-','-',
			'-','-','-','-','-','@','•','@','@','.','@','@','@','@','@','@','@','@','.','@','@','•','@','-','-','-','-','-',
			'@','@','@','@','@','@','•','@','@','.','@','@','@','@','@','@','@','@','.','@','@','•','@','@','@','@','@','@',
			'@','I','•','•','•','•','I','•','•','I','•','•','I','@','@','I','•','•','I','•','•','I','•','•','•','•','I','@',
			'@','•','@','@','@','@','•','@','@','@','@','@','•','@','@','•','@','@','@','@','@','•','@','@','@','@','•','@',
			'@','•','@','@','@','@','•','@','@','@','@','@','•','@','@','•','@','@','@','@','@','•','@','@','@','@','•','@',
			'@','J','•','I','@','@','I','•','•','I','•','•','I','.','.','I','•','•','I','•','•','I','@','@','I','•','J','@',
			'@','@','@','•','@','@','•','@','@','•','@','@','@','@','@','@','@','@','•','@','@','•','@','@','•','@','@','@',
			'@','@','@','•','@','@','•','@','@','•','@','@','@','@','@','@','@','@','•','@','@','•','@','@','•','@','@','@',
			'@','I','•','I','•','•','I','@','@','I','•','•','I','@','@','I','•','•','I','@','@','I','•','•','I','•','I','@',
			'@','•','@','@','@','@','@','@','@','@','@','@','•','@','@','•','@','@','@','@','@','@','@','@','@','@','•','@',
			'@','•','@','@','@','@','@','@','@','@','@','@','•','@','@','•','@','@','@','@','@','@','@','@','@','@','•','@',
			'@','I','•','•','•','•','•','•','•','•','•','•','I','•','•','I','•','•','•','•','•','•','•','•','•','•','I','@',
			'@','@','@','@','@','@','@','@','@','@','@','@','@','@','@','@','@','@','@','@','@','@','@','@','@','@','@','@',
			'-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-',
			'-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-'};	
	

	public Map(final float x, final float y) {
		super(x, y);
		addImageWithBoundingBox(ResourceManager.getImage(pacmanGame.MAP_RSC));	
		
		// Loop through array of MAP_MASK and build tile based on character
		int k = 0; // start of array
		
		for(int i = 0; i < 36; i++){
            for(int j = 0; j < 28; j++){
            	// Tile(x, y, isWALL, isPacDot, isPowerPill, isIntersection, L/Rteleport true::L false::R)
            	// PacMan can not move into these tiles.
            	if (MAP_MASK[k] == '@' || MAP_MASK[k] == '-' || MAP_MASK[k] == 'H' || MAP_MASK[k] == 'D' ){
            		grid[j][i] = new Tile(j*this.TILESIZE, i*this.TILESIZE, true, false, false, false, false);
            	}
            	else 
            	// Create a PacDot at this location.
            	if (MAP_MASK[k] == '•'){
            		grid[j][i] = new Tile(j*this.TILESIZE, i*this.TILESIZE, false, true, false, false, false);
            	}
            	else
            	// Create a PacDot & Intersection at this location.
            	if (MAP_MASK[k] == 'I'){
            		grid[j][i] = new Tile(j*this.TILESIZE, i*this.TILESIZE, false, true, false, true, false);
            	}
            	else
		        // Create a PowerPill at this location
		       	if(MAP_MASK[k] == 'P'){
            		grid[j][i] = new Tile(j*this.TILESIZE, i*this.TILESIZE, false, false, true, false, false);
            	}
		       	else
            	// Create a PowerPill & Intersection at this location
		       	if(MAP_MASK[k] == 'J'){
            		grid[j][i] = new Tile(j*this.TILESIZE, i*this.TILESIZE, false, false, true, true, false);
            	}
		       	else
	            // Create a Intersection at this location
			    if(MAP_MASK[k] == 'i'){
			    	grid[j][i] = new Tile(j*this.TILESIZE, i*this.TILESIZE, false, false, false, true, false);
	            }
			    else
		            // Create a teleport at this location
				    if(MAP_MASK[k] == 'L' || MAP_MASK[k] == 'R'){
				    	grid[j][i] = new Tile(j*this.TILESIZE, i*this.TILESIZE, false, false, false, false, true);
		            }
            	
            	else
            		grid[j][i] = new Tile(j*this.TILESIZE, i*this.TILESIZE, false, false, false, false, false);
            	
            	//To check if array is reading in the correct order
            	//System.out.println(MAP_MASK[k]);
            	k++;
            }       
		
		}
	}
}
