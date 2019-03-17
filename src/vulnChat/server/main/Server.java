package vulnChat.server.main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.HashMap;

import vulnChat.data.LinePrinter;
import vulnChat.server.data.ClientEntry;
import vulnChat.server.data.Settings;
import vulnChat.server.display.ConfigDialog;
import vulnChat.server.display.Console;


/**
 * This class starts and manages a vulnerable chat messaging server. It starts by opening a configuration
 * dialog to ask the user for the port to start listening on. Then, it will display information about the
 * current connections, chat messages, input, output, transmissions, etc... on a small white-on-black console.
 * @see Console
 * @see vulnChat.client.main.Client Client
 * @see Settings
 * @author Paul Mabileau
 * @version 0.1
 */
public class Server {
	private final ServerSocket connSocket;
	private final HashMap<String, ClientEntry> clientsMap;
	private final Console console;
	private boolean isRunning = false;
	private final Settings settings;
	
	public static void main(String[] args) throws IOException {
		(new ConfigDialog("Configuration", "4321")).start();
	}
	
	/**
	 * Initializes a server on default port 4321.
	 * @throws IOException
	 */
	public Server() throws IOException {
		this(4321, new Settings());
	}
	
	/**
	 * Initializes a server on given port.
	 * @param port The network port the server will start listening on for chat messages.
	 * @throws IOException
	 */
	public Server(int port, Settings settings) throws IOException {
		this.connSocket = new ServerSocket(port);								// Start the server socket on <port>,
		this.clientsMap = new HashMap<String, ClientEntry>();					// create an empty hash map to store connected clients,
		this.console = new Console("vulnChat Server Console", 20, 90);			// and construct a server console.
		this.settings = settings;
		
		this.console.addWindowListener(new WindowListener() {
			@Override public void windowClosing(WindowEvent e) {
				try {
					Server.this.stop();
				} catch (IOException exc) {
					exc.printStackTrace();
				}
			}
			public void windowOpened(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowDeactivated(WindowEvent e) {}
			public void windowClosed(WindowEvent e) {}
			public void windowActivated(WindowEvent e) {}
		});
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
					  Server.this.console.linePrinter.println("Server started on " + probeSocket.getLocalAddress().getHostAddress() + " port " + Server.this.connSocket.getLocalPort());
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				while (Server.this.console.isEnabled() && Server.this.isRunning) {		// Loop while authorized to
					try {																// wait and receive the connection of a new client;
						(new Thread(new ClientWorker(Server.this, Server.this.connSocket.accept()))).start();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				try {
					Server.this.stop();													// when done looping, close everything.
				} catch (IOException e) {
					e.printStackTrace();
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
	 * @return The {@link LinePrinter} that enables one to print to the server's console.
	 */
	public final LinePrinter getLinePrinter() {
		return this.console.linePrinter;
	}
	
	/**
	 * @return This server's {@link Settings} instance, which describes what vulnerabilities should be left or not.
	 */
	public final Settings getSettings() {
		return this.settings;
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
		this.clientsMap.clear();
	}
}
