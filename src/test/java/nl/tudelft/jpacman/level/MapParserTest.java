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

@RunWith(MockitoJUnitRunner.class)
public class MapParserTest {
	private MapParser parser;
	private List<String> grid1;
	private List<String> grid2;
	
	@Mock LevelFactory levelCreator;
	@Mock BoardFactory boardCreator;
	@Mock Ghost ghost;
	@Mock Pellet pellet;
	@Mock Square square;
	@Mock Level level;
	
	@Captor ArgumentCaptor<List<Square>> captor;
	
	private List<Square> squares = new ArrayList<Square>();

	@Before
	public void setUp() {
		grid1 = new ArrayList<String>();
		grid1.add("P #");
		grid1.add("G .");
		
		grid2 = new ArrayList<String>();
		
		parser = new MapParser(levelCreator, boardCreator);
		squares.add(square);
		
		when(levelCreator.createGhost()).thenReturn(ghost);
		when(levelCreator.createPellet()).thenReturn(pellet);
		when(boardCreator.createGround()).thenReturn(square);
		when(boardCreator.createWall()).thenReturn(square);
	}
	
	@Test
	public void ground() {		
		parser.parseMap(grid1);
		verify(boardCreator, times(5)).createGround();
	}
	
	@Test
	public void ghost() {
		parser.parseMap(grid1);
		verify(levelCreator, times(1)).createGhost();
	}
	
	@Test
	public void pellet() {
		parser.parseMap(grid1);
		verify(levelCreator, times(1)).createPellet();
	}
	
	@Test
	public void player() {
		parser.parseMap(grid1);
		verify(levelCreator, times(1)).createLevel(any(Board.class), anyListOf(NPC.class), captor.capture());
		assertEquals(1, captor.getValue().size());
	}
	
	@Test
	public void wall() {
		parser.parseMap(grid1);
		verify(boardCreator, times(1)).createWall();
	}
	
	@Test(expected = PacmanConfigurationException.class)
	public void invalidChar() {
		grid2.add("C ");
		grid2.add("  ");
		parser.parseMap(grid2);
	}
	
	@Test(expected = PacmanConfigurationException.class)
	public void invalidSize() {
		grid2.add("  ");
		grid2.add("   ");
		parser.parseMap(grid2);
	}
	
	@Test(expected = PacmanConfigurationException.class)
	public void invalidString() {
		grid2.add("");
		parser.parseMap(grid2);
	}
	
	@Test(expected = PacmanConfigurationException.class)
	public void invalidNull() {
		parser.parseMap(grid2);
	}
}
