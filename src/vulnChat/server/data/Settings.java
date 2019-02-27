package vulnChat.server.data;


/**
 * This class defines objects acting as tuples describing a server's current settings state.
 * As of now, it includes two:
 * 
 * <ul>
 *		<li>check if a new connection has a nickname already in the database</li>
 *		<li>check whether a client is communicating with its original IP address and port number</li>
 *		<li>kick a client when he attempted to perform an attack on the server</li> 	
 * </ul>
 * 
 * @see MutableBoolean
 * @see CheckBoxHolder
 * @author Paul Mabileau
 * @version 0.2
 */
public class Settings {
	public final MutableBoolean	checkNewClientName 		=	new MutableBoolean(false),
								checkClientIPAndPort	=	new MutableBoolean(false),
								kickOnHack				=	new MutableBoolean(false);
}
