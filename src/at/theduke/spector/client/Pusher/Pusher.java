package at.theduke.spector.client.Pusher;

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
	public void pushEvent(String event);
	
}
