package vulnChat.server.data;

import vulnChat.data.MutableBoolean;

/**
 * This class defines objects acting as tuples describing a server's current settings state.
 * As of now, it includes four:
 * 
 * <ul>
 *		<li>check if a new connection has a nickname already in the database</li>
 *		<li>check whether a client is communicating with its original IP address and port number</li>
 *		<li>kick a client when he attempted to perform an attack on the server</li>
 *		<li>transmit information with serialized objects</li>
 * </ul>
 * 
 * @see MutableBoolean
 * @see CheckBoxHolder
 * @author Paul Mabileau
 * @version 0.3
 */
public class Settings {
	public final MutableBoolean	checkNewClientName 		=	new MutableBoolean(false),
								checkClientIPAndPort	=	new MutableBoolean(false),
								kickOnHack				=	new MutableBoolean(false),
								objTransmit				=	new MutableBoolean(false);
}
