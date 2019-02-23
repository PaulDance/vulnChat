package vulnChat.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


/**
 * Starts and displays the server's port dialog on startup. Extends JFrame.
 * @see JFrame
 * @see Server
 * @author Paul Mabileau
 * @version 0.1
*/
public class PortDialog extends JFrame {
	private static final long serialVersionUID = 226780886731769923L;
	
	/**
	 * Initializes the port dialog with custom window title and port number the server will then open its socket on.
	 * @param title - The window title
	 * @param defaultPort - The string representing the port number the server will then open its socket on.
	 * @see Server
	 */
	public PortDialog(String title, String defaultPort) {
		super(title);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);				// Kill this window only when closed with cross button.
		
		JPanel mainPanel = new JPanel();										// A panel is a way to structure the widgets with a logical tree formation,
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		
		JPanel portPanel = new JPanel();										// so you create a panel,
		JTextField portField = new JTextField(defaultPort, 6);
		portPanel.add(new JLabel("Server Port", SwingConstants.LEFT));			// add widgets to it
		portPanel.add(portField);
		mainPanel.add(portPanel);												// and then add it to the main panel.
		
		JPanel buttonsPanel = new JPanel();										// Same for buttons.
		JButton okButton = new JButton("OK"), cancelButton = new JButton("Cancel");
		okButton.setActionCommand("ok");
		cancelButton.setActionCommand("cancel");
		
		ActionListener cmdListen = new ActionListener() {						// Defines an event listener anonymous class instance that will catch
			@Override public void actionPerformed(ActionEvent event) {			// events related to buttons,it will be used for the ok and cancel buttons here:
				if (event.getActionCommand().equals("ok")) {					// if ok is clicked,
					if (portField.getText().matches("^[0-9]+$")) {				// and if the information typed corresponds to a port number,
						try {													// then start the server.
							(new Server(Integer.parseInt(portField.getText()))).start();
						} catch (NumberFormatException | IOException e) {
							e.printStackTrace();
						}
					}
				}
				PortDialog.this.dispose();										// in any other case, the application closes.
			}
		};
		
		okButton.addActionListener(cmdListen);
		cancelButton.addActionListener(cmdListen);
		buttonsPanel.add(okButton);
		buttonsPanel.add(cancelButton);
		mainPanel.add(buttonsPanel);
		
		this.add(mainPanel);													// Finally you connect the main panel which holds everything to the frame.
		this.setResizable(false);
	}
	
	/**
	 * Builds the frame graphics and shows the window to the foreground.
	 */
	public void start() {
		this.pack();
		this.setVisible(true);
		this.requestFocus();
	}
}
