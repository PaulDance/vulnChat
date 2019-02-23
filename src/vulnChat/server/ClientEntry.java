package vulnChat.server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.InetAddress;


/**
 * This class defines the core elements as a tuple for a messaging server to communicate with an
 * already connected client. It involves three things: an input {@link BufferedReader}
 * that enables reading messages lines sent by the client, an output {@link PrintWriter}
 * you can {@code .print()} or {@code .println()} on to send lines to the client over the network,
 * and finally an {@link InetAdress} to save the client's IP address.
 * @see Client
 * @author Paul Mabileau
 * @version 0.1
 */
public class ClientEntry {
	public final BufferedReader in;
	public final PrintWriter out;
	public final InetAddress ip;
	
	/**
	 * Builds the entry tuple describing the client.
	 * @param ip - the client's {@link InetAddress} identifying it on the network
	 * @param in - the {@link BufferedReader} connected to and receiving from the client
	 * @param out - the {@link PrintWriter} connected and sending to the client
	 */
	public ClientEntry(InetAddress ip, BufferedReader in, PrintWriter out) {
		this.ip = ip;
		this.in = in;
		this.out = out;
	}
}
