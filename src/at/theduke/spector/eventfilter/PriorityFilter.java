package at.theduke.spector.eventfilter;

import at.theduke.spector.Event;

/**
 * This filter only accepts events that are higher than a certain priority.
 * 
 * @author theduke
 *
 */
public class PriorityFilter implements Filter {
	
	/**
	 * Event priority is between 0 and 1000.
	 */
	private int priority;
	
	public PriorityFilter(int priority) {
		this.priority = priority;
	}

	@Override
	public boolean doFilter(Event event) {
		return event.getPriority() >= priority;
	}
}
