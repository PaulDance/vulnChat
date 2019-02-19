package vulnChat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ClientWorker implements Runnable {
	private final Server server;
	private final Socket commSocket;
	private boolean isRunning = false;
	
	public ClientWorker(Server server, Socket commSocket) {
		this.server = server;
		this.commSocket = commSocket;
	}
	
	public final void run() {
		String clientMsg;
		BufferedReader inFromConnect = null;
		PrintWriter outToConnect = null;
		this.isRunning = true;
		
		try {
			inFromConnect = new BufferedReader(new InputStreamReader(this.commSocket.getInputStream()));
			outToConnect = new PrintWriter(this.commSocket.getOutputStream(), true);
		} catch (IOException exc) {
			this.server.getPrintStream().println("in or out failed");
			exc.printStackTrace(this.server.getPrintStream());
		}
		
		while (this.server.isRunning() && this.isRunning) {
			try {
				clientMsg = inFromConnect.readLine();
				this.server.getPrintStream().println(this.commSocket.getInetAddress() + ", " + this.commSocket.getPort() + ": " + clientMsg);
				
				if (clientMsg.matches("[a-z]{3}\\s[\\p{Alnum}\\p{Punct}]{1,50}\\s?.{0,1000}")) {		// [\\p{Alnum}\\p{Punct}\\p{Print}]
					String[] elements = clientMsg.split(" ", 3);
					
					if (elements[0].equals("new")) {
						if (!this.server.getClientsMap().containsKey(elements[1])) {
							this.server.getClientsMap().put(elements[1],	new ClientEntry(new BufferedReader(new InputStreamReader(this.commSocket.getInputStream())),
																			new PrintWriter(this.commSocket.getOutputStream(), true)));
							this.distributeMsg(clientMsg, elements[1]);
							this.server.getPrintStream().println("ok new");
						}
						else {
							this.server.getPrintStream().println("not new");
							outToConnect.println("bye");
							inFromConnect.close();
							outToConnect.close();
							this.commSocket.close();
							this.isRunning = false;
						}
					}
					else if (elements[0].equals("bye")) {
						this.server.getClientsMap().get(elements[1]).in().close();
						this.server.getClientsMap().get(elements[1]).out().close();
						this.server.getClientsMap().remove(elements[1]);
						this.distributeMsg(clientMsg, elements[1]);
						this.server.getPrintStream().println("ok bye");
					}
					else if (elements[0].equals("say")) {
						this.distributeMsg(clientMsg, elements[1]);
						this.server.getPrintStream().println("ok say");
					}
					else {
						this.server.getPrintStream().println("not an operation");
					}
				}
				else {
					this.server.getPrintStream().println("not a msg");
				}
			}
			catch (IOException exc) {
				this.isRunning = false;
				
				if (!this.commSocket.isClosed()) {
					this.server.getPrintStream().println("Read failed");
					exc.printStackTrace(this.server.getPrintStream());
				}
			}
		}
		
		try {
			inFromConnect.close();
			outToConnect.close();
		} catch (IOException exc) {
			exc.printStackTrace(this.server.getPrintStream());
		}
	}
	
	private final void distributeMsg(String msg, String sender) {
		for (String client: this.server.getClientsMap().keySet()) {
			if (!client.equals(sender)) {
				this.server.getPrintStream().println(msg + " --> " + client);
				this.server.getClientsMap().get(client).out().println(msg);
			}
		}
	}

	protected final void finalize() throws IOException {
		this.isRunning = false;
		this.commSocket.close();
	}
}
