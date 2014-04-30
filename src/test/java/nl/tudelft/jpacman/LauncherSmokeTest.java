package nl.tudelft.jpacman;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.Pellet;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.npc.ghost.Ghost;
import nl.tudelft.jpacman.ui.PacManUiBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Smoke test launching the full game,
 * and attempting to make a number of typical moves.
 *
 * This is <strong>not</strong> a <em>unit</em> test -- it is an end-to-end test
 * trying to execute a large portion of the system's behavior directly from the
 * user interface. It uses the actual sprites and monster AI, and hence
 * has little control over what is happening in the game.
 *
 * Because it is an end-to-end test, it is somewhat longer
 * and has more assert statements than what would be good
 * for a small and focused <em>unit</em> test.
 *
 * @author Arie van Deursen, March 2014.
 */
public class LauncherSmokeTest {

	private Launcher launcher;

	@Before
	public void setUpPacman() {
		launcher = new Launcher();
		launcher.launch();
	}

	@After
	public void tearDown() {
		launcher.dispose();
	}

	/**
	 * Launch the game, and imitate what would happen
	 * in a typical game.
	 * @throws InterruptedException Since we're sleeping in this test.
	 */
	@Test
	public void smokeTest() throws InterruptedException {
		Game game = launcher.getGame();        
		Player player = game.getPlayers().get(0);

		// start cleanly.
		assertFalse(game.isInProgress());
		game.start();
		assertTrue(game.isInProgress());
		assertEquals(0, player.getScore());

		// get points
		game.move(player, Direction.EAST);
		assertEquals(10, player.getScore());

		// now moving back does not change the score
		game.move(player, Direction.WEST);
		assertEquals(10, player.getScore());

		// try to move as far as we can
		move(game, Direction.EAST, 7);
		assertEquals(60, player.getScore());

		// move towards the monsters
		move(game, Direction.NORTH, 6);
		assertEquals(120, player.getScore());

		// no more points to earn here.
		move(game, Direction.WEST, 2);
		assertEquals(120, player.getScore());

		move(game, Direction.NORTH, 2);

		// Sleeping in tests is generally a bad idea.
		// Here we do it just to let the monsters move.
		Thread.sleep(500L);

		// we're close to monsters, this will get us killed.
		move(game, Direction.WEST, 10);
		move(game, Direction.EAST, 10);
		assertFalse(player.isAlive());

		game.stop();
		assertFalse(game.isInProgress());
	}

	/**
	 * Scenario S1.1: Start.
	 * Given the user has launched the JPacman GUI;
	 * When  the user presses the "Start" button;
	 * Then  the game should start.
	 */
	@Test
	public void start() {
		Game game = launcher.getGame();

		// start cleanly.
		assertFalse(game.isInProgress());
		game.start();
		assertTrue(game.isInProgress());
	}

	/**
	 * Scenario S2.1: The player consumes
	 * Given the game has started,
	 *  and  my Pacman is next to a square containing a pellet;
	 * When  I press an arrow key towards that square;
	 * Then  my Pacman can move to that square,
	 *  and  I earn the points for the pellet,
	 *  and  the pellet disappears from that square.
	 * @throws InterruptedException
	 */
	@Test
	public void consume() throws InterruptedException {
		Game game = launcher.getGame();
		Player player = game.getPlayers().get(0);
		Square myLocation = player.getSquare();
		Square nextLocation = myLocation.getSquareAt(Direction.EAST);

		// Before starting, make sure that the square to the East contains a Pellet
		assertTrue(nextLocation.getOccupants().get(0) instanceof Pellet);

		// start cleanly.
		game.start();

		// Move 1 square to the East
		game.move(player, Direction.EAST);
		// Check that we've moved 1 square to the east
		assertEquals(nextLocation, player.getSquare());
		// Check that we've picked up a Pellet worth 10 points
		assertEquals(10, player.getScore());
		// Check that the pellet is not an occupant of the square anymore
		assertFalse(nextLocation.getOccupants().get(0) instanceof Pellet);

		Thread.sleep(100L);
	}

	/**
	 * Scenario S2.2: The player moves on empty square
	 * Given the game has started,
	 *  and  my Pacman is next to an empty square;
	 * When  I press an arrow key towards that square;
	 * Then  my Pacman can move to that square
	 *  and  my points remain the same.
	 * @throws InterruptedException
	 */
	@Test
	public void moveEmpty() throws InterruptedException {
		Game game = launcher.getGame();
		Player player = game.getPlayers().get(0);
		Square myLocation = player.getSquare();

		// Start the game and move one square to the east
		game.start();
		game.move(player, Direction.EAST);
		
		// The square at our original location should have no Pellets on it
		assertTrue(myLocation.getOccupants().isEmpty());

		// Store our score before we move back to our starting position
		int score = player.getScore();
		game.move(player, Direction.WEST);
		
		// Check that we've moved to our starting position
		assertEquals(myLocation, player.getSquare());
		// Check that our score has remained the same
		assertEquals(score, player.getScore());

		Thread.sleep(100L);
	}
	
