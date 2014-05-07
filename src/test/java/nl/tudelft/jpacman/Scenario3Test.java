package nl.tudelft.jpacman;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.group25.SimpleGhostMap;
import nl.tudelft.jpacman.group25.npc.ghost.CustomGhostFactory;
import nl.tudelft.jpacman.level.Pellet;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.npc.ghost.Ghost;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/** The test class for user story 3. */
public class Scenario3Test {
	private static final long DEFAULT_INTERVAL = 200L;

	private Launcher launcher;
	private Game game;
	private Player player;
	
	/**
	 * Sets up the game using a simple map and a custom ghost factory.
	 * Also initializes some variables we use quite often.
	 */
	@Before
	public void setUpSimpleGhostPacman() {
		launcher = new SimpleGhostMap();
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
	 * Scenario S3.1: A ghost moves.
	 * Given the game has started,
	 *  and  a ghost is next to an empty cell;
	 * When  a tick event occurs;
	 * Then  the ghost can move to that cell.
	 * @throws InterruptedException - throws exception of the build factories go wrong
	 */
	@Test
	public void ghostMoves() throws InterruptedException {
		//Build our custom ghosts
		CustomGhostFactory gf = ((SimpleGhostMap) launcher).getCustomGhostFactory();

		//Pop one ghost to test with
		Ghost blinky = gf.popBlinky();

		//Launch the created game
		game.start();

		//Get the current square the ghost is standing
		Square temp = blinky.getSquare();
		//Get the square the bot can to move to next
		Square nextSquare = temp.getSquareAt(blinky.nextMove());
		//Confirm that it is a square the gost can go to
		assertTrue(nextSquare.isAccessibleTo(blinky));
		//Whait for the bot to perform it's actions
		Thread.sleep(DEFAULT_INTERVAL);
		//Get the new square the bot is standing on next
		Square temp2 = blinky.getSquare();
		//Check if the square the bot could move to is indeed the square it is now
		assertEquals(nextSquare, temp2);

		//If so, the bot has indeed moved to a square next to him which he could stand on
	}

	/**
	 * Scenario S3.2: The ghost moves over food.
	 * Given the game has started,
	 *  and  a ghost is next to a cell containing food;
	 * When  a tick event occurs;
	 * Then  the ghost can move to the food cell,
	 *  and  the food on that cell is not visible anymore.
	 * @throws InterruptedException - Throws an error if the build factory goes wrong
	 */
	@Test
	public void ghostMovesOverFood() throws InterruptedException {
		CustomGhostFactory gf = ((SimpleGhostMap) launcher).getCustomGhostFactory();
		Ghost blinky = gf.popBlinky();
		game.start();
		Square sq = blinky.getSquare();
		Square sqNext = sq.getSquareAt(blinky.getDirection());

		// We sleep until the Ghost is going to a square with food next
		while (!Util.contains(sqNext, Pellet.class)) {
			Thread.sleep(DEFAULT_INTERVAL);
			sq = blinky.getSquare();
			sqNext = sq.getSquareAt(blinky.getDirection());
		}
		List<Unit> occupants;
		Unit lastOccupant;

		// We're at the food, now verify that the visible occupant (the last one) is a Pellet
		occupants = sqNext.getOccupants();
		lastOccupant = occupants.get(occupants.size() - 1);
		assertTrue(lastOccupant instanceof Pellet);

		// Now sleep until we moved and check that the last occupant is not a Pellet anymore
		Thread.sleep(DEFAULT_INTERVAL);
		assertEquals(sqNext, blinky.getSquare());

		// Verify that the visible occupant is not a Pellet anymore
		occupants = sqNext.getOccupants();
		lastOccupant = occupants.get(occupants.size() - 1);
		assertFalse(lastOccupant instanceof Pellet);
		Thread.sleep(DEFAULT_INTERVAL);
	}

	/**
	 * Scenario S3.3: The ghost leaves a food cell.
	 * Given a ghost is on a food cell (see S3.2);
	 * When  a tick even occurs;
	 * Then  the ghost can move to away from the food cell,
	 *  and  the food on that cell is is visible again.
	 *  @throws InterruptedException - Throws an error if the build factory goes wrong
	 */
	@Test
	public void ghostLeavesFood() throws InterruptedException {
		CustomGhostFactory gf = ((SimpleGhostMap) launcher).getCustomGhostFactory();
		Ghost blinky = gf.popBlinky();

		game.start();

		Square sq = blinky.getSquare();
		Direction next = blinky.getDirection();
		Square sqNext = sq.getSquareAt(next);

		// We sleep until the Ghost is going to a square with food next
		while(!Util.contains(sqNext, Pellet.class)) {
			Thread.sleep(DEFAULT_INTERVAL);

			sq = blinky.getSquare();
			next = blinky.getDirection();
			sqNext = sq.getSquareAt(next);
		}

		List<Unit> occupants;
		Unit lastOccupant;

		// We're at the food, now verify that the visible occupant (the last one) is a Pellet
		occupants = sqNext.getOccupants();
		lastOccupant = occupants.get(occupants.size() - 1);
		assertTrue(lastOccupant instanceof Pellet);

		// Now sleep until we moved and check that the last occupant is not a Pellet anymore
		Thread.sleep(200);
		assertEquals(sqNext, blinky.getSquare());

		// Verify that the visible occupant is not a Pellet anymore
		// Also verify that the food is still on the Pellet
		occupants = sqNext.getOccupants();
		lastOccupant = occupants.get(occupants.size() - 1);
		assertFalse(lastOccupant instanceof Pellet);
		assertTrue(Util.contains(sqNext, Pellet.class));

		Thread.sleep(200);
	}

	/**
	 * Scenario S3.4: The player dies.
	 * Given the game has started,
	 *  and  a ghost is next to a cell containing the player;
	 * When  a tick event occurs;
	 * Then  the ghost can move to the player,
	 *  and  the game is over.
	 *  @throws InterruptedException - Throws an error if the build factory goes wrong
	 */
	@Test
	public void playerDiesByGhost() throws InterruptedException {
		//ANNOTATION: Because of our custom map, the bot has one direction in which it can go
		//Load our customized ghosts
		CustomGhostFactory gf = ((SimpleGhostMap) launcher).getCustomGhostFactory();
		//Pop another Blinky on which we can test
		Ghost blinky = gf.popBlinky();
		//Start the actual game
		game.start();
		//Get the square the ghost is standing on
		Square sq = blinky.getSquare();
		//Get the direction in with the ghost is heading
		Direction next = blinky.getDirection();
		//Obtain the square in the direction of the ghost
		Square sqNext = sq.getSquareAt(next);

		//Suspend the test until we are actually at the player to check if it gets killed
		//Break if the next square contains the player
		while(!Util.contains(sqNext, Player.class)) {
			//update the squares and directions of the ghost
			sq = blinky.getSquare();
			next = blinky.getDirection();
			sqNext = sq.getSquareAt(next);
			Thread.sleep(DEFAULT_INTERVAL);
		}
		//Suspend to give the ghost time to move to the square of the player
		Thread.sleep(200);
		//Check if the player is indeed no longer alive
		assertEquals(false, player.isAlive());
	}
}
