package at.theduke.spector.client.events;

import java.util.ArrayList;

import at.theduke.spector.Session;
import at.theduke.spector.client.Application;
import at.theduke.spector.client.Configuration;

public class EventRecorder
{
	Configuration config;
	Session session;
	
	ArrayList<EventWatcher> watchers = new ArrayList<EventWatcher>();
	
	public EventRecorder(Session session, Configuration config) {
		this.config = config;
		this.session = session;
	}
	
	public void connect() {
		JNativeHookWatcher jNativeWatcher = new JNativeHookWatcher();
		jNativeWatcher.connect(session);
		watchers.add(jNativeWatcher);
		
		// Establish filesystem watcher.
		FilesystemWatcher fsWatcher = new FilesystemWatcher();
		fsWatcher.setSession(session);
		fsWatcher.setPaths(config.getMonitoredPaths());
		fsWatcher.start();
		watchers.add(fsWatcher);
	}
	
	public void pollEvents() {
		for (EventWatcher watcher : watchers) {
			if (watcher instanceof PolledEventWatcher) {
				((PolledEventWatcher) watcher).pollEvents();
			}
		}
	}
}
