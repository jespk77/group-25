package nl.tudelft.jpacman.group25;

import java.io.IOException;

import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.PacmanConfigurationException;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.MapParser;

/**
 * Extend the JPacman framework by subclassing relevant classes.
 * Creation of the subclass instances requires adjusting the factory,
 * and injecting the relevant instances at the right places.
 */

public class SimpleMap extends Launcher {

	/**
	 * Start the pacman user interface.
	 * @param argv Ignored
	 */
	public static void main(String[] argv) {
		(new SimpleMap()).launch();
	}
	
	/**
	 * Creates a new level. By default this method will use the map parser to
	 * parse the default board stored in the <code>board.txt</code> resource.
	 * 
	 * @return A new level.
	 */
	public Level makeLevel() {
		MapParser parser = getMapParser();
		try {
			return parser.parseMap(Launcher.class
					.getResourceAsStream("/simplemap.txt"));
		} catch (IOException e) {
			throw new PacmanConfigurationException("Unable to create level.", e);
		}
	}
}
