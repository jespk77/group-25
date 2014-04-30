package nl.tudelft.jpacman;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.Pellet;
import nl.tudelft.jpacman.level.Player;

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
    public void consume() {
        Game game = launcher.getGame();
        Player player = game.getPlayers().get(0);

        // start cleanly.
        game.start();
        
        Square myLocation = player.getSquare();
        Square nextLocation = myLocation.getSquareAt(Direction.EAST);
        assertTrue(nextLocation.getOccupants().get(0) instanceof Pellet);
        
        game.move(player, Direction.EAST);
        assertEquals(nextLocation, player.getSquare());
		assertEquals(10, player.getScore());
		assertFalse(nextLocation.getOccupants().get(0) instanceof Pellet);
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
}
