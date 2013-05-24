package at.theduke.spector.client.Pusher;

import at.theduke.spector.Event;

/**
 * @author theduke
 *
 */
public class StdOutPusher extends BasePusher implements Pusher {
	
	public void onSessionStart(String id) {
		
	}
	
	@Override
	public void pushEvent(Event event) {
		System.out.println(event.serialize());
	}

	@Override
	public void onSessionStop() {
	}
}
