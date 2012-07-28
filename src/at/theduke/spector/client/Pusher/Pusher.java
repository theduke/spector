package at.theduke.spector.client.Pusher;

import at.theduke.spector.Event;

public interface Pusher {
	
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
	
}
