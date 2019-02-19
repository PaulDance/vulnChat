package oChat.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JSplitPane;
import java.awt.BorderLayout;


public class Client {
	private final JFrame frame = new JFrame();
	private String title = "", chatterName = "";
	private final ClientInternals internals;
	private boolean isRunning = false;
	
	public static void main(String[] args) throws IOException {
		Client client = new Client();
		client.start();
	}
	
	public Client() throws IOException {
		this.internals = new ClientInternals();
		this.title = "oChat";
	}
	
	public final void start() {
		JFrame idFrame = new JFrame("Configuation");
		idFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		
		JPanel srvIPpanel = new JPanel();
		JTextField srvIPfield = new JTextField("127.0.0.1", 16);
		srvIPpanel.add(new JLabel("Server IP Address", SwingConstants.LEFT));
		srvIPpanel.add(srvIPfield);
		mainPanel.add(srvIPpanel);
		
		JPanel srvPortPanel = new JPanel();
		JTextField srvPortField = new JTextField("8080", 5);
		srvPortPanel.add(new JLabel("Server Port", SwingConstants.LEFT));
		srvPortPanel.add(srvPortField);
		mainPanel.add(srvPortPanel);
		
		JPanel nicknamePanel = new JPanel();
		JTextField nicknameField = new JTextField(20);
		nicknamePanel.add(new JLabel("Nickname", SwingConstants.LEFT));
		nicknamePanel.add(nicknameField);
		mainPanel.add(nicknamePanel);
		
		JPanel buttonsPanel = new JPanel();
		JButton okButton = new JButton("OK"), cancelButton = new JButton("Cancel");
		okButton.setActionCommand("ok");
		cancelButton.setActionCommand("cancel");
		ActionListener cmdListen = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand().equals("ok")) {
					if (srvIPfield.getText().matches("^[0-9]{1,3}[.][0-9]{1,3}[.][0-9]{1,3}[.][0-9]{1,3}$") && srvPortField.getText().matches("[0-9]+")) {
						try {
							Client.this.chatterName = nicknameField.getText();
							Client.this.connectTo(srvIPfield.getText(), Integer.parseInt(srvPortField.getText()));
							Client.this.startConsole();
						} catch (NumberFormatException | IOException e) {
							e.printStackTrace();
						}
					}
				}
				idFrame.dispose();
			}
		};
		okButton.addActionListener(cmdListen);
		cancelButton.addActionListener(cmdListen);
		buttonsPanel.add(okButton);
		buttonsPanel.add(cancelButton);
		mainPanel.add(buttonsPanel);
		
		idFrame.add(mainPanel);
		idFrame.setResizable(false);
		idFrame.pack();
		idFrame.setVisible(true);
		idFrame.requestFocus();
	}
	
	private final void connectTo(String ipAddr, int port) throws IOException {
		this.internals.setClientSocket(new Socket(ipAddr, port));
		this.internals.getClientSocket().setKeepAlive(true);
		this.internals.setFromServerReader(new BufferedReader(new InputStreamReader(this.internals.getClientSocket().getInputStream())));
		this.internals.setToServerWriter(new PrintWriter(this.internals.getClientSocket().getOutputStream(), true));
		this.internals.getToServerWriter().println("new " + this.chatterName + "\n");
	}
	
	private final void startConsole() {
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final JTextArea outArea = new JTextArea(24, 80);
		outArea.setBackground(Color.BLACK);
		outArea.setForeground(Color.LIGHT_GRAY);
		outArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 17));
		outArea.setCaretColor(Color.WHITE);
		outArea.setLineWrap(true);
		outArea.setEditable(false);
		
		this.internals.setPrintStream(new PrintStream(new OutputStream() {
			public void write(int b) throws IOException {
				outArea.append(String.valueOf((char) b));
			}
		}));
		
		this.frame.setTitle(this.title + ": " + this.chatterName);
		this.frame.add(outArea, BorderLayout.CENTER);
		
		final JTextArea inArea = new JTextArea(5, 80);
		inArea.setBackground(Color.BLACK);
		inArea.setForeground(Color.LIGHT_GRAY);
		inArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		inArea.setCaretColor(Color.WHITE);
		inArea.setLineWrap(true);
		inArea.setAutoscrolls(true);
		
		inArea.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent event) {
				if (event.getKeyChar() == '\n') {
					String text = inArea.getText();
					
					if (!text.equals("\n")) {
						text = text.substring(0, text.length() - 1);
						Client.this.internals.getPrintStream().println(Client.this.chatterName + ": " + text);
						Client.this.internals.getToServerWriter().println("say " + Client.this.chatterName + " " + text);
					}
					
					inArea.setText("");
				}
			}
			public void keyPressed(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {}
		});
		this.frame.add(inArea, BorderLayout.SOUTH);
		
		JScrollPane outScrollPane = new JScrollPane(outArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		JScrollPane inScrollPane = new JScrollPane(inArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, outScrollPane, inScrollPane);
		splitPane.setEnabled(false);
		this.frame.add(splitPane);
		this.frame.pack();
		
		this.frame.addWindowListener(new WindowListener() {
			public void windowClosing(WindowEvent event) {
				Client.this.internals.getToServerWriter().println("bye " + Client.this.chatterName);
				Client.this.frame.dispose();
				Client.this.isRunning = false;
			}
			public void windowOpened(WindowEvent e) {}
			public void windowClosed(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowActivated(WindowEvent e) {}
			public void windowDeactivated(WindowEvent e) {}
		});
		
		ServerWorker serverWorker = new ServerWorker(this);
		Thread serverThread = new Thread(serverWorker);
		this.isRunning = true;
		serverThread.start();
		this.frame.setVisible(true);
	}
	
	public final boolean isRunning() {
		return this.isRunning;
	}

	protected final ClientInternals getInternals() {
		return this.internals;
	}
}
