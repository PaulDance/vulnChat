package vulnChat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * <p>This class creates the client side of a vulnerable chat messaging service. When executed, it starts by
 * opening a configuration dialog asking for the server information and the user's nickname for the session.
 * When this is done, it then opens a window that enables the user to send messages to all the other
 * clients connected to the server and receive from them too.</p><br>
 * <p>For now there are also a few other minor functionalities associated to the chat: user joined and user
 * left the channel messages when it happens. The vulnerability level management is yet to come.
 * @author Paul Mabileau
 * @version 0.1
 * @see ConfigDialog
 * @see ChatWindow
 * @see Server
 */
public class Client {
	private String chatterName = "";
	private final ClientInternals internals;
	private boolean isRunning = false;
	private final ConfigDialog configDialog;
	private final ChatWindow chatWindow;
	
	/**
	 * A {@link Client} instance is executable by Java. This starts a new client.
	 * @param args - the array of {@link String} of calling arguments
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		(new Client()).start();
	}
	
	/**
	 * Builds the internal elements of a {@link Client} object: a {@link ClientInternals} instance,
	 * a {@link ConfigDialog} and a {@link ChatWindow}.
	 * @throws IOException
	 */
	public Client() throws IOException {
		this.internals = new ClientInternals();
		this.configDialog = new ConfigDialog(this, "Configuration", "127.0.0.1", "4321");
		this.chatWindow = new ChatWindow(this, "vulnChat");
	}
	
	/**
	 * Starts the client by first opening a configuration window.
	 */
	public final void start() {
		this.configDialog.start();
	}
	
	/**
	 * Sets up the {@link Client} to connect to a new chat server.
	 * @param ipAddr - the IP address as a {@link String} to connect to 
	 * @param port - the port number of the server
	 * @throws IOException
	 */
	protected final void connectTo(String ipAddr, int port) throws IOException {
		this.internals.setClientSocket(new Socket(ipAddr, port));
		this.internals.getClientSocket().setKeepAlive(true);
		this.internals.setFromServerReader(new BufferedReader(new InputStreamReader(this.internals.getClientSocket().getInputStream())));
		this.internals.setToServerWriter(new PrintWriter(this.internals.getClientSocket().getOutputStream(), true));
		this.internals.getToServerWriter().println("new " + this.chatterName);
	}
	
	protected final void startChatWindow() {
		this.chatWindow.start();
	}
	
	/**
	 * @return Whether this client is running or not
	 */
	public final boolean isRunning() {
		return this.isRunning;
	}
	
	/**
	 * Sets isRunning
	 * @param state - the new state of isRunning
	 */
	protected final void setRunning(boolean state) {
		this.isRunning = state;
	}
	
	/**
	 * Closes the client and releases any system resources associated with it.
	 * @throws IOException
	 */
	protected final void stop() throws IOException {
		this.internals.getToServerWriter().flush();
		this.internals.getToServerWriter().close();
		this.internals.getFromServerReader().close();
		this.internals.getClientSocket().close();
	}
	
	/**
	 * @return The {@link ClientInternals} object of this client
	 */
	protected final ClientInternals getInternals() {
		return this.internals;
	}
	
	/**
	 * Sets the client's chatter name to a new value.
	 * @param chatterName - the said new value
	 */
	protected void setChatterName(String chatterName) {
		this.chatterName = chatterName;
	}
	
	/**
	 * @return The client's chatter name as a {@link String}.
	 */
	protected String getChatterName() {
		return this.chatterName;
	}
}
