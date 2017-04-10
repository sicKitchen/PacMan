package pacman;

import jig.ResourceManager;
import jig.Entity;

import java.util.ArrayList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.state.StateBasedGame;

//===== PacMan Game Controller ==============================

public class pacmanGame extends StateBasedGame {
	
	private final boolean DEBUG = false;
	
	public final int ScreenWidth;
	public final int ScreenHeight;
	public int score = 0; 				// Players score
	
	// States of game
	public static final int STARTUPSTATE = 0;
	public static final int PLAYINGSTATE = 1;
	public static final int LVL2STATE = 2;
	public static final int LVL3STATE = 3;
	public static final int GAMEOVERSTATE = 4;
	public static final int WINSTATE = 5;
	
	// For loading spite Sheets ----------------------------------------------------
	// MAP
	public static final String MAP_RSC = "pacman/resource/pacman_map.png";
	public static final String PACDOT_RSC = "pacman/resource/pacdot.png";
	public static final String POWERPILL_RSC = "pacman/resource/PowerPill.png";
	// PACMAN SPRITE
	public static final String PACMOUTH_RIGHT_RSC = "pacman/resource/pac_right.png";
	public static final String PACMOUTH_LEFT_RSC = "pacman/resource/pac_left.png";
	public static final String PACMOUTH_UP_RSC = "pacman/resource/pac_up.png";
	public static final String PACMOUTH_DOWN_RSC = "pacman/resource/pac_down.png";
	// BLINKY
	public static final String BLINKY_RIGHT_RSC = "pacman/resource/blinky_right.png";
	public static final String BLINKY_LEFT_RSC = "pacman/resource/blinky_left.png";
	public static final String BLINKY_UP_RSC = "pacman/resource/blinky_up.png";
	public static final String BLINKY_DOWN_RSC = "pacman/resource/blinky_down.png";
	// PINKY
	public static final String PINKY_RIGHT_RSC = "pacman/resource/Pinky_right.png";
	public static final String PINKY_LEFT_RSC = "pacman/resource/Pinky_left.png";
	public static final String PINKY_UP_RSC = "pacman/resource/Pinky_up.png";
	public static final String PINKY_DOWN_RSC = "pacman/resource/Pinky_down.png";
	// Inky
	public static final String INKY_RIGHT_RSC = "pacman/resource/Inky_right.png";
	public static final String INKY_LEFT_RSC = "pacman/resource/Inky_left.png";
	public static final String INKY_UP_RSC = "pacman/resource/Inky_up.png";
	public static final String INKY_DOWN_RSC = "pacman/resource/Inky_down.png";
	// Clyde
	public static final String CLYDE_RIGHT_RSC = "pacman/resource/Clyde_right.png";
	public static final String CLYDE_LEFT_RSC = "pacman/resource/Clyde_left.png";
	public static final String CLYDE_UP_RSC = "pacman/resource/Clyde_up.png";
	public static final String CLYDE_DOWN_RSC = "pacman/resource/Clyde_down.png";
	// Scatter Ghost
	public static final String SCATTER_GHOST_RSC = "pacman/resource/Scatter_ghost.png";
	public static final String NAKED_GHOST_RSC = "pacman/resource/uncovered_ghost.png";
	// Fruit
	public static final String CHERRY_FRUIT_RSC = "pacman/resource/cherry.png";
	public static final String STRAWBERRY_FRUIT_RSC = "pacman/resource/strawberry.png";
	// SPLASH SCREEN RSC
	public static final String TITLE_SPLASH_RSC = "pacman/resource/pacman_title.png";
	public static final String ENTER_SPLASH_RSC = "pacman/resource/enter_title.png";
	public static final String READY_RSC = "pacman/resource/ready.png";
	public static final String LIVES_RSC = "pacman/resource/PAC_lives.png";
	public static final String GAMEOVER_BANNER_RSC = "pacman/resource/game_over.png";
	public static final String WIN_BANNER_RSC = "pacman/resource/you_win.png";
	public static final String NAME_RSC = "pacman/resource/spencer.png";
	public static final String LOGO_RSC = "pacman/resource/pacman_logo.png";
	public static final String PRESS_SPACE_RSC = "pacman/resource/press_space.png";
	// SOUNDS
	public static final String PACDOT_SOUND_RSC = "pacman/resource/pacdot.wav";
	public static final String PACINTRO_SOUND_RSC = "pacman/resource/pacinterm.wav";
	public static final String PACREADY_SOUND_RSC = "pacman/resource/pacintro.wav";
	public static final String PACDEATH_SOUND_RSC = "pacman/resource/pacdeath.wav";
	public static final String EATGHOST_SOUND_RSC = "pacman/resource/pacman_eatghost.wav";
	public static final String WIN_SOUND_RSC = "pacman/resource/mspacintro.wav";
	public static final String POWPILL_SOUND_RSC = "pacman/resource/pacman_eatfruit.wav";
	
