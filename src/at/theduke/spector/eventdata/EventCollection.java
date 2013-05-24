package at.theduke.spector.eventdata;

import java.util.ArrayList;

import at.theduke.spector.Event;

public class EventCollection {
	
	public String id;
	public ArrayList<Event> events;
	
	public EventCollection(String id, ArrayList<Event> events) {
		this.id = id;
		this.events = events;
	}
}
