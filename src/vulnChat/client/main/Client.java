package vulnChat.client.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import vulnChat.client.data.ClientInternals;
import vulnChat.client.data.Settings;
import vulnChat.client.display.ChatWindow;
import vulnChat.client.display.ConfigDialog;
import vulnChat.data.Action;
import vulnChat.data.New;


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
	private ClientInternals internals;
	private boolean isRunning = false;
	private final ConfigDialog configDialog;
	public final Settings settings;
	private final ChatWindow chatWindow;
	
	/**
	 * A {@link Client} instance is executable by Java. This starts a new client.
	 * @param args The array of {@link String} of calling arguments
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
		this.settings = new Settings();
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
	 * @param ipAddr The IP address as a {@link String} to connect to 
	 * @param port The port number of the server
	 * @throws IOException
	 */
	public final void connectTo(String ipAddr, int port) throws IOException {
		this.internals.setClientSocket(new Socket(ipAddr, port));
		this.internals.getClientSocket().setKeepAlive(true);
		
		if (this.settings.objTransmit.getValue()) {
			this.internals.setToServerObjectStream(new ObjectOutputStream(this.internals.getClientSocket().getOutputStream()));
			this.internals.setFromServerObjectStream(new ObjectInputStream(this.internals.getClientSocket().getInputStream()));
		} else {
			this.internals.setToServerTextWriter(new PrintWriter(this.internals.getClientSocket().getOutputStream(), true));
			this.internals.setFromServerTextReader(new BufferedReader(new InputStreamReader(this.internals.getClientSocket().getInputStream())));
		}
		this.sendToServer(new New(this.chatterName));
	}
	
	/**
	 * Opens up and starts the main window of the client: the chat window.
	 * @see Client
	 */
	public final void startChatWindow() {
		this.chatWindow.setTitle(this.chatWindow.getTitle() + ": " + this.chatterName);
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
	 * @param state The new state of isRunning
	 */
	public final void setRunning(boolean state) {
		this.isRunning = state;
	}
	
	/**
	 * Closes the client and releases any system resources associated with it.
	 * @throws IOException
	 */
	public final void stop() throws IOException {
		this.internals.getToServerObjectStream().flush();
		this.internals.getToServerObjectStream().close();
		this.internals.getFromServerObjectStream().close();
		this.internals.getClientSocket().close();
	}
	
	/**
	 * @return The {@link ClientInternals} object of this client
	 */
	public final ClientInternals getInternals() {
		return this.internals;
	}
	
	/**
	 * Takes an {@link Action} and decides to send it over the network to the server when in serialized mode,
	 * or deconstruct it and send the corresponding text message to the server.
	 * @param action The {@link Action} to be sent itself or its corresponding {@link String } to the server.
	 * @throws IOException
	 */
	public void sendToServer(Action action) throws IOException {
		if (this.settings.objTransmit.getValue()) {
			this.internals.getToServerObjectStream().writeObject(action);
			this.internals.getToServerObjectStream().flush();
		}
		else {
			this.internals.getToServerTextWriter().println(action.toString());
		}
	}
	
	/**
	 * Sets the client's chatter name to a new value.
	 * @param chatterName The said new value
	 */
	public void setChatterName(String chatterName) {
		this.chatterName = chatterName;
	}
	
	/**
	 * @return The client's chatter name as a {@link String}.
	 */
	public String getChatterName() {
		return this.chatterName;
	}
}
