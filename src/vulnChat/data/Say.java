package vulnChat.data;


/**
 * Enables a {@link Client} to send a message to all the others connected to the same
 * {@link Server}. It uses its nickname and a message as a {@link String}. For now,
 * it is completely unprotected, no cipher whatsoever is used, but it will be available.
 * 
 * @author Paul Mabileau
 * @version 0.1
 */
public class Say extends Action {
	private static final long serialVersionUID = -7501355695863402964L;
	public final String message;
	
	/**
	 * Builds a new {@link Say} instance, which allows the transmission of a message.
	 * @param chatterName The nickname use to send the message with
	 * @param message The message that will be sent.
	 */
	public Say(final String chatterName, final String message) {
		super(chatterName);
		this.message = message;
	}
}
