package pacman;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import jig.ResourceManager;

class StartUpState extends BasicGameState {
	private boolean sound_playing;
	private int sound_counter;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		container.setSoundOn(true);
		
	}
	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		pacmanGame pg = (pacmanGame)game;
		
		g.drawImage(ResourceManager.getImage(pacmanGame.TITLE_SPLASH_RSC),30, 90);
		g.drawImage(ResourceManager.getImage(pacmanGame.LOGO_RSC),85, 145);
		g.drawImage(ResourceManager.getImage(pacmanGame.ENTER_SPLASH_RSC),25, 400);
		g.drawImage(ResourceManager.getImage(pacmanGame.NAME_RSC),75, 500);

	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {

		Input input = container.getInput();
		pacmanGame pg = (pacmanGame)game;
		
		if(sound_playing){
			if (sound_counter <= 0) sound_playing = false;
			sound_counter -= delta;
		}else{
			sound_counter = 5600;   //<---------- Change how long to lock out sound effect
			sound_playing = true;
			ResourceManager.getSound(pacmanGame.PACINTRO_SOUND_RSC).play();
		}
		
		if (input.isKeyDown(Input.KEY_ENTER))
			pg.enterState(pacmanGame.PLAYINGSTATE);	
	}

	@Override
	public int getID() {
		return pacmanGame.STARTUPSTATE;
	}
	
}
