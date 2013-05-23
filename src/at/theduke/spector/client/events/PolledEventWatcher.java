package at.theduke.spector.client.events;

/**
 * An event watcher that needs to be manually polled for new events.
 * Used for non time-critical events that can wait a few milliseconds 
 * to be updated. Otherwise, use a threaded event watcher with it's own 
 * thread to allow for constant polling.
 * 
 * @author theduke
 *
 */
public interface PolledEventWatcher {
	public void pollEvents();
}
