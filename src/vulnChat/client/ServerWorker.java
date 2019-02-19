package oChat.client;

import java.io.IOException;


public class ServerWorker implements Runnable {
	private final Client client;
	private boolean isRunning;
	
	public ServerWorker(Client client) {
		this.client = client;
	}
	
	public final void run() {
		String serverMsg;
		this.isRunning = true;
		
		while (this.client.isRunning() && this.isRunning) {
			try {
				do {
					serverMsg = client.getInternals().getFromServerReader().readLine();
				} while (serverMsg == null);
				
				if (serverMsg.matches("[a-z]{3}\\s[\\p{Alnum}\\p{Punct}]{1,50}\\s?.{0,1000}")) {		// [\\p{Alnum}\\p{Punct}\\p{Print}]
					String[] elements = serverMsg.split(" ", 3);
					
					if (elements[0].equals("new")) {
						this.client.getInternals().getPrintStream().println(elements[1] + " joined the channel.");
					}
					else if (elements[0].equals("bye")) {
						this.client.getInternals().getPrintStream().println(elements[1] + " left the channel.");
					}
					else if (elements[0].equals("say")) {
						this.client.getInternals().getPrintStream().println(elements[1] + ": " + elements[2]);
					}
				}
			}
			catch (IOException exc) {
				this.isRunning = false;
				
				if (!this.client.getInternals().getClientSocket().isClosed()) {
					this.client.getInternals().getPrintStream().println("Read failed");
					exc.printStackTrace(this.client.getInternals().getPrintStream());
				}
			}
		}
	}
	
	protected final void finalize() {
		this.isRunning = false;
	}
}
