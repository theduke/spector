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
	
	/**
	 * If the priority of an event exceeds this limit,
	 * the events are immediately pushed, even if the buffer is not 
	 * full yet.
	 */
	static final int IMMEDIATE_FLUSH_PRIORITY_LIMIT = 700;
	
	ArrayList<Event> eventQueue = new ArrayList<Event>();
	
	protected String eventsToString(ArrayList<Event> events) {
		StringBuilder builder = new StringBuilder();
		
		for (Event event : eventQueue) {
			builder.append(event.serialize() + "\n");
		}
		
		String data = builder.toString();
		
		return data;
	}
	
	public void pushEvent(Event event) {
		eventQueue.add(event);
		
		if (eventQueue.size() >= FLUSH_INTERVAL || event.getPriority() >= IMMEDIATE_FLUSH_PRIORITY_LIMIT) {
			doPush();
			eventQueue.clear();
		}
	}
	
	/**
	 * Overwrite in child class!
	 */
	protected void doPush() {
	}
	
	public void onSessionStop() {
		if (eventQueue.size() > 0) {
			doPush();
		}
	}
}
