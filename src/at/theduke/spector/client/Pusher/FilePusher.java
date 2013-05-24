package at.theduke.spector.client.Pusher;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author theduke
 *
 */
public class FilePusher extends BasePusher implements Pusher {
	String filePath;
	File file;
	
	BufferedWriter writer;

	public FilePusher(String filePath) {
		this.filePath = filePath;
	}
	
	public void onSessionStart(String id) {
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
	
	protected void doPush() {
		if (writer != null) {
			logger.debug("FilePusher flushing " + Integer.toString(eventQueue.size()) + " events");
			String data = eventsToString(eventQueue);
			
			try {
				writer.write(data);
				writer.flush();
			} catch (IOException e) {
				logger.error("Could not write to file: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onSessionStop() {
		super.onSessionStop();
		
		try {
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
