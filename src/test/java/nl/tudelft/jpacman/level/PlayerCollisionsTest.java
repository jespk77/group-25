package nl.tudelft.jpacman.level;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;

import nl.tudelft.jpacman.npc.ghost.Ghost;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * This class tests the functionality of the JPacman Collision Maps.
 * @author gerlof
 */
@RunWith(Parameterized.class)
public class PlayerCollisionsTest {
	@Mock private Player player;
	@Mock private Ghost ghost;
	@Mock private Pellet pellet;
	
	@InjectMocks private CollisionMap collision;
	
	/**
	 * Initializes and injects all needed mock methods.
	 */
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		when(pellet.getValue()).thenReturn(10);
	}
	
	/**
	 * Creates an instance of the parameterized JUnit class PlayerCollisionsTest.
	 * We initialize Mockito annotations manually so we can parameterize the CollisionMaps. 
	 * @param collision	the CollisionMap we want to use in the JUnit test case
	 */
	public PlayerCollisionsTest(CollisionMap collision) {
		this.collision = collision;
	}
	
	/**
	 * This method specifies all parameterized CollisionMaps.
	 * @return	list containing all parameterized CollisionMaps
	 */
	@Parameters
	public static Collection<Object[]> withinBordersData() {
		return Arrays.asList(new Object[][] {
				{ new PlayerCollisions() },
				{ new DefaultPlayerInteractionMap() }
		});
	}
	
	/**
	 * When two players collide, nothing should happen.
	 */
	@Test
	public void playerOnPlayer() {
		collision.collide(player, player);
		verify(player, never()).setAlive(anyBoolean());
		verify(player, never()).addPoints(anyInt());
		verify(pellet, never()).leaveSquare();
	}
	
	/**
	 * When a player and a ghost collide, the player should die.
	 */
	@Test
	public void playerOnGhost() {
		collision.collide(player, ghost);
		verify(player, times(1)).setAlive(false);
		verify(player, never()).addPoints(anyInt());
		verify(pellet, never()).leaveSquare();
	}
	
	/**
	 * When a player and a pellet collide,
	 * the player should receive points and the pellet should disappear.
	 */
	@Test
	public void playerOnPellet() {
		collision.collide(player, pellet);
		verify(player, never()).setAlive(anyBoolean());
		verify(player, times(1)).addPoints(pellet.getValue());
		verify(pellet, times(1)).leaveSquare();
	}
	
	/**
	 * When a ghost and a player collide, the player should die.
	 */
	@Test
	public void ghostOnPlayer() {
		collision.collide(ghost, player);
		verify(player, times(1)).setAlive(false);
		verify(player, never()).addPoints(anyInt());
		verify(pellet, never()).leaveSquare();
	}
	
	/**
	 * When a ghost and a ghost collide, nothing should happen.
	 */
	@Test
	public void ghostOnGhost() {
		collision.collide(ghost, ghost);
		verify(player, never()).setAlive(anyBoolean());
		verify(player, never()).addPoints(anyInt());
		verify(pellet, never()).leaveSquare();
	}
	
	/**
	 * When a ghost and a pellet collide, nothing should happen.
	 */
	@Test
	public void ghostOnPellet() {
		collision.collide(ghost, pellet);
		verify(player, never()).setAlive(anyBoolean());
		verify(player, never()).addPoints(anyInt());
		verify(pellet, never()).leaveSquare();
	}
}
