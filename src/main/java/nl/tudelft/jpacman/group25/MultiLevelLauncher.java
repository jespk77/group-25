package nl.tudelft.jpacman.group25;

import java.util.ArrayList;

import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.group25.game.MultiLevelGame;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.MapParser;
import nl.tudelft.jpacman.level.PlayerFactory;

public class MultiLevelLauncher extends Launcher {
	private MultiLevelGame multiGame;
	private PlayerFactory playerFactory;
	
	public static void main(String[] argv) {
		(new MultiLevelLauncher()).launch();
	}
	
	@Override
	public Game makeGame() {
		Level level[] = new Level[10];
		for (int i = 0; i < 3; i++) {
			level[i] = makeLevel();
		}
		
		playerFactory = super.getPlayerFactory();
		return multiGame = new MultiLevelGame(playerFactory.createPacMan(), level);
	}
	
	@Override
	public MultiLevelGame getGame() {
		return multiGame;
	}
	
	/**
	 * Creates a new level. By default this method will use the map parser to
	 * parse the default board stored in the <code>board.txt</code> resource.
	 * 
	 * @return A new level.
	 */
	@Override
	public Level makeLevel() {
		MapParser parser = getMapParser();
		ArrayList<String> grid = new ArrayList<String>();
		grid.add("#####");
		grid.add("#GP.#");
		grid.add("#####");
		return parser.parseMap(grid);
	}
}
