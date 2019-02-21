package vulnChat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Client {
	private String chatterName = "";
	private final ClientInternals internals;
	private boolean isRunning = false;
	private final ConfigDialog configDialog;
	private final ChatWindow chatWindow;
	
	public static void main(String[] args) throws IOException {
		(new Client()).start();
	}
	
	public Client() throws IOException {
		this.internals = new ClientInternals();
		this.configDialog = new ConfigDialog(this, "Configuration", "127.0.0.1", "4321");
		this.chatWindow = new ChatWindow(this, "vulnChat");
	}
	
	public final void start() {
		this.configDialog.start();
	}
	
	protected final void connectTo(String ipAddr, int port) throws IOException {
		this.internals.setClientSocket(new Socket(ipAddr, port));
		this.internals.getClientSocket().setKeepAlive(true);
		this.internals.setFromServerReader(new BufferedReader(new InputStreamReader(this.internals.getClientSocket().getInputStream())));
		this.internals.setToServerWriter(new PrintWriter(this.internals.getClientSocket().getOutputStream(), true));
		this.internals.getToServerWriter().println("new " + this.chatterName + "\n");
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
	
	protected final void stop() throws IOException {
		this.internals.getToServerWriter().flush();
		this.internals.getToServerWriter().close();
		this.internals.getFromServerReader().close();
		this.internals.getClientSocket().close();
	}
	
	/**
	 * @return The ClientInternals object of this client
	 * @see ClientInternals
	 */
	protected final ClientInternals getInternals() {
		return this.internals;
	}
	
	protected void setChatterName(String chatterName) {
		this.chatterName = chatterName;
	}
	
	protected String getChatterName() {
		return this.chatterName;
	}
}
