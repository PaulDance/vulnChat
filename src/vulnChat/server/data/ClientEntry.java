package vulnChat.server.data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;


/**
 * This class defines the core elements as a tuple for a messaging server to communicate with an
 * already connected client. It involves four things:
 * 
 * <ul>
 *		<li>an {@link InetAdress} to save the client's IP address</li>
 *		<li>a port number to save the client's port number</li>
 *		<li>an input {@link ObjectInputStream} that enables reading messages lines sent by the client</li>
 *		<li>an output {@link ObjectOutputStream} you can {@code .print()} or {@code .println()} on to send lines to the client over the network</li>
 * </ul>
 * 
 * @see Client
 * @author Paul Mabileau
 * @version 0.1
 */
public class ClientEntry {
	public final InetAddress ip;
	public final int port;
//	public final BufferedReader in;
//	public final PrintWriter out;
	public final ObjectInputStream in;
	public final ObjectOutputStream out;
		
	/**
	 * Builds the entry tuple describing the client.
	 * 
	 * @param ip The client's {@link InetAddress} identifying it on the network
	 * @param in The {@link ObjectInputStream} connected to and receiving from the client
	 * @param out The {@link ObjectOutputStream} connected and sending to the client
	 */
	public ClientEntry(InetAddress ip, int port, ObjectInputStream in, ObjectOutputStream out) {
		this.ip = ip;
		this.port = port;
		this.in = in;
		this.out = out;
	}
	
	/**
	 * Flushes, closes and releases resources associated with streams and writers of this entry. 
	 * @throws IOException
	 */
	public void close() throws IOException {
		this.in.close();
		this.out.flush();
		this.out.close();
	}
}
