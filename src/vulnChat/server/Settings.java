package vulnChat.server;


/**
 * This class defines objects acting as tuples describing a server's current settings state.
 * As of now, it includes two:
 * 
 * <ul>
 * 		<li>check if a new connection has a nickname already in the database</li>
 *		<li>check whether a client is communicating with its original IP address</li>
 * </ul>
 * 
 * @author Paul Mabileau
 * @version 0.1
 */
public class Settings {
	public boolean	checkNewClientName 	=	false,
					checkClientIP		=	false;
}
