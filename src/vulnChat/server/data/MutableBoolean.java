package vulnChat.server.data;


/**
 * A mutable wrapper around the native boolean type, that is to say a class which
 * defines object having a single attribute: a boolean, and that this boolean can
 * be later retrieved or modified.
 * 
 * @see Settings
 * @see CheckBoxHolder
 * @author Paul Mabileau
 * @version 0.1
 */
public class MutableBoolean implements Comparable<MutableBoolean> {
	private boolean bool;
	
	/**
	 * Builds a {@link MutableBoolean} instance with the default value of false.
	 * @see #MutableBoolean(boolean)
	 */
	public MutableBoolean() {
		this(false);
	}
	
	/**
	 * Constructs a {@link MutableBoolean} instance with the given boolean value.
	 * @param bool The value for the inner boolean field to have.
	 * @see #MutableBoolean()
	 */
	public MutableBoolean(boolean bool) {
		this.bool = bool;
	}
	
	/**
	 * Resets the value to the given boolean
	 * @param bool The value for the inner boolean field to have.
	 */
	public void setValue(boolean bool) {
		this.bool = bool;
	}
	
	/**
	 * @return The value the inner boolean field has.
	 */
	public boolean getValue() {
		return this.bool;
	}
	
	/**
	 * @param obj The object to test the equality on with the {@link MutableBoolean} instance
	 * @return {@code true} if the two {@link MutableBoolean} instances have the same inner boolean field values, {@code false} otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		return obj instanceof MutableBoolean && this.bool == ((MutableBoolean) obj).bool;
	}

	/**
	 * @param mutableBool The {@link MutableBoolean} to compare to
	 * @return {@code 0} if the two {@link MutableBoolean} instances are equal,
	 * {@code +1} if {@code this} has value {@code true} and mutableBool has
	 * value {@code false}, {@code -1} otherwise
	 * @see #equals(Object)
	 */
	@Override
	public int compareTo(MutableBoolean mutableBool) {
		if (this.bool == mutableBool.bool) {
			return 0;
		}
		else if (this.bool == true && mutableBool.bool == false) {
			return 1;
		}
		else if (this.bool == false && mutableBool.bool == true) {
			return -1;
		}
		else {
			return 0;
		}
	}
	
	/**
	 * @return A string representation of {@code this} {@link MutableBoolean} object, with the following correspondance:
	 * <ul>
	 *		<li>"{true}" if the value is {@code true}</li>
	 *		<li>"{false}" otherwise</li>
	 * </ul>
	 */
	@Override
	public String toString() {
		if (this.bool) {
			return "{true}";
		}
		else {
			return "{false}";
		}
	}
}
