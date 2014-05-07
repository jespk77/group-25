package nl.tudelft.jpacman;

import java.util.List;

import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;

public class Util {
	/**
	 * Test whether a given square has an occupant of the given class.
	 * @param square	the given square
	 * @param c			the given class
	 * @return			true if square contains an instance of class c
	 * 					false otherwise
	 */
	public static boolean contains(Square square, Class<?> c) {
		List<Unit> occupants = square.getOccupants();
		for (Unit unit: occupants) {
			if (c.isInstance(unit)) {
				return true;
			}
		}
		return false;
	}
}
