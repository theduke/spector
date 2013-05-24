package at.theduke.spector.eventfilter;

import java.util.ArrayList;

import at.theduke.spector.Event;

/**
 * This filter allows chaining various other filters.
 * 
 * @author theduke
 *
 */
public class ChainFilter implements Filter {
	
	private ArrayList<Filter> filters = new ArrayList<Filter>();

	@Override
	public boolean doFilter(Event event) {
		for (Filter filter : filters) {
			if (filter.doFilter(event) == false) {
				return false;
			}
		}
		
		return true;
	}
	
	public void addFilter(Filter filter) {
		filters.add(filter);
	}
}
