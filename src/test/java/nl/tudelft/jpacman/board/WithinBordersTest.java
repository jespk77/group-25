package nl.tudelft.jpacman.board;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import nl.tudelft.jpacman.sprite.Sprite;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * This class uses parameterized tests to test the Board.withinBorders method.
 */
@RunWith(Parameterized.class)
public class WithinBordersTest {
	private int x, y;
	private boolean out;
	private Board board;
	
	/**
	 * Creates an instance of the JUnit class WithinBordersTest.
	 * @param x		the parameterized x variable
	 * @param y		the parameterized y variable
	 * @param out	the expected result
	 */
	public WithinBordersTest(int x, int y, boolean out) {
		this.x = x;
		this.y = y;
		this.out = out;
	}
	
	/**
	 * Create a default 5x5 grid.
	 * We use this grid to initialize our Board.
	 */
	@Before
	public void createGame() {
		Square[][] grid = new Square[5][5];
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				grid[i][j] = new Square() {
					@Override
					public boolean isAccessibleTo(Unit unit) {
						return false;
					}
					
					@Override
					public Sprite getSprite() {
						return null;
					}
				};
			}
		}
		
		board = new Board(grid);
	}
	
	/**
	 * Method that specifies all parameterized values.
	 * @return	list containing all parameterized values and expected results.
	 */
	@Parameters
	public static Collection<Object[]> withinBordersData() {
		return Arrays.asList(new Object[][] {
				{  0,  1, true },
				{ -1,  2, false },
				{  5,  3, false },
				{  4,  4, true},
				{  1,  0, true },
				{  2, -1, false },
				{  3,  5, false },
				{  4,  4, true}
		});
	}
	
	/**
	 * The actual test which uses our class variables x and y.
	 */
	@Test
	public void withinBordersTest() {
		assertEquals(out, board.withinBorders(x, y));
	}
}
