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

@RunWith(Theories.class)
public class MultiAndSingleGameTest {
	@Mock private Player player;
	
	@DataPoints
	public static Game[] games() {
		return new Game[] {
			new SimpleMapWithGhost().makeGame(),
			new MultiLevelLauncher().makeGame()
		};
	}
	
	@Theory
	public void initial(Game game) {
		assertFalse(game.isInProgress());

	}

	@Theory
	public void stop(Game game) {
		game.stop();
		assertFalse(game.isInProgress());
	}
	
	@Theory
	public void startStop(Game game) {
		game.start();
		game.stop();
		assertFalse(game.isInProgress());
	}
	
	@Theory
	public void startStart(Game game) {
		game.start();
		game.start();
		assertTrue(game.isInProgress());
	}
	
	@Theory
	public void startWinStop(Game game) throws InterruptedException {
		player = game.getPlayers().get(0);
		game.start();
		game.move(player, Direction.EAST);
		assertTrue(player.isAlive());
		game.stop();
		assertFalse(game.isInProgress());
	}
	
	@Theory
	public void startWinStart(Game game) {
		player = game.getPlayers().get(0);
		game.start();
		game.move(player, Direction.EAST);
		assertTrue(player.isAlive());
		game.start();
		assertFalse(game.isInProgress());
	}
	
	@Theory
	public void startLose(Game game) {
		player = game.getPlayers().get(0);
		game.start();
		game.move(player, Direction.WEST);
		assertFalse(game.isInProgress());
		assertFalse(player.isAlive());
	}
	
	@Theory
	public void readyStart(Game game) {
		game.start();
		assertTrue(game.isInProgress());
	}
	
	@Theory
	public void readyStop(Game game) {
		game.stop();
		assertFalse(game.isInProgress());
	}
	
	@Theory
	public void playingStart(Game game) {
		game.start();
		game.start();
		assertTrue(game.isInProgress());
	}
	
	@Theory
	public void playingStop(Game game) {
		game.start();
		game.stop();
		assertFalse(game.isInProgress());
	}
	
	@Theory
	public void playingWin(Game game) {
		player = game.getPlayers().get(0);
		game.start();
		game.move(player, Direction.EAST);
		assertFalse(game.isInProgress());
		assertTrue(player.isAlive());
	}
	
	@Theory
	public void playingLose(Game game) {
		player = game.getPlayers().get(0);
		game.start();
		game.move(player, Direction.WEST);
		assertFalse(game.isInProgress());
		assertFalse(player.isAlive());
	}
	
	@Theory
	public void endedStart(Game game) {
		player = game.getPlayers().get(0);
		game.start();
		game.move(player, Direction.WEST);
		game.start();
		assertFalse(game.isInProgress());
	}
	
	@Theory
	public void endedStop(Game game) {
		player = game.getPlayers().get(0);
		game.start();
		game.move(player, Direction.WEST);
		game.stop();
		assertFalse(game.isInProgress());
	}
}
