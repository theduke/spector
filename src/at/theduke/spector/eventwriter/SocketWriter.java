package at.theduke.spector.eventwriter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import at.theduke.spector.Event;

public class SocketWriter extends BaseWriter implements Writer {
	
	/**
	 * Unique writer identifier.
	 */
	static final String name = "socket";
	
	String host;
	int port;
	
	Socket socket;
	BufferedWriter socketWriter;
	
	public SocketWriter(String host, int port, boolean doGzip)  {
		this.host = host;
		this.port = port;
		this.doGzip = doGzip;
	}
	
	@Override
	protected void connect() {
		try {
		    InetAddress addr = InetAddress.getByName(host);

		    // This constructor will block until the connection succeeds
		    socket = new Socket(addr, port);
		    
		    socketWriter = new BufferedWriter(new OutputStreamWriter(
		            socket.getOutputStream()));
		} catch (UnknownHostException e) {
			logger.error("COuld not reach host", e);
			connected = false;
		} catch (IOException e) {
			logger.error("Could not open socket", e);
			connected = false;
		}
		
		connected = true;
		
		logger.debug("Connected to server " + host + " on port " + Integer.toString(port));
	}
	
	@Override
	protected void close() {
		try {
			socketWriter.close();
			socket.close();
		} catch (IOException e) {
			logger.error("Could not close socket", e);
		}
		
		connected = false;
	}
	
	protected ArrayList<Event> executeFlush(ArrayList<Event> events) {
		String data = eventsToString(events, doGzip);
		
		logger.debug("Socketwriter is flushing " + events.size() + " events");
		
		try {
			socketWriter.write(data);
			socketWriter.flush();
		} catch (IOException e) {
			logger.error("Could not write to socket", e);
			return events;
		}
		
		return null;
	}
}
