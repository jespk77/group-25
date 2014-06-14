package nl.tudelft.jpacman.game;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.group25.SimpleMapWithGhost;
import nl.tudelft.jpacman.level.Player;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class GameTest {
	private Game simpleGame;
	@Mock private Player player;
	
	@Before
	public void setUp() {
		simpleGame = new SimpleMapWithGhost().makeGame();
	}
	
	@Test
	public void initial() {
		assertFalse(simpleGame.isInProgress());

	}

	@Test
	public void stop() {
		simpleGame.stop();
		assertFalse(simpleGame.isInProgress());
	}
	
	@Test
	public void startStop() {
		simpleGame.start();
		simpleGame.stop();
		assertFalse(simpleGame.isInProgress());
	}
	
	@Test
	public void startStart() {
		simpleGame.start();
		simpleGame.start();
		assertTrue(simpleGame.isInProgress());
	}
	
	@Test
	public void startWinStop() throws InterruptedException {
		player = simpleGame.getPlayers().get(0);
		simpleGame.start();
		simpleGame.move(player, Direction.EAST);
		assertTrue(player.isAlive());
		simpleGame.stop();
		assertFalse(simpleGame.isInProgress());
	}
	
	@Test
	public void startWinStart() {
		player = simpleGame.getPlayers().get(0);
		simpleGame.start();
		simpleGame.move(player, Direction.EAST);
		assertTrue(player.isAlive());
		simpleGame.start();
		assertFalse(simpleGame.isInProgress());
	}
	
	@Test
	public void startLose() {
		player = simpleGame.getPlayers().get(0);
		simpleGame.start();
		simpleGame.move(player, Direction.WEST);
		assertFalse(simpleGame.isInProgress());
		assertFalse(player.isAlive());
	}
	
	@Test
	public void readyStart() {
		simpleGame.start();
		assertTrue(simpleGame.isInProgress());
	}
	
	@Test
	public void readyStop() {
		simpleGame.stop();
		assertFalse(simpleGame.isInProgress());
	}
	
	@Test
	public void playingStart() {
		simpleGame.start();
		simpleGame.start();
		assertTrue(simpleGame.isInProgress());
	}
	
	@Test
	public void playingStop() {
		simpleGame.start();
		simpleGame.stop();
		assertFalse(simpleGame.isInProgress());
	}
	
	@Test
	public void playingWin() {
		player = simpleGame.getPlayers().get(0);
		simpleGame.start();
		simpleGame.move(player, Direction.EAST);
		assertFalse(simpleGame.isInProgress());
		assertTrue(player.isAlive());
	}
	
	@Test
	public void playingLose() {
		player = simpleGame.getPlayers().get(0);
		simpleGame.start();
		simpleGame.move(player, Direction.WEST);
		assertFalse(simpleGame.isInProgress());
		assertFalse(player.isAlive());
	}
	
	@Test
	public void endedStart() {
		player = simpleGame.getPlayers().get(0);
		simpleGame.start();
		simpleGame.move(player, Direction.WEST);
		simpleGame.start();
		assertFalse(simpleGame.isInProgress());
	}
	
	@Test
	public void endedStop() {
		player = simpleGame.getPlayers().get(0);
		simpleGame.start();
		simpleGame.move(player, Direction.WEST);
		simpleGame.stop();
		assertFalse(simpleGame.isInProgress());
	}
}
