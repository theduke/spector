package at.theduke.spector.eventwriter;

import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;

import at.theduke.spector.Event;
import at.theduke.spector.Utils;
import at.theduke.spector.client.Application;

abstract public class BaseWriter implements Writer {
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
	
	boolean doGzip = false;
	
	ArrayList<Event> eventQueue = new ArrayList<Event>();
	
	protected String eventsToString(ArrayList<Event> events, boolean gzip) {
		StringBuilder builder = new StringBuilder();
		
		for (Event event : eventQueue) {
			builder.append(event.serialize() + "\n");
		}
		
		String data = builder.toString();
		
		if (gzip) {
			try {
				data = Utils.doGzip(data);
			} catch (IOException e) {
				logger.error("Could not gzip event data!", e);
			}
		}
		
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

	public boolean isDoGzip() {
		return doGzip;
	}

	public void setDoGzip(boolean doGzip) {
		this.doGzip = doGzip;
	}
	
	
}
