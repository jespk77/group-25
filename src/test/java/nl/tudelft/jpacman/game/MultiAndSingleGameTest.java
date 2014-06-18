package nl.tudelft.jpacman.game;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.group25.MultiLevelLauncher;
import nl.tudelft.jpacman.group25.SimpleMapWithGhost;
import nl.tudelft.jpacman.level.Player;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.mockito.Mock;

/**
 * This class tests all state switches of the MultiGame and the SingleGame class using theories.
 * Note that these are the same tests as for the Game class.
 */
@RunWith(Theories.class)
public class MultiAndSingleGameTest {
	@Mock private Player player;

	/**
	 * This method sets up the data points for use in this test suite.
	 * @return the games that will be tested
	 */	
	@DataPoints
	public static Game[] games() {
		return new Game[] {
				new SimpleMapWithGhost().makeGame(),
				new MultiLevelLauncher().makeGame()
		};
	}

	/**
	 * Test the initial state.
	 * @param game the game to be tested
	 */
	@Theory
	public void initial(Game game) {
		assertFalse(game.isInProgress());

	}

	/**
	 * Test what happens if you stop a game that is ready to start.
	 * @param game the game to be tested
	 */
	@Theory
	public void stop(Game game) {
		game.stop();
		assertFalse(game.isInProgress());
	}

	/**
	 * Test what happens if you stop a game that has been started.
	 * @param game the game to be tested
	 */	
	@Theory
	public void startStop(Game game) {
		game.start();
		game.stop();
		assertFalse(game.isInProgress());
	}

	/**
	 * Test what happens if you start a game that has been started.
	 * @param game the game to be tested
	 */
	@Theory
	public void startStart(Game game) {
		game.start();
		game.start();
		assertTrue(game.isInProgress());
	}

	/**
	 * Test what happens if you start a game, then win it, and then stop it.
	 * @param game the game to be tested
	 */
	@Theory
	public void startWinStop(Game game) {
		player = game.getPlayers().get(0);
		game.start();
		game.move(player, Direction.EAST);
		assertTrue(player.isAlive());
		game.stop();
		assertFalse(game.isInProgress());
	}

	/**
	 * Test what happens if you start a game and then lose it.
	 * @param game the game to be tested
	 */
	@Theory
	public void startLose(Game game) {
		player = game.getPlayers().get(0);
		game.start();
		game.move(player, Direction.WEST);
		assertFalse(game.isInProgress());
		assertFalse(player.isAlive());
	}

	/**
	 * Test what happens if you start a game that hasn't been started.
	 * @param game the game to be tested
	 */
	@Theory
	public void readyStart(Game game) {
		game.start();
		assertTrue(game.isInProgress());
	}

	/**
	 * Test what happens if you stop a game that hasn't been started.
	 * @param game the game to be tested
	 */
	@Theory
	public void readyStop(Game game) {
		game.stop();
		assertFalse(game.isInProgress());
	}

	/**
	 * Test what happens if you start a game that has been started.
	 * @param game the game to be tested
	 */
	@Theory
	public void playingStart(Game game) {
		game.start();
		game.start();
		assertTrue(game.isInProgress());
	}

	/**
	 * Test what happens if you stop a game that has been started.
	 * @param game the game to be tested
	 */
	@Theory
	public void playingStop(Game game) {
		game.start();
		game.stop();
		assertFalse(game.isInProgress());
	}

	/**
	 * Test what happens if you win a game.
	 * @param game the game to be tested
	 */
	@Theory
	public void playingWin(Game game) {
		player = game.getPlayers().get(0);
		game.start();
		game.move(player, Direction.EAST);
		assertFalse(game.isInProgress());
		assertTrue(player.isAlive());
	}

	/**
	 * Test what happens if you lose a game.
	 * @param game the game to be tested
	 */
	@Theory
	public void playingLose(Game game) {
		player = game.getPlayers().get(0);
		game.start();
		game.move(player, Direction.WEST);
		assertFalse(game.isInProgress());
		assertFalse(player.isAlive());
	}

	/**
	 * Test what happens if you start a game that you have lost.
	 * @param game the game to be tested
	 */
	@Theory
	public void endedStart(Game game) {
		player = game.getPlayers().get(0);
		game.start();
		game.move(player, Direction.WEST);
		game.start();
		assertFalse(game.isInProgress());
	}

	/**
	 * Test what happens if you stop a game that you have lost.
	 * @param game the game to be tested
	 */
	@Theory
	public void endedStop(Game game) {
		player = game.getPlayers().get(0);
		game.start();
		game.move(player, Direction.WEST);
		game.stop();
		assertFalse(game.isInProgress());
	}
}
