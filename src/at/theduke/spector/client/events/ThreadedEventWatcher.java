package at.theduke.spector.client.events;

import org.slf4j.Logger;

import at.theduke.spector.Session;
import at.theduke.spector.client.Application;

public abstract class ThreadedEventWatcher extends Thread implements EventWatcher {
	Logger logger = Application.getLogger();
	
	protected Session session;
	
	public void run(Session session) {
		this.connect(session);
	}
}
