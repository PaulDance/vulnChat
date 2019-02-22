package vulnChat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * This {@linkplain Runnable} class implements the receiving from one client only and distributing
 * of its chat messages to all the other clients. It is meant to be constructed for use by a Thread object:
 * the waiting for a new message from the managed client by an instance of this class causes the execution
 * flow to block until a line of text is received from the network.
 * @see Thread
 * @see Server
 * @author Paul Mabileau
 * @version 0.1
 */
public class ClientWorker implements Runnable {
	private final Server server;
	private final Socket commSocket;
	private boolean isRunning = false;
	
	/**
	 * Constructs a {@linkplain ClientWorker} objects connected to the calling chat {@linkplain Server} and its communication {@linkplain Socket} 
	 * @param server - the {@linkplain Server} object that calls this constructor
	 * @param commSocket - the {@linkplain Socket} object of the server linked to the newly connected client
	 */
	public ClientWorker(Server server, Socket commSocket) {
		this.server = server;
		this.commSocket = commSocket;
	}
	
	/**
	 * Starts the waiting for a new message from the client and manages it when received then go again.
	 */
	public final void run() {
		String clientMsg;
		BufferedReader inFromConnect = null;
		PrintWriter outToConnect = null;
		this.isRunning = true;
		
		try {																// Start the communication elements from and to the client,
			inFromConnect = new BufferedReader(new InputStreamReader(this.commSocket.getInputStream()));
			outToConnect = new PrintWriter(this.commSocket.getOutputStream(), true);
		} catch (IOException exc) {
			this.server.getPrintStream().println("in or out failed");		// report if failed.
			exc.printStackTrace(this.server.getPrintStream());
		}
		
		while (this.server.isRunning() && this.isRunning) {					// In a loop while authorized to,
			try {
				clientMsg = inFromConnect.readLine();						// wait for a message from the client;
				this.server.getPrintStream().println(this.commSocket.getInetAddress() + ", " + this.commSocket.getPort() + ": " + clientMsg);
				
				if (clientMsg.matches("[a-z]{3}\\s[\\p{Alnum}\\p{Punct}]{1,50}\\s?.{0,1000}")) {
					String[] elements = clientMsg.split(" ", 3);			// if the received message is in the correct format (action, [chatterName, [message]]), then parse it:
					
					if (elements[0].equals("new")) {						// "new": adds a newly connected client to the database and enables communication to others,
						if (!this.server.getClientsMap().containsKey(elements[1])) {
							this.server.getClientsMap().put(elements[1],	new ClientEntry(this.commSocket.getInetAddress(),
																			new BufferedReader(new InputStreamReader(this.commSocket.getInputStream())),
																			new PrintWriter(this.commSocket.getOutputStream(), true)));
							this.distributeMsg(clientMsg, elements[1]);
							this.server.getPrintStream().println("ok new");
						}
						else {												// but if the client lied,
							this.server.getPrintStream().println("not new");
							outToConnect.println("bye");					// kick him in the butt;
							inFromConnect.close();
							outToConnect.close();
							this.commSocket.close();
							this.isRunning = false;
						}
					}
					else if (elements[0].equals("bye")) {					// "bye": terminates the connection;
						this.server.getClientsMap().get(elements[1]).in.close();
						this.server.getClientsMap().get(elements[1]).out.close();
						this.server.getClientsMap().remove(elements[1]);
						this.distributeMsg(clientMsg, elements[1]);
						this.server.getPrintStream().println("ok bye");
					}
					else if (elements[0].equals("say")) {					// "say": sends a message on the server for all the clients to receive;
						this.distributeMsg(clientMsg, elements[1]);
						this.server.getPrintStream().println("ok say");
					}
					else {													// "$!?%$£": nothing interesting;
						this.server.getPrintStream().println("not an operation");
					}
				}
				else {														// wrong format: ignore.
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
	
	/**
	 * Distributes a message to all the clients connected to the server, except the sender.
	 * @param msg - the message as a {@linkplain String} meant to be sent over the network
	 * @param sender - the chatter nickname as a {@linkplain String} of the sending client
	 */
	private final void distributeMsg(String msg, String sender) {
		for (String client: this.server.getClientsMap().keySet()) {					// For all the clients of the server,
			if (!client.equals(sender)) {											// except the <sender>,
				this.server.getClientsMap().get(client).out.println(msg);			// send them the <msg>
				this.server.getPrintStream().println(msg + " --> " + client);		// and report that on the server's console.
			}
		}
	}

	protected final void finalize() throws IOException {
		this.isRunning = false;
		this.commSocket.close();
	}
}
