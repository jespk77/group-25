package nl.tudelft.jpacman;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.group25.SimpleMap;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.Level.LevelObserver;
import nl.tudelft.jpacman.level.Pellet;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.npc.ghost.Ghost;

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
	private static final long DEFAULT_INTERVAL = 100L;
	
	private Launcher launcher;
	private Game game;
	private Level level;
	private Player player;
	private Square myLocation;

	/**
	 * Sets up the game and initializes some variables we use quite often.
	 */
	@Before
	public void setUpPacman() {
		launcher = new Launcher();
		launcher.launch();
		
		game = launcher.getGame();
		level = game.getLevel();
		player = game.getPlayers().get(0);
		myLocation = player.getSquare();
	}

	/**
	 * Tears down the game so it can gracefully exit.
	 */
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
	 * @throws InterruptedException	Although not recommended, we use sleep here
	 */
	@Test
	public void playerConsumes() throws InterruptedException {
		Square nextLocation = myLocation.getSquareAt(Direction.EAST);

		// Before starting, make sure that the square to the East contains a Pellet
		assertTrue(contains(nextLocation, Pellet.class));

		// start cleanly.
		game.start();
		game.move(player, Direction.EAST);
		
		// Check that we've moved 1 square to the east
		assertEquals(nextLocation, player.getSquare());
		// Check that we've picked up a Pellet worth 10 points
		assertEquals(10, player.getScore());
		// Check that the pellet is not an occupant of the square anymore
		assertFalse(contains(nextLocation, Pellet.class));

		Thread.sleep(DEFAULT_INTERVAL);
	}

	/**
	 * Scenario S2.2: The player moves on empty square
	 * Given the game has started,
	 *  and  my Pacman is next to an empty square;
	 * When  I press an arrow key towards that square;
	 * Then  my Pacman can move to that square
	 *  and  my points remain the same.
	 * @throws InterruptedException	Although not recommended, we use sleep here
	 */
	@Test
	public void playerMoves() throws InterruptedException {
		game.start();
		
		// Move one square to the east
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

		Thread.sleep(DEFAULT_INTERVAL);
	}
	
	/**
	 * Scenario S2.3: The player dies
	 * Given the game has started,
	 *  and  my Pacman is next to a cell containing a ghost;
	 * When  I press an arrow key towards that square;
	 * Then  my Pacman dies,
	 *  and  the game is over.
	 * @throws InterruptedException	Although not recommended, we use sleep here
	 */
	@Test
	public void playerDies() throws InterruptedException {
		game.start();
		
		assertTrue(player.isAlive());

		// Wait until there is a ghost next to us
		Direction monsterDirection = null;
		while (monsterDirection == null) {
			// Loop over the squares in all four directions
			for (Direction dir: Direction.values()) {
				// ... and check whether a monster is on them
				Square adjacent = myLocation.getSquareAt(dir);
				if (contains(adjacent, Ghost.class)) {
					monsterDirection = dir;
				}
			}
			Thread.sleep(DEFAULT_INTERVAL);
		}

		// Now move towards the ghost
		game.move(player, monsterDirection);
		
		// Check that we have died
		assertFalse(player.isAlive());
		// Check that the game is no longer in progress
		assertFalse(game.isInProgress());
		
		Thread.sleep(DEFAULT_INTERVAL);
	}
	
	/**
	 * Scenario S2.4: The move fails
	 * Given the game has started,
	 *  and my Pacman is next to a cell containing a wall;
	 * When  I press an arrow key towards that cell;
	 * Then  the move is not conducted.
	 * @throws InterruptedException	Although not recommended, we use sleep here
	 */
	@Test
	public void moveFails() throws InterruptedException {
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
		
		Thread.sleep(DEFAULT_INTERVAL);
	}
	
	/**
	 * Scenario S2.5: Player wins, extends S2.2
	 * When  I have eaten the last pellet;
	 * Then  I win the game.
	 * @throws InterruptedException	Although not recommended, we use sleep here
	 */
	@Test
	public void playerWins() throws InterruptedException {
		// The only way to know the game has been won is through an observer.
		class TestObserver implements LevelObserver {
			private boolean hasWon = false;
			public boolean hasWon() { return hasWon; }
			public void	levelWon() { hasWon = true; }
			public void levelLost() { hasWon = false; }
		};
		
		launcher.dispose();
		launcher = new SimpleMap();
		launcher.launch();
		
		game = launcher.getGame();
		player = game.getPlayers().get(0);
		level = game.getLevel();
		
		// Create a testobserver and register it with the level.
		TestObserver observer = new TestObserver();
		level.addObserver(observer);
		
		game.start();
		
		// Pick up the only pellet in the level.
		game.move(player, Direction.EAST);
		
		// Wait for the player to either die or to win.
		while (level.isAnyPlayerAlive() && level.remainingPellets() > 0) {
			Thread.sleep(DEFAULT_INTERVAL);
		}
		
		// Check whether the game has been won.
		assertTrue(level.isAnyPlayerAlive());
		assertFalse(level.isInProgress());
		assertTrue(observer.hasWon());
		
		Thread.sleep(DEFAULT_INTERVAL);
	}
	
	/**
	 * Scenario S3.1: A ghost moves.
	 * Given the game has started,
	 *  and  a ghost is next to an empty cell;
	 * When  a tick event occurs;
	 * Then  the ghost can move to that cell.
	 */
	@Test
	public void ghostMoves() throws InterruptedException {
		
	}
	
	/**
	 * Scenario S3.2: The ghost moves over food.
	 * Given the game has started,
	 *  and  a ghost is next to a cell containing food;
	 * When  a tick event occurs;
	 * Then  the ghost can move to the food cell,
	 *  and  the food on that cell is not visible anymore.
	 */
	@Test
	public void ghostMovesOverFood() throws InterruptedException {
		
	}
	
	/**
	 * Scenario S3.3: The ghost leaves a food cell.
	 * Given a ghost is on a food cell (see S3.2);
	 * When  a tick even occurs;
	 * Then  the ghost can move to away from the food cell,
	 *  and  the food on that cell is is visible again.
	 */
	@Test
	public void ghostLeavesFood() throws InterruptedException {
		
	}
	
	/**
	 * Scenario S3.4: The player dies.
	 * Given the game has started,
	 *  and  a ghost is next to a cell containing the player;
	 * When  a tick event occurs;
	 * Then  the ghost can move to the player,
	 *  and  the game is over.
	 */
	@Test
	public void playerDiesByGhost() throws InterruptedException {
		
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
		assertFalse(game.isInProgress());

		// and start the game again
		game.start();
		Thread.sleep(DEFAULT_INTERVAL);
		assertTrue(game.isInProgress());
	}

	/**
	 * Make number of moves in given direction.
	 *
	 * @param game		The game we're playing
	 * @param dir		The direction to be taken
	 * @param numSteps	The number of steps to take
	 */
	public static void move(Game game, Direction dir, int numSteps) {
		Player player = game.getPlayers().get(0);
		for (int i = 0; i < numSteps; i++) {
			game.move(player, dir);
		}
	}
	
	/**
	 * Test whether a given square has an occupant of the given class.
	 * @param square	the given square
	 * @param c			the given class
	 * @return			true if square contains an instance of class c
	 * 					false otherwise
	 */
	public boolean contains(Square square, Class<?> c) {
		List<Unit> occupants = square.getOccupants();
		for (Unit unit: occupants) {
			if (c.isInstance(unit)) {
				return true;
			}
		}
		return false;
	}
}
