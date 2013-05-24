package at.theduke.spector.eventwriter;

import at.theduke.spector.Event;

public interface Writer {
	
	/**
	 * This will always be called BEFORE pushEvent() is called.
	 * 
	 * 
	 * @param id
	 */
	public void onSessionStart(String id);
	
	/**
	 * 
	 */
	public void onSessionStop();
	
	/**
	 * @param event
	 */
	public void pushEvent(Event event);
	
	/**
	 * FLush all queued events.
	 */
	public void flushEvents();
	
}