	// For loading SpriteSheet's used in animations-----------------------------------
	public SpriteSheet PAC_R_SHEET;
	public SpriteSheet PAC_L_SHEET;
	public SpriteSheet PAC_U_SHEET;
	public SpriteSheet PAC_D_SHEET;
	
	public SpriteSheet BLINKY_R_SHEET;
	public SpriteSheet BLINKY_L_SHEET;
	public SpriteSheet BLINKY_U_SHEET;
	public SpriteSheet BLINKY_D_SHEET;
	
	public SpriteSheet PINKY_R_SHEET;
	public SpriteSheet PINKY_L_SHEET;
	public SpriteSheet PINKY_U_SHEET;
	public SpriteSheet PINKY_D_SHEET;
	
	public SpriteSheet INKY_R_SHEET;
	public SpriteSheet INKY_L_SHEET;
	public SpriteSheet INKY_U_SHEET;
	public SpriteSheet INKY_D_SHEET;
	
	public SpriteSheet CLYDE_R_SHEET;
	public SpriteSheet CLYDE_L_SHEET;
	public SpriteSheet CLYDE_U_SHEET;
	public SpriteSheet CLYDE_D_SHEET;
	
	public SpriteSheet SCATTER_GHOST_SHEET;
	public SpriteSheet NAKED_GHOST_SHEET;
	
	// Create asset variables ---------------------------------------
	Map map;
	PacMan pacman;
	
	// Ghosts
	Blinky blinky;
	Pinky pinky;
	Inky inky;
	Clyde clyde;
	
	// Create array of PacDot's & PowerPill's
	ArrayList<PacDot> pacdot_array;
	ArrayList<PowerPill> powerpill_array;
	//---------------------------------------------------------------
	
