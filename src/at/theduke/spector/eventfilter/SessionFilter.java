package at.theduke.spector.eventfilter;

import at.theduke.spector.Event;

/**
 * This filter accepts all events that are related to a session.
 * Like session start, keypress, mouse moves, ....
 * 
 * @author theduke
 *
 */
public class SessionFilter implements Filter {

	@Override
	public boolean doFilter(Event event) {
		
		boolean isSession = false;
		
		switch (event.getType()) {
			case Event.EVENT_SESSION_START:
			case Event.EVENT_SESSION_END:	
				 
			case Event.EVENT_SCREEN_RESOLUTION_SET:
			case Event.EVENT_KEYBOARD_LAYOUT_SET:
				
			case Event.EVENT_KEYDOWN:
			case Event.EVENT_KEYUP:
			case Event.EVENT_KEYPRESS:
				
			case Event.EVENT_MOUSEUP:
			case Event.EVENT_MOUSEDOWN:
			case Event.EVENT_MOUSECLICK:
			case Event.EVENT_MOUSEMOVE:
				
			case Event.EVENT_DIR_CREATE:
			case Event.EVENT_DIR_DELETE:
			case Event.EVENT_FILE_CREATE:
			case Event.EVENT_FILE_MODIFY:
			case Event.EVENT_FILE_DELETE:
				isSession = true;
				break;
		}
		
		return isSession;
	}

}
