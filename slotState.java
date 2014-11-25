package Minor.P3.DS; 
 
/**
 * Slot state
 * @author Joe
 *
 */
public enum slotState {
	
	/**
	 * A cell is empty if is has nothing stored in it
	 */
	EMPTY, 
	
	/**
	 * A cell is full if it has something in it
	 */
	FULL, 
	
	/**
	 * A cell is a tombstone if it is available for insertion
	 */
	TOMBSTONE;
	}