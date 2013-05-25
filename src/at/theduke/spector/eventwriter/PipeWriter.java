package at.theduke.spector.eventwriter;

import java.util.ArrayList;

import at.theduke.spector.Event;

/**
 * This class pipes events to multiple writers.
 * 
 * @author theduke
 *
 */
public class PipeWriter implements Writer {
	
	private ArrayList<Writer> writers = new ArrayList<Writer>();
	
	public PipeWriter() {
	}
	
	/**
	 * Convenience constructor.
	 * 
	 * @param writers
	 */
	public PipeWriter(Writer[] writers) {
		for (Writer writer : writers) {
			this.writers.add(writer);
		}
	}

	

	@Override
	public void onSessionStart(String id) {
		for (Writer writer : writers) {
			writer.onSessionStart(id);
		}
	}

	@Override
	public void onSessionStop() {
		for (Writer writer : writers) {
			writer.onSessionStop();
		}
	}

	@Override
	public boolean pushEvent(Event event) {
		int acceptCount = 0;
		
		for (Writer writer : writers) {
			if (writer.pushEvent(event)) {
				++acceptCount;
			}
		}
		
		return acceptCount > 0;
	}

	@Override
	public void flushEvents() {
		for (Writer writer : writers) {
			writer.flushEvents();
		}
	}
	
	public void addWriter(Writer writer) {
		writers.add(writer);
	}
}
