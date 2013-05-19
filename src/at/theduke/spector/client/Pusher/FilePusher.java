package at.theduke.spector.client.Pusher;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import at.theduke.spector.Event;

/**
 * @author theduke
 *
 */
public class FilePusher extends BasePusher implements Pusher {
	String filePath;
	File file;
	
	BufferedWriter writer;
	
	
	/**
	 * The buffer will be flushed every interval events.
	 */
	static final int FLUSH_INTERVAL = 100;
	
	/**
	 * Counter to enforce flushing in regular intervals.  
	 */
	int eventCounter = 0;

	public FilePusher(String filePath) {
		this.filePath = filePath;
		
	}
	
	public void onSessionStart(String id) {
		filePath += "/" + id + ".session";
		
		file = new File(filePath);
		
		try {
			  writer = new BufferedWriter(new FileWriter(file, file.exists()));

		  } catch (IOException e) {
			writer = null;
			
			logger.error("Could not open file writer: " + e.getMessage());
			e.printStackTrace();
		  }
		
		logger.debug("Opened file " + filePath + " for writing event data to.");
	}
	
	@Override
	public void pushEvent(Event event) {
		if (writer != null) {
			try {
				writer.write(event.serialize());
				
				++eventCounter;
				
				if (eventCounter >= FLUSH_INTERVAL) {
					writer.flush();
					eventCounter = 0;
					
					logger.debug("Flushed file pusher buffer.");
				}
			} catch (IOException e) {
				logger.error("Could not write to file: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onSessionStop() {
		try {
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
