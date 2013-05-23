package at.theduke.spector;

import java.util.Date;

import com.google.gson.Gson;

public class Event {

	public static final String EVENT_SESSION_START = "session_start";
	public static final String EVENT_SESSION_END = "session_end";
	public static final String EVENT_SCREEN_RESOLUTION_SET = "screen_resolution";
	public static final String EVENT_KEYBOARD_LAYOUT_SET = "screen_resolution";

	/**
	 * Keyboard and mouse events.
	 */
	
	public static final String EVENT_KEYPRESS = "key_press";
	public static final String EVENT_KEYDOWN = "key_down";
	public static final String EVENT_KEYUP = "key_up";

	public static final String EVENT_MOUSECLICK = "mouse_click";
	public static final String EVENT_MOUSEDOWN = "mouse_down";
	public static final String EVENT_MOUSEUP = "mouse_up";
	public static final String EVENT_MOUSEMOVE = "mouse_move";

	/**
	 * Filesystem events.
	 */

	public static final String EVENT_FILE_CREATE = "file_create";
	public static final String EVENT_FILE_MODIFY = "file_modify";
	public static final String EVENT_FILE_DELETE = "file_delete";
	public static final String EVENT_DIR_CREATE = "dir_create";
	public static final String EVENT_DIR_DELETE = "dir_delete";

	/**
	 * Event type.
	 */
	public String type;

	public String user;

	public String host;

	/**
	 * Session id.
	 */
	public String session;

	/**
	 * Priority between 0 and 1000.
	 */
	public int priority = 0;

	/**
	 * Event-specifig data. Either string or json.
	 */
	String data;

	/**
	 * Time the event happened.
	 */
	Date time;

	public static Event parseEvent(String entry) {
		Gson gson = new Gson();
		Event event = gson.fromJson(entry, Event.class);
		
		return event;
	}

	Event() {

	}

	Event(String type, String data, Session session) {
		this(type, data, new Date(), session);
	}
	
	Event(String type, String data, Date time, Session session) {
		this.type = type;
		this.data = data;
		this.user = session.getUsername();
		this.host = session.getHostname();
		this.session = session.getId();
		this.time = time;
	}

	/**
	 * Serialize this event to JSON.
	 *
	 * @return Event as json string.
	 */
	public String serialize() {
		Gson gson = new Gson();
		String json = gson.toJson(this);
		return json;
	}
}