	public pacmanGame(String title, int width, int height) {
		super(title);
		ScreenHeight = height;
		ScreenWidth = width;
		
		Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
		pacdot_array = new ArrayList<PacDot>(244);			// 244 total PacDots in maze
		powerpill_array = new ArrayList<PowerPill>(4);     
	}
	
	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new StartUpState());
		addState(new PlayState());
		addState(new levelTwo());
		addState(new levelThree());
		addState(new GameOverState());
		addState(new WinState());
		
		// PREload all the resources to avoid warnings & minimize latency...
		ResourceManager.loadImage(TITLE_SPLASH_RSC);
		ResourceManager.loadImage(ENTER_SPLASH_RSC);
		ResourceManager.loadImage(READY_RSC);
		ResourceManager.loadImage(LIVES_RSC);
		ResourceManager.loadImage(GAMEOVER_BANNER_RSC);
		ResourceManager.loadImage(WIN_BANNER_RSC);
		ResourceManager.loadImage(NAME_RSC);
		ResourceManager.loadImage(LOGO_RSC);
		ResourceManager.loadImage(PRESS_SPACE_RSC);
		
		ResourceManager.loadImage(MAP_RSC);
		ResourceManager.loadImage(PACDOT_RSC);
		ResourceManager.loadImage(POWERPILL_RSC);
		
		ResourceManager.loadImage(PACMOUTH_RIGHT_RSC);
		ResourceManager.loadImage(PACMOUTH_LEFT_RSC);
		ResourceManager.loadImage(PACMOUTH_UP_RSC);
		ResourceManager.loadImage(PACMOUTH_DOWN_RSC);
		
		ResourceManager.loadImage(BLINKY_RIGHT_RSC);
		ResourceManager.loadImage(BLINKY_LEFT_RSC);
		ResourceManager.loadImage(BLINKY_UP_RSC);
		ResourceManager.loadImage(BLINKY_DOWN_RSC);
		
		ResourceManager.loadImage(PINKY_RIGHT_RSC);
		ResourceManager.loadImage(PINKY_LEFT_RSC);
		ResourceManager.loadImage(PINKY_UP_RSC);
		ResourceManager.loadImage(PINKY_DOWN_RSC);
		
		ResourceManager.loadImage(INKY_RIGHT_RSC);
		ResourceManager.loadImage(INKY_LEFT_RSC);
		ResourceManager.loadImage(INKY_UP_RSC);
		ResourceManager.loadImage(INKY_DOWN_RSC);
		
		ResourceManager.loadImage(CLYDE_RIGHT_RSC);
		ResourceManager.loadImage(CLYDE_LEFT_RSC);
		ResourceManager.loadImage(CLYDE_UP_RSC);
		ResourceManager.loadImage(CLYDE_DOWN_RSC);
		
		ResourceManager.loadImage(SCATTER_GHOST_RSC);
		ResourceManager.loadImage(NAKED_GHOST_RSC);
		
		ResourceManager.loadImage(CHERRY_FRUIT_RSC);
		ResourceManager.loadImage(STRAWBERRY_FRUIT_RSC);
		
		// Pre Load sounds
		ResourceManager.loadSound(PACDOT_SOUND_RSC);
		ResourceManager.loadSound(PACINTRO_SOUND_RSC);
		ResourceManager.loadSound(PACREADY_SOUND_RSC);
		ResourceManager.loadSound(PACDEATH_SOUND_RSC);
		ResourceManager.loadSound(EATGHOST_SOUND_RSC);
		ResourceManager.loadSound(WIN_SOUND_RSC);
		ResourceManager.loadSound(POWPILL_SOUND_RSC);
		
		// Create sprite sheet for animations -----------------------------------
		PAC_R_SHEET = new SpriteSheet(PACMOUTH_RIGHT_RSC, 32, 32);
		PAC_L_SHEET = new SpriteSheet(PACMOUTH_LEFT_RSC, 32, 32);
		PAC_U_SHEET = new SpriteSheet(PACMOUTH_UP_RSC, 32, 32);
		PAC_D_SHEET = new SpriteSheet(PACMOUTH_DOWN_RSC, 32, 32);
		
		BLINKY_R_SHEET = new SpriteSheet(BLINKY_RIGHT_RSC, 32, 32);
		BLINKY_L_SHEET = new SpriteSheet(BLINKY_LEFT_RSC, 32, 32);
		BLINKY_U_SHEET = new SpriteSheet(BLINKY_UP_RSC, 32, 32);
		BLINKY_D_SHEET = new SpriteSheet(BLINKY_DOWN_RSC, 32, 32);
		
		PINKY_R_SHEET = new SpriteSheet(PINKY_RIGHT_RSC, 32, 32);
		PINKY_L_SHEET = new SpriteSheet(PINKY_LEFT_RSC, 32, 32);
		PINKY_U_SHEET = new SpriteSheet(PINKY_UP_RSC, 32, 32);
		PINKY_D_SHEET = new SpriteSheet(PINKY_DOWN_RSC, 32, 32);
		
		INKY_R_SHEET = new SpriteSheet(INKY_RIGHT_RSC, 32, 32);
		INKY_L_SHEET = new SpriteSheet(INKY_LEFT_RSC, 32, 32);
		INKY_U_SHEET = new SpriteSheet(INKY_UP_RSC, 32, 32);
		INKY_D_SHEET = new SpriteSheet(INKY_DOWN_RSC, 32, 32);
		
		CLYDE_R_SHEET = new SpriteSheet(CLYDE_RIGHT_RSC, 32, 32);
		CLYDE_L_SHEET = new SpriteSheet(CLYDE_LEFT_RSC, 32, 32);
		CLYDE_U_SHEET = new SpriteSheet(CLYDE_UP_RSC, 32, 32);
		CLYDE_D_SHEET = new SpriteSheet(CLYDE_DOWN_RSC, 32, 32);
		
		SCATTER_GHOST_SHEET = new SpriteSheet(SCATTER_GHOST_RSC, 32, 32);
		NAKED_GHOST_SHEET = new SpriteSheet(NAKED_GHOST_RSC, 32, 32);
		
		// Create the map ----------------------------------------------------
		map = new Map(224, 288); // center of the map
		
		// SET STARTING POINTS FOR ALL ENTITYS -----------------------------------------------------------------
		// Create pacMan at starting point on map grid. (Can change where pacman starts by changing grid cell)
		pacman = new PacMan(Map.grid[13][26].getTileMid().getX() + 8,Map.grid[13][26].getTileMid().getY(), 13, 26);
		
		// Create Blinky at starting point
		blinky = new Blinky(Map.grid[13][14].getTileMid().getX() + 8 , Map.grid[13][14].getTileMid().getY(), 13, 14);
		
		// Create Pinky at starting point
		pinky = new Pinky(Map.grid[13][17].getTileMid().getX() + 8, Map.grid[13][17].getTileMid().getY(), 13, 17);
		
		// Create Inky at starting point
		inky = new Inky(Map.grid[11][17].getTileMid().getX() + 8, Map.grid[11][17].getTileMid().getY(), 11, 17);
		
		// Create Clyde at starting point
		clyde = new Clyde(Map.grid[15][17].getTileMid().getX() + 8, Map.grid[15][17].getTileMid().getY(), 15, 17);
		
		if(DEBUG) System.out.println("PacMan starting tile");
		if(DEBUG) System.out.println("x: "+ pacman.grid_x);
		if(DEBUG) System.out.println("y: "+ pacman.grid_y);
		
		// Create animation controller  // Maybe come back and fix this, it is ugly
		AnimationController ac = new AnimationController(false, false, false, false); 
	}
	
	public static void main(String[] args) {
		AppGameContainer app;
		try {
			app = new AppGameContainer(new pacmanGame("PacMan!", 448, 576));
			app.setDisplayMode(448, 576, false);
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
