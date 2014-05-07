package nl.tudelft.jpacman;

import static org.junit.Assert.*;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.group25.SimpleMap;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.Pellet;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.level.Level.LevelObserver;
import nl.tudelft.jpacman.npc.ghost.Ghost;

import org.junit.Before;
import org.junit.Test;

public class Scenario2Test {
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
	
	public void setUpSimplePacman() {
		launcher.dispose();
		launcher = new SimpleMap();
		launcher.launch();

		game = launcher.getGame();
		level = game.getLevel();
		player = game.getPlayers().get(0);
		myLocation = player.getSquare();
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
		assertTrue(Util.contains(nextLocation, Pellet.class));

		// start cleanly.
		game.start();
		game.move(player, Direction.EAST);

		// Check that we've moved 1 square to the east
		assertEquals(nextLocation, player.getSquare());
		// Check that we've picked up a Pellet worth 10 points
		assertEquals(10, player.getScore());
		// Check that the pellet is not an occupant of the square anymore
		assertFalse(Util.contains(nextLocation, Pellet.class));

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
				if (Util.contains(adjacent, Ghost.class)) {
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
		setUpSimplePacman();
		
		// The only way to know the game has been won is through an observer.
		class TestObserver implements LevelObserver {
			private boolean hasWon = false;
			public boolean hasWon() { return hasWon; }
			public void	levelWon() { hasWon = true; }
			public void levelLost() { hasWon = false; }
		};

		// Create a testobserver and register it with the level.
		TestObserver observer = new TestObserver();
		level.addObserver(observer);

		game.start();

		// Pick up the only pellet in the level.
		game.move(player, Direction.EAST);

		// Check whether the game has been won.
		assertTrue(level.isAnyPlayerAlive());
		assertFalse(level.isInProgress());
		assertTrue(observer.hasWon());

		Thread.sleep(DEFAULT_INTERVAL);
	}
}
