package at.theduke.spector.eventfilter;

import at.theduke.spector.Event;

/**
 * This filter only accepts events of a specific type.
 * 
 * @author theduke
 *
 */
public class TypeFilter implements Filter {
	
	/**
	 * Event type. See Event.EVENT_* constants.
	 */
	private String type;
	
	public TypeFilter(String type) {
		this.type = type;
	}

	@Override
	public boolean doFilter(Event event) {
		return event.getType().equals(type);
	}
}
