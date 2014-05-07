package nl.tudelft.jpacman;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.level.Player;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/** 
 * 
 * @author joris en gerlof
 * This class tests the user story described by 4
 *
 */
public class Scenario4Test {
	private static final long DEFAULT_INTERVAL = 100L;

	private Launcher launcher;
	private Game game;
	private Player player;
	
	/**
	 * Sets up the game and initializes some variables we use quite often.
	 */
	@Before
	public void setUpPacman() {
		launcher = new Launcher();
		launcher.launch();

		game = launcher.getGame();
		player = game.getPlayers().get(0);
	}
	
	/**
	 * Tears down the game so it can gracefully exit.
	 */
	@After
	public void tearDown() {
		launcher.dispose();
	}
	
	/**
	 * Scenario S4.1: Suspend the game.
	 * Given the game has started;
	 * When  the player clicks the "Stop" button;
	 * Then  all moves from ghosts and the player are suspended.
	 * @throws InterruptedException	Although not recommended, we use sleep here
	 */
	@Test
	public void suspend() throws InterruptedException {
		game.start();
		//Set the initial direction of the ghost
		player.setDirection(Direction.WEST);
		Thread.sleep(DEFAULT_INTERVAL);

		// stop the game
		game.stop();
		assertEquals(false, game.isInProgress());

		// change the direction of the player
		player.setDirection(Direction.EAST);
		assertEquals(Direction.EAST, player.getDirection());

		// move the player to another square
		// check if he is still at the same square
		Square myLocation = player.getSquare();
		game.move(player, Direction.EAST);
		assertEquals(myLocation, player.getSquare());
	}

	/**
	 * Scenario S4.2: Restart the game.
	 * Given the game is suspended;
	 * When  the player hits the "Start" button;
	 * Then  the game is resumed.
	 * @throws InterruptedException	Although not recommended, we use sleep here
	 */
	@Test
	public void restart() throws InterruptedException {
		game.start();
		Thread.sleep(DEFAULT_INTERVAL);

		// stop the game
		game.stop();
		Thread.sleep(DEFAULT_INTERVAL);
		//Check if the game is still running
		assertFalse(game.isInProgress());

		// and start the game again
		game.start();
		Thread.sleep(DEFAULT_INTERVAL);
		assertTrue(game.isInProgress());
	}
}
