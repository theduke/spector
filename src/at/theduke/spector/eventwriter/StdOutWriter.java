package at.theduke.spector.eventwriter;

import java.io.IOException;

import at.theduke.spector.Event;
import at.theduke.spector.Utils;

/**
 * @author theduke
 *
 */
public class StdOutWriter extends BaseWriter implements Writer {
	
	public void onSessionStart(String id) {
		
	}
	
	@Override
	public void pushEvent(Event event) {
		String output = event.serialize();
		
		if (doGzip) {
			try {
				output = Utils.doGzip(output);
			} catch (IOException e) {
				logger.error("COuld not gzip event", e);
			}
		}
		
		System.out.println(output);
	}

	@Override
	public void onSessionStop() {
	}
}