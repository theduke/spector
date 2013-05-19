package at.theduke.spector.client.events;

import org.slf4j.Logger;

import at.theduke.spector.Session;
import at.theduke.spector.client.Application;

public abstract class BaseEventWatcher implements EventWatcher {
	static final Logger logger = Application.getLogger();
	protected Session session;
}
