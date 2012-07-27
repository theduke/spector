package at.theduke.spector.client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.bson.types.ObjectId;

import at.theduke.spector.Session;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;

public class SocketPusher implements Pusher
{
	ConfigData config;
	
	Socket socket;
	BufferedWriter socketWriter;

	public SocketPusher(ConfigData c) 
	{
		config = c;
		
		connect();
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
		
		return true;
	}
	
	public void pushEvent(String entry) {
		try {
			socketWriter.write(entry);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
