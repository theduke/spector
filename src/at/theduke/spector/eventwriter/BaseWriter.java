package at.theduke.spector.eventwriter;

import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;

import com.db4o.ObjectContainer;

import at.theduke.spector.Event;
import at.theduke.spector.Utils;
import at.theduke.spector.client.Application;
import at.theduke.spector.eventdata.EventCollection;

abstract public class BaseWriter implements Writer {
	static final Logger logger = Application.getLogger();
	
	/**
	 * Unique writer identifier.
	 */
	static final String name = "base";
	
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
	
	protected String sessionId;
	
	protected boolean connected = false;
	
	boolean doGzip = false;
	
	ArrayList<Event> eventQueue = new ArrayList<Event>();
	
	ObjectContainer bufferDb = null;
	
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
	
	public void onSessionStart(String id) {
		this.sessionId = id;
		if (!connected) {
			connect();
		}
	}
	
	public void onSessionStop() {
		if (eventQueue.size() > 0) {
			flushEvents();
		}
		
		if (connected) {
			close();
		}
	}
	
	/**
	 * Connect to the remote if necessary.
	 */
	protected void connect() {
		
	}
	
	/**
	 * Close the connection if necesary.
	 */
	protected void close() {
		
	}
	
	public void pushEvent(Event event) {
		eventQueue.add(event);
		
		if (eventQueue.size() >= FLUSH_INTERVAL || event.getPriority() >= IMMEDIATE_FLUSH_PRIORITY_LIMIT) {
			flushEvents();
		}
	}
	
	/**
	 * Flush all queued events.
	 */
	public void flushEvents() {
		ArrayList<Event> failedEvents = executeFlush(eventQueue);

		if (failedEvents != null) {
			bufferEvents(failedEvents);
		}
		eventQueue.clear();
	}
	
	protected ArrayList<Event> executeFlush(ArrayList<Event> events) {
		/**
		 * IMPLEMENT IN CHILD CLASS!.
		 */
		return null;
	}
	
	protected void bufferEvents(ArrayList<Event> events) {
		if (bufferDb != null && events.size() > 0) {
			bufferDb.store(new EventCollection(name, events));
		}
	}
	
	public boolean isDoGzip() {
		return doGzip;
	}

	public void setDoGzip(boolean doGzip) {
		this.doGzip = doGzip;
	}

	public ObjectContainer getBufferDb() {
		return bufferDb;
	}

	public void setBufferDb(ObjectContainer bufferDb) {
		this.bufferDb = bufferDb;
	}

	public static String getName() {
		return name;
	}
}
