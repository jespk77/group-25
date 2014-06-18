package nl.tudelft.jpacman.game;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.group25.MultiLevelLauncher;
import nl.tudelft.jpacman.level.Player;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * This class tests all extra state switches of the MultiGame class
 */
public class MultiGameTest {
	private Game simpleMultiGame;
	@Mock private Player player;

	/**
	 * This method sets up the MultiLevel Game that we will be testing
	 */
	@Before
	public void setUp() {
		simpleMultiGame = new MultiLevelLauncher().makeGame();
	}

	/**
	 * Test that we can start a game, win it, and start a new game
	 */
	@Test
	public void startWinStart() {
		player = simpleMultiGame.getPlayers().get(0);
		simpleMultiGame.start();
		simpleMultiGame.move(player, Direction.EAST);
		assertFalse(simpleMultiGame.isInProgress());
		simpleMultiGame.start();
		assertTrue(simpleMultiGame.isInProgress());
	}

}
