package vulnChat.client.display;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import vulnChat.client.main.Client;
import vulnChat.data.Bye;
import vulnChat.data.LinePrinter;
import vulnChat.data.Say;


/**
 * This class extends {@link InOutConsole} and redefines its behavior specifically for the use
 * in an online chat application. Must be used by a {@link Client} object.
 * 
 * @author Paul Mabileau
 * @version 0.2
 */
public class ChatWindow extends InOutConsole {
	private static final long serialVersionUID = -3281596045921734919L;
	
	/**
	 * Builds the {@link ChatWindow} instance by using the {@link InOutConsole} superclass and
	 * associating to this object a {@link Client} object for the specific use meant by vulnChat.
	 * 
	 * @param client The calling {@link Client} instance
	 * @param title The window's title as a {@link String}.
	 */
	public ChatWindow(Client client, String title) {
		super(title, new LinePrinter() {							// Behavior specific to a chat window:
			@Override public void println(String line) {
				this.print(line);
			}
			
			@Override public void print(String line) {				// when receiving a line of text from the user, display it and send it;
				client.getInternals().getLinePrinter().println(client.getChatterName() + ": " + line);
				try {
					client.sendToServer(new Say(client.getChatterName(), line));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		client.getInternals().setLinePrinter(this.inputPrinter);	// give to the client object the printer to this window;
		this.setCharLimit(1000);
		
		this.addWindowListener(new WindowListener() {				// also, there is communication needed upon closing the window:
			public void windowClosing(WindowEvent event) {			// send to the server an end connection message.
				try {
					client.sendToServer(new Bye(client.getChatterName()));
					client.stop();
					ChatWindow.this.stop();
				} catch (IOException e) {
					e.printStackTrace();
				}
				client.setRunning(false);
			}
			public void windowOpened(WindowEvent e) {}
			public void windowClosed(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowActivated(WindowEvent e) {}
			public void windowDeactivated(WindowEvent e) {}
		});
	}
}
