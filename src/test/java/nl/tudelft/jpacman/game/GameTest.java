package nl.tudelft.jpacman.game;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.group25.SimpleMapWithGhost;
import nl.tudelft.jpacman.level.Player;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * This class tests all state switches of the Game class
 */
public class GameTest {
	private Game simpleGame;
	@Mock private Player player;

	/**
	 * This method sets up the Game that we will be testing
	 */
	@Before
	public void setUp() {
		simpleGame = new SimpleMapWithGhost().makeGame();
	}

	/**
	 * Test the initial state
	 */
	@Test
	public void initial() {
		assertFalse(simpleGame.isInProgress());

	}

	/**
	 * Test what happens if you stop a game that is ready to start
	 */
	@Test
	public void stop() {
		simpleGame.stop();
		assertFalse(simpleGame.isInProgress());
	}

	/**
	 * Test what happens if you stop a game that has been started
	 */
	@Test
	public void startStop() {
		simpleGame.start();
		simpleGame.stop();
		assertFalse(simpleGame.isInProgress());
	}

	/**
	 * Test what happens if you start a game that has been started
	 */
	@Test
	public void startStart() {
		simpleGame.start();
		simpleGame.start();
		assertTrue(simpleGame.isInProgress());
	}

	/**
	 * Test what happens if you start a game, then win it, and then stop it
	 */
	@Test
	public void startWinStop() {
		player = simpleGame.getPlayers().get(0);
		simpleGame.start();
		simpleGame.move(player, Direction.EAST);
		assertTrue(player.isAlive());
		simpleGame.stop();
		assertFalse(simpleGame.isInProgress());
	}

	/**
	 * Test what happens if you start a game, then win it, and then start it
	 */
	@Test
	public void startWinStart() {
		player = simpleGame.getPlayers().get(0);
		simpleGame.start();
		simpleGame.move(player, Direction.EAST);
		assertTrue(player.isAlive());
		simpleGame.start();
		assertFalse(simpleGame.isInProgress());
	}

	/**
	 * Test what happens if you start a game and then lose it
	 */
	@Test
	public void startLose() {
		player = simpleGame.getPlayers().get(0);
		simpleGame.start();
		simpleGame.move(player, Direction.WEST);
		assertFalse(simpleGame.isInProgress());
		assertFalse(player.isAlive());
	}

	/**
	 * Test what happens if you start a game that hasn't been started
	 */
	@Test
	public void readyStart() {
		simpleGame.start();
		assertTrue(simpleGame.isInProgress());
	}

	/**
	 * Test what happens if you stop a game that hasn't been started
	 */
	@Test
	public void readyStop() {
		simpleGame.stop();
		assertFalse(simpleGame.isInProgress());
	}

	/**
	 * Test what happens if you start a game that has been started
	 */
	@Test
	public void playingStart() {
		simpleGame.start();
		simpleGame.start();
		assertTrue(simpleGame.isInProgress());
	}

	/**
	 * Test what happens if you stop a game that has been started
	 */
	@Test
	public void playingStop() {
		simpleGame.start();
		simpleGame.stop();
		assertFalse(simpleGame.isInProgress());
	}

	/**
	 * Test what happens if you win a game
	 */
	@Test
	public void playingWin() {
		player = simpleGame.getPlayers().get(0);
		simpleGame.start();
		simpleGame.move(player, Direction.EAST);
		assertFalse(simpleGame.isInProgress());
		assertTrue(player.isAlive());
	}

	/**
	 * Test what happens if you lose a game
	 */
	@Test
	public void playingLose() {
		player = simpleGame.getPlayers().get(0);
		simpleGame.start();
		simpleGame.move(player, Direction.WEST);
		assertFalse(simpleGame.isInProgress());
		assertFalse(player.isAlive());
	}

	/**
	 * Test what happens if you start a game that you have lost
	 */
	@Test
	public void endedStart() {
		player = simpleGame.getPlayers().get(0);
		simpleGame.start();
		simpleGame.move(player, Direction.WEST);
		simpleGame.start();
		assertFalse(simpleGame.isInProgress());
	}

	/**
	 * Test what happens if you stop a game that you have lost
	 */
	@Test
	public void endedStop() {
		player = simpleGame.getPlayers().get(0);
		simpleGame.start();
		simpleGame.move(player, Direction.WEST);
		simpleGame.stop();
		assertFalse(simpleGame.isInProgress());
	}
}
