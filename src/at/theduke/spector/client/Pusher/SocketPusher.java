package at.theduke.spector.client.Pusher;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketPusher extends BasePusher implements Pusher
{
	String host;
	int port;
	
	Socket socket;
	BufferedWriter socketWriter;
	
	public SocketPusher(String host, int port, boolean doGzip)  {
		this.host = host;
		this.port = port;
		this.doGzip = doGzip;
	}
	
	public void onSessionStart(String id) {
		connect();
	}
	
	protected void doPush() {
		String data = eventsToString(eventQueue, doGzip);
		
		try {
			socketWriter.write(data);
			socketWriter.flush();
		} catch (IOException e) {
			logger.error("Could not write to socket", e);
		}
	}
	
	protected boolean connect() {
		try {
		    InetAddress addr = InetAddress.getByName(host);

		    // This constructor will block until the connection succeeds
		    socket = new Socket(addr, port);
		    
		    socketWriter = new BufferedWriter(new OutputStreamWriter(
		            socket.getOutputStream()));
		    
		} catch (UnknownHostException e) {
			logger.error("COuld not reach host", e);
			return false;
		} catch (IOException e) {
			logger.error("Could not open socket", e);
			return false;
		}
		
		logger.debug("Connected to server " + host + " on port " + Integer.toString(port));
		
		return true;
	}
	
	void close() {
		try {
			socketWriter.close();
			socket.close();
		} catch (IOException e) {
			logger.error("Could not close socket", e);
		}
	}

	@Override
	public void onSessionStop() {
		super.onSessionStop();
		close();
	}
}
