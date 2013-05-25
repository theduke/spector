package at.theduke.spector.eventwriter;

import at.theduke.spector.Event;

/**
 * @author theduke
 *
 */
public class StdOutWriter implements Writer {
	
	/**
	 * Unique writer identifier.
	 */
	static final String name = "stdout";
	
	public void onSessionStart(String id) {
	}
	
	@Override
	public boolean pushEvent(Event event) {
		String output = event.serialize();
		System.out.println(output);
		
		return true;
	}

	@Override
	public void onSessionStop() {
	}

	@Override
	public void flushEvents() {
	}
}
