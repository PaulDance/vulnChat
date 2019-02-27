package vulnChat.client.display;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.PrintStream;

import vulnChat.client.data.LineOutputStream;
import vulnChat.client.main.Client;


/**
 * This class extends {@link InOutConsole} and redefines its behavior specifically for the use
 * in an online chat application. Must be used by a {@link Client} object.
 * @author Paul Mabileau
 * @version 0.1
 */
public class ChatWindow extends InOutConsole {
	private static final long serialVersionUID = -3281596045921734919L;
	
	/**
	 * Builds the {@link ChatWindow} instance by using the {@link InOutConsole} superclass and
	 * associating to this object a {@link Client} object for the specific use meant by vulnChat.
	 * @param client - the calling {@link Client} instance
	 * @param title - the window's title as a {@link String}.
	 */
	public ChatWindow(Client client, String title) {
		super(title, new PrintStream(new LineOutputStream () {					// Behavior specific to a chat window:
			@Override public void write(String line) throws IOException {		// when receiving a line of text from the user, display it and send it;
				client.getInternals().getPrintStream().println(client.getChatterName() + ": " + line);
				client.getInternals().getToServerWriter().println("say " + client.getChatterName() + " " + line);
			}
		}));
		
		client.getInternals().setPrintStream(this.inputStream);					// give to the client object the stream to this window;
		this.setCharLimit(1000);
		
		this.addWindowListener(new WindowListener() {							// also, there is communication needed upon closing the window:
			public void windowClosing(WindowEvent event) {						// send to the server an end connection message.
				client.getInternals().getToServerWriter().println("bye " + client.getChatterName());
				try {
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
