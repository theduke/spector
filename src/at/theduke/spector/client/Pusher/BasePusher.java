package at.theduke.spector.client.Pusher;

import java.util.ArrayList;

import org.slf4j.Logger;

import at.theduke.spector.Event;
import at.theduke.spector.client.Application;

abstract public class BasePusher implements Pusher {
	static final Logger logger = Application.getLogger();
	
	/**
	 * The buffer will be flushed every interval events.
	 */
	static final int FLUSH_INTERVAL = 100;
	
	ArrayList<Event> eventQueue = new ArrayList<Event>();
	
	public void pushEvent(Event event) {
		eventQueue.add(event);
		
		if (eventQueue.size() >= FLUSH_INTERVAL) {
			doPush();
			eventQueue.clear();
		}
	}
	
	/**
	 * Overwrite in child class!
	 */
	private void doPush() {
	}
	
	public void onSessionStop() {
		if (eventQueue.size() > 0) {
			doPush();
		}
	}
}
