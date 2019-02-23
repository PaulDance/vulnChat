package vulnChat.server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;


/**
 * This class starts and manages a vulnerable chat messaging server. It starts by opening a configuration
 * dialog to ask the user for the port to start listening on. Then, it will display information about the
 * current connections, chat messages, input, output, transmissions, etc... on a small white-on-black console.
 * @see Console
 * @see vulnChat.client.Client Client
 * @author Paul Mabileau
 * @version 0.1
 */
public class Server {
	private final ServerSocket connSocket;
	private final HashMap<String, ClientEntry> clientsMap;
	private final Console console;
	private boolean isRunning = false;
	
	public static void main(String[] args) throws IOException {
		(new PortDialog("Configuration", "4321")).start();
	}
	
	/**
	 * Initializes a server on default port 4321.
	 * @throws IOException
	 */
	public Server() throws IOException {
		this(4321);
	}
	
	/**
	 * Initializes a server on given port.
	 * @param port - the network port the server will start listening on for chat messages.
	 * @throws IOException
	 */
	public Server(int port) throws IOException {
		this.connSocket = new ServerSocket(port);								// Start the server socket on <port>,
		this.clientsMap = new HashMap<String, ClientEntry>();					// create an empty hash map to store connected clients,
		this.console = new Console("Server console", 20, 90);					// and construct a server console.
	}
	
	/**
	 * Starts the server and its console on a new Thread to avoid breaking the eventual code flow you may have.
	 * @throws IOException
	 */
	public void start() throws IOException {
		(new Thread(new Runnable() {
			@Override public void run() {
				Server.this.isRunning = true;
				Server.this.console.start();
				
				try (final DatagramSocket probeSocket = new DatagramSocket()) {
					  probeSocket.connect(InetAddress.getByName("8.8.8.8"), 10002);		// Trick to get the IP address the computer actually has on the network.
					  Server.this.console.printStream.println("Server started on " + probeSocket.getLocalAddress().getHostAddress() + " port " + Server.this.connSocket.getLocalPort());
				} catch (UnknownHostException | SocketException e) {
					e.printStackTrace(Server.this.getPrintStream());
				}
				
				while (Server.this.console.isEnabled() && Server.this.isRunning) {		// Loop while authorized to
					try {																// wait and receive the connection of a new client;
						(new Thread(new ClientWorker(Server.this, Server.this.connSocket.accept()))).start();
					}
					catch (IOException exc) {
						exc.printStackTrace(Server.this.console.printStream);
					}
				}
				
				try {
					Server.this.stop();												// when done looping, close everything.
				} catch (IOException e) {
					e.printStackTrace(Server.this.getPrintStream());
				}
			}
		})).start();
	}
	
	/**
	 * @return The clients map of this server. It is used to store in memory clients connected to the server.
	 */
	public final HashMap<String, ClientEntry> getClientsMap() {
		return this.clientsMap;
	}
	
	/**
	 * @return The {@link PrintStream} that enables one to print to the server's console.
	 */
	public final PrintStream getPrintStream() {
		return this.console.printStream;
	}
	
	/**
	 * @return The boolean stating whether this server is currently running or not.
	 */
	public final boolean isRunning() {
		return this.isRunning;
	}
	
	/**
	 * Stops and closes the server part of this application. Triggered by window closing.
	 * @throws IOException
	 */
	public void stop() throws IOException {
		this.isRunning = false;
		this.console.stop();
		this.connSocket.close();
	}
}
