package pacman;

import java.util.Iterator;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import jig.ResourceManager;

class levelTwo extends BasicGameState {
	
	// TURN ON FOR DEBUG
	private boolean DEBUG = false;					// Turns on general debug statements such as counters and timers
	private boolean SHOW_GHOST_TARGETS = false;	    // Shows what each ghost is targeting
	private boolean DEBUG_CLYDE = false;            // SHow debugs related to Clyde
	private boolean DEBUG_BLINKY = false;            // SHow debugs related to Blinky
	private boolean DEBUG_PINKY = false;            // SHow debugs related to Pinky
	private boolean DEBUG_INKY = false;            // SHow debugs related to Inky
	private boolean DEBUG_SCATTER = false;            // SHow debugs related to scatter state
	private boolean DEBUG_SCATTER_BLINKY = false;            // SHow debugs specific to scatter state blinky
	private boolean DEBUG_SCATTER_PINKY = false;            // SHow debugs specific to scatter state pinky
	private boolean DEBUG_SCATTER_INKY = false;            // SHow debugs specific to scatter state inky
	private boolean DEBUG_SCATTER_CLYDE = false;            // SHow debugs specific to scatter state clyde
	
	private boolean show_fruit = false;
	
	int pacdots_remaining = 244;		// number of PacDots 
	
	private boolean SCATTER;
	private boolean reset;
	
	// DEAD GHOST VARIABLES
	private boolean BLINKY_DEAD;
	private boolean INKY_DEAD;
	private boolean PINKY_DEAD;
	private boolean CLYDE_DEAD;
	
	private boolean gh_blinky_lock;
	private boolean gh_pinky_lock;
	private boolean gh_inky_lock;
	private boolean gh_clyde_lock;
	
	private boolean sound_playing;
	private int sound_counter;
	private int Scatter_Countdown;
	private int LOCKOUT_TIME = 100; // <-------- Change speed of PacMan 		
	private int GHOST_LOCKOUT_TIME = 110; // <-------- Change speed of Ghosts
	private int countdown; 
	private int countdown_ghosts;
	private boolean canMove;  
	private boolean ghostsTurn;
	private int SPEED = 120; //   <---------- Change PacMans animation speed here
	private int GHOST_SPEED = 80; //   <---------- Change ghosts animation speed here
	
	int blinky_death_counter;
	
	// ANIMATIONS
	private Animation pacman_anime;     // holds pacman animation
	// Ghosts
	private Animation blinky_anime;     // holds blinky animation
	private Animation pinky_anime;      // holds pinky animation
	private Animation inky_anime;       // holds inky animation
	private Animation clyde_anime;      // holds clyde animation
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		pacmanGame pg = (pacmanGame)game;
		pacman_anime = new Animation(pg.PAC_R_SHEET, SPEED);	 // Create pacman animation at start
		blinky_anime = new Animation(pg.BLINKY_R_SHEET, SPEED);	 // Create blinky animation at start
		pinky_anime = new Animation(pg.PINKY_R_SHEET, SPEED);	 // Create pinky animation at start
		inky_anime = new Animation(pg.INKY_R_SHEET, SPEED);	     // Create inky animation at start
		clyde_anime = new Animation(pg.CLYDE_R_SHEET, SPEED);	 // Create clyde animation at start
		
		canMove = true;	// can move at start
		ghostsTurn = true;
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		container.setSoundOn(true);
		pacmanGame pg = (pacmanGame)game;
		ResourceManager.getSound(pacmanGame.PACINTRO_SOUND_RSC).stop();
		ResourceManager.getSound(pacmanGame.PACREADY_SOUND_RSC).play();
		SCATTER = false;          // game start with state false
		Scatter_Countdown = 0;		   // Countdown is 0 at start
		reset = true;
		
		// Non of the ghosts are locked in ghost house
		gh_blinky_lock = false;
		gh_pinky_lock = false;
		gh_inky_lock = false;
		gh_clyde_lock = false;
		
		
		pg.pacman.grid_x = 13;
		pg.pacman.grid_y = 26;
		pg.pacman.setPosition(Map.grid[pg.pacman.grid_x][pg.pacman.grid_y].getTileMid().getX()   ,
				Map.grid[pg.pacman.grid_x][pg.pacman.grid_y].getTileMid().getY());
		
		// Set blinky outside of door
		pg.blinky.grid_x = 13;
		pg.blinky.grid_y = 14;
		pg.blinky.setPosition(Map.grid[pg.blinky.grid_x][pg.blinky.grid_y].getTileMid().getX() + 8 ,
				Map.grid[pg.blinky.grid_x][pg.blinky.grid_y].getTileMid().getY());
		
		// Set pinky to middle of ghost house
		pg.pinky.grid_x = 13;
		pg.pinky.grid_y = 17;
		pg.pinky.setPosition(Map.grid[pg.pinky.grid_x][pg.pinky.grid_y].getTileMid().getX() + 8 ,
				Map.grid[pg.pinky.grid_x][pg.pinky.grid_y].getTileMid().getY());
		
		// Set inky to left of ghost house
		pg.inky.grid_x = 11;
		pg.inky.grid_y = 17;
		pg.inky.setPosition(Map.grid[pg.inky.grid_x][pg.inky.grid_y].getTileMid().getX() + 8 ,
				Map.grid[pg.inky.grid_x][pg.inky.grid_y].getTileMid().getY());
		
		// Set clyde to right of ghost house
		pg.clyde.grid_x = 15;
		pg.clyde.grid_y = 17;
		pg.clyde.setPosition(Map.grid[pg.clyde.grid_x][pg.clyde.grid_y].getTileMid().getX() + 8 ,
				Map.grid[pg.clyde.grid_x][pg.clyde.grid_y].getTileMid().getY());
	
