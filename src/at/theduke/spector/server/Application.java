package at.theduke.spector.server;

import java.io.PipedWriter;

import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.theduke.spector.Event;
import at.theduke.spector.Utils;
import at.theduke.spector.eventwriter.PipeWriter;
import at.theduke.spector.server.http.JettyServer;
import at.theduke.spector.session.ServerSessionHandler;

public class Application {
	JettyServer httpServer;
	
	Client esClient;
	
	PipeWriter eventReceivers = new PipeWriter();
	
	public static void main(String[] args) {
		Application app = new Application();
		app.run();
	}
	
	public static Logger getLogger() {
		Logger logger = LoggerFactory.getLogger("spector");
		return logger;
	}
	
	void run() {
		
		// Establish elasticsearch connection.
		esClient = Utils.getElasticSearchConnection("elasticsearch", "localhost", 9300);
		
		// Add session handler.
		eventReceivers.addWriter(new ServerSessionHandler(esClient));
		
		httpServer = new JettyServer(8081, this);
		httpServer.run();
	}
	
	public int receiveEvents(String data, boolean gzipped) {
		
		if (gzipped) {
			/**
			 * @TODO unzip data
			 */
		}
		
		String[] items = data.split("\n");
		
		int counter = 0;
		
		for (String item : items) {
			Event event = Event.parseEvent(item);
			
			if (event != null) {
				eventReceivers.pushEvent(event);
				++counter;
			}
		}
		
		return counter;
	}
}
