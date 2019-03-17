package vulnChat.client.data;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import vulnChat.data.LinePrinter;

/**
 * Dummy class simulating a tuple holding the necessary information for a client object to communicate with a chat server
 * in order to enable its use in an anonymous class instance which refuses access to non-final objects.
 * @author Paul Mabileau
 * @version 0.1
 */
public class ClientInternals {
	private Socket clientSocket;
	private ObjectInputStream fromServerStream;
	private ObjectOutputStream toServerStream;
	private LinePrinter linePrinter;
	
	public ClientInternals() {
		
	}
	
	public final Socket getClientSocket() {
		return this.clientSocket;
	}
	
	public final void setClientSocket(Socket clientSocket) throws SocketException {
		this.clientSocket = clientSocket;
	}
	
	public final ObjectInputStream getFromServerStream() {
		return this.fromServerStream;
	}
	
	public final void setFromServerStream(ObjectInputStream fromServerStream) {
		this.fromServerStream = fromServerStream;
	}
	
	public final ObjectOutputStream getToServerStream() {
		return this.toServerStream;
	}
	
	public final void setToServerStream(ObjectOutputStream toServerStream) {
		this.toServerStream = toServerStream;
	}
		
	public LinePrinter getLinePrinter() {
		return this.linePrinter;
	}
	
	public final void setLinePrinter(LinePrinter linePrinter) {
		this.linePrinter = linePrinter;
	}
}
