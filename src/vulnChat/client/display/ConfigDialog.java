package vulnChat.client.display;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ConnectException;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import vulnChat.client.main.Client;
import vulnChat.client.main.ServerWorker;


/**
 * Defines a window inheriting from {@link JFrame} in order to make a configuration window
 * for a {@link Client} to set a window title, an IP address and a port number to connect to.
 * 
 * @author Paul Mabileau
 * @version 0.3
 */
public class ConfigDialog extends JFrame {
	private static final long serialVersionUID = 907344703389239762L;
	
	/**
	 * Builds the desired configuration dialog with a bit of customization available:
	 * @param client The calling {@link Client} instance
	 * @param title The window's title as a {@link String}
	 * @param defaultIP The IP address as a {@link String} that will be put by default in the corresponding {@link JTextField} upon startup
	 * @param defaultPort The default port number as a {@link String} that will be put by default in the corresponding {@link JTextField} upon startup
	 */
	public ConfigDialog(Client client, String title, String defaultIP, String defaultPort) {
		super("Configuration");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		final JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		
		final JPanel srvIPpanel = new JPanel();
		final JTextField srvIPfield = new JTextField(defaultIP, 16);
		srvIPpanel.add(new JLabel("Server IP Address", SwingConstants.LEFT));
		srvIPpanel.add(srvIPfield);
		mainPanel.add(srvIPpanel);
		
		final JPanel srvPortPanel = new JPanel();
		final JTextField srvPortField = new JTextField(defaultPort, 5);
		srvPortPanel.add(new JLabel("Server Port", SwingConstants.LEFT));
		srvPortPanel.add(srvPortField);
		mainPanel.add(srvPortPanel);
		
		final JPanel nicknamePanel = new JPanel();
		final JTextField nicknameField = new JTextField(20);
		nicknamePanel.add(new JLabel("Nickname", SwingConstants.LEFT));
		nicknamePanel.add(nicknameField);
		mainPanel.add(nicknamePanel);
		
		final JPanel txtObJPanel = new JPanel();
		txtObJPanel.setLayout(new BoxLayout(txtObJPanel, BoxLayout.PAGE_AXIS));
		final ButtonGroup txtObjGroup = new ButtonGroup();
		final JRadioButton txtChoice = new JRadioButton("Use fully clear text communication", true);
		final JRadioButton objChoice = new JRadioButton("Use serialized communication", false);
		
		final ActionListener choiceListen = new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				client.settings.objTransmit.setValue(objChoice.isSelected());
			}
		};
		
		txtChoice.addActionListener(choiceListen);
		objChoice.addActionListener(choiceListen);
		txtObjGroup.add(txtChoice);
		txtObjGroup.add(objChoice);
		txtObJPanel.add(txtChoice);
		txtObJPanel.add(objChoice);
		mainPanel.add(txtObJPanel);
		
		final JLabel errorLabel = new JLabel(" ");									// The label used to report input mistakes.
		
		final JPanel buttonsPanel = new JPanel();
		final JButton okButton = new JButton("OK"), cancelButton = new JButton("Cancel");
		okButton.setActionCommand("ok");
		cancelButton.setActionCommand("cancel");
		
		final ActionListener cmdListen = new ActionListener() {						// Detects user clicks on the 'OK' button or the 'Cancel' button.
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand().equals("ok")) {						// If ok is pressed, check the fields and start the client:
					final String ip = srvIPfield.getText().trim(), port = srvPortField.getText().trim(), nickname = nicknameField.getText().trim();
					if (ip.matches("^[0-9]{1,3}[.][0-9]{1,3}[.][0-9]{1,3}[.][0-9]{1,3}$")) {
						if (port.matches("[0-9]+")) {
							if (nickname.matches("\\p{Print}+")) {
								try {
									client.setChatterName(nickname);
									client.connectTo(ip, Integer.parseInt(port));
									client.setRunning(true);
									(new Thread(new ServerWorker(client))).start();
									client.startChatWindow();
									client.getInternals().getLinePrinter().println(nickname + " joined the channel.");
									ConfigDialog.this.stop();
								} catch (ConnectException exc) {					// If server is unavailable,
									client.setRunning(false);						// cancel the starting,
									errorLabel.setText("Server seems unavailable");	// and inform the user;
								} catch (NumberFormatException | IOException e) {	// If major incident,
									e.printStackTrace();							// report to the system console,
									ConfigDialog.this.stop();						// and stop everything.
								}
							}
							else {													// If the nickname is empty or contains weird characters,
								errorLabel.setText("Nickname is empty");			// tell the user;
							}
						}
						else {														// If the port field is empty or contains non-digit characters,
							errorLabel.setText("Port number is digits only");		// tell the user;
						}
					}
					else {															// If the IP address field does not have a correctly formated IP,
						errorLabel.setText("Wrong IP format");						// tell the user.
					}
				}
				else {																// If cancel button is pressed,
					ConfigDialog.this.stop();										// stop everything and close.
				}
			}
		};
		
		okButton.addActionListener(cmdListen);
		cancelButton.addActionListener(cmdListen);
		buttonsPanel.add(okButton);
		buttonsPanel.add(cancelButton);
		mainPanel.add(buttonsPanel);
		
		final JPanel errorLabelPanel = new JPanel();
		errorLabelPanel.add(errorLabel);
		mainPanel.add(errorLabelPanel);
		
		this.add(mainPanel);
		this.setResizable(false);
	}
	
	/**
	 * Computes the graphics for the console window, starts it and makes it visible to the user.
	 */
	public void start() {
		final Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		final ThreadLocalRandom randThread = ThreadLocalRandom.current();
		
		this.pack();
		this.setVisible(true);
		this.setLocation(randThread.nextInt(0, screenDim.width - this.getSize().width), randThread.nextInt(0, screenDim.height - this.getSize().height));
		this.requestFocus();
	}
	
	/**
	 * Stops completely this console, frees the occupied resources and disposes the frame.
	 */
	public void stop() {
		this.dispose();
	}
}
