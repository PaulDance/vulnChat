package vulnChat.server.main;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import vulnChat.data.Action;
import vulnChat.server.data.ClientEntry;


/**
 * This {@link Runnable} class implements the receiving from one client only and distributing
 * of its chat messages to all the other clients. It is meant to be constructed for use by a Thread object:
 * the waiting for a new message from the managed client by an instance of this class causes the execution
 * flow to block until a line of text is received from the network.
 * 
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
	 * Constructs a {@link ClientWorker} objects connected to the calling chat {@link Server} and its communication {@link Socket} 
	 * @param server The {@link Server} object that calls this constructor
	 * @param commSocket The {@link Socket} object of the server linked to the newly connected client
	 */
	public ClientWorker(Server server, Socket commSocket) {
		this.server = server;
		this.commSocket = commSocket;
	}
	
	/**
	 * Starts the waiting for a new message from the client and manages it when received then go again.
	 */
	public final void run() {
		String clientMsg = null;
		Action clientAction = null;
		BufferedReader txtInFromConnect = null;
		PrintWriter txtOutToConnect = null;
		ObjectInputStream objInFromConnect = null;
		ObjectOutputStream objOutToConnect = null;
		this.isRunning = true;
		
		try {																// Start the communication elements from and to the client,
			if (this.server.getSettings().objTransmit.getValue()) {
				objInFromConnect = new ObjectInputStream(this.commSocket.getInputStream());
				objOutToConnect = new ObjectOutputStream(this.commSocket.getOutputStream());
			} else {
				txtInFromConnect = new BufferedReader(new InputStreamReader(this.commSocket.getInputStream()));
				txtOutToConnect = new PrintWriter(this.commSocket.getOutputStream(), true);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		while (this.server.isRunning() && this.isRunning) {					// In a loop while authorized to,
			try {															// wait for a message from the client;
				if (server.getSettings().objTransmit.getValue()) {			// If object is expected,
					do {
						try {												// try to read it,
							clientAction = (Action) objInFromConnect.readObject();
						} catch (EOFException exc) {
							try {
								clientAction = null;
								Thread.sleep(10);							// wait if stream is empty;
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					} while (clientAction == null);
				}
				else {
					do {													// otherwise wait for string.
						if (server.getSettings().checkTxtLimit.getValue()) {
							clientMsg = txtInFromConnect.readLine().substring(0, server.getSettings().txtLimit.getValue() - 1);
						} else {
							clientMsg = txtInFromConnect.readLine();
						}
					} while (clientMsg == null);
				}
				
				if (!this.server.getSettings().objTransmit.getValue()) {	// If string was received,
					if (clientMsg != null) {								// and it is non null, report about it;
						this.server.getLinePrinter().println(this.commSocket.getInetAddress() + ", " + this.commSocket.getPort() + ": " + clientMsg);
					}
					else {													// else stop the connection;
						this.server.getLinePrinter().println("connection ended to: " + this.commSocket.getInetAddress());
						this.kick(txtInFromConnect, txtOutToConnect, objInFromConnect, objOutToConnect);
						return;
					}
				}
				else {
					if (clientAction != null) {								// otherwise, if action object is non null, report about it.
						this.server.getLinePrinter().println(this.commSocket.getInetAddress() + ", " + this.commSocket.getPort() + ": " + clientAction.toString());
					}
				}
				
				if (this.server.getSettings().objTransmit.getValue()) {		// If object was received,
					clientMsg = clientAction.toString();					// transform it to string to avoid duplicating tests.
				}
				
				if (clientMsg.matches("[a-z]{3}\\s[\\p{Alnum}\\p{Punct}]{1,50}\\s?.{0,1000}")) {
					final String[] elements = clientMsg.split(" ", 3);		// if the received message is in the correct format (action, [chatterName, [message]]), then parse it:
					final ClientEntry clientEntry = this.server.getClientsMap().get(elements[1]);
					
					if (clientEntry == null || !this.server.getSettings().checkClientIPAndPort.getValue()
					|| (clientEntry.ip.equals(this.commSocket.getInetAddress()) && clientEntry.port == this.commSocket.getPort())) {
						if (elements[0].equals("new")) {					// "new": adds a newly connected client to the database and enables communication to others,
							if (!(clientEntry != null && this.server.getSettings().checkNewClientName.getValue())) {
								this.server.getClientsMap().put(elements[1], 	new ClientEntry(this.commSocket.getInetAddress(),
																				this.commSocket.getPort(),
																				txtInFromConnect, txtOutToConnect,
																				objInFromConnect, objOutToConnect));
								this.distribute(clientAction, clientMsg, elements[1]);
								this.server.getLinePrinter().println("ok new");
							}
							else {											// but if the client lied and the server does not permit it,
								this.server.getLinePrinter().println("not new");
								this.kickIfOn(txtInFromConnect, txtOutToConnect, objInFromConnect, objOutToConnect);	// kick him in the butt;
							}
						}
						else if (elements[0].equals("bye")) {				// "bye": terminates the connection;
							if (clientEntry != null) {
								clientEntry.close(this.server.getSettings().objTransmit.getValue());
							}
							this.server.getClientsMap().remove(elements[1]);
							this.distribute(clientAction, clientMsg, elements[1]);
							this.server.getLinePrinter().println("ok bye");
						}
						else if (elements[0].equals("say")) {				// "say": sends a message on the server for all the clients to receive;
							this.distribute(clientAction, clientMsg, elements[1]);
							this.server.getLinePrinter().println("ok say");
						}
						else {												// "$!?%$£": nothing interesting;
							this.server.getLinePrinter().println("not an operation");
							this.kickIfOn(txtInFromConnect, txtOutToConnect, objInFromConnect, objOutToConnect);
						}
					}
					else {
						this.server.getLinePrinter().println("wrong IP or port");	// wrong IP or port number: ignore
						this.kickIfOn(txtInFromConnect, txtOutToConnect, objInFromConnect, objOutToConnect);	// and kick if activated by the server;
					}
				}
				else {
					this.server.getLinePrinter().println("not a msg");		// wrong format: ignore
					this.kickIfOn(txtInFromConnect, txtOutToConnect, objInFromConnect, objOutToConnect);		// and kick if activated by the server.
				}
			}
			catch (ClassNotFoundException e) {}								// If unknown class received, ignore;
			catch (IOException e) {											// In case of major problem,
				this.isRunning = false;										// stop everything
				
				if (!this.commSocket.isClosed()) {							// and report.
					this.server.getLinePrinter().println("Read failed");
					e.printStackTrace();
				}
			}
		}
		
		try {
			this.kick(txtInFromConnect, txtOutToConnect, objInFromConnect, objOutToConnect);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method is meant to be used in a general manner, hence the doubling of parameters. It evaluates
	 * the current value of the server serialization mode setting in order to call the corresponding
	 * distribute method: {@link #distributeAction(Action)} when serialized, otherwise {@link #distributeMsg(String, String)}.
	 * 
	 * @param action The {@link Action} to send over the network to all the other connected clients, when in serialized mode.
	 * @param msg The message to distribute, when in text mode.
	 * @param sender The sender of the message, when in text mode.
	 * @throws IOException
	 */
	private final void distribute(final Action action, final String msg, final String sender) throws IOException {
		if (this.server.getSettings().objTransmit.getValue()) {
			this.distributeAction(action);
		}
		else {
			this.distributeMsg(msg, sender);
		}
	}
	
	/**
	 * Distributes an {@link Action} to all the connected clients, except the sender (included in the action).
	 * @param action The {@code Action} to send over the network to all the clients.
	 * @throws IOException
	 */
	private final void distributeAction(final Action action) throws IOException {
		for (String client: this.server.getClientsMap().keySet()) {				// For all the clients of the server,
			if (!client.equals(action.chatterName)) {							// except the <sender>,
				this.server.getClientsMap().get(client).objOut.writeObject(action);
				this.server.getLinePrinter().println(action.toString() + " --> " + client);	// and report that on the server's console.
			}
		}
	}
	
	/**
	 * Distributes a text message to all the clients connected to the server, except the sender.
	 * @param msg The message as a {@link String} meant to be sent over the network
	 * @param sender The chatter nickname as a {@link String} of the sending client
	 * @throws IOException 
	 */
	private final void distributeMsg(final String msg, final String sender) throws IOException {
		for (String client: this.server.getClientsMap().keySet()) {				// For all the clients of the server,
			if (!client.equals(sender)) {										// except the <sender>,
				this.server.getClientsMap().get(client).txtOut.println(msg);	// send them the <msg>
				this.server.getLinePrinter().println(msg + " --> " + client);	// and report that on the server's console.
			}
		}
	}
	
	/**
	 * Kicks a client from the server if the corresponding setting is activated. This method is meant
	 * to be used in a general manner: whether the server is in serialized mode or not, hence the doubling
	 * of parameters.
	 * 
	 * @param txtInFromConnect The {@link BufferedReader} connected to the {@link Client} when in text mode.
	 * @param txtOutToConnect The {@link PrintWriter} connected to the {@link Client} when in text mode.
	 * @param objInFromConnect The {@link ObjectInputStream} connected to the {@link Client} when in serialized mode.
	 * @param objOutToConnect The {@link ObjectOutputStream} connected to the {@link Client} when in serialized mode.
	 * @throws IOException
	 * @see {@link #kick(BufferedReader, PrintWriter, ObjectInputStream, ObjectOutputStream)}
	 */
	private final void kickIfOn(final BufferedReader txtInFromConnect, final PrintWriter txtOutToConnect,
			final ObjectInputStream objInFromConnect, final ObjectOutputStream objOutToConnect) throws IOException {
		if (this.server.getSettings().kickOnHack.getValue()) {
			this.kick(txtInFromConnect, txtOutToConnect, objInFromConnect, objOutToConnect);
		}
	}
	
	/**
	 * Kicks a client from the server whether the corresponding setting is activated or not. This method is meant
	 * to be used in a general manner: whether the server is in serialized mode or not, hence the doubling
	 * of parameters.
	 * 
	 * @param txtInFromConnect The {@link BufferedReader} connected to the {@link Client} when in text mode.
	 * @param txtOutToConnect The {@link PrintWriter} connected to the {@link Client} when in text mode.
	 * @param objInFromConnect The {@link ObjectInputStream} connected to the {@link Client} when in serialized mode.
	 * @param objOutToConnect The {@link ObjectOutputStream} connected to the {@link Client} when in serialized mode.
	 * @throws IOException
	 */
	private final void kick(final BufferedReader txtInFromConnect, final PrintWriter txtOutToConnect,
			final ObjectInputStream objInFromConnect, final ObjectOutputStream objOutToConnect) throws IOException {
		if (this.server.getSettings().objTransmit.getValue()) {
			objInFromConnect.close();
			objOutToConnect.flush();
			objOutToConnect.close();
		} else {
			txtInFromConnect.close();
			txtOutToConnect.flush();
			txtOutToConnect.close();
		}
		this.finalize();
	}
	
	protected final void finalize() throws IOException {
		this.isRunning = false;
		this.commSocket.close();
	}
}
