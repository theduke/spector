package at.theduke.spector.server;

import java.io.IOException;

public class Application {
	
	SocketServer socketServer;
	
	public static void main(String[] args) {
		Application app = new Application();
		app.run();
	}
	
	void run() {
		
		// start the socket server
		socketServer = new SocketServer();
		try {
			socketServer.start();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
