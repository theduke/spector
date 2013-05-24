package at.theduke.spector.eventwriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import at.theduke.spector.Event;

/**
 * @author theduke
 * @TODO properly implement gzipping, see http://stackoverflow.com/questions/5994674/java-save-string-as-gzip-file.
 *
 */
public class FileWriter extends BaseWriter implements Writer {
	
	/**
	 * Unique writer identifier.
	 */
	static final String name = "file";
	
	String filePath;
	File file;
	
	/**
	 * If true, the session id is appended to the filePath.
	 * In this case, the filePath should be a directory.
	 */
	boolean sessionFile = true;
	
	BufferedWriter writer;

	public FileWriter(String filePath, boolean sessionFile, boolean doGzip) {
		this.filePath = filePath;
		this.doGzip = doGzip;
	}
	
	@Override
	protected void connect() {
		String actualFilepath = filePath;
		
		if (sessionFile) {
			actualFilepath += "/" + sessionId;
		}
		
		file = new File(actualFilepath);
		
		try {
			  writer = new BufferedWriter(new java.io.FileWriter(file, file.exists()));
		  } catch (IOException e) {
			writer = null;
			connected = false;
			
			logger.error("Could not open file writer: " + e.getMessage());
			e.printStackTrace();
			
			return;
		  }
		
		connected = true;
		logger.debug("Opened file " + actualFilepath + " for writing event data to.");
	}
	
	@Override
	protected void close() {
		try {
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		connected = false;
	}

	protected ArrayList<Event> executeFlush(ArrayList<Event> events) {
		logger.debug("FilePusher flushing " + Integer.toString(events.size()) + " events");
		String data = eventsToString(events, doGzip);

		try {
			writer.write(data);
			writer.flush();
		} catch (IOException e) {
			return events;
		}
		
		return null;
	}
}
