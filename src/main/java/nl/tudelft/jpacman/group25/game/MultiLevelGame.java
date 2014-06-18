package nl.tudelft.jpacman.group25.game;

import java.io.IOException;
import java.util.List;

import com.google.common.collect.ImmutableList;

import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.PacmanConfigurationException;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.MapParser;
import nl.tudelft.jpacman.level.Player;

public class MultiLevelGame extends Game {
	private Level[] levels;
	private Player player;
	private int count = 0;
	
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
