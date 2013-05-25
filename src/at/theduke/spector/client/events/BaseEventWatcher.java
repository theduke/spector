package at.theduke.spector.client.events;

import org.slf4j.Logger;

import at.theduke.spector.client.Application;
import at.theduke.spector.client.Session;

public abstract class BaseEventWatcher implements EventWatcher {
	static final Logger logger = Application.getLogger();
	protected Session session;
}
