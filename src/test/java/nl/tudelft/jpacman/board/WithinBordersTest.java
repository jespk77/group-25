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

@RunWith(Parameterized.class)
public class WithinBordersTest {
	int x, y;
	boolean out;
	Board board;
	
	public WithinBordersTest(int x, int y, boolean out) {
		this.x = x;
		this.y = y;
		this.out = out;
	}
	
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
	
	@Parameters
	public static Collection<Object[]> withinBordersData() {
		return Arrays.asList(new Object[][] {
				{  0,  1, true },
				{ -1,  2, false },
				{  5,  3, false },
				{  6,  4, false},
				{  1,  0, true },
				{  2, -1, false },
				{  3,  5, false },
				{  4,  6, false }
		});
	}
	
	@Test
	public void withinBordersTest() {
		assertEquals(out, board.withinBorders(x, y));
	}
}
