package nl.tudelft.jpacman.group25.game;

import java.util.List;

import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.Player;

import com.google.common.collect.ImmutableList;

/**
 * This class contains a basic implementation of a multi-level game.
 * You can win a game, and then start a new one, up to three times.
 * @author gerlof
 */
public class MultiLevelGame extends Game {
	private Level[] levels;
	private Player player;
	private int count = 0;
	
	/**
	 * Constructor for the multi-level game.
	 * @param p	the player
	 * @param l	the level
	 */
	public MultiLevelGame(Player p, Level[] l) {
		assert p != null;
		assert l != null;

		this.player = p;
		this.levels = l;
		levels[count].registerPlayer(p);
	}
	
	@Override
	public Level getLevel() {
		return this.levels[count];
	}

	@Override
	public List<Player> getPlayers() {
		return ImmutableList.of(player);
	}
	
	@Override
	public void levelWon() {
		super.levelWon();
		if (count < 2) {
			count++;
			levels[count].registerPlayer(player);
		}
	}
}
