package nl.tudelft.jpacman.level;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import nl.tudelft.jpacman.PacmanConfigurationException;
import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.npc.NPC;
import nl.tudelft.jpacman.npc.ghost.Ghost;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * This class tests the functionality of the JPacman MapParser.
 * @author gerlof
 */
@RunWith(MockitoJUnitRunner.class)
public class MapParserTest {
	private MapParser parser;
	private List<String> grid1;
	private List<String> grid2;
	
	@Mock private LevelFactory levelCreator;
	@Mock private BoardFactory boardCreator;
	@Mock private Ghost ghost;
	@Mock private Pellet pellet;
	@Mock private Square square;
	
	@Captor private ArgumentCaptor<List<Square>> captor;

	/**
	 * Sets up the MapParser using a simple map.
	 * Also initializes all needed mock methods.
	 */
	@Before
	public void setUp() {
		grid1 = new ArrayList<String>();
		grid1.add("P #");
		grid1.add("G .");
		
		grid2 = new ArrayList<String>();
		
		parser = new MapParser(levelCreator, boardCreator);
		
		when(levelCreator.createGhost()).thenReturn(ghost);
		when(levelCreator.createPellet()).thenReturn(pellet);
		when(boardCreator.createGround()).thenReturn(square);
		when(boardCreator.createWall()).thenReturn(square);
	}
	
	/**
	 * Given the map grid1, the MapParser should
	 * create exactly 5 ground tiles.
	 */
	@Test
	public void ground() {		
		parser.parseMap(grid1);
		verify(boardCreator, times(5)).createGround();
	}
	
	/**
	 * Given the map grid1, the MapParser should
	 * create exactly 1 Ghost NPC.
	 */
	@Test
	public void ghost() {
		parser.parseMap(grid1);
		verify(levelCreator, times(1)).createGhost();
	}
	
	/**
	 * Given the map grid1, the MapParser should
	 * create exactly 1 Pellet.
	 */
	@Test
	public void pellet() {
		parser.parseMap(grid1);
		verify(levelCreator, times(1)).createPellet();
	}
	
	/**
	 * Given the map grid1, the MapParser should
	 * create exactly a List of startingSquares with size 1.
	 */
	@Test
	public void player() {
		parser.parseMap(grid1);
		verify(levelCreator, times(1)).
			createLevel(any(Board.class), anyListOf(NPC.class), captor.capture());
		assertEquals(1, captor.getValue().size());
	}
	
	/**
	 * Given the map grid1, the MapParser should
	 * create exactly 1 wall tile.
	 */
	@Test
	public void wall() {
		parser.parseMap(grid1);
		verify(boardCreator, times(1)).createWall();
	}
	
	/**
	 * Given the map grid2, which we initialize with invalid values,
	 * the MapParser should throw a {@link PacmanConfigurationException}.
	 */
	@Test(expected = PacmanConfigurationException.class)
	public void invalidChar() {
		grid2.add("C ");
		grid2.add("  ");
		parser.parseMap(grid2);
	}
	
	/**
	 * Given the map grid2, which we initialize with different row lengths,
	 * the MapParser should throw a {@link PacmanConfigurationException}.
	 */
	@Test(expected = PacmanConfigurationException.class)
	public void invalidSize() {
		grid2.add("  ");
		grid2.add("   ");
		parser.parseMap(grid2);
	}
	
	/**
	 * Given the map grid2, which we initialize with an empty String,
	 * the MapParser should throw a {@link PacmanConfigurationException}.
	 */
	@Test(expected = PacmanConfigurationException.class)
	public void invalidString() {
		grid2.add("");
		parser.parseMap(grid2);
	}
	
	/**
	 * Given the map grid2, which we do not initialize,
	 * the MapParser should throw a {@link PacmanConfigurationException}.
	 */
	@Test(expected = PacmanConfigurationException.class)
	public void invalidNull() {
		parser.parseMap(grid2);
	}
}
