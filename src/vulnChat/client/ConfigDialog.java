package vulnChat.client;

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


public class ConfigDialog extends JFrame {
	private static final long serialVersionUID = 907344703389239762L;
	private final Client client;
	
	public ConfigDialog(Client client, String title, String defaultIP, String defaultPort) {
		super("Configuration");
		this.client = client;
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		
		JPanel srvIPpanel = new JPanel();
		JTextField srvIPfield = new JTextField(defaultIP, 16);
		srvIPpanel.add(new JLabel("Server IP Address", SwingConstants.LEFT));
		srvIPpanel.add(srvIPfield);
		mainPanel.add(srvIPpanel);
		
		JPanel srvPortPanel = new JPanel();
		JTextField srvPortField = new JTextField(defaultPort, 5);
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
							ConfigDialog.this.client.setChatterName(nicknameField.getText());
							ConfigDialog.this.client.connectTo(srvIPfield.getText(), Integer.parseInt(srvPortField.getText()));
							ConfigDialog.this.client.startChatWindow();
						} catch (NumberFormatException | IOException e) {
							e.printStackTrace();
						}
					}
				}
				ConfigDialog.this.dispose();
			}
		};
		okButton.addActionListener(cmdListen);
		cancelButton.addActionListener(cmdListen);
		buttonsPanel.add(okButton);
		buttonsPanel.add(cancelButton);
		mainPanel.add(buttonsPanel);
		
		this.add(mainPanel);
		this.setResizable(false);
	}
	
	public void start() {
		this.pack();
		this.setVisible(true);
		this.requestFocus();
	}
	
	public void stop() {
		this.dispose();
	}
}
