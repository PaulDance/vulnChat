package vulnChat.data;

import java.io.Serializable;


/**
 * Defines a serializable structure for communication actions. Every action starts by
 * saying who performs it, thus the necessity to provide a nickname to the constructor.
 * 
 * @see #Action(String)
 * @author Paul Mabileau
 * @version 0.1
 */
public abstract class Action implements Serializable {
	private static final long serialVersionUID = -7252157314789058349L;
	/**
	 * The nickname used to send the action with.
	 */
	public final String chatterName;
	
	/**
	 * Builds a new {@link Action} object with a new nickname.
	 * @param chatterName The nickname that will be used for this action
	 */
	public Action(final String chatterName) {
		this.chatterName = chatterName;
	}
	
	@Override
	public abstract String toString();
}
