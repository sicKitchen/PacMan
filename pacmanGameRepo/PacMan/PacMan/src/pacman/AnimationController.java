package pacman;

public class AnimationController {
	
	public static boolean right;
	public static boolean left;
	public static boolean up;
	public static boolean down;
	
	public static boolean blinky_right = false;
	public static boolean blinky_left = false;
	public static boolean blinky_up = false;
	public static boolean blinkydown = false;
	
	public AnimationController(boolean R, boolean L, boolean U, boolean D){
		right = R;
		left = L;
		up = U;
		down = D;
	}
}
