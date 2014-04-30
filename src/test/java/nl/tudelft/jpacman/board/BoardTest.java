package nl.tudelft.jpacman.board;

import static org.junit.Assert.assertEquals;
import nl.tudelft.jpacman.sprite.Sprite;

import org.junit.Before;
import org.junit.Test;

/**
 * A very simple test class to have a starting point where to put tests.
 * 
 * @author Arie van Deursen
 *
 */
public class BoardTest {
	private Square[][] gridNull, gridInit;
	
	/**
	 * Sets up the grids we use for initializing the boards.
	 */
	@Before
	public void setupGrid() {
		gridNull = new Square[1][1];
		gridInit = new Square[1][1];
		gridInit[0][0] = new Square() {
			
			@Override
			public boolean isAccessibleTo(Unit unit) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public Sprite getSprite() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}
	
	/**
	 * Creating a board from an array of Null squares should fail.
	 */
	@Test (expected = AssertionError.class)
	public void boardNull() {
		Board board = new Board(gridNull);
		assertEquals(gridNull[0][0], board.squareAt(0, 0));
	}
	
	/**
	 * Creating a board from an array of initialized squares should succeed.
	 */
	@Test
	public void boardInitTest() { 
		Board board = new Board(gridInit);
		assertEquals(gridInit[0][0], board.squareAt(0, 0));
	}
}
