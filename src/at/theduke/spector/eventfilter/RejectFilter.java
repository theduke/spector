package at.theduke.spector.eventfilter;

import at.theduke.spector.Event;

/**
 * This filter rejects all events.
 * 
 * @author theduke
 *
 */
public class RejectFilter implements Filter {

	@Override
	public boolean doFilter(Event event) {
		return false;
	}

}
