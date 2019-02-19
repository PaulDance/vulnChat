package vulnChat.server;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class ClientEntry {
	private final BufferedReader in;
	private final PrintWriter out;
	
	public ClientEntry(BufferedReader in, PrintWriter out) {
		this.in = in;
		this.out = out;
	}
	
	public BufferedReader in() {
		return this.in;
	}
	
	public PrintWriter out() {
		return this.out;
	}
}