		// Loop through Map.grid and check if PacDot lives there. If there is then create PacDot there.
		for(int i = 0; i < 36; i++){
			for(int j = 0; j < 28; j++){
				// The tile contains a PacDot
				if (Map.grid[j][i].hasPacDot()){        												// true flag means the PacDot is visible
					pg.pacdot_array.add(new PacDot(Map.grid[j][i].getPacDotLoc().getX(), Map.grid[j][i].getPacDotLoc().getY(), j, i , true));
				}
				
				if (Map.grid[j][i].hasPowerPill()){
					// PowerPills are spawned in the same location of the tile as PacDots are spawned
					pg.powerpill_array.add(new PowerPill(Map.grid[j][i].getPacDotLoc().getX(), Map.grid[j][i].getPacDotLoc().getY(), j, i , true ));
				}
			}
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		pacmanGame pg = (pacmanGame)game;
		
		pg.map.render(g);
		
		// Go through the array of PacDots and draw them to the screen
		for (Iterator<PacDot> i = pg.pacdot_array.iterator(); i.hasNext();) {
			i.next().render(g);
		}
		
		// Go through the array of PowerPills and draw them to the screen
		for (Iterator<PowerPill> i = pg.powerpill_array.iterator(); i.hasNext();) {
			i.next().render(g);
		}
		
		// Shows how many PacDots are left on the map
		g.drawString("PacDots: " + this.pacdots_remaining, 10, 30);
		g.drawString("SCORE: " + pg.score, 170, 10);
		g.drawString("Level: 2", 170, 30);
		
		for (int i = 1; i <= pg.pacman.getHealth(); i++){
			g.drawImage(ResourceManager.getImage(pacmanGame.LIVES_RSC),
					32 * i, 545 );
		}
		
		if (reset){
			// draw the ready banner
			g.drawImage(ResourceManager.getImage(pacmanGame.READY_RSC),75, 100);
			g.drawImage(ResourceManager.getImage(pacmanGame.PRESS_SPACE_RSC),48, 305);
			pacman_anime.draw(pg.pacman.X, pg.pacman.Y);
			blinky_anime.draw(pg.blinky.X, pg.blinky.Y);
			pinky_anime.draw(pg.pinky.X, pg.pinky.Y);
			inky_anime.draw(pg.inky.X, pg.inky.Y);
			clyde_anime.draw(pg.clyde.X, pg.clyde.Y);
			
		}else{
			// Draw PacMan & Ghosts at there current x and y coordinate. 
			pacman_anime.draw(pg.pacman.X, pg.pacman.Y);
			
			blinky_anime.draw(pg.blinky.X, pg.blinky.Y);
			pinky_anime.draw(pg.pinky.X, pg.pinky.Y);
			inky_anime.draw(pg.inky.X, pg.inky.Y);
			clyde_anime.draw(pg.clyde.X, pg.clyde.Y);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {

		Input input = container.getInput();
		pacmanGame pg = (pacmanGame)game;
	
		// Check PacMans health at the start of the frame
		if (pg.pacman.isDead()){
			pg.enterState(pacmanGame.GAMEOVERSTATE);	
		}
		
		// Check if there are any pacdots left
		if (pacdots_remaining <= 0){
			pg.enterState(pacmanGame.LVL3STATE);	
		}
	
		// +===========+
		// | KEY INPUT |===========================================================================================
		// +===========+
		
		if (reset){
			if(input.isKeyDown(Input.KEY_SPACE)){
				reset = false;
				// Reset PacMan
				pg.pacman.grid_x = 13;
				pg.pacman.grid_y = 26;
				pg.pacman.setPosition(Map.grid[pg.pacman.grid_x][pg.pacman.grid_y].getTileMid().getX()  ,
						Map.grid[pg.pacman.grid_x][pg.pacman.grid_y].getTileMid().getY());
				
				// Set blinky to intersection tile outside of door
				pg.blinky.grid_x = 12;
				pg.blinky.grid_y = 14;
				pg.blinky.setPosition(Map.grid[pg.blinky.grid_x][pg.blinky.grid_y].getTileMid().getX() + 8 ,
						Map.grid[pg.blinky.grid_x][pg.blinky.grid_y].getTileMid().getY());
				
				// Set Pinky to intersection tile outside of door
				pg.pinky.grid_x = 15;
				pg.pinky.grid_y = 14;
				pg.pinky.setPosition(Map.grid[pg.pinky.grid_x][pg.pinky.grid_y].getTileMid().getX() + 8 ,
						Map.grid[pg.pinky.grid_x][pg.pinky.grid_y].getTileMid().getY());
				
				// Set Inky to intersection tile outside of door when 30 dots are ate
				if (pacdots_remaining <= 210){
					pg.inky.grid_x = 12;
					pg.inky.grid_y = 14;
					pg.inky.setPosition(Map.grid[pg.inky.grid_x][pg.inky.grid_y].getTileMid().getX() +8 ,
							Map.grid[pg.inky.grid_x][pg.inky.grid_y].getTileMid().getY());
				}
			
				
				// Set Clyde to intersection tile outside of door
				if(pacdots_remaining <= 163){
					pg.clyde.grid_x = 15;
					pg.clyde.grid_y = 14;
					pg.clyde.setPosition(Map.grid[pg.clyde.grid_x][pg.clyde.grid_y].getTileMid().getX() + 8 ,
							Map.grid[pg.clyde.grid_x][pg.clyde.grid_y].getTileMid().getY());
				}
					
			}
			//else System.out.println("dont update");
		}
		else
		if ((input.isKeyDown(Input.KEY_W)) && (Map.grid[pg.pacman.grid_x][pg.pacman.grid_y - 1].isWall() == false)) {
			
			if (DEBUG) System.out.println("grid x: " + pg.pacman.grid_x);
			if (DEBUG) System.out.println("canMove: "+ canMove);
			 
		   /* Here we update pacmans position based on the map grid. Grid is [28][36]
			* Each grid[][] has a vector containing its center point. We extract the x and y coordinate
			* of the grid cell vector that pacman is currently in and move him there. As of now to slow down 
			* movement we lock movement for a few frames until we allow movement to occur again. 
			*/
			if (canMove){
				pg.pacman.grid_y--;
				
				// ANIMATION CONTROLLER ---------------------------------------------------------
				if(AnimationController.up){
					// we are already going up at this point, dont update animation
					// reset other directions on animation_controller
					AnimationController.right = false;
					AnimationController.left = false;
					AnimationController.down = false;
					
				}else {
					// register that we are turning up with animation_controller
					AnimationController.up = true;
					AnimationController.right = false;
					AnimationController.left = false;
					AnimationController.down = false;
					pg.pacman.setDirection('u');
					pacman_anime.stop();
					pacman_anime = new Animation(ResourceManager.getSpriteSheet(
					pacmanGame.PACMOUTH_UP_RSC, 32, 32), 0, 0, 0, 2, false, SPEED,true); 
					pacman_anime.start();
				}
				//------------------------------------------------------------------------------
				
				if(DEBUG) System.out.println("up tile: "+ Map.grid[pg.pacman.grid_x][pg.pacman.grid_y - 1].isWall());
				if(DEBUG) System.out.println("x: "+ pg.pacman.grid_x);
				if(DEBUG) System.out.println("y: "+ pg.pacman.grid_y);
				
				pg.pacman.setPosition(Map.grid[pg.pacman.grid_x][pg.pacman.grid_y].getTileMid().getX() ,
						Map.grid[pg.pacman.grid_x][pg.pacman.grid_y].getTileMid().getY());
				canMove = false;
				if (DEBUG) System.out.println("canMove after lock: "+ canMove);
				countdown = LOCKOUT_TIME;
				
			} else {
				if (DEBUG) System.out.println("countdown: "+ countdown);
				if (countdown > 0)
					countdown -= delta;
				if (countdown <= 0) {
					canMove = true;
					if (DEBUG) System.out.println("canMove unlock: "+ canMove);
				}
			}
		}
		else
		if ((input.isKeyDown(Input.KEY_S)) && (Map.grid[pg.pacman.grid_x][pg.pacman.grid_y + 1].isWall() == false)) {
			if (DEBUG) System.out.println("grid x: " + pg.pacman.grid_x);
			if (DEBUG) System.out.println("canMove: "+ canMove);
			
			if (canMove){
				pg.pacman.grid_y++;
				
				// ANIMATION CONTROLLER ---------------------------------------------------------
				if(AnimationController.down){
					// we are already going down at this point, dont update animation
					// reset other directions on animation_controller
					AnimationController.right = false;
					AnimationController.left = false;
					AnimationController.up = false;
				}else {
					// register that we are turning down with animation_controller
					AnimationController.down = true;
					AnimationController.right = false;
					AnimationController.left = false;
					AnimationController.up = false;
					pg.pacman.setDirection('d');
					
					pacman_anime.stop();
					pacman_anime = new Animation(ResourceManager.getSpriteSheet(
							pacmanGame.PACMOUTH_DOWN_RSC, 32, 32), 0, 0, 0, 2, false, SPEED,true); 
					pacman_anime.start();
				}
				//------------------------------------------------------------------------------
				
				pg.pacman.setPosition(Map.grid[pg.pacman.grid_x][pg.pacman.grid_y].getTileMid().getX() ,
						Map.grid[pg.pacman.grid_x][pg.pacman.grid_y].getTileMid().getY());
				canMove = false;
				if (DEBUG) System.out.println("canMove after lock: "+ canMove);
				countdown = LOCKOUT_TIME;
				
			} else {
				if (DEBUG) System.out.println("countdown: "+ countdown);
				if (countdown > 0)
					countdown -= delta;
				if (countdown <= 0) {
					canMove = true;
					if (DEBUG) System.out.println("canMove unlock: "+ canMove);
				}
			}
		}
		else
		if ((input.isKeyDown(Input.KEY_A)) && (Map.grid[pg.pacman.grid_x - 1][pg.pacman.grid_y].isWall() == false)) {
			if (DEBUG) System.out.println("grid x: " + pg.pacman.grid_x);
			if (DEBUG) System.out.println("canMove: "+ canMove);
			
			if (canMove){
				// PacMan is in left teleport, move to other side of map
				if(pg.pacman.grid_x == 1 && pg.pacman.grid_y == 17){
					pg.pacman.grid_x = 26;
				}else{
				pg.pacman.grid_x--;
				}
				
				// ANIMATION CONTROLLER ---------------------------------------------------------
				if(AnimationController.left){
					// we are already going left at this point, dont update animation
					// reset other directions on animation_controller
					AnimationController.right = false;
					AnimationController.up = false;
					AnimationController.down = false;
				}else {
					// register that we are turning right with animation_controller
					AnimationController.left = true;
					AnimationController.right = false;
					AnimationController.up = false;
					AnimationController.down = false;
					pg.pacman.setDirection('l');
				
					pacman_anime.stop();
					pacman_anime = new Animation(pg.PAC_L_SHEET, SPEED);
					pacman_anime.start();
				}
				//------------------------------------------------------------------------------
				
				pg.pacman.setPosition(Map.grid[pg.pacman.grid_x][pg.pacman.grid_y].getTileMid().getX() ,
						Map.grid[pg.pacman.grid_x][pg.pacman.grid_y].getTileMid().getY());
				canMove = false;
				if (DEBUG) System.out.println("canMove after lock: "+ canMove);
				countdown = LOCKOUT_TIME;
				
			} else {
				if (DEBUG) System.out.println("countdown: "+ countdown);
				if (countdown > 0)
					countdown -= delta;
				if (countdown <= 0) {
					canMove = true;
					if (DEBUG) System.out.println("canMove unlock: "+ canMove);
				}
			}
		}
		else
		if ((input.isKeyDown(Input.KEY_D)) && (Map.grid[pg.pacman.grid_x + 1][pg.pacman.grid_y].isWall() == false)) {
			if (DEBUG) System.out.println("grid x: " + pg.pacman.grid_x);
			if (DEBUG) System.out.println("canMove: "+ canMove);
			
			if (canMove){
				// PacMan is in right teleport, move to other side of map
				if(pg.pacman.grid_x == 26 && pg.pacman.grid_y == 17){
					pg.pacman.grid_x = 1;
				}else{
				pg.pacman.grid_x++;
				}
				
				// ANIMATION CONTROLLER ---------------------------------------------------------
				if(AnimationController.right){
					// we are already going right at this point, dont update animation
					AnimationController.left = false;
					AnimationController.up = false;
					AnimationController.down = false;
				}else {
					// register that we are turning right with animation_controller
					AnimationController.right = true;
					AnimationController.left = false;
					AnimationController.up = false;
					AnimationController.down = false;
					pg.pacman.setDirection('r');
					
					pacman_anime.stop();
					pacman_anime = new Animation(pg.PAC_R_SHEET, SPEED);
					pacman_anime.start();
				}
				//------------------------------------------------------------------------------
				pg.pacman.setPosition(Map.grid[pg.pacman.grid_x][pg.pacman.grid_y].getTileMid().getX() ,
						Map.grid[pg.pacman.grid_x][pg.pacman.grid_y].getTileMid().getY());
				canMove = false;
				if (DEBUG) System.out.println("canMove after lock: "+ canMove);
				countdown = LOCKOUT_TIME;
				
			// We are locked out of moving, so instead decrement countdown.	
			} else {
				if (DEBUG) System.out.println("countdown: "+ countdown);
				if (countdown > 0)
					countdown -= delta;
				if (countdown <= 0) {
					canMove = true;
					if (DEBUG) System.out.println("canMove unlock: "+ canMove);
				}
			}
		}
		
		// +========+
		// | PACDOT |====================================================================================
		// +========+
		// Did we move into a tile that holds a PacDot?
		// create iter, tmp_Dot is working pacdot
		for (Iterator<PacDot> i = pg.pacdot_array.iterator(); i.hasNext();) {
			PacDot tmp_Dot = i.next();
			if(tmp_Dot.grid_x == pg.pacman.grid_x && tmp_Dot.grid_y == pg.pacman.grid_y && tmp_Dot.Visable == true){
				this.pacdots_remaining--; // subtract a PacDot
				tmp_Dot.Visable = false;  // Hide the PacDot from map
				//remove the image, toggle the PacDot off
				tmp_Dot.removeImage(ResourceManager.getImage(pacmanGame.PACDOT_RSC)); 
				pg.score = pg.score + 10;
				
				if(sound_playing){
					if (sound_counter <= 0) sound_playing = false;
					sound_counter -= delta;
				}else{
					sound_counter = 40;   //<---------- Change how long to lock out sound effect
					sound_playing = true;
					ResourceManager.getSound(pacmanGame.PACDOT_SOUND_RSC).play();
				}
				
			}
		}
		
		
		// +============+
		// | POWER PILL |====================================================================================
		// +============+
		// Did we move into a tile that holds a Power Pill?
		// create iter, tmp_Pill is working powerPill
		for (Iterator<PowerPill> i = pg.powerpill_array.iterator(); i.hasNext();) {
			PowerPill tmp_Pill = i.next();
			if(tmp_Pill.grid_x == pg.pacman.grid_x && tmp_Pill.grid_y == pg.pacman.grid_y && tmp_Pill.Visable == true){
				this.pacdots_remaining--; // subtract a PacDot
				tmp_Pill.Visable = false;  // Hide the PowerPill from map
				//remove the image, toggle the PowerPill off
				tmp_Pill.removeImage(ResourceManager.getImage(pacmanGame.POWERPILL_RSC));
				pg.score = pg.score + 50;
				ResourceManager.getSound(pacmanGame.POWPILL_SOUND_RSC).play();
				
				// Set Power Pill State to active
				SCATTER = true;
				if(DEBUG_SCATTER) System.out.println("Scatter State Active");
				
				// Set Power Pill Countdown
				Scatter_Countdown = 7000;  // <------------ Change how long power Scatter State is active here
				
			}
		}
		
		if(Scatter_Countdown <= 0){
			SCATTER = false;
			if(DEBUG_SCATTER) System.out.println("Scatter State Deactive");
		}else {
			Scatter_Countdown -= delta;
		}
		
		// +=================================+
		// |Does PacMan Collide with a ghost?|------------------------------------------------------------
		// +=================================+
		// BLINKY
		if(pg.pacman.sameTile(pg.blinky.grid_x, pg.blinky.grid_y)){
			if(SCATTER){
				if(DEBUG_SCATTER_BLINKY) System.out.println("SCATTER: PacMan collided with a BLINKY");
				// We need to set a flag here that will tell them to target the ghost house when they collide.
				if (BLINKY_DEAD){
					
				}else{
				BLINKY_DEAD = true;
				ResourceManager.getSound(pacmanGame.EATGHOST_SOUND_RSC).play();
				pg.score = pg.score + 200;
				}
				
			}else {
				if(DEBUG_SCATTER) System.out.println("PacMan Collided with a ghost -----> GO TO DEATH STATE");
				ResourceManager.getSound(pacmanGame.PACDEATH_SOUND_RSC).play();
				reset = true;
				pg.pacman.TakeHealth();
				// Reset PacMan
				pg.pacman.grid_x = 13;
				pg.pacman.grid_y = 26;
				pg.pacman.setPosition(Map.grid[pg.pacman.grid_x][pg.pacman.grid_y].getTileMid().getX()   ,
						Map.grid[pg.pacman.grid_x][pg.pacman.grid_y].getTileMid().getY());
				
				// Set blinky outside of door
				pg.blinky.grid_x = 13;
				pg.blinky.grid_y = 14;
				pg.blinky.setPosition(Map.grid[pg.blinky.grid_x][pg.blinky.grid_y].getTileMid().getX() + 8 ,
						Map.grid[pg.blinky.grid_x][pg.blinky.grid_y].getTileMid().getY());
				
				// Set pinky to middle of ghost house
				pg.pinky.grid_x = 13;
				pg.pinky.grid_y = 17;
				pg.pinky.setPosition(Map.grid[pg.pinky.grid_x][pg.pinky.grid_y].getTileMid().getX() + 8 ,
						Map.grid[pg.pinky.grid_x][pg.pinky.grid_y].getTileMid().getY());
				
				// Set inky to left of ghost house
				pg.inky.grid_x = 11;
				pg.inky.grid_y = 17;
				pg.inky.setPosition(Map.grid[pg.inky.grid_x][pg.inky.grid_y].getTileMid().getX() + 8 ,
						Map.grid[pg.inky.grid_x][pg.inky.grid_y].getTileMid().getY());
				
				// Set clyde to right of ghost house
				pg.clyde.grid_x = 15;
				pg.clyde.grid_y = 17;
				pg.clyde.setPosition(Map.grid[pg.clyde.grid_x][pg.clyde.grid_y].getTileMid().getX() + 8 ,
						Map.grid[pg.clyde.grid_x][pg.clyde.grid_y].getTileMid().getY());
				
				
			}
		}
		
		// PINKY
		if(pg.pacman.sameTile(pg.pinky.grid_x, pg.pinky.grid_y)){
			if(SCATTER){
				if(DEBUG_SCATTER_PINKY) System.out.println("SCATTER: PacMan collided with a PINKY");
				// We need to set a flag here that will tell them to target the ghost house when they collide.
				if (PINKY_DEAD){
					
				}else {
					PINKY_DEAD = true;
					ResourceManager.getSound(pacmanGame.EATGHOST_SOUND_RSC).play();
					pg.score = pg.score + 200;
				}
				
			}else {
				if(DEBUG_SCATTER) System.out.println("PacMan Collided with a ghost -----> GO TO DEATH STATE");
				ResourceManager.getSound(pacmanGame.PACDEATH_SOUND_RSC).play();
				reset = true;
				pg.pacman.TakeHealth();
				// Reset PacMan
				pg.pacman.grid_x = 13;
				pg.pacman.grid_y = 26;
				pg.pacman.setPosition(Map.grid[pg.pacman.grid_x][pg.pacman.grid_y].getTileMid().getX()   ,
						Map.grid[pg.pacman.grid_x][pg.pacman.grid_y].getTileMid().getY());
				
				// Set blinky outside of door
				pg.blinky.grid_x = 13;
				pg.blinky.grid_y = 14;
				pg.blinky.setPosition(Map.grid[pg.blinky.grid_x][pg.blinky.grid_y].getTileMid().getX() + 8 ,
						Map.grid[pg.blinky.grid_x][pg.blinky.grid_y].getTileMid().getY());
				
				// Set pinky to middle of ghost house
				pg.pinky.grid_x = 13;
				pg.pinky.grid_y = 17;
				pg.pinky.setPosition(Map.grid[pg.pinky.grid_x][pg.pinky.grid_y].getTileMid().getX() + 8 ,
						Map.grid[pg.pinky.grid_x][pg.pinky.grid_y].getTileMid().getY());
				
				// Set inky to left of ghost house
				pg.inky.grid_x = 11;
				pg.inky.grid_y = 17;
				pg.inky.setPosition(Map.grid[pg.inky.grid_x][pg.inky.grid_y].getTileMid().getX() + 8 ,
						Map.grid[pg.inky.grid_x][pg.inky.grid_y].getTileMid().getY());
				
				// Set clyde to right of ghost house
				pg.clyde.grid_x = 15;
				pg.clyde.grid_y = 17;
				pg.clyde.setPosition(Map.grid[pg.clyde.grid_x][pg.clyde.grid_y].getTileMid().getX() + 8 ,
						Map.grid[pg.clyde.grid_x][pg.clyde.grid_y].getTileMid().getY());
			}
		}
		
		
		// INKY
		if(pg.pacman.sameTile(pg.inky.grid_x, pg.inky.grid_y)){
			if(SCATTER){
				if(DEBUG_SCATTER_INKY) System.out.println("SCATTER: PacMan collided with a INKY");
				// We need to set a flag here that will tell them to target the ghost house when they collide.
				if (INKY_DEAD){
					
				}else{
					INKY_DEAD = true;
					ResourceManager.getSound(pacmanGame.EATGHOST_SOUND_RSC).play();
					pg.score = pg.score + 200;
				}
			}else {
				if(DEBUG_SCATTER) System.out.println("PacMan Collided with a ghost -----> GO TO DEATH STATE");
				ResourceManager.getSound(pacmanGame.PACDEATH_SOUND_RSC).play();
				reset = true;
				pg.pacman.TakeHealth();
				// Reset PacMan
				pg.pacman.grid_x = 13;
				pg.pacman.grid_y = 26;
				pg.pacman.setPosition(Map.grid[pg.pacman.grid_x][pg.pacman.grid_y].getTileMid().getX()   ,
						Map.grid[pg.pacman.grid_x][pg.pacman.grid_y].getTileMid().getY());
				
				// Set blinky outside of door
				pg.blinky.grid_x = 13;
				pg.blinky.grid_y = 14;
				pg.blinky.setPosition(Map.grid[pg.blinky.grid_x][pg.blinky.grid_y].getTileMid().getX() + 8 ,
						Map.grid[pg.blinky.grid_x][pg.blinky.grid_y].getTileMid().getY());
				
				// Set pinky to middle of ghost house
				pg.pinky.grid_x = 13;
				pg.pinky.grid_y = 17;
				pg.pinky.setPosition(Map.grid[pg.pinky.grid_x][pg.pinky.grid_y].getTileMid().getX() + 8 ,
						Map.grid[pg.pinky.grid_x][pg.pinky.grid_y].getTileMid().getY());
				
				// Set inky to left of ghost house
				pg.inky.grid_x = 11;
				pg.inky.grid_y = 17;
				pg.inky.setPosition(Map.grid[pg.inky.grid_x][pg.inky.grid_y].getTileMid().getX() + 8 ,
						Map.grid[pg.inky.grid_x][pg.inky.grid_y].getTileMid().getY());
				
				// Set clyde to right of ghost house
				pg.clyde.grid_x = 15;
				pg.clyde.grid_y = 17;
				pg.clyde.setPosition(Map.grid[pg.clyde.grid_x][pg.clyde.grid_y].getTileMid().getX() + 8 ,
						Map.grid[pg.clyde.grid_x][pg.clyde.grid_y].getTileMid().getY());
			}
		}
		
		// CLYDE
		if(pg.pacman.sameTile(pg.clyde.grid_x, pg.clyde.grid_y)){
			if(SCATTER){
				if(DEBUG_SCATTER_CLYDE) System.out.println("SCATTER: PacMan collided with a CLYDE");
				// We need to set a flag here that will tell them to target the ghost house when they collide.
				if (CLYDE_DEAD){
					
				}else {
					CLYDE_DEAD = true;
					ResourceManager.getSound(pacmanGame.EATGHOST_SOUND_RSC).play();
					pg.score = pg.score + 200;
				}
				
			}else {
				if(DEBUG_SCATTER) System.out.println("PacMan Collided with a ghost -----> GO TO DEATH STATE");
				ResourceManager.getSound(pacmanGame.PACDEATH_SOUND_RSC).play();
				reset = true;
				pg.pacman.TakeHealth();
				// Reset PacMan
				pg.pacman.grid_x = 13;
				pg.pacman.grid_y = 26;
				pg.pacman.setPosition(Map.grid[pg.pacman.grid_x][pg.pacman.grid_y].getTileMid().getX()   ,
						Map.grid[pg.pacman.grid_x][pg.pacman.grid_y].getTileMid().getY());
				
				// Set blinky outside of door
				pg.blinky.grid_x = 13;
				pg.blinky.grid_y = 14;
				pg.blinky.setPosition(Map.grid[pg.blinky.grid_x][pg.blinky.grid_y].getTileMid().getX() + 8 ,
						Map.grid[pg.blinky.grid_x][pg.blinky.grid_y].getTileMid().getY());
				
				// Set pinky to middle of ghost house
				pg.pinky.grid_x = 13;
				pg.pinky.grid_y = 17;
				pg.pinky.setPosition(Map.grid[pg.pinky.grid_x][pg.pinky.grid_y].getTileMid().getX() + 8 ,
						Map.grid[pg.pinky.grid_x][pg.pinky.grid_y].getTileMid().getY());
				
				// Set inky to left of ghost house
				pg.inky.grid_x = 11;
				pg.inky.grid_y = 17;
				pg.inky.setPosition(Map.grid[pg.inky.grid_x][pg.inky.grid_y].getTileMid().getX() + 8 ,
						Map.grid[pg.inky.grid_x][pg.inky.grid_y].getTileMid().getY());
				
				// Set clyde to right of ghost house
				pg.clyde.grid_x = 15;
				pg.clyde.grid_y = 17;
				pg.clyde.setPosition(Map.grid[pg.clyde.grid_x][pg.clyde.grid_y].getTileMid().getX() + 8 ,
						Map.grid[pg.clyde.grid_x][pg.clyde.grid_y].getTileMid().getY());
			}
		}
		
	
		// When ghosts are in SCATTER state they run to their designated corners of the map.
		//		Blinky  = Top Right corner [26][2]
		//		Pinky   =  Top Left corner [1][2]
		//		Inky = Bottom Right corner [26][34]
		// 		Clyde = Bottom Left corner [1][34]
		
	
		// Ghosts are in CHASE state here. They actively seek PacMan based on pathfinding
	
		// +========+
		// | GHOSTS |====================================================================================
		// +========+
		if (DEBUG) System.out.println("grid x: " + pg.pacman.grid_x);
		if (DEBUG) System.out.println("canMove: "+ canMove);
			
		if (ghostsTurn && reset == false){
			//+==========+
			//|  BLINKY  |==========================================================================
			//+==========+
		   /* Holds the distances from ghost to PacMan
			* 100000 is just a really big number that is out of bounds for gameboard.
			* represents infinity
			*/
			float tmp_r = 100000;
			float tmp_l = 100000;
			float tmp_u = 100000;
			float tmp_d = 100000;
			float heading;
			// Check if Blinky is at a door before anything else
			if (SCATTER){
				if(BLINKY_DEAD){
					// When we get to the door of the ghost house, go inside and wait
					if (pg.blinky.grid_x == 13 && pg.blinky.grid_y == 14){
						if(DEBUG_SCATTER_BLINKY) System.out.println("@@@@@@@@@@@@@@@@Im at the door to the ghost house");
						pg.blinky.grid_x = 12;
						pg.blinky.grid_y =17;
						pg.blinky.setPosition(Map.grid[pg.blinky.grid_x][pg.blinky.grid_y].getTileMid().getX() -8 ,
								Map.grid[pg.blinky.grid_x][pg.blinky.grid_y].getTileMid().getY());
						// Set blinkys position to up
						gh_blinky_lock = true;
					}
				}
			}
			
			// We check if ghost is in an Intersection tile first.
			if(Map.grid[pg.blinky.grid_x][pg.blinky.grid_y].isIntersect()){
				// It is a intersection tile, calculate a new direction
				// check each tile adjacent to ghost if it is not a wall
				if(Map.grid[pg.blinky.grid_x + 1][pg.blinky.grid_y].isWall() == false && pg.blinky.getLastDirection() != 'l'){
					if(SCATTER){
						if(DEBUG_SCATTER_BLINKY) System.out.println("in SCATTER State");
						if (BLINKY_DEAD){
							// Head Back to the ghost house
							if(DEBUG_SCATTER_BLINKY) System.out.println("Blinkys Dead... Going back to house");
							tmp_r = Map.grid[pg.blinky.grid_x + 1][pg.blinky.grid_y].getTileMid().distance(
									Map.grid[13][15].getTileMid());
						}else{
							tmp_r = Map.grid[pg.blinky.grid_x + 1][pg.blinky.grid_y].getTileMid().distance(
									Map.grid[26][2].getTileMid());	
						}
					}
					else {
						// get the distance between ghost and PacMan
						tmp_r = Map.grid[pg.blinky.grid_x + 1][pg.blinky.grid_y].getTileMid().distance(
									Map.grid[pg.pacman.grid_x][pg.pacman.grid_y].getTileMid());
					}
				}
				if(Map.grid[pg.blinky.grid_x - 1][pg.blinky.grid_y].isWall() == false && pg.blinky.getLastDirection() != 'r'){	
					if(SCATTER){
						if(DEBUG_SCATTER_BLINKY) System.out.println("in SCATTER State");
						
						if (BLINKY_DEAD) {
							if(DEBUG_SCATTER_BLINKY) System.out.println("Blinkys Dead... Going back to house");
							tmp_l = Map.grid[pg.blinky.grid_x - 1][pg.blinky.grid_y].getTileMid().distance(
									Map.grid[13][15].getTileMid());
						}else {
							tmp_l = Map.grid[pg.blinky.grid_x - 1][pg.blinky.grid_y].getTileMid().distance(
									Map.grid[26][2].getTileMid());
						}
					}
							
					else {
						// get the distance between ghost and PacMan
						tmp_l = Map.grid[pg.blinky.grid_x - 1][pg.blinky.grid_y].getTileMid().distance(
									Map.grid[pg.pacman.grid_x][pg.pacman.grid_y].getTileMid());
					}
				}
				if(Map.grid[pg.blinky.grid_x][pg.blinky.grid_y + 1].isWall() == false && pg.blinky.getLastDirection() != 'u'){
					if(SCATTER){
						if(DEBUG_SCATTER_BLINKY) System.out.println("in SCATTER State");
						
						if(BLINKY_DEAD){
							if(DEBUG_SCATTER_BLINKY) System.out.println("Blinkys Dead... Going back to house");
							tmp_d = Map.grid[pg.blinky.grid_x][pg.blinky.grid_y + 1].getTileMid().distance(
									Map.grid[13][15].getTileMid());
						} else {
							tmp_d = Map.grid[pg.blinky.grid_x][pg.blinky.grid_y + 1].getTileMid().distance(
									Map.grid[26][2].getTileMid());	
						}
					} 
					else {
						// get the distance between ghost and PacMan
						tmp_d = Map.grid[pg.blinky.grid_x][pg.blinky.grid_y + 1].getTileMid().distance(
									Map.grid[pg.pacman.grid_x][pg.pacman.grid_y].getTileMid());
					}	
				}
				if(Map.grid[pg.blinky.grid_x][pg.blinky.grid_y - 1].isWall() == false && pg.blinky.getLastDirection() != 'd'){
					if(SCATTER){
						if(DEBUG_SCATTER_BLINKY) System.out.println("in SCATTER State");
						
						if (BLINKY_DEAD) {
							if(DEBUG_SCATTER_BLINKY) System.out.println("Blinkys Dead... Going back to house");
							tmp_u = Map.grid[pg.blinky.grid_x][pg.blinky.grid_y - 1].getTileMid().distance(
									Map.grid[13][15].getTileMid());
						}else {
							tmp_u = Map.grid[pg.blinky.grid_x][pg.blinky.grid_y - 1].getTileMid().distance(
									Map.grid[26][2].getTileMid());
						}
					} 
					else {
						// get the distance between ghost and PacMan
						tmp_u = Map.grid[pg.blinky.grid_x][pg.blinky.grid_y - 1].getTileMid().distance(
									Map.grid[pg.pacman.grid_x][pg.pacman.grid_y].getTileMid());
					}
				}
				
				// set ghosts heading; heading is shortest distance form ghost to target
				heading = Math.min(Math.min(tmp_l, tmp_r), Math.min(tmp_u, tmp_d));
				if(DEBUG_BLINKY) System.out.println("Blinky's heading : "+ heading);
				
				// Now depending what our heading is, we set the ghosts direction.
				if(DEBUG_BLINKY) System.out.println("blinkys direction: "+ pg.blinky.getLastDirection());
				
				if(heading == tmp_r){
					pg.blinky.setDirection('r');
				}
				else
				if(heading == tmp_l){
					pg.blinky.setDirection('l');
				}
				else
				if(heading == tmp_u){
					pg.blinky.setDirection('u');
				}
				else
				if(heading == tmp_d){
					pg.blinky.setDirection('d');				
				}
				
				// Update ghosts animation based on updated grid
				pg.blinky.updateGrid();
				pg.blinky.setPosition(Map.grid[pg.blinky.grid_x][pg.blinky.grid_y].getTileMid().getX() ,
						Map.grid[pg.blinky.grid_x][pg.blinky.grid_y].getTileMid().getY());
				if(SHOW_GHOST_TARGETS) System.out.println("BLINKY'S TARGET X COORD: "+ pg.blinky.grid_x);
				if(SHOW_GHOST_TARGETS) System.out.println("BLINKY'S TARGET Y COORD: "+ pg.blinky.grid_y);
				if(SHOW_GHOST_TARGETS) System.out.println();
				
			} else {
				// Not at a Intersection
				// Check the ghosts last direction and continue
				if (gh_blinky_lock){
					if (SCATTER){

					}else{
						gh_blinky_lock = false;
						// Set blinky to intersection tile outside of door
						pg.blinky.grid_x = 12;
						pg.blinky.grid_y =14;
						pg.blinky.setPosition(Map.grid[pg.blinky.grid_x][pg.blinky.grid_y].getTileMid().getX() -8 ,
								Map.grid[pg.blinky.grid_x][pg.blinky.grid_y].getTileMid().getY());
						BLINKY_DEAD = false;
					}
				}else {
					pg.blinky.updateGrid();
					pg.blinky.setPosition(Map.grid[pg.blinky.grid_x][pg.blinky.grid_y].getTileMid().getX() ,
							Map.grid[pg.blinky.grid_x][pg.blinky.grid_y].getTileMid().getY());
				}
			}
			// Reset ghosts life
			if (SCATTER == false) BLINKY_DEAD = false;

			// ANIMATION CONTROLLER ---------------------------------------------------------
			// Update blinky animation based on lastDirection
			char anime_direction = pg.blinky.getLastDirection();
			if (anime_direction == 'r'){
				if (SCATTER){
					if(BLINKY_DEAD){
						blinky_anime = new Animation(pg.NAKED_GHOST_SHEET, GHOST_SPEED);
					}else{
						blinky_anime = new Animation(pg.SCATTER_GHOST_SHEET, GHOST_SPEED);
					}
				} else
				blinky_anime = new Animation(pg.BLINKY_R_SHEET, GHOST_SPEED);
			}
			
			if (anime_direction == 'l'){
				if (SCATTER){
					if(BLINKY_DEAD){
						blinky_anime = new Animation(pg.NAKED_GHOST_SHEET, GHOST_SPEED);
					}else{
						blinky_anime = new Animation(pg.SCATTER_GHOST_SHEET, GHOST_SPEED);
					}
				} else
				blinky_anime = new Animation(pg.BLINKY_L_SHEET, GHOST_SPEED);
			}
			
			if (anime_direction == 'u'){
				if (SCATTER){
					if(BLINKY_DEAD){
						blinky_anime = new Animation(pg.NAKED_GHOST_SHEET, GHOST_SPEED);
					}else{
						blinky_anime = new Animation(pg.SCATTER_GHOST_SHEET, GHOST_SPEED);
					}
				} else
				blinky_anime = new Animation(pg.BLINKY_U_SHEET, GHOST_SPEED);
			}
			
			if (anime_direction == 'd'){
				if (SCATTER){
					if(BLINKY_DEAD){
						blinky_anime = new Animation(pg.NAKED_GHOST_SHEET, GHOST_SPEED);
					}else{
						blinky_anime = new Animation(pg.SCATTER_GHOST_SHEET, GHOST_SPEED);
					}
				} else
				blinky_anime = new Animation(pg.BLINKY_D_SHEET, GHOST_SPEED);
			}
		
			//+==========+
			//|  PINKY   |==========================================================================
			//+==========+
		   /* Holds the distances from ghost to PacMan
			* 100000 is just a really bug number that is out of bounds for gameboard.
			* represents infinity
			*/
			tmp_r = 100000;
			tmp_l = 100000;
			tmp_u = 100000;
			tmp_d = 100000;
			
			// Check if Pinky is at a door before anything else
			if (SCATTER){
				if(PINKY_DEAD){
					// When we get to the door of the ghost house, go inside and wait
					if (pg.pinky.grid_x == 13 && pg.pinky.grid_y == 14){
						if(DEBUG_SCATTER_PINKY) System.out.println("@@@@@@@@@@@@@@@@Im at the door to the ghost house");
						pg.pinky.grid_x = 14;
						pg.pinky.grid_y =17;
						pg.pinky.setPosition(Map.grid[pg.pinky.grid_x][pg.pinky.grid_y].getTileMid().getX() -8 ,
								Map.grid[pg.pinky.grid_x][pg.pinky.grid_y].getTileMid().getY());
						
						gh_pinky_lock = true;
						
					}
				}
			}
			
			
			// We check if ghost is in an Intersection tile first.
			if(Map.grid[pg.pinky.grid_x][pg.pinky.grid_y].isIntersect()){
				// It is a intersection tile, calculate a new direction
				
				// Need to update pinkys target based on what direction pacman is heading
				pg.pacman.pinkys_target_x();
				pg.pacman.pinkys_target_y();
				
				if(SHOW_GHOST_TARGETS) System.out.println("PINKYS TARGET X COORD: "+ pg.pacman.pinkyTarget_x);
				if(SHOW_GHOST_TARGETS) System.out.println("PINKYS TARGET Y COORD: "+ pg.pacman.pinkyTarget_y);
				if(SHOW_GHOST_TARGETS) System.out.println();
				
				// check each tile adjacent to ghost if it is not a wall
				if(Map.grid[pg.pinky.grid_x + 1][pg.pinky.grid_y].isWall() == false && pg.pinky.getLastDirection() != 'l'){
					if(SCATTER){
						if(DEBUG_SCATTER_PINKY) System.out.println("in SCATTER State");
						
						if (PINKY_DEAD){
							// Head Back to the ghost house
							if(DEBUG_SCATTER_PINKY) System.out.println("Pinkys Dead... Going back to house");
							tmp_r = Map.grid[pg.pinky.grid_x + 1][pg.pinky.grid_y].getTileMid().distance(
									Map.grid[13][15].getTileMid());
						}else{
							tmp_r = Map.grid[pg.pinky.grid_x + 1][pg.pinky.grid_y].getTileMid().distance(
									Map.grid[1][2].getTileMid());	
						}
					} else {
						// get the distance between ghost and PacMan
						tmp_r = Map.grid[pg.pinky.grid_x + 1][pg.pinky.grid_y].getTileMid().distance(
									Map.grid[pg.pacman.pinkyTarget_x][pg.pacman.pinkyTarget_y].getTileMid());
					}	
				}
				if(Map.grid[pg.pinky.grid_x - 1][pg.pinky.grid_y].isWall() == false && pg.pinky.getLastDirection() != 'r'){
					if(SCATTER){
						if(DEBUG_SCATTER_PINKY) System.out.println("in SCATTER State");
						
						if (PINKY_DEAD){
							// Head Back to the ghost house
							if(DEBUG_SCATTER_PINKY) System.out.println("Pinkys Dead... Going back to house");
							tmp_l = Map.grid[pg.pinky.grid_x - 1][pg.pinky.grid_y].getTileMid().distance(
									Map.grid[13][15].getTileMid());
						}else{
							tmp_l = Map.grid[pg.pinky.grid_x - 1][pg.pinky.grid_y].getTileMid().distance(
									Map.grid[1][2].getTileMid());	
						}
				
					} else {
						// get the distance between ghost and PacMan
						tmp_l = Map.grid[pg.pinky.grid_x - 1][pg.pinky.grid_y].getTileMid().distance(
									Map.grid[pg.pacman.pinkyTarget_x][pg.pacman.pinkyTarget_y].getTileMid());
					}	
				}
				if(Map.grid[pg.pinky.grid_x][pg.pinky.grid_y + 1].isWall() == false && pg.pinky.getLastDirection() != 'u'){
					if(SCATTER){
						if(DEBUG_SCATTER_PINKY) System.out.println("in SCATTER State");
						
						if (PINKY_DEAD){
							// Head Back to the ghost house
							if(DEBUG_SCATTER_PINKY) System.out.println("Pinkys Dead... Going back to house");
							tmp_d = Map.grid[pg.pinky.grid_x][pg.pinky.grid_y + 1].getTileMid().distance(
									Map.grid[13][15].getTileMid());
						}else{
							tmp_d = Map.grid[pg.pinky.grid_x][pg.pinky.grid_y + 1].getTileMid().distance(
									Map.grid[1][2].getTileMid());	
						}
					} else {
						// get the distance between ghost and PacMan
						tmp_d = Map.grid[pg.pinky.grid_x][pg.pinky.grid_y + 1].getTileMid().distance(
									Map.grid[pg.pacman.pinkyTarget_x][pg.pacman.pinkyTarget_y].getTileMid());
					}
				}
				if(Map.grid[pg.pinky.grid_x][pg.pinky.grid_y - 1].isWall() == false && pg.pinky.getLastDirection() != 'd'){
					if(SCATTER){
						if(DEBUG_SCATTER_PINKY) System.out.println("in SCATTER State");
						
						if (PINKY_DEAD){
							// Head Back to the ghost house
							if(DEBUG_SCATTER_PINKY) System.out.println("Pinkys Dead... Going back to house");
							tmp_u = Map.grid[pg.pinky.grid_x][pg.pinky.grid_y - 1].getTileMid().distance(
									Map.grid[13][15].getTileMid());
						}else{
							tmp_u = Map.grid[pg.pinky.grid_x][pg.pinky.grid_y - 1].getTileMid().distance(
									Map.grid[1][2].getTileMid());	
						}
					} else {
						// get the distance between ghost and PacMan
						tmp_u = Map.grid[pg.pinky.grid_x][pg.pinky.grid_y - 1].getTileMid().distance(
									Map.grid[pg.pacman.pinkyTarget_x][pg.pacman.pinkyTarget_y].getTileMid());
					}
				}
				
				// set ghosts heading
				heading = Math.min(Math.min(tmp_l, tmp_r), Math.min(tmp_u, tmp_d));
				if(DEBUG_PINKY) System.out.println("Pinkys heading : "+ heading);
				
				// Now depending what our heading is, we set the ghosts direction.
				if(heading == tmp_r){
					pg.pinky.setDirection('r');
				}
				else
				if(heading == tmp_l){
					pg.pinky.setDirection('l');
				}
				else
				if(heading == tmp_u){
					pg.pinky.setDirection('u');
				}
				else
				if(heading == tmp_d){
					pg.pinky.setDirection('d');				
				}
				
				// Update ghosts sprite based on updated grid
				pg.pinky.updateGrid();
				pg.pinky.setPosition(Map.grid[pg.pinky.grid_x][pg.pinky.grid_y].getTileMid().getX() ,
						Map.grid[pg.pinky.grid_x][pg.pinky.grid_y].getTileMid().getY());
			} else {
				
				// Not at a Intersection
				// Check the ghosts last direction and continue
				if (gh_pinky_lock){
					if (SCATTER){
						
					}else{
						gh_pinky_lock = false;
						// Set PINKY to intersection tile outside of door
						pg.pinky.grid_x = 12;
						pg.pinky.grid_y =14;
						pg.pinky.setPosition(Map.grid[pg.pinky.grid_x][pg.pinky.grid_y].getTileMid().getX() -8 ,
								Map.grid[pg.pinky.grid_x][pg.pinky.grid_y].getTileMid().getY());
						PINKY_DEAD = false;
					}
				}else {
					pg.pinky.updateGrid();
					pg.pinky.setPosition(Map.grid[pg.pinky.grid_x][pg.pinky.grid_y].getTileMid().getX() ,
							Map.grid[pg.pinky.grid_x][pg.pinky.grid_y].getTileMid().getY());
				}
			}
			// Reset ghosts life
			if (SCATTER == false) PINKY_DEAD = false;
		
			// ANIMATION CONTROLLER ---------------------------------------------------------
			// Update pinky animation based on lastDirection
			anime_direction = pg.pinky.getLastDirection();
			if (anime_direction == 'r'){
				if (SCATTER){
					if(PINKY_DEAD){
						pinky_anime = new Animation(pg.NAKED_GHOST_SHEET, GHOST_SPEED);
					}else{
						pinky_anime = new Animation(pg.SCATTER_GHOST_SHEET, GHOST_SPEED);
					}
				} else
				pinky_anime = new Animation(pg.PINKY_R_SHEET, GHOST_SPEED);
			}
			
			if (anime_direction == 'l'){
				if (SCATTER){
					if(PINKY_DEAD){
						pinky_anime = new Animation(pg.NAKED_GHOST_SHEET, GHOST_SPEED);
					}else{
						pinky_anime = new Animation(pg.SCATTER_GHOST_SHEET, GHOST_SPEED);
					}
				} else
				pinky_anime = new Animation(pg.PINKY_L_SHEET, GHOST_SPEED);
				
			}
			
			if (anime_direction == 'u'){
				if (SCATTER){
					if(PINKY_DEAD){
						pinky_anime = new Animation(pg.NAKED_GHOST_SHEET, GHOST_SPEED);
					}else{
						pinky_anime = new Animation(pg.SCATTER_GHOST_SHEET, GHOST_SPEED);
					}
				} else
				pinky_anime = new Animation(pg.PINKY_U_SHEET, GHOST_SPEED);
				
			}
			
			if (anime_direction == 'd'){
				if (SCATTER){
					if(PINKY_DEAD){
						pinky_anime = new Animation(pg.NAKED_GHOST_SHEET, GHOST_SPEED);
					}else{
						pinky_anime = new Animation(pg.SCATTER_GHOST_SHEET, GHOST_SPEED);
					}
				} else
				pinky_anime = new Animation(pg.PINKY_D_SHEET, GHOST_SPEED);
		
			}
		
			//+==========+
			//| INKY     |==========================================================================
			//+==========+
		   /* Holds the distances from ghost to PacMan
			* 100000 is just a really bug number that is out of bounds for gameboard.
			* represents infinity
			*/
			tmp_r = 100000;
			tmp_l = 100000;
			tmp_u = 100000;
			tmp_d = 100000;
			
			// Check if Inky is at a door before anything else
			if (SCATTER){
				if(INKY_DEAD){
					// When we get to the door of the ghost house, go inside and wait
					if (pg.inky.grid_x == 13 && pg.inky.grid_y == 14){
						if(DEBUG_SCATTER_INKY) System.out.println("@@@@@@@@@@@@@@@@Im at the door to the ghost house");
						pg.inky.grid_x = 14;
						pg.inky.grid_y =17;
						pg.inky.setPosition(Map.grid[pg.inky.grid_x][pg.inky.grid_y].getTileMid().getX() + 8 ,
								Map.grid[pg.inky.grid_x][pg.inky.grid_y].getTileMid().getY());
						gh_inky_lock = true;
					}
				}
			}

			// We check if ghost is in an Intersection tile first.
			if(Map.grid[pg.inky.grid_x][pg.inky.grid_y].isIntersect()){
				// It is a intersection tile, calculate a new direction
				
				// Need to update inkys target. Inkys target needs blinky's position in its calculations
				pg.pacman.inkys_target(pg.blinky.grid_x, pg.blinky.grid_y);
				
				if (SHOW_GHOST_TARGETS) System.out.println("INKYS TARGET COORD X: "+ pg.pacman.inkyTarget_x);
				if (SHOW_GHOST_TARGETS) System.out.println("INKYS TARGET COORD Y: "+ pg.pacman.inkyTarget_y);
				if(SHOW_GHOST_TARGETS) System.out.println();
				
				// check each tile adjacent to ghost if it is not a wall
				if(Map.grid[pg.inky.grid_x + 1][pg.inky.grid_y].isWall() == false && pg.inky.getLastDirection() != 'l'){
					if(SCATTER){
						if(DEBUG_SCATTER_INKY) System.out.println("in SCATTER State");
						
						if (INKY_DEAD){
							// Head Back to the ghost house
							if(DEBUG_SCATTER_INKY) System.out.println("Inkys Dead... Going back to house");
							tmp_r = Map.grid[pg.inky.grid_x + 1][pg.inky.grid_y].getTileMid().distance(
									Map.grid[13][15].getTileMid());
						}else{
							tmp_r = Map.grid[pg.inky.grid_x + 1][pg.inky.grid_y].getTileMid().distance(
									Map.grid[26][34].getTileMid());		
						}
					} else {
						// get the distance between ghost and PacMan
						tmp_r = Map.grid[pg.inky.grid_x + 1][pg.inky.grid_y].getTileMid().distance(
									Map.grid[pg.pacman.inkyTarget_x][pg.pacman.inkyTarget_y].getTileMid());
					}
				}
				if(Map.grid[pg.inky.grid_x - 1][pg.inky.grid_y].isWall() == false && pg.inky.getLastDirection() != 'r'){
					if(SCATTER){
						if(DEBUG_SCATTER_INKY) System.out.println("in SCATTER State");
						if (INKY_DEAD){
							// Head Back to the ghost house
							if(DEBUG_SCATTER_INKY) System.out.println("Inkys Dead... Going back to house");
							tmp_l = Map.grid[pg.inky.grid_x - 1][pg.inky.grid_y].getTileMid().distance(
									Map.grid[13][15].getTileMid());
						}else{
							tmp_l = Map.grid[pg.inky.grid_x - 1][pg.inky.grid_y].getTileMid().distance(
									Map.grid[26][34].getTileMid());		
						}
					} else {
						// get the distance between ghost and PacMan
						tmp_l = Map.grid[pg.inky.grid_x - 1][pg.inky.grid_y].getTileMid().distance(
									Map.grid[pg.pacman.inkyTarget_x][pg.pacman.inkyTarget_y].getTileMid());
					}
				}
				if(Map.grid[pg.inky.grid_x][pg.inky.grid_y + 1].isWall() == false && pg.inky.getLastDirection() != 'u'){
					if(SCATTER){
						if(DEBUG_SCATTER_INKY) System.out.println("in SCATTER State");
						if (INKY_DEAD){
							// Head Back to the ghost house
							if(DEBUG_SCATTER_INKY) System.out.println("Inkys Dead... Going back to house");
							tmp_d = Map.grid[pg.inky.grid_x][pg.inky.grid_y + 1].getTileMid().distance(
									Map.grid[13][15].getTileMid());
						}else{
							tmp_d = Map.grid[pg.inky.grid_x][pg.inky.grid_y + 1].getTileMid().distance(
									Map.grid[26][34].getTileMid());		
						}
					} else {
						// get the distance between ghost and PacMan
						tmp_d = Map.grid[pg.inky.grid_x][pg.inky.grid_y + 1].getTileMid().distance(
									Map.grid[pg.pacman.inkyTarget_x][pg.pacman.inkyTarget_y].getTileMid());
					}
				}
				if(Map.grid[pg.inky.grid_x][pg.inky.grid_y - 1].isWall() == false && pg.inky.getLastDirection() != 'd'){
					if(SCATTER){
						if(DEBUG_SCATTER_INKY) System.out.println("in SCATTER State");
						if (INKY_DEAD){
							// Head Back to the ghost house
							if(DEBUG_SCATTER_INKY) System.out.println("Inkys Dead... Going back to house");
							tmp_u = Map.grid[pg.inky.grid_x][pg.inky.grid_y - 1].getTileMid().distance(
									Map.grid[13][15].getTileMid());
						}else{
							tmp_u = Map.grid[pg.inky.grid_x][pg.inky.grid_y - 1].getTileMid().distance(
									Map.grid[26][34].getTileMid());		
						}
					} else {
						// get the distance between ghost and PacMan
						tmp_u = Map.grid[pg.inky.grid_x][pg.inky.grid_y - 1].getTileMid().distance(
									Map.grid[pg.pacman.inkyTarget_x][pg.pacman.inkyTarget_y].getTileMid());
					}
				}
				
				// set ghosts heading
				heading = Math.min(Math.min(tmp_l, tmp_r), Math.min(tmp_u, tmp_d));
				if(DEBUG_INKY) System.out.println("inkys heading : "+ heading);
				
				// Now depending what our heading is, we set the ghosts direction.
				if(heading == tmp_r){
					pg.inky.setDirection('r');
				}
				else
				if(heading == tmp_l){
					pg.inky.setDirection('l');
				}
				else
				if(heading == tmp_u){
					pg.inky.setDirection('u');
				}
				else
				if(heading == tmp_d){
					pg.inky.setDirection('d');				
				}
				
				// Update ghosts sprite based on updated grid
				pg.inky.updateGrid();
				pg.inky.setPosition(Map.grid[pg.inky.grid_x][pg.inky.grid_y].getTileMid().getX() ,
						Map.grid[pg.inky.grid_x][pg.inky.grid_y].getTileMid().getY());			
			} else {
				// Not at a Intersection
				if (gh_inky_lock){
					if (SCATTER){
						if (pacdots_remaining == 210){
							pg.inky.grid_x = 12;
							pg.inky.grid_y = 14;
							pg.inky.setPosition(Map.grid[pg.inky.grid_x][pg.inky.grid_y].getTileMid().getX() ,
									Map.grid[pg.inky.grid_x][pg.inky.grid_y].getTileMid().getY());
						}
						
					}else{
						gh_inky_lock = false;
						// Set PINKY to intersection tile outside of door
						pg.inky.grid_x = 12;
						pg.inky.grid_y =14;
						pg.inky.setPosition(Map.grid[pg.inky.grid_x][pg.inky.grid_y].getTileMid().getX() + 8 ,
								Map.grid[pg.inky.grid_x][pg.inky.grid_y].getTileMid().getY());
						INKY_DEAD = false;
					}
				}else {
					if (pacdots_remaining == 210){
						pg.inky.grid_x = 12;
						pg.inky.grid_y = 14;
						pg.inky.setPosition(Map.grid[pg.inky.grid_x][pg.inky.grid_y].getTileMid().getX() ,
								Map.grid[pg.inky.grid_x][pg.inky.grid_y].getTileMid().getY());
					}else
					
					if (pacdots_remaining > 210){
						pg.inky.setPosition(Map.grid[pg.inky.grid_x][pg.inky.grid_y].getTileMid().getX() + 8 ,
								Map.grid[pg.inky.grid_x][pg.inky.grid_y].getTileMid().getY());
					}
					else {
						// still in ghost house, move Inky out
						if (pg.inky.grid_x == 11 && pg.inky.grid_y == 17){
							pg.inky.grid_x = 12;
							pg.inky.grid_y = 14;
							pg.inky.setPosition(Map.grid[pg.inky.grid_x][pg.inky.grid_y].getTileMid().getX() ,
									Map.grid[pg.inky.grid_x][pg.inky.grid_y].getTileMid().getY());
						}else {
							pg.inky.updateGrid();
							pg.inky.setPosition(Map.grid[pg.inky.grid_x][pg.inky.grid_y].getTileMid().getX() ,
									Map.grid[pg.inky.grid_x][pg.inky.grid_y].getTileMid().getY());
						}
					}
				}
			}
			// Reset ghosts life
			if (SCATTER == false) INKY_DEAD = false;
			
			// ANIMATION CONTROLLER ---------------------------------------------------------
			// Update pinky animation based on lastDirection
			anime_direction = pg.inky.getLastDirection();
			if (anime_direction == 'r'){
				if (SCATTER){
					if(INKY_DEAD){
						inky_anime = new Animation(pg.NAKED_GHOST_SHEET, GHOST_SPEED);
					}else{
						inky_anime = new Animation(pg.SCATTER_GHOST_SHEET, GHOST_SPEED);
					}
				} else
				inky_anime = new Animation(pg.INKY_R_SHEET, GHOST_SPEED);
			}
			
			if (anime_direction == 'l'){
				if (SCATTER){
					if(INKY_DEAD){
						inky_anime = new Animation(pg.NAKED_GHOST_SHEET, GHOST_SPEED);
					}else{
						inky_anime = new Animation(pg.SCATTER_GHOST_SHEET, GHOST_SPEED);
					}
				} else
				inky_anime = new Animation(pg.INKY_L_SHEET, GHOST_SPEED);
			}
			
			if (anime_direction == 'u'){
				if (SCATTER){
					if(INKY_DEAD){
						inky_anime = new Animation(pg.NAKED_GHOST_SHEET, GHOST_SPEED);
					}else{
						inky_anime = new Animation(pg.SCATTER_GHOST_SHEET, GHOST_SPEED);
					}
				} else
				inky_anime = new Animation(pg.INKY_U_SHEET, GHOST_SPEED);
			}
			
			if (anime_direction == 'd'){
				if (SCATTER){
					if(INKY_DEAD){
						inky_anime = new Animation(pg.NAKED_GHOST_SHEET, GHOST_SPEED);
					}else{
						inky_anime = new Animation(pg.SCATTER_GHOST_SHEET, GHOST_SPEED);
					}
				} else
				inky_anime = new Animation(pg.INKY_D_SHEET, GHOST_SPEED);

			}
		
			//+==========+
			//| CLYDE    |==========================================================================
			//+==========+
		   /* Holds the distances from ghost to PacMan
			* 100000 is just a really big number that is out of bounds for gameboard.
			* represents infinity
			*/
			tmp_r = 100000;
			tmp_l = 100000;
			tmp_u = 100000;
			tmp_d = 100000;
			
			// Check if CLYDE is at a door before anything else
			if (SCATTER){
				if(CLYDE_DEAD){
					// When we get to the door of the ghost house, go inside and wait
					if (pg.clyde.grid_x == 13 && pg.clyde.grid_y == 14){
						if(DEBUG_SCATTER_CLYDE) System.out.println("@@@@@@@@@@@@@@@@Im at the door to the ghost house");
						pg.clyde.grid_x = 16;
						pg.clyde.grid_y =17;
						pg.clyde.setPosition(Map.grid[pg.clyde.grid_x][pg.clyde.grid_y].getTileMid().getX() -8 ,
								Map.grid[pg.clyde.grid_x][pg.clyde.grid_y].getTileMid().getY());
						gh_clyde_lock = true;
					}
				}
			}
		
			// We check if ghost is in an Intersection tile first.
			if(Map.grid[pg.clyde.grid_x][pg.clyde.grid_y].isIntersect()){
				// If we are at a intersection, Clyde first checks if he is 8 or less tiles away from PacMan
				boolean inRange = false;		// Turn true if packman is within 8 tiles from Clyde
				for (int i = 0; i < 8; i++){
				
					// Check tiles expanding out from Clydes location. If the tile is the same as PacMan
					// then trip the flag 'inRange'.
				
					// This is a cheesy way to not overflow the grid buffers. As we expand out from Clyde
					// we check the distance from the tile to pacman. If it is 0 then we know they are in the 
					// same square. if we go out of range of grid then artificially cap at end bounds.
				
					int tmp_grid;			// holds the temporary spot we are looking at
				
					// to right of Clyde
					tmp_grid = pg.clyde.grid_x + i;
					if(tmp_grid > 27) tmp_grid = 27;	// right side of map
					if(tmp_grid < 0) tmp_grid = 0;	// left side of map
			
					if( Map.grid[tmp_grid][pg.clyde.grid_y].isWall() == false &&
							 pg.clyde.getLastDirection() != 'l' &&
							 Map.grid[tmp_grid][pg.clyde.grid_y].getTileMid().distance(
								Map.grid[pg.pacman.grid_x][pg.pacman.grid_y].getTileMid()) == 0 ){
					
						if (DEBUG_CLYDE) System.out.println("Pacman is "+ i + " tiles to the right of Clyde");	
						inRange = true;
						if (DEBUG_CLYDE) System.out.println("Clyde inRange of Pacman: " + inRange);
					
					}
				
					// left of Clyde
					tmp_grid = pg.clyde.grid_x - i;
					if(tmp_grid > 27) tmp_grid = 27;	// right side of map
					if(tmp_grid < 0) tmp_grid = 0;	// left side of map
				
					if(Map.grid[tmp_grid][pg.clyde.grid_y].isWall() == false &&
							pg.clyde.getLastDirection() != 'r' &&
							Map.grid[tmp_grid][pg.clyde.grid_y].getTileMid().distance(
								Map.grid[pg.pacman.grid_x][pg.pacman.grid_y].getTileMid()) == 0){	
					
						if (DEBUG_CLYDE) System.out.println("Pacman is "+i+" tiles to the left of Clyde");
						inRange = true;
						if (DEBUG_CLYDE) System.out.println("Clyde inRange of Pacman: " + inRange);
					}
				
					// Below Clyde
					tmp_grid = pg.clyde.grid_y + i;
					if(tmp_grid > 31) tmp_grid = 31;	// bottom side of map
					if(tmp_grid < 0) tmp_grid = 0;	// top side of map
				
					if(Map.grid[pg.clyde.grid_x][tmp_grid].isWall() == false &&
							pg.clyde.getLastDirection() != 'u' &&
							Map.grid[pg.clyde.grid_x][tmp_grid].getTileMid().distance(
								Map.grid[pg.pacman.grid_x][pg.pacman.grid_y].getTileMid()) == 0){
					
						if (DEBUG_CLYDE) System.out.println("Pacman is "+i+" tiles below Clyde");
						inRange = true;
						if (DEBUG_CLYDE) System.out.println("Clyde inRange of Pacman: " + inRange);
					}
				
					// Above Clyde
					tmp_grid = pg.clyde.grid_y - i;
					if(tmp_grid > 31) tmp_grid = 31;	// bottom side of map
					if(tmp_grid < 0) tmp_grid = 0;	// top side of map
				
					if(Map.grid[pg.clyde.grid_x][tmp_grid].isWall() == false &&
							pg.clyde.getLastDirection() != 'd' &&
							Map.grid[pg.clyde.grid_x][tmp_grid].getTileMid().distance(
								Map.grid[pg.pacman.grid_x][pg.pacman.grid_y].getTileMid()) == 0){
					
						if (DEBUG_CLYDE) System.out.println("Pacman is "+i+" tiles above Clyde");
						inRange = true;
						if (DEBUG_CLYDE) System.out.println("Clyde inRange of Pacman: " + inRange);
					}
				}
			
				// IF we are 'inRange' of PacMan, Clyde Runs for bottom left of map
				// ELSE Clyde behaves like Blinky
				if(inRange){
				
					if(Map.grid[pg.clyde.grid_x + 1][pg.clyde.grid_y].isWall() == false && pg.clyde.getLastDirection() != 'l'){
						// get the distance between ghost and bottom left of Map
						if (CLYDE_DEAD){
							// Head Back to the ghost house
							if(DEBUG_SCATTER_CLYDE) System.out.println("CLYDES Dead... Going back to house");
							tmp_r = Map.grid[pg.clyde.grid_x + 1][pg.clyde.grid_y].getTileMid().distance(
									Map.grid[13][15].getTileMid());
						}else{
							tmp_r = Map.grid[pg.clyde.grid_x + 1][pg.clyde.grid_y].getTileMid().distance(
									Map.grid[1][34].getTileMid());	
						}
					}
					if(Map.grid[pg.clyde.grid_x - 1][pg.clyde.grid_y].isWall() == false && pg.clyde.getLastDirection() != 'r'){	
						// get the distance between ghost and bottom left of Map
						if (CLYDE_DEAD){
							// Head Back to the ghost house
							if(DEBUG_SCATTER_CLYDE) System.out.println("CLYDES Dead... Going back to house");
							tmp_l = Map.grid[pg.clyde.grid_x - 1][pg.clyde.grid_y].getTileMid().distance(
									Map.grid[13][15].getTileMid());
						}else{
							tmp_l = Map.grid[pg.clyde.grid_x - 1][pg.clyde.grid_y].getTileMid().distance(
									Map.grid[1][34].getTileMid());
						}
					}
					if(Map.grid[pg.clyde.grid_x][pg.clyde.grid_y + 1].isWall() == false && pg.clyde.getLastDirection() != 'u'){
						// get the distance between ghost and bottom left of Map
						if (CLYDE_DEAD){
							// Head Back to the ghost house
							if(DEBUG_SCATTER_CLYDE) System.out.println("CLYDES Dead... Going back to house");
							tmp_d = Map.grid[pg.clyde.grid_x][pg.clyde.grid_y + 1].getTileMid().distance(
									Map.grid[13][15].getTileMid());
						}else{
							tmp_d = Map.grid[pg.clyde.grid_x][pg.clyde.grid_y + 1].getTileMid().distance(
									Map.grid[1][34].getTileMid());
						}
					}
					if(Map.grid[pg.clyde.grid_x][pg.clyde.grid_y - 1].isWall() == false && pg.clyde.getLastDirection() != 'd'){
						// get the distance between ghost and bottom left of Map
						if (CLYDE_DEAD){
							// Head Back to the ghost house
							if(DEBUG_SCATTER_CLYDE) System.out.println("CLYDES Dead... Going back to house");
							tmp_u = Map.grid[pg.clyde.grid_x][pg.clyde.grid_y - 1].getTileMid().distance(
									Map.grid[13][15].getTileMid());
						}else{
							tmp_u = Map.grid[pg.clyde.grid_x][pg.clyde.grid_y - 1].getTileMid().distance(
									Map.grid[1][34].getTileMid());
						}
					}
				
					// set ghosts heading; heading is shortest distance form ghost to target
					heading = Math.min(Math.min(tmp_l, tmp_r), Math.min(tmp_u, tmp_d));
					if(DEBUG_CLYDE) System.out.println("Clyds' heading : "+ heading);
				
					// Now depending what our heading is, we set the ghosts direction.
					if(DEBUG_CLYDE) System.out.println("Clydes direction: "+ pg.clyde.getLastDirection());
				
					if(heading == tmp_r){
						pg.clyde.setDirection('r');
					}
					else
					if(heading == tmp_l){
						pg.clyde.setDirection('l');
					}
					else
					if(heading == tmp_u){
						pg.clyde.setDirection('u');
					}
					else
					if(heading == tmp_d){
						pg.clyde.setDirection('d');				
					}
				
					// Update ghosts animation based on updated grid
					pg.clyde.updateGrid();
					pg.clyde.setPosition(Map.grid[pg.clyde.grid_x][pg.clyde.grid_y].getTileMid().getX() ,
							Map.grid[pg.clyde.grid_x][pg.clyde.grid_y].getTileMid().getY());
					if(SHOW_GHOST_TARGETS) System.out.println("CLYDES TARGET X COORD: "+ pg.clyde.grid_x);
					if(SHOW_GHOST_TARGETS) System.out.println("CLYDES TARGET Y COORD: "+ pg.clyde.grid_y);
					if(SHOW_GHOST_TARGETS) System.out.println();
				
				
				}else {
				
					// It is a intersection tile, calculate a new direction
					// check each tile adjacent to ghost if it is not a wall
					if(Map.grid[pg.clyde.grid_x + 1][pg.clyde.grid_y].isWall() == false){
						if(SCATTER){
							if(DEBUG_SCATTER_CLYDE) System.out.println("in SCATTER State");
							if (CLYDE_DEAD){
								// Head Back to the ghost house
								if(DEBUG_SCATTER_CLYDE) System.out.println("Clydes Dead... Going back to house");
								tmp_r = Map.grid[pg.clyde.grid_x + 1][pg.clyde.grid_y].getTileMid().distance(
										Map.grid[13][15].getTileMid());
							}else{
								tmp_r = Map.grid[pg.clyde.grid_x + 1][pg.clyde.grid_y].getTileMid().distance(
										Map.grid[1][34].getTileMid());	
							}
						} else {
							// get the distance between ghost and PacMan
							tmp_r = Map.grid[pg.clyde.grid_x + 1][pg.clyde.grid_y].getTileMid().distance(
										Map.grid[pg.pacman.inkyTarget_x][pg.pacman.inkyTarget_y].getTileMid());
						}
					}
					if(Map.grid[pg.clyde.grid_x - 1][pg.clyde.grid_y].isWall() == false){
						if(SCATTER){
							if(DEBUG_SCATTER_CLYDE) System.out.println("in SCATTER State");
							if (CLYDE_DEAD){
								// Head Back to the ghost house
								if(DEBUG_SCATTER_CLYDE) System.out.println("Clydes Dead... Going back to house");
								tmp_l = Map.grid[pg.clyde.grid_x - 1][pg.clyde.grid_y].getTileMid().distance(
										Map.grid[13][15].getTileMid());
							}else{
								tmp_l = Map.grid[pg.clyde.grid_x - 1][pg.clyde.grid_y].getTileMid().distance(
										Map.grid[1][34].getTileMid());	
							}
						} else {
							// get the distance between ghost and PacMan
							tmp_l = Map.grid[pg.clyde.grid_x - 1][pg.clyde.grid_y].getTileMid().distance(
										Map.grid[pg.pacman.inkyTarget_x][pg.pacman.inkyTarget_y].getTileMid());
						}
					}
					if(Map.grid[pg.clyde.grid_x][pg.clyde.grid_y + 1].isWall() == false){
						if(SCATTER){
							if(DEBUG_SCATTER_CLYDE) System.out.println("in SCATTER State");
							if (CLYDE_DEAD){
								// Head Back to the ghost house
								if(DEBUG_SCATTER_CLYDE) System.out.println("Clydes Dead... Going back to house");
								tmp_d = Map.grid[pg.clyde.grid_x][pg.clyde.grid_y + 1].getTileMid().distance(
										Map.grid[13][15].getTileMid());
							}else{
								tmp_d = Map.grid[pg.clyde.grid_x][pg.clyde.grid_y + 1].getTileMid().distance(
										Map.grid[1][34].getTileMid());	
							}
						} else {
							// get the distance between ghost and PacMan
							tmp_d = Map.grid[pg.clyde.grid_x][pg.clyde.grid_y + 1].getTileMid().distance(
										Map.grid[pg.pacman.inkyTarget_x][pg.pacman.inkyTarget_y].getTileMid());
						}
					}
					if(Map.grid[pg.clyde.grid_x][pg.clyde.grid_y - 1].isWall() == false){
						if(SCATTER){
							if(DEBUG_SCATTER_CLYDE) System.out.println("in SCATTER State");
							if (CLYDE_DEAD){
								// Head Back to the ghost house
								if(DEBUG_SCATTER_CLYDE) System.out.println("Clydes Dead... Going back to house");
								tmp_u = Map.grid[pg.clyde.grid_x][pg.clyde.grid_y - 1].getTileMid().distance(
										Map.grid[13][15].getTileMid());
							}else{
								tmp_u = Map.grid[pg.clyde.grid_x][pg.clyde.grid_y - 1].getTileMid().distance(
										Map.grid[1][34].getTileMid());	
							}
						} else {
							// get the distance between ghost and PacMan
							tmp_u = Map.grid[pg.clyde.grid_x][pg.clyde.grid_y - 1].getTileMid().distance(
										Map.grid[pg.pacman.inkyTarget_x][pg.pacman.inkyTarget_y].getTileMid());
						}
					}
				
					// set ghosts heading; heading is shortest distance form ghost to target
					heading = Math.min(Math.min(tmp_l, tmp_r), Math.min(tmp_u, tmp_d));
					if(DEBUG_CLYDE) System.out.println("Clyds' heading : "+ heading);
				
					// Now depending what our heading is, we set the ghosts direction.
					if(DEBUG_CLYDE) System.out.println("Clydes direction: "+ pg.clyde.getLastDirection());
				
					if(heading == tmp_r){
						pg.clyde.setDirection('r');
					}
					else
					if(heading == tmp_l){
						pg.clyde.setDirection('l');
					}
					else
					if(heading == tmp_u){
						pg.clyde.setDirection('u');
					}
					else
					if(heading == tmp_d){
						pg.clyde.setDirection('d');				
					}
				
					// Update ghosts animation based on updated grid
					pg.clyde.updateGrid();
					pg.clyde.setPosition(Map.grid[pg.clyde.grid_x][pg.clyde.grid_y].getTileMid().getX() ,
							Map.grid[pg.clyde.grid_x][pg.clyde.grid_y].getTileMid().getY());
					if(SHOW_GHOST_TARGETS) System.out.println("CLYDES TARGET X COORD: "+ pg.clyde.grid_x);
					if(SHOW_GHOST_TARGETS) System.out.println("CLYDES TARGET Y COORD: "+ pg.clyde.grid_y);
					if(SHOW_GHOST_TARGETS) System.out.println();
				
				
				}
		
			} else {
				// Not at a Intersection
				if (gh_clyde_lock){
					if (SCATTER){
						if (pacdots_remaining == 163){
							pg.clyde.grid_x = 15;
							pg.clyde.grid_y = 14;
							pg.clyde.setPosition(Map.grid[pg.clyde.grid_x][pg.clyde.grid_y].getTileMid().getX() ,
									Map.grid[pg.clyde.grid_x][pg.clyde.grid_y].getTileMid().getY());
						}
					}else{
						gh_clyde_lock = false;
						// Set PINKY to intersection tile outside of door
						pg.clyde.grid_x = 15;
						pg.clyde.grid_y =14;
						pg.clyde.setPosition(Map.grid[pg.clyde.grid_x][pg.clyde.grid_y].getTileMid().getX() ,
								Map.grid[pg.clyde.grid_x][pg.clyde.grid_y].getTileMid().getY());
						CLYDE_DEAD = false;
					}
				}else {
					if (pacdots_remaining == 163){
						pg.clyde.grid_x = 15;
						pg.clyde.grid_y = 14;
						pg.clyde.setPosition(Map.grid[pg.clyde.grid_x][pg.clyde.grid_y].getTileMid().getX() ,
								Map.grid[pg.clyde.grid_x][pg.clyde.grid_y].getTileMid().getY());
					}else
					
					if (pacdots_remaining > 163){
						pg.clyde.setPosition(Map.grid[pg.clyde.grid_x][pg.clyde.grid_y].getTileMid().getX() + 8 ,
								Map.grid[pg.clyde.grid_x][pg.clyde.grid_y].getTileMid().getY());
					}
					else {
						// still in ghost house, move clyde out
						if (pg.clyde.grid_x == 15 && pg.clyde.grid_y == 17){
							pg.clyde.grid_x = 15;
							pg.clyde.grid_y = 14;
							pg.clyde.setPosition(Map.grid[pg.clyde.grid_x][pg.clyde.grid_y].getTileMid().getX() ,
									Map.grid[pg.clyde.grid_x][pg.clyde.grid_y].getTileMid().getY());
						}else{
							pg.clyde.updateGrid();
							pg.clyde.setPosition(Map.grid[pg.clyde.grid_x][pg.clyde.grid_y].getTileMid().getX() ,
									Map.grid[pg.clyde.grid_x][pg.clyde.grid_y].getTileMid().getY());
						}
					}
				}
			}
			// Reset ghosts life
			if (SCATTER == false) CLYDE_DEAD = false;
			
			// ANIMATION CONTROLLER ---------------------------------------------------------
			// Update blinky animation based on lastDirection
			anime_direction = pg.clyde.getLastDirection();
			if (anime_direction == 'r'){
				if (SCATTER){
					if(CLYDE_DEAD){
						clyde_anime = new Animation(pg.NAKED_GHOST_SHEET, GHOST_SPEED);
					}else{
						clyde_anime = new Animation(pg.SCATTER_GHOST_SHEET, GHOST_SPEED);
					}
				} else
				clyde_anime = new Animation(pg.CLYDE_R_SHEET, GHOST_SPEED);

			}
		
			if (anime_direction == 'l'){
				if (SCATTER){
					if(CLYDE_DEAD){
						clyde_anime = new Animation(pg.NAKED_GHOST_SHEET, GHOST_SPEED);
					}else{
						clyde_anime = new Animation(pg.SCATTER_GHOST_SHEET, GHOST_SPEED);
					}
				} else
				clyde_anime = new Animation(pg.CLYDE_L_SHEET, GHOST_SPEED);
			}
		
			if (anime_direction == 'u'){
				if (SCATTER){
					if(CLYDE_DEAD){
						clyde_anime = new Animation(pg.NAKED_GHOST_SHEET, GHOST_SPEED);
					}else{
						clyde_anime = new Animation(pg.SCATTER_GHOST_SHEET, GHOST_SPEED);
					}
				} else
				clyde_anime = new Animation(pg.CLYDE_U_SHEET, GHOST_SPEED);
			}
		
			if (anime_direction == 'd'){
				if (SCATTER){
					if(CLYDE_DEAD){
						clyde_anime = new Animation(pg.NAKED_GHOST_SHEET, GHOST_SPEED);
					}else{
						clyde_anime = new Animation(pg.SCATTER_GHOST_SHEET, GHOST_SPEED);
					}
				} else
				clyde_anime = new Animation(pg.CLYDE_D_SHEET, GHOST_SPEED);
			}
		
			//End of ghost logic ===================================================
			ghostsTurn = false;
			if (DEBUG) System.out.println("canMove after lock: "+ canMove);
			countdown_ghosts = GHOST_LOCKOUT_TIME;
			
		} else {
			if (DEBUG) System.out.println("countdown: "+ countdown);
			if (countdown_ghosts > 0)
				countdown_ghosts -= delta;
			if (countdown_ghosts <= 0) {
				ghostsTurn = true;
				if (DEBUG) System.out.println("canMove unlock: "+ canMove);
			}
		}
		
		
		if (DEBUG) System.out.println("X: " + Map.grid[30][27].LeftCorner.getX());
		if (DEBUG) System.out.println("y: " + Map.grid[30][27].LeftCorner.getY());
		if (DEBUG) System.out.println("center X: " + Map.grid[6][5].getTileMid().getX());
		if (DEBUG) System.out.println("center Y: " + Map.grid[6 ][5].getTileMid().getY());
	}
	

	@Override
	public int getID() {
		return pacmanGame.LVL2STATE;
	}
	
}

