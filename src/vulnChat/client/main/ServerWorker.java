package vulnChat.client.main;

import java.io.IOException;


/**
 * This class, implementing {@link Runnable}, manages the chat messages server connected to a client.
 * It is meant to be done so in the background by constructing and starting a {@link Thread} object
 * of it.
 * @author Paul Mabileau
 * @version 0.1
 */
public class ServerWorker implements Runnable {
	private final Client client;
	private boolean isRunning;
	
	/**
	 * Default constructor: saves the given {@link Client} object for later use.
	 * @param client - the {@link Client} object that calls this constructor
	 */
	public ServerWorker(Client client) {
		this.client = client;
	}
	
	/**
	 * Starts the background task of waiting for messages from the server and executing them for the associated client.
	 */
	public final void run() {
		String serverMsg;
		this.isRunning = true;
		
		while (this.client.isRunning() && this.isRunning) {			// While authorized to,
			try {
				do {												// wait for a message from the server;
					serverMsg = client.getInternals().getFromServerStream().readUTF();
				} while (serverMsg == null);						// if it respect the expected format,
				
				if (serverMsg.matches("[a-z]{3}\\s[\\p{Alnum}\\p{Punct}]{1,50}\\s?.{0,1000}")) {
					String[] elements = serverMsg.split(" ", 3);	// separate the three elements of the message,
					
					if (elements[0].equals("new")) {				// "new" action -> a new user joind the channel,
						this.client.getInternals().getPrintStream().println(elements[1] + " joined the channel.");
					}
					else if (elements[0].equals("bye")) {			// "bye" action -> a user left the channel,
						this.client.getInternals().getPrintStream().println(elements[1] + " left the channel.");
					}
					else if (elements[0].equals("say")) {			// "say" action -> someone said something (probably useless, like always).
						this.client.getInternals().getPrintStream().println(elements[1] + ": " + elements[2]);
					}
				}
			}
			catch (IOException exc) {								// If there is a problem at any point, close everything and report.
				this.isRunning = false;
				
				if (!this.client.getInternals().getClientSocket().isClosed()) {
					this.client.getInternals().getPrintStream().println("Read failed");
					//exc.printStackTrace(this.client.getInternals().getPrintStream());
					exc.printStackTrace();
				}
			}
		}
	}
	
	protected final void finalize() {
		this.isRunning = false;
	}
}
