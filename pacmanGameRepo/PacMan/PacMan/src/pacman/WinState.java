package pacman;


import jig.ResourceManager;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

class WinState extends BasicGameState {
	private boolean sound_playing;
	private int sound_counter;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		
	}

	public void setUserScore(int bounces) {
		
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {

		pacmanGame pg = (pacmanGame)game;
		
		
		
		g.drawImage(ResourceManager.getImage(pacmanGame.WIN_BANNER_RSC), 24,150);
		g.drawString("HIGH SCORE: " + pg.score, 150, 250);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {
		
		if(sound_playing){
			if (sound_counter <= 0) sound_playing = false;
			sound_counter -= delta;
		}else{
			sound_counter = 5600;   //<---------- Change how long to lock out sound effect
			sound_playing = true;
			ResourceManager.getSound(pacmanGame.WIN_SOUND_RSC).play();
		}

	}

	@Override
	public int getID() {
		return pacmanGame.WINSTATE;
	}
	
}