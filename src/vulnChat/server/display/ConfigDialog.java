package vulnChat.server.display;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.BindException;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import vulnChat.data.LabelFieldHolder;
import vulnChat.server.data.CheckBoxHolder;
import vulnChat.server.data.CheckBoxHolder.CheckBoxListener;
import vulnChat.server.data.Settings;
import vulnChat.server.main.Server;


/**
 * Starts and displays the server's configuration dialog on startup, enabling the modification
 * of the port the server will start on and its difficulty settings.
 * 
 * @see Settings
 * @see JFrame
 * @see Server
 * @author Paul Mabileau
 * @version 0.3
*/
public class ConfigDialog extends JFrame {
	private static final long serialVersionUID = 226780886731769923L;
	
	/**
	 * Initializes the port dialog with custom window title and port number the server will then open its socket on.
	 * @param title The window title
	 * @param defaultPort The string representing the port number the server will then open its socket on.
	 * @see Server
	 */
	public ConfigDialog(String title, String defaultPort) {
		super(title);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);				// Kill this window only when closed with cross button.
		
		final Settings serverSettings = new Settings();
		final JPanel mainPanel = new JPanel();									// A panel is a way to structure the widgets with a logical tree formation,
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		
		final LabelFieldHolder portFieldHolder = new LabelFieldHolder()			// so you create a panel,
				.addLine("Server Port Number", defaultPort, "port", 5, 50)		// add widgets to it
		;
		mainPanel.add(portFieldHolder);											// and then add it to the main panel.
		
		final LabelFieldHolder txtLimitFieldHolder = new LabelFieldHolder();
		txtLimitFieldHolder.addLine("Maximum text length", Integer.toString(serverSettings.txtLimit.getValue()), "txtLimit", 5, 50);
		txtLimitFieldHolder.setVisible(serverSettings.checkTxtLimit.getValue());
		txtLimitFieldHolder.setAlignmentX(LEFT_ALIGNMENT);						// Weirdly inverted...
		
		final CheckBoxHolder checksHolder = new CheckBoxHolder();				// The holder easily creating and storing the associated JCheckBox instances.
		checksHolder.setAlignmentX(CENTER_ALIGNMENT);
		checksHolder.setLayout(new BoxLayout(checksHolder, BoxLayout.PAGE_AXIS));
		checksHolder.addNew("Refuse a new connection with an already existing nickname", serverSettings.checkNewClientName);
		checksHolder.addNew("Check the client's IP address and port number", serverSettings.checkClientIPAndPort);
		checksHolder.addNew("Kick a connected client on hack attempt", serverSettings.kickOnHack);
		checksHolder.addNew("Delete content created by a kicked client    [inactive]", serverSettings.delKickedClient);
		checksHolder.addNew("Limit a message's length    [inactive]", serverSettings.checkTxtLimit, new CheckBoxListener() {
			@Override public void onClick(JCheckBox checkBox) {
				txtLimitFieldHolder.setVisible(checkBox.isSelected());
				serverSettings.checkTxtLimit.setValue(checkBox.isSelected());
				ConfigDialog.this.pack();
			}
		}, txtLimitFieldHolder);
		checksHolder.addNew("Refuse two \"new\" actions from one client    [inactive]", serverSettings.checkTwoNews);
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
		
		final JPanel txtObJPanel = new JPanel();
		txtObJPanel.setAlignmentX(CENTER_ALIGNMENT);
		txtObJPanel.setLayout(new BoxLayout(txtObJPanel, BoxLayout.PAGE_AXIS));
		final ButtonGroup txtObjGroup = new ButtonGroup();						// Choose between raw text and serialized communication.
		final JRadioButton txtChoice = new JRadioButton("Use fully clear text communication", true);
		final JRadioButton objChoice = new JRadioButton("Use serialized communication", false);
		
		final ActionListener choiceListen = new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				serverSettings.objTransmit.setValue(objChoice.isSelected());
			}
		};
		
		txtChoice.addActionListener(choiceListen);
		objChoice.addActionListener(choiceListen);
		txtObjGroup.add(txtChoice);
		txtObjGroup.add(objChoice);
		txtObJPanel.add(txtChoice);
		txtObJPanel.add(objChoice);
		mainPanel.add(txtObJPanel);
		
		final JLabel errorLabel = new JLabel(" ");								// The label used for reporting input mistakes or connection errors to the user.
		
		final JPanel buttonsPanel = new JPanel();
		final JButton okButton = new JButton("OK"), cancelButton = new JButton("Cancel");
		okButton.setActionCommand("ok");
		cancelButton.setActionCommand("cancel");
		
		ActionListener buttonListen = new ActionListener() {					// Defines an event listener anonymous class instance that will catch
			@Override public void actionPerformed(ActionEvent event) {			// events related to buttons,it will be used for the ok and cancel buttons here:
				if (event.getActionCommand().equals("ok")) {					// if ok is clicked,
					if (portFieldHolder.getField("port").getText().matches("^[0-9]+$")) {	// and if the information typed are correct,
						if (txtLimitFieldHolder.getField("txtLimit").getText().matches("^[0-9]+$")) {
							try {												// then start the server.
								if (serverSettings.checkTxtLimit.getValue()) {
									serverSettings.txtLimit.setValue(Integer.parseInt(txtLimitFieldHolder.getField("txtLimit").getText()));
								}
								(new Server(Integer.parseInt(portFieldHolder.getField("port").getText()), serverSettings)).start();
								ConfigDialog.this.stop();
							} catch (BindException e) {							// If the server socket could not bind the port to itself,
								errorLabel.setText("Port seems used");			// inform the user;
							} catch (NumberFormatException | IOException e) {	// in case of major problem,
								e.printStackTrace();							// report to the system console
								ConfigDialog.this.stop();						// and stop everything.
							}
						}
						else {
							errorLabel.setText("Message length limit must be digits only");
						}
					}
					else {														// If port number is not in the correct format,
						errorLabel.setText("Port number must be digits only");	// report to the user.
					}
				}
				else {
					ConfigDialog.this.stop();									// In any other case, the application closes.
				}
			}
		};
		
		okButton.addActionListener(buttonListen);
		cancelButton.addActionListener(buttonListen);
		buttonsPanel.add(okButton);
		buttonsPanel.add(cancelButton);
		mainPanel.add(buttonsPanel);
		
		final JPanel errorLabelPanel = new JPanel();
		errorLabelPanel.add(errorLabel);
		mainPanel.add(errorLabelPanel);
		
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
