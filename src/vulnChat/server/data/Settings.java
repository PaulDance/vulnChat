package vulnChat.server.data;

import vulnChat.data.MutableBoolean;
import vulnChat.data.MutableInteger;

/**
 * This class defines objects acting as tuples describing a server's current settings state.
 * As of now, it includes eight:
 * 
 * <ul>
 *		<li>check if a new connection has a nickname already in the database</li>
 *		<li>check whether a client is communicating with its original IP address and port number</li>
 *		<li>kick a client when he attempted to perform an attack on the server</li>
 *		<li>transmit information with serialized objects</li>
 *		<li>put a limit on the message length the server actually accepts to receive and to retransmit</li>
 *		<li>the number for this limit</li>
 *		<li>check if an already registered client tries to send two "new" actions</li>
 *		<li>when a client is kicked, try to delete everything he left</li>
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
								objTransmit				=	new MutableBoolean(false),
								checkTxtLimit			=	new MutableBoolean(false),		// TODO: implement text size limit
								checkTwoNews			=	new MutableBoolean(false),		// TODO: add two new actions check
								delKickedClient			=	new MutableBoolean(false);		// TODO: make delete kicked client option rule
	
	public final MutableInteger	txtLimit				=	new MutableInteger(1000);
}
