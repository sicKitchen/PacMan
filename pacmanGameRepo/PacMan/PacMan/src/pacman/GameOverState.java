package pacman;

import jig.ResourceManager;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

class GameOverState extends BasicGameState {
	
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
		
		g.drawImage(ResourceManager.getImage(pacmanGame.GAMEOVER_BANNER_RSC), 46,150);
		g.drawString("HIGH SCORE: " + pg.score, 150, 250);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {	
	}

	@Override
	public int getID() {
		return pacmanGame.GAMEOVERSTATE;
	}
	
}