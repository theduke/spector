package at.theduke.spector.eventwriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

/**
 * @author theduke
 * @TODO properly implement gzipping, see http://stackoverflow.com/questions/5994674/java-save-string-as-gzip-file.
 *
 */
public class FileWriter extends BaseWriter implements Writer {
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
	
	public void onSessionStart(String id) {
		String actualFilepath = filePath;
		
		if (sessionFile) {
			actualFilepath += "/" + id;
		}
		
		file = new File(actualFilepath);
		
		try {
			  writer = new BufferedWriter(new java.io.FileWriter(file, file.exists()));

		  } catch (IOException e) {
			writer = null;
			
			logger.error("Could not open file writer: " + e.getMessage());
			e.printStackTrace();
		  }
		
		logger.debug("Opened file " + actualFilepath + " for writing event data to.");
	}
	
	protected void doPush() {
		if (writer != null) {
			logger.debug("FilePusher flushing " + Integer.toString(eventQueue.size()) + " events");
			String data = eventsToString(eventQueue, doGzip);
			
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
