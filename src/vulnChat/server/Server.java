package vulnChat.server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


public class Server {
	private final ServerSocket connSocket;
	private final HashMap<String, ClientEntry> clientsMap;
	private final JFrame frame;
	private final PrintStream printStream;
	private boolean isRunning = false;
	
	public static void main(String[] args) throws IOException {
		JFrame portFrame = new JFrame("Configuration");
		portFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		
		JPanel portPanel = new JPanel();
		JTextField portField = new JTextField("4321", 6);
		portPanel.add(new JLabel("Server Port", SwingConstants.LEFT));
		portPanel.add(portField);
		mainPanel.add(portPanel);
		
		JPanel buttonsPanel = new JPanel();
		JButton okButton = new JButton("OK"), cancelButton = new JButton("Cancel");
		okButton.setActionCommand("ok");
		cancelButton.setActionCommand("cancel");
		ActionListener cmdListen = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand().equals("ok")) {
					if (portField.getText().matches("^[0-9]+$")) {
						try {
							(new Thread(new ServerStarter(new Server(Integer.parseInt(portField.getText()))))).start();
//						} catch (NumberFormatException e) {
//							e.printStackTrace();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				portFrame.dispose();
			}
		};
		okButton.addActionListener(cmdListen);
		cancelButton.addActionListener(cmdListen);
		buttonsPanel.add(okButton);
		buttonsPanel.add(cancelButton);
		mainPanel.add(buttonsPanel);
		
		portFrame.add(mainPanel);
		portFrame.setResizable(false);
		portFrame.pack();
		portFrame.setVisible(true);
		portFrame.requestFocus();
	}
	
	public Server() throws IOException {
		this(4321);
	}
	
	public Server(int port) throws IOException {
		this.connSocket = new ServerSocket(port);
		this.clientsMap = new HashMap<String, ClientEntry>();
		
		this.frame = new JFrame();
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JTextArea printArea = new JTextArea(20, 90);
		printArea.setBackground(Color.BLACK);
		printArea.setForeground(Color.LIGHT_GRAY);
		printArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 17));
		printArea.setCaretColor(Color.WHITE);
		printArea.setLineWrap(true);
		printArea.setEditable(false);
		
		this.printStream = new PrintStream(new OutputStream() {
			public void write(int b) throws IOException {
				printArea.append(String.valueOf((char) b));
			}
		});
		
		this.frame.addWindowListener(new WindowListener() {
			public void windowClosing(WindowEvent event) {
				try {
					Server.this.close();
				} catch (IOException exc) {
					exc.printStackTrace(Server.this.printStream);
				}
			}
			public void windowOpened(WindowEvent e) {}
			public void windowClosed(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowActivated(WindowEvent e) {}
			public void windowDeactivated(WindowEvent e) {}
		});
		
		JScrollPane scrollPane = new JScrollPane(printArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.frame.add(scrollPane, BorderLayout.CENTER);
		this.frame.setTitle("Server console");
		this.frame.pack();
		this.frame.setResizable(false);
		this.frame.setVisible(true);
	}
	
	public void start() throws IOException {
		this.isRunning = true;
		try (final DatagramSocket probeSocket = new DatagramSocket()) {
			  probeSocket.connect(InetAddress.getByName("8.8.8.8"), 10002);
			  this.printStream.println("Server started on " + probeSocket.getLocalAddress().getHostAddress() + " port " + this.connSocket.getLocalPort());
		}
		
		while (this.frame.isEnabled() && this.isRunning) {
			try {
				(new Thread(new ClientWorker(this, this.connSocket.accept()))).start();
			}
			catch (SocketException exc) {
				exc.printStackTrace(this.printStream);
			}
		}
		
		this.close();
	}
		
	public final ServerSocket getConnSocket() {
		return connSocket;
	}

	public final HashMap<String, ClientEntry> getClientsMap() {
		return clientsMap;
	}

	public final JFrame getFrame() {
		return frame;
	}

	public final PrintStream getPrintStream() {
		return printStream;
	}
	
	public final boolean isRunning() {
		return this.isRunning;
	}
	
	public void close() throws IOException {
		this.isRunning = false;
		this.frame.dispose();
		this.connSocket.close();
	}
}
