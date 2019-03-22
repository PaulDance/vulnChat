package vulnChat.client.main;

import java.io.EOFException;
import java.io.IOException;

import vulnChat.data.Action;
import vulnChat.data.Bye;
import vulnChat.data.New;
import vulnChat.data.Say;


/**
 * This class, implementing {@link Runnable}, manages the chat messages server connected to a client.
 * It is meant to be done so in the background by constructing and starting a {@link Thread} object
 * of it.
 * 
 * @author Paul Mabileau
 * @version 0.3
 */
public class ServerWorker implements Runnable {
	private final Client client;
	private boolean isRunning;
	
	/**
	 * Default constructor: saves the given {@link Client} object for later use.
	 * @param client The {@link Client} object that calls this constructor
	 */
	public ServerWorker(Client client) {
		this.client = client;
	}
	
	/**
	 * Starts the background task of waiting for messages from the server and executing them for the associated client.
	 */
	public final void run() {
		String serverMsg = null;
		Action serverAction = null;
		this.isRunning = true;
		
		while (this.client.isRunning() && this.isRunning) {			// While authorized to,
			try {													// wait for a message from the server;
				if (client.settings.objTransmit.getValue()) {		// If an object is to be expected,
					do {
						try {										// read it from the stream,
							serverAction = (Action) client.getInternals().getFromServerObjectStream().readObject();
						}
						catch (EOFException exc) {
							try {
								serverAction = null;
								Thread.sleep(10);					// or wait for it if the stream has reached its end;
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					} while (serverAction == null);
				}
				else {
					do {											// otherwise wait for a line of text.
						serverMsg = client.getInternals().getFromServerTextReader().readLine();
					} while (serverMsg == null);
				}
				
				if (client.settings.objTransmit.getValue()) {		// If an object has been received,
					if (serverAction instanceof New) {				// parse it and print its result;
						this.client.getInternals().getLinePrinter().println(serverAction.chatterName + " joined the channel.");
					}
					else if (serverAction instanceof Bye) {
						this.client.getInternals().getLinePrinter().println(serverAction.chatterName + " left the channel.");
					}
					else if (serverAction instanceof Say) {
						final Say actionSay = (Say) serverAction; 
						this.client.getInternals().getLinePrinter().println(actionSay.chatterName + ": " + actionSay.message);
					}
				}													// if text has been read and it respects the expected format,
				else if (serverMsg.matches("[a-z]{3}\\s[\\p{Alnum}\\p{Punct}]{1,50}\\s?.{0,1000}")) {
					String[] elements = serverMsg.split(" ", 3);	// separate the three elements of the message,
					
					if (elements[0].equals("new")) {				// "new" action -> a new user joind the channel,
						this.client.getInternals().getLinePrinter().println(elements[1] + " joined the channel.");
					}
					else if (elements[0].equals("bye")) {			// "bye" action -> a user left the channel,
						this.client.getInternals().getLinePrinter().println(elements[1] + " left the channel.");
					}
					else if (elements[0].equals("say")) {			// "say" action -> someone said something (probably useless, like always).
						this.client.getInternals().getLinePrinter().println(elements[1] + ": " + elements[2]);
					}
				}
			}
			catch (ClassNotFoundException exc) {}					// If the object is not from a known class, ignore.
			catch (IOException exc) {								// If there is a major problem at any point, close everything and report.
				this.isRunning = false;
				
				if (!this.client.getInternals().getClientSocket().isClosed()) {
					this.client.getInternals().getLinePrinter().println("Read failed");
					exc.printStackTrace();
				}
			}
		}
	}
	
	protected final void finalize() {
		this.isRunning = false;
	}
}
