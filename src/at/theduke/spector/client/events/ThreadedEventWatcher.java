package at.theduke.spector.client.events;

import at.theduke.spector.Session;

public abstract class ThreadedEventWatcher extends Thread implements EventWatcher {
	protected Session session;
	
	public void run(Session session) {
		this.connect(session);
	}
}
