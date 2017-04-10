package pacman;

import jig.Vector;

public class Tile {
	public Vector LeftCorner;
	// Used to align PacMan sprite correctly
	public int TILEOFFSET_X = 8;
	public int TILEOFFSET_Y = 6;
	// What 'lives' in tiles
	private boolean Wall= false;
	private boolean PacDot;
	private boolean PowerPill = false;
	private boolean Intersection;
	private boolean tele;
	
	public Tile(float a, float b, boolean isWall, boolean hasPacDot, boolean hasPowerPill, boolean isIntersect, boolean isTeleport){
		LeftCorner = new Vector(a,b);
		Wall = isWall;
		PacDot = hasPacDot;
		PowerPill = hasPowerPill;
		Intersection = isIntersect;
		tele = isTeleport;
		
	}
	
	public Vector getTileMid(){
		Vector center;
		center = new Vector(LeftCorner.getX() - TILEOFFSET_X, LeftCorner.getY() - TILEOFFSET_Y);
		return center;
	}
	
	public boolean isWall(){
		return Wall;
	}
	public boolean isTele(){
		return tele;
	}
	public boolean isIntersect(){
		return Intersection;
	}
	
	public boolean hasPacDot(){
		return PacDot;
	}
	
	public boolean hasPowerPill(){
		return PowerPill;
	}
	
	public Vector getPacDotLoc(){
		Vector center;                              // the plus 2 makes TILEOFFSET_Y = 8 instead of 6
		center = new Vector(LeftCorner.getX() + TILEOFFSET_X , LeftCorner.getY() + TILEOFFSET_Y + 2 ); 
		
		return center;
	}
}
