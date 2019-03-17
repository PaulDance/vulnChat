package vulnChat.data;


/**
 * Defines a type of {@link Action} that enables a client to unregister itself
 * from a vulnChat {@link Server}. It only uses its nickname.
 * 
 * @author Paul Mabileau
 * @version 0.1
 */
public class Bye extends Action {
	private static final long serialVersionUID = 121438179676644315L;
	
	/**
	 * Builds a {@link Bye} communication {@link Action} with the given nickname.
	 * @param chatterName The nickname that will be used to send the {@code Bye} action with.
	 */
	public Bye(final String chatterName) {
		super(chatterName);
	}
}
