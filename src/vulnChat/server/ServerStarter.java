package vulnChat.server;

import java.io.IOException;

public class ServerStarter implements Runnable {
	private final Server server;
	
	public ServerStarter(Server server) {
		this.server = server;
	}
	
	@Override
	public void run() {
		try {
			this.server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
