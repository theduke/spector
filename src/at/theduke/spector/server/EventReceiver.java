package at.theduke.spector.server;

import org.slf4j.Logger;

import at.theduke.spector.Event;

public class EventReceiver {
	
	protected final Logger logger = Application.getLogger();
	
	/**
	 * 
	 * @param data
	 * @param gzipped
	 * @return The number of processed events.
	 */
	public int receiveEvents(String data, boolean gzipped) {
		
		if (gzipped) {
			/**
			 * @TODO unzip data
			 */
		}
		
		String[] items = data.split("\n");
		
		int counter = 0;
		
		for (String item : items) {
			Event event = Event.parseEvent(item);
			if (event != null) {
				receiveEvent(event);
				++counter;
			}
		}
		
		return counter;
	}
	
	public void receiveEvent(Event event) {
		logger.debug("Received event: " + event.serialize());
	}
}
