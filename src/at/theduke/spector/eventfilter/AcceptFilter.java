package at.theduke.spector.eventfilter;

import at.theduke.spector.Event;

/**
 * This filter accepts all events.
 * 
 * @author theduke
 *
 */
public class AcceptFilter implements Filter {

	@Override
	public boolean doFilter(Event event) {
		return true;
	}

}
