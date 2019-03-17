package vulnChat.data;


/**
 * Defines a type of {@link Action} that enables a client to register itself
 * to a vulnChat {@link Server}. It only uses a nickname.
 * @author Paul Mabileau
 * @version 0.1
 */
public class New extends Action {
	private static final long serialVersionUID = -2358665165424842993L;
	
	/**
	 * Builds a {@link New} communication {@link Action} with the given nickname.
	 * @param chatterName The nickname that will be used to send the {@code New} action with.
	 */
	public New(final String chatterName) {
		super(chatterName);
	}
}
