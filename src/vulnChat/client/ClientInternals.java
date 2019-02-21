package vulnChat.client;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

/**
 * Dummy class simulating a tuple holding the necessary information for a client object to communicate with a chat server
 * in order to enable its use in an anonymous class instance which refuses access to non-final objects.
 * @author Paul Mabileau
 * @version 0.1
 */
public class ClientInternals {
	private Socket clientSocket;
	private BufferedReader fromServerReader;
	private PrintWriter toServerWriter;
	private PrintStream printStream;
	
	public ClientInternals() {
		
	}
	
	public final Socket getClientSocket() {
		return clientSocket;
	}
	
	public final void setClientSocket(Socket clientSocket) throws SocketException {
		this.clientSocket = clientSocket;
	}
	
	public final BufferedReader getFromServerReader() {
		return fromServerReader;
	}
	
	public final void setFromServerReader(BufferedReader fromServerReader) {
		this.fromServerReader = fromServerReader;
	}
	
	public final PrintWriter getToServerWriter() {
		return toServerWriter;
	}
	
	public final void setToServerWriter(PrintWriter toServerWriter) {
		this.toServerWriter = toServerWriter;
	}
	
	public final PrintStream getPrintStream() {
		return printStream;
	}
	
	public final void setPrintStream(PrintStream printStream) {
		this.printStream = printStream;
	}
}
