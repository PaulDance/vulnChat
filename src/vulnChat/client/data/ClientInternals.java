package vulnChat.client.data;

import java.io.BufferedReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import vulnChat.data.LinePrinter;

/**
 * Dummy class simulating a tuple holding the necessary information for a {@link vulnChat.client.main.Client Client}
 * object to communicate with a chat {@link vulnChat.server.main.Server} in order to enable its use by other classes
 * creating the vulnChat. Might be deleted in the future. As of now, it holds six objects which all have the appropriate
 * getters and setters:
 * 
 * <ul>
 *		<li>The {@link Socket} ("{@code clientSocket}") that the client uses to connect to a server.</li>
 *		<li>A {@link BufferedReader} instance ("{@code fromServerTextReader}") which allows reading strings from the network stream.</li>
 *		<li>A {@link PrintWriter} object ("{@code toServerTextWriter}") that enables printing and then sending strings over the network.</li>
 *		<li>An {@link ObjectInputStream} ("{@code fromServerObjectStream}") which enables receiving serialized objects from the network.</li>
 *		<li>An {@link ObjectOutputStream} ("{@code toServerObjectStream}") that allows serializing and then sending objects over the network.</li>
 *		<li>The {@link LinePrinter} ("{@code linePrinter}") that classes may use in order to print strings to the client text area.</li>
 * </ul>
 * 
 * @author Paul Mabileau
 * @version 0.2
 */
public class ClientInternals {
	private Socket clientSocket;
	private BufferedReader fromServerTextReader;
	private PrintWriter toServerTextWriter;
	private ObjectInputStream fromServerObjectStream;
	private ObjectOutputStream toServerObjectStream;
	private LinePrinter linePrinter;
	
	/**
	 * @return The current value of the client's {@link Socket}.
	 * @see ClientInternals
	 */
	public final Socket getClientSocket() {
		return this.clientSocket;
	}
	
	/**
	 * Sets up a new value for the client's {@link Socket}.
	 * @param clientSocket The said new value
	 * @see ClientInternals
	 */
	public final void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	
	/**
	 * @return The current value of the client's {@link BufferedReader}.
	 * @see ClientInternals
	 */
	public final BufferedReader getFromServerTextReader() {
		return this.fromServerTextReader;
	}
	
	/**
	 * Sets up a new value for the client's {@link BufferedReader}.
	 * @param fromServerTextReader The said new value
	 * @see ClientInternals
	 */
	public final void setFromServerTextReader(BufferedReader fromServerTextReader) {
		this.fromServerTextReader = fromServerTextReader;
	}
	
	/**
	 * @return The current value of the client's {@link PrintWriter}.
	 * @see ClientInternals
	 */
	public final PrintWriter getToServerTextWriter() {
		return this.toServerTextWriter;
	}
	
	/**
	 * Sets up a new value for the client's {@link PrintWriter}.
	 * @param toServerTextWriter The said new value
	 * @see ClientInternals
	 */
	public final void setToServerTextWriter(PrintWriter toServerTextWriter) {
		this.toServerTextWriter = toServerTextWriter;
	}
	
	/**
	 * @return The current value of the client's {@link ObjectInputStream}.
	 * @see ClientInternals
	 */
	public final ObjectInputStream getFromServerObjectStream() {
		return this.fromServerObjectStream;
	}
	
	/**
	 * Sets up a new value for the client's {@link ObjectInputStream}.
	 * @param fromServerObjectStream The said new value
	 * @see ClientInternals
	 */
	public final void setFromServerObjectStream(ObjectInputStream fromServerObjectStream) {
		this.fromServerObjectStream = fromServerObjectStream;
	}
	
	/**
	 * @return The current value of the client's {@link ObjectOutputStream}.
	 * @see ClientInternals
	 */
	public final ObjectOutputStream getToServerObjectStream() {
		return this.toServerObjectStream;
	}
	
	/**
	 * Sets up a new value for the client's {@link ObjectOutputStream}.
	 * @param toServerObjectStream The said new value
	 * @see ClientInternals
	 */
	public final void setToServerObjectStream(ObjectOutputStream toServerObjectStream) {
		this.toServerObjectStream = toServerObjectStream;
	}
	
	/**
	 * @return The current value of the client's {@link LinePrinter}.
	 * @see ClientInternals
	 */
	public LinePrinter getLinePrinter() {
		return this.linePrinter;
	}
	
	/**
	 * Sets up a new value for the client's {@link LinePrinter}.
	 * @param linePrinter The said new value
	 * @see ClientInternals
	 */
	public final void setLinePrinter(LinePrinter linePrinter) {
		this.linePrinter = linePrinter;
	}
}
