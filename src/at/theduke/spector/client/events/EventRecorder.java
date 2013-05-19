package at.theduke.spector.client.events;

import at.theduke.spector.Session;
import at.theduke.spector.client.Application;
import at.theduke.spector.client.Configuration;

public class EventRecorder
{
	Configuration config;
	Session session;
	
	public EventRecorder(Session session, Configuration config)
	{
		this.config = config;
		this.session = session;
		
		JNativeHookWatcher jNativeWatcher = new JNativeHookWatcher();
		jNativeWatcher.connect(session);
		
		// Establish filesystem watcher.
		FilesystemWatcher fsWatcher = new FilesystemWatcher();
		fsWatcher.setSession(session);
		fsWatcher.setPaths(config.getMonitoredPaths());
		fsWatcher.start();
	}
}
