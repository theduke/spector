package at.theduke.spector.client.Pusher;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import at.theduke.spector.Event;
import at.theduke.spector.client.ConfigData;

public class SocketPusher extends BasePusher implements Pusher
{
	ConfigData config;
	
	Socket socket;
	BufferedWriter socketWriter;
	
	boolean connected = false;
	
	/**
	 * The buffer will be flushed every interval events.
	 */
	static final int FLUSH_INTERVAL = 100;
	
	/**
	 * Counter to enforce flushing in regular intervals.  
	 */
	int eventCounter = 0;
	
	/*
	 * We need to persist the session start event 
	 * in case we need to reopen the connection and resend the
	 * session-start.
	 */
	Event sessionStart;
	
	public SocketPusher(ConfigData c) 
	{
		config = c;
		
		connect();
	}
	
	public void onSessionStart(String id) {
		
	}
	
	public void pushEvent(Event entry) {
		if (entry.getType() == Event.EVENT_SESSION_END) {
			sessionStart = entry;
		}
		
		if (!connected) {
			// try to re-connect if no active socket con
			boolean success = connect();
			
			// if the connection was re-opened, we need to 
			// re-send the session-start event.
			if (success && sessionStart != null && entry.getType() != Event.EVENT_SESSION_END) {
				pushEvent(sessionStart);
			}
		}
		
		try {
			socketWriter.write(entry.serialize());
			
			++eventCounter;
			
			if (eventCounter >= FLUSH_INTERVAL) {
				socketWriter.flush();
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			// close the socket, try to re-open it later
			close();
		}
	}
	
	protected boolean connect() {
		try {
		    InetAddress addr = InetAddress.getByName(config.serverHost);
		    int port = config.serverPort;

		    // This constructor will block until the connection succeeds
		    socket = new Socket(addr, port);
		    
		    socketWriter = new BufferedWriter(new OutputStreamWriter(
		            socket.getOutputStream()));
		    
		} catch (UnknownHostException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return false;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return false;
		}
		
		System.out.println("Opened connection to server " 
		  + config.serverHost + " on port " + config.serverPort);
		
		connected = true;
		return true;
	}
	
	void close() {
		try {
			socketWriter.close();
			socket.close();
			
			connected = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onSessionStop() {
		close();
	}
}
