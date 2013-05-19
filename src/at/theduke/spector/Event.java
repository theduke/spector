package at.theduke.spector;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Event {

	public static final String EVENT_SESSION_START = "session_start";
	public static final String EVENT_SESSION_END = "session_end";
	public static final String EVENT_SCREEN_RESOLUTION_SET = "screen_resolution";
	
	/**
	 * Keyboard and mouse events.
	 */
	public static final String EVENT_KEYPRESS = "kp";
	public static final String EVENT_MOUSECLICK = "mc";
	public static final String EVENT_MOUSEMOVE = "mm";
	
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
	String type;
	
	/**
	 * Event-specifig data.
	 */
	String data;
	
	/**
	 * Time the event happened.
	 */
	long time;
	
	public static Event parseEvent(String entry) {
		Event e = new Event();
		
		Pattern p = Pattern.compile("(.*)\\:(.*?)\\|([0-9]+)\\n");
		Matcher m = p.matcher(entry);
		
		if (m.find()) {
			e.setType(m.group(1));
			e.setData(m.group(2));
			e.setTime(Long.parseLong(m.group(3)));
		}
		else {
			return null;
		}
		
		return e;
	}
	
	Event() {
		
	}
	
	Event(String type, String data, long time) {
		this.type = type;
		this.data = data;
		this.time = time;
	}
	
	public String serialize() {
		String data = this.data;
		
		// prevent special characters in data
		data = data.replace("|", ",");
		data = data.replace("\n", "");
		
		String entry = type + ":" + data + "|" + time + "\n";
		
		return entry;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
