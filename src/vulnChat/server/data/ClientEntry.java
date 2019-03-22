package vulnChat.server.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;


/**
 * This class defines the core elements as a tuple for a messaging server to communicate with an
 * already connected client. As of now, it involves six things:
 * 
 * <ul>
 *		<li>an {@link InetAdress} to save the client's IP address</li>
 *		<li>a port number to save the client's port number</li>
 *		<li>an input {@link BufferedReader} that enables reading lines of text sent by the client</li>
 *		<li>an output {@link PrintWriter} you can {@code .print()} or {@code .println()} on to send lines to the client over the network</li>
 *		<li>an input {@link ObjectInputStream} that enables reading action objects sent by the client</li>
 *		<li>an output {@link ObjectOutputStream} to send action instances to the client over the network</li>
 * </ul>
 * 
 * @see Client
 * @author Paul Mabileau
 * @version 0.2
 */
public class ClientEntry {
	/**
	 * The {@link InetAddress} identifying the client on the network.
	 */
	public final InetAddress ip;
	/**
	 * The port number the client uses to communicate with the server.
	 */
	public final int port;
	/**
	 * The input {@link BufferedReader} that enables reading lines of text sent by the client.
	 */
	public final BufferedReader txtIn;
	/**
	 * The output {@link PrintWriter} you can {@code .print()} or {@code .println()} on to send lines to the client over the network.
	 */
	public final PrintWriter txtOut;
	/**
	 * The input {@link ObjectInputStream} that enables reading action objects sent by the client.
	 */
	public final ObjectInputStream objIn;
	/**
	 * The output {@link ObjectOutputStream} to send action instances to the client over the network.
	 */
	public final ObjectOutputStream objOut;
		
	/**
	 * Builds the entry tuple describing the client.
	 * 
	 * @param ip The client's {@link InetAddress} identifying it on the network
	 * @param txtIn The {@link BufferedReader} connected to and receiving text lines from the client
	 * @param txtOut The {@link PrintWriter} connected and sending text lines to the client
	 * @param objIn The {@link ObjectInputStream} connected to and receiving objects from the client
	 * @param objOut The {@link ObjectOutputStream} connected and sending objects to the client
	 */
	public ClientEntry(InetAddress ip, int port,
			BufferedReader txtIn, PrintWriter txtOut,
			ObjectInputStream objIn, ObjectOutputStream objOut) {
		this.ip = ip;
		this.port = port;
		this.txtIn = txtIn;
		this.txtOut = txtOut;
		this.objIn = objIn;
		this.objOut = objOut;
	}
	
	/**
	 * Flushes, closes and releases resources associated with streams and writers of this entry. 
	 * @throws IOException
	 */
	public void close(final boolean objTransmit) throws IOException {
		if (objTransmit) {
			this.objIn.close();
			this.objOut.flush();
			this.objOut.close();
		} else {
			this.txtIn.close();
			this.txtOut.flush();
			this.txtOut.close();
		}
	}
}
