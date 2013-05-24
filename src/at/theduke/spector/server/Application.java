package at.theduke.spector.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.theduke.spector.server.http.JettyServer;

public class Application {
	
	EventReceiver eventReceiver;
	
	JettyServer httpServer;
	
	
	public static void main(String[] args) {
		Application app = new Application();
		app.run();
	}
	
	public static Logger getLogger() {
		Logger logger = LoggerFactory.getLogger("spector");
		return logger;
	}
	
	void run() {
		eventReceiver = new EventReceiver();
		
		httpServer = new JettyServer(8081, eventReceiver);
		httpServer.run();
	}
}
