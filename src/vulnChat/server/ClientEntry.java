package vulnChat.server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.InetAddress;

public class ClientEntry {
	public final BufferedReader in;
	public final PrintWriter out;
	public final InetAddress ip;
	
	public ClientEntry(InetAddress ip, BufferedReader in, PrintWriter out) {
		this.ip = ip;
		this.in = in;
		this.out = out;
	}
}
