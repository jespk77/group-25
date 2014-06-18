package nl.tudelft.jpacman.game;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.group25.MultiLevelLauncher;
import nl.tudelft.jpacman.level.Player;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class MultiGameTest {
	private Game simpleMultiGame;
	@Mock private Player player;
	
	@Before
	public void setUp() {
		simpleMultiGame = new MultiLevelLauncher().makeGame();
	}

	@Test
	public void startWinStart() {
		simpleMultiGame.start();
		simpleMultiGame.move(player, Direction.EAST);
		assertFalse(simpleMultiGame.isInProgress());
		simpleMultiGame.start();
		assertTrue(simpleMultiGame.isInProgress());
	}

}
