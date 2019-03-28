package vulnChat.data;


/**
 * A mutable wrapper around the native integer type, that is to say a class which
 * defines object having a single attribute: an integer, and that this integer can
 * be later retrieved or modified.
 * 
 * @author Paul Mabileau
 * @version 0.1
 */
public class MutableInteger implements Comparable<MutableInteger> {
	private int integer;
	
	/**
	 * Builds a mutable integer with the given value.
	 * @param integer The value of the integer to be stored in the object
	 */
	public MutableInteger(int integer) {
		this.integer = integer;
	}
	
	/**
	 * Sets a new value for the stored integer.
	 * @param newValue The said new value
	 */
	public void setValue(int newValue) {
		this.integer = newValue;
	}
	
	/**
	 * @return The integer currently stored inside the {@link MutableInteger}.
	 */
	public int getValue() {
		return this.integer;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof MutableInteger && ((MutableInteger) obj).integer == this.integer;
	}

	@Override
	public int compareTo(MutableInteger obj) {
		return this.integer - obj.integer;
	}
	
	/**
	 * @return The string representation of the mutable integer, with the following format:
	 * <code>{int}</code>
	 */
	@Override
	public String toString() {
		return "{" + Integer.toString(this.integer) + "}";
	}
}
