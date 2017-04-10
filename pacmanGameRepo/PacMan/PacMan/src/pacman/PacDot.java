package pacman;

import jig.Entity;
import jig.ResourceManager;


class PacDot extends Entity {
	
	public int grid_x;
	public int grid_y;
	
	public boolean Visable;
	
	public PacDot(final float x, final float y, int cell_x, int cell_y, boolean active) {
		super(x, y);
		grid_x = cell_x;
		grid_y = cell_y;
		Visable = active;
		addImageWithBoundingBox(ResourceManager.getImage(pacmanGame.PACDOT_RSC));	
	}
}

	
	

