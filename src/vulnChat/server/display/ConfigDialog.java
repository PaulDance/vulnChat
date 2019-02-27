package vulnChat.server.display;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import vulnChat.server.data.CheckBoxHolder;
import vulnChat.server.data.Settings;
import vulnChat.server.main.Server;


/**
 * Starts and displays the server's configuration dialog on startup, enabling the modification
 * of the port the server will start on and its difficulty settings.
 * @see Settings
 * @see JFrame
 * @see Server
 * @author Paul Mabileau
 * @version 0.2
*/
public class ConfigDialog extends JFrame {
	private static final long serialVersionUID = 226780886731769923L;
	
	/**
	 * Initializes the port dialog with custom window title and port number the server will then open its socket on.
	 * @param title - The window title
	 * @param defaultPort - The string representing the port number the server will then open its socket on.
	 * @see Server
	 */
	public ConfigDialog(String title, String defaultPort) {
		super(title);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);				// Kill this window only when closed with cross button.
		
		final Settings serverSettings = new Settings();
		final JPanel mainPanel = new JPanel();									// A panel is a way to structure the widgets with a logical tree formation,
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		
		final JPanel portPanel = new JPanel();									// so you create a panel,
		final JTextField portField = new JTextField(defaultPort, 6);
		portPanel.add(new JLabel("Server Port", SwingConstants.CENTER));			// add widgets to it
		portPanel.add(portField);
		mainPanel.add(portPanel);												// and then add it to the main panel.
		
		final JPanel buttonsPanel = new JPanel();								// Same for buttons.
		final JButton okButton = new JButton("OK"), cancelButton = new JButton("Cancel");
		okButton.setActionCommand("ok");
		cancelButton.setActionCommand("cancel");
		
		ActionListener buttonListen = new ActionListener() {					// Defines an event listener anonymous class instance that will catch
			@Override public void actionPerformed(ActionEvent event) {			// events related to buttons,it will be used for the ok and cancel buttons here:
				if (event.getActionCommand().equals("ok")) {					// if ok is clicked,
					if (portField.getText().matches("^[0-9]+$")) {				// and if the information typed corresponds to a port number,
						try {													// then start the server.
							(new Server(Integer.parseInt(portField.getText()), serverSettings)).start();
						} catch (NumberFormatException | IOException e) {
							e.printStackTrace();
						}
					}
				}
				ConfigDialog.this.stop();										// in any other case, the application closes.
			}
		};
		
		okButton.addActionListener(buttonListen);
		cancelButton.addActionListener(buttonListen);
		buttonsPanel.add(okButton);
		buttonsPanel.add(cancelButton);
		mainPanel.add(buttonsPanel);
		
		final CheckBoxHolder checksHolder = new CheckBoxHolder();				// The holder easily creating and storing the associated JCheckBox instances.
		checksHolder.setLayout(new BoxLayout(checksHolder, BoxLayout.PAGE_AXIS));
		checksHolder.addNew("Refuse a new connection with an already existing nickname", serverSettings.checkNewClientName);
		checksHolder.addNew("Check the client's IP address and port number", serverSettings.checkClientIPAndPort);
		checksHolder.addNew("Kick a connected client on hack attempt", serverSettings.kickOnHack);
		checksHolder.createItemListener();
		mainPanel.add(checksHolder);
		
		final JPanel selectionButtonsPanel = new JPanel();						// Select all and deselect all buttons.
		final JButton selectAllButton = new JButton("Select all"), deselectAllButton = new JButton("Deselect all");
		selectAllButton.setActionCommand("selAll");
		deselectAllButton.setActionCommand("deselAll");
		
		final ActionListener selectButtonsListener = new ActionListener() {
			@Override public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand().equals("selAll")) {
					checksHolder.selectAll();
				}
				else if (event.getActionCommand().equals("deselAll")) {
					checksHolder.deselectAll();
				}
			}
		};
		
		selectAllButton.addActionListener(selectButtonsListener);
		deselectAllButton.addActionListener(selectButtonsListener);
		selectionButtonsPanel.add(selectAllButton);
		selectionButtonsPanel.add(deselectAllButton);
		
		mainPanel.add(selectionButtonsPanel);
		this.add(mainPanel);													// Finally you connect the main panel which holds everything to the frame.
		this.setResizable(false);
	}
	
	/**
	 * Builds the frame graphics and shows the window to the foreground.
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
	 * Stops the dialog and releases all of the native screen resources used.
	 */
	public void stop() {
		this.dispose();
	}
}
