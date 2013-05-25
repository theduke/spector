package at.theduke.spector;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import at.theduke.spector.client.Session;

import com.google.gson.Gson;

public class Event {
	
	public static final String EVENT_SESSION_START = "session_start";
	public static final String EVENT_SESSION_UPDATE = "session_update";
	public static final String EVENT_SESSION_END = "session_end";
	
	public static final String EVENT_SCREEN_RESOLUTION_SET = "screen_resolution_set";
	public static final String EVENT_KEYBOARD_LAYOUT_SET = "keyboard_layout_set";

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
	 * Web events
	 */
	
	public static final String EVENT_WEBISTE_OPEN = "website_open";
	

	/**
	 * Event type.
	 */
	private String type;

	private String user;

	private String host;
	
	/**
	 * Source application.
	 */
	private String source;

	/**
	 * Session id.
	 */
	private String session;

	/**
	 * Priority between 0 and 1000.
	 */
	private int priority = 0;

	/**
	 * Event-specifig data. Either string or json.
	 */
	String data;

	/**
	 * Time the event happened.
	 */
	String time;
	
	ArrayList<String> tags = new ArrayList<String>();

	public static Event parseEvent(String entry) {
		return fromJson(entry);
	}
	
	public static Event fromJson(String entry) {
		Gson gson = new Gson();
		Event event = gson.fromJson(entry, Event.class);
		
		return event;
	}

	public Event() {

	}
	
	public Event(String type, HashMap<String, String> data, Session session) {
		this(type, new Gson().toJson(data), new Date(), session);
	}
	
	public Event(String type, HashMap<String, String> data, Date time, Session session) {
		this(type, new Gson().toJson(data), time, session);
	}

	public Event(String type, String data, Session session) {
		this(type, data, new Date(), session);
	}
	
	public Event(String type, String data, Date time, Session session) {
		this.type = type;
		this.data = data;
		this.user = session.getUsername();
		this.host = session.getHostname();
		this.session = session.getId();
		this.time = Utils.getIso8601Time(time);
	}

	/**
	 * Serialize this event to JSON.
	 *
	 * @return Event as json string.
	 */
	public String serialize() {
		ensureValues();
		return toJson();
	}
	
	public String toJson() {
		Gson gson = new Gson();
		String json = gson.toJson(this);
		return json;
	}
	
	/**
	 * Ensure that all properties have at least a default value.
	 */
	protected void ensureValues() {
		
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Date getTime() {
		return javax.xml.bind.DatatypeConverter.parseDateTime(time).getTime();
	}
	
	public String getIsoTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = Utils.getIso8601Time(time);
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public ArrayList<String> getTags() {
		return tags;
	}

	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}
	
	public void addTag(String tag) {
		tags.add(tag);
	}
}
