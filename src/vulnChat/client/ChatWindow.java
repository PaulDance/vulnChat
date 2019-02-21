package vulnChat.client;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.PrintStream;


public class ChatWindow extends InOutConsole {
	private static final long serialVersionUID = -3281596045921734919L;
	private final Client client;

	public ChatWindow(Client client, String title) {
		super(title, new PrintStream(new LineOutputStream () {
			@Override public void write(String line) throws IOException {
				client.getInternals().getPrintStream().println(client.getChatterName() + ": " + line);
				client.getInternals().getToServerWriter().println("say " + client.getChatterName() + " " + line);
			}
		}));
		
		this.client = client;
		this.client.getInternals().setPrintStream(this.inputStream);
		
		this.addWindowListener(new WindowListener() {
			public void windowClosing(WindowEvent event) {
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
	
	@Override
	public void start() {
		this.pack();
		this.setVisible(true);
		this.requestFocus();
		this.client.setRunning(true);
		(new Thread(new ServerWorker(client))).start();
	}
}
