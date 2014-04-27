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
	private Square[][] grid;
	
	@Before
	public void setupBoard() {
		grid = new Square[1][1];
	}
	
	/**
	 * Is the Board correct?
	 */
	//@Test
	//public void BoardNull() {
	//	Board board = new Board(grid);
	//	assertEquals(grid[0][0], board.squareAt(0, 0));
	//}
	
	@Test
	public void BoardInitTest() {
		grid[0][0] = new Square() {
			
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
		Board board = new Board(grid);
		assertEquals(grid[0][0], board.squareAt(0, 0));
	}
}
