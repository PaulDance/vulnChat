package vulnChat.client.data;

import java.io.BufferedReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import vulnChat.data.LinePrinter;

/**
 * Dummy class simulating a tuple holding the necessary information for a client object to communicate with a chat server
 * in order to enable its use by other classes creating the vulnChat. Might be deleted in the future.
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
	
	public final Socket getClientSocket() {
		return this.clientSocket;
	}
	
	public final void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	
	public final BufferedReader getFromServerTextReader() {
		return this.fromServerTextReader;
	}

	public final void setFromServerTextReader(BufferedReader fromServerTextReader) {
		this.fromServerTextReader = fromServerTextReader;
	}

	public final PrintWriter getToServerTextWriter() {
		return this.toServerTextWriter;
	}

	public final void setToServerTextWriter(PrintWriter toServerTextWriter) {
		this.toServerTextWriter = toServerTextWriter;
	}

	public final ObjectInputStream getFromServerObjectStream() {
		return this.fromServerObjectStream;
	}
	
	public final void setFromServerObjectStream(ObjectInputStream fromServerObjectStream) {
		this.fromServerObjectStream = fromServerObjectStream;
	}
	
	public final ObjectOutputStream getToServerObjectStream() {
		return this.toServerObjectStream;
	}
	
	public final void setToServerObjectStream(ObjectOutputStream toServerObjectStream) {
		this.toServerObjectStream = toServerObjectStream;
	}
		
	public LinePrinter getLinePrinter() {
		return this.linePrinter;
	}
	
	public final void setLinePrinter(LinePrinter linePrinter) {
		this.linePrinter = linePrinter;
	}
}
