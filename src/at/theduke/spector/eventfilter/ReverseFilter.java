package at.theduke.spector.eventfilter;

import at.theduke.spector.Event;

/**
 * Revert the result from another filter.
 * 
 * @author theduke
 *
 */
public class ReverseFilter implements Filter {
	
	private Filter originalFilter;
	
	public ReverseFilter(Filter originalFilter) {
		this.originalFilter = originalFilter;
	}

	@Override
	public boolean doFilter(Event event) {
		return !originalFilter.doFilter(event);
	}
}
