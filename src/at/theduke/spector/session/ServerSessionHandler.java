package at.theduke.spector.session;

import java.util.HashMap;

import org.elasticsearch.client.Client;
import org.slf4j.Logger;

import com.google.gson.Gson;

import at.theduke.spector.Event;
import at.theduke.spector.Utils;
import at.theduke.spector.eventdata.MouseClickData;
import at.theduke.spector.eventdata.MouseMoveData;
import at.theduke.spector.eventfilter.SessionFilter;
import at.theduke.spector.eventfilter.TypeFilter;
import at.theduke.spector.eventwriter.FileWriter;
import at.theduke.spector.eventwriter.Writer;
import at.theduke.spector.server.Application;

public class ServerSessionHandler implements Writer {
	
	private final Logger logger = Application.getLogger();
	
	TypeFilter filter = new TypeFilter(Event.EVENT_SESSION_UPDATE);
	
	Client esClient;
	
	public ServerSessionHandler(Client esClient) {
		this.esClient = esClient;
	}
	
	@Override
	public void onSessionStart(String id) {
		
	}

	@Override
	public void onSessionStop() {
	}

	@Override
	public boolean pushEvent(Event event) {
		Session session = new Gson().fromJson(event.getData(), Session.class);
		logger.info("Received event");
		 
		return true;
	}
	
	@Override
	public void flushEvents() {
	}

}
