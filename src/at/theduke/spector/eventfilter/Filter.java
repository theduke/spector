package at.theduke.spector.eventfilter;

import at.theduke.spector.Event;

/**
 * A filter decides which events to process and which to discard.
 */
public interface Filter {
	
	/**
	 * Check one event.
	 * 
	 * @param event
	 * @return
	 *   True if accepted, false if rejected.
	 */
	public boolean doFilter(Event event);
}
