package vulnChat.client;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;


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
