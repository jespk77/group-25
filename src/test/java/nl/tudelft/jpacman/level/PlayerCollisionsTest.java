package nl.tudelft.jpacman.level;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import nl.tudelft.jpacman.npc.ghost.Ghost;

import org.junit.Before;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * This class tests the functionality of the JPacman Collision Maps.
 * @author gerlof
 */
@RunWith(Theories.class)
public class PlayerCollisionsTest {
	@Mock private Player player;
	@Mock private Ghost ghost;
	@Mock private Pellet pellet;
	
	/**
	 * This method specifies all CollisionMaps that will be tested.
	 * @return	list containing all CollisionMaps
	 */
	@DataPoints
	public static CollisionMap[] maps() {
		return new CollisionMap[] {
				new PlayerCollisions(),
				new DefaultPlayerInteractionMap()
		};
	}
	
	/**
	 * Initializes and injects all needed mock methods.
	 */
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		when(pellet.getValue()).thenReturn(10);
	}
	
	/**
	 * When two players collide, nothing should happen.
	 */
	@Theory
	public void playerOnPlayer(CollisionMap map) {
		map.collide(player, player);
		verify(player, never()).setAlive(anyBoolean());
		verify(player, never()).addPoints(anyInt());
		verify(pellet, never()).leaveSquare();
	}
	
	/**
	 * When a player and a ghost collide, the player should die.
	 */
	@Theory
	public void playerOnGhost(CollisionMap map) {
		map.collide(player, ghost);
		verify(player, times(1)).setAlive(false);
		verify(player, never()).addPoints(anyInt());
		verify(pellet, never()).leaveSquare();
	}
	
	/**
	 * When a player and a pellet collide,
	 * the player should receive points and the pellet should disappear.
	 */
	@Theory
	public void playerOnPellet(CollisionMap map) {
		map.collide(player, pellet);
		verify(player, never()).setAlive(anyBoolean());
		verify(player, times(1)).addPoints(pellet.getValue());
		verify(pellet, times(1)).leaveSquare();
	}
	
	/**
	 * When a ghost and a player collide, the player should die.
	 */
	@Theory
	public void ghostOnPlayer(CollisionMap map) {
		map.collide(ghost, player);
		verify(player, times(1)).setAlive(false);
		verify(player, never()).addPoints(anyInt());
		verify(pellet, never()).leaveSquare();
	}
	
	/**
	 * When a ghost and a ghost collide, nothing should happen.
	 */
	@Theory
	public void ghostOnGhost(CollisionMap map) {
		map.collide(ghost, ghost);
		verify(player, never()).setAlive(anyBoolean());
		verify(player, never()).addPoints(anyInt());
		verify(pellet, never()).leaveSquare();
	}
	
	/**
	 * When a ghost and a pellet collide, nothing should happen.
	 */
	@Theory
	public void ghostOnPellet(CollisionMap map) {
		map.collide(ghost, pellet);
		verify(player, never()).setAlive(anyBoolean());
		verify(player, never()).addPoints(anyInt());
		verify(pellet, never()).leaveSquare();
	}
}