	/**
	 * Scenario S2.3: The player dies
	 * Given the game has started,
	 *  and  my Pacman is next to a cell containing a ghost;
	 * When  I press an arrow key towards that square;
	 * Then  my Pacman dies,
	 *  and  the game is over.
	 */
	@Test
	public void playerDies() throws InterruptedException {
		Game game = launcher.getGame();
		Player player = game.getPlayers().get(0);
		Square myLocation = player.getSquare();
		Direction monsterDirection = null;
		
		// Start the game
		game.start();
		assertTrue(player.isAlive());

		// Wait until there is a ghost next to us
		while (monsterDirection == null) {
			// Loop over the squares in all four directions
			for (Direction dir: Direction.values()) {
				// ... and check whether a monster is on them
				Square adjacent = myLocation.getSquareAt(dir);
				if (monsterOn(adjacent))
					monsterDirection = dir;
			}
			Thread.sleep(50L);
		}

		// Now move towards the ghost
		game.move(player, monsterDirection);
		
		// Check that we have died
		assertFalse(player.isAlive());
		// Check that the game is no longer in progress
		assertFalse(game.isInProgress());
		
		Thread.sleep(100L);
	}
	
	/**
	 * Scenario S2.4: The move fails
	 * Given the game has started,
	 *  and my Pacman is next to a cell containing a wall;
	 * When  I press an arrow key towards that cell;
	 * Then  the move is not conducted.
	 */
	@Test
	public void moveWall() throws InterruptedException {
		Game game = launcher.getGame();
		Player player = game.getPlayers().get(0);
		game.start();
		
		Square myLocation, adjacent;
		do {
			// Move to the east
			game.move(player, Direction.EAST);
			
			// Update our current location and adjacent square
			myLocation = player.getSquare();
			adjacent = myLocation.getSquareAt(Direction.EAST);
		
		// Keep doing this until the adjacent square isn't accessible (ie. it's a wall)
		} while (adjacent.isAccessibleTo(player));
		
		// Check that it is indeed not accessible
		assertFalse(adjacent.isAccessibleTo(player));
		
		// Try to go there anyway
		game.move(player, Direction.EAST);
		
		// Check that we haven't moved since breaking out of the loop
		assertEquals(myLocation, player.getSquare());
		
		Thread.sleep(100L);
	}
	
	/**
	 * Scenario S2.5: Player wins, extends S2.2
	 * When  I have eaten the last pellet;
	 * Then  I win the game.
	 */
	//@Test
	public void win() throws InterruptedException {
		Game game = launcher.getGame();
		Level level = game.getLevel();
		
		// Wait for the player to either die or to win
		while (level.isAnyPlayerAlive() && level.remainingPellets() > 0) {
			Thread.sleep(100L);
		}
		
		assertTrue(level.isAnyPlayerAlive());
		assertFalse(level.isInProgress());
	}

	/**
	 * Scenario S4.1: Suspend the game.
	 * Given the game has started;
	 * When  the player clicks the "Stop" button;
	 * Then  all moves from ghosts and the player are suspended.
	 * @throws InterruptedException
	 */
	@Test
	public void suspend() throws InterruptedException {
		Game game = launcher.getGame();
		Player player = game.getPlayers().get(0);
		game.start();

		player.setDirection(Direction.WEST);
		Thread.sleep(100L);

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
	 * @throws InterruptedException
	 */
	@Test
	public void restart() throws InterruptedException {
		Game game = launcher.getGame();        
		game.start();
		Thread.sleep(100L);

		// stop the game
		game.stop();
		Thread.sleep(100L);
		assertFalse(game.isInProgress());

		// and start the game again
		game.start();
		Thread.sleep(100L);
		assertTrue(game.isInProgress());
	}

	/**
	 * Make number of moves in given direction.
	 *
	 * @param game The game we're playing
	 * @param dir The direction to be taken
	 * @param numSteps The number of steps to take
	 */
	public static void move(Game game, Direction dir, int numSteps) {
		Player player = game.getPlayers().get(0);
		for (int i = 0; i < numSteps; i++) {
			game.move(player, dir);
		}
	}
	
	/**
	 * Test whether there are ghosts on a give square
	 * @param square
	 * @return
	 */
	public static boolean monsterOn(Square square) {
		List<Unit> occupants = square.getOccupants();
		for (Unit unit: occupants) {
			if (unit instanceof Ghost) {
				return true;
			}
		}
		return false;
	}
}
