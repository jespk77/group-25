package nl.tudelft.jpacman.group25.npc.ghost;

import java.util.LinkedList;

import nl.tudelft.jpacman.npc.ghost.Ghost;
import nl.tudelft.jpacman.npc.ghost.GhostFactory;
import nl.tudelft.jpacman.sprite.PacManSprites;

/**
 * Factory that creates ghosts.
 * 
 * @author Jeroen Roosen 
 */
public class CustomGhostFactory extends GhostFactory {
	private LinkedList<Ghost> blinkies;
	
	/**
	 * Creates a new ghost factory.
	 * 
	 * @param spriteStore The sprite provider.
	 */
	public CustomGhostFactory(PacManSprites spriteStore) {
		super(spriteStore);
		blinkies = new LinkedList<Ghost>();
	}

	/**
	 * Creates a new Blinky / Shadow, the red Ghost.
	 * @return A new Blinky.
	 */
	@Override
	public Ghost createBlinky() {
		blinkies.push(super.createBlinky()); 
		return blinkies.getLast();
	}
	
	/**
	 * Returns the Blinky that was created last.
	 * 
	 * @return An existing Blinky
	 */
	public Ghost popBlinky() {
		return blinkies.pop();
	}
}
