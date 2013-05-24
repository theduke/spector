package at.theduke.spector.server.receiver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import at.theduke.spector.Event;

class SocketHandler implements Runnable {
	private final Socket client;
	private final ServerSocket serverSocket;

	SocketHandler(ServerSocket serverSocket, Socket client) {
		this.client = client;
		this.serverSocket = serverSocket;
	}
	
	void recordEvent(Event e) {
		if (e.getType() == Event.EVENT_SESSION_START) {
			startSession(e);
		}
	}
	
	void startSession(Event e) {
		
	}

	public void run() {
		StringBuffer sb = new StringBuffer();
		PrintWriter out = null;
		
		try {
			System.out.println("Accepted new client - "
					+ Thread.currentThread());

			out = new PrintWriter(client.getOutputStream(), true);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(client.getInputStream()));

			char[] buffer = new char[100];
			String eventBuffer = "";
			
			String[] eventBufferParts;
			
			while (true) {
				// blocks until we get data
				int anzahlZeichen = bufferedReader.read(buffer, 0, 100);
				
				// aggregate text data we get over the socket
				String part = new String(buffer, 0, anzahlZeichen);
				eventBuffer += part;
				
				eventBufferParts = eventBuffer.split("\n");
				
				// check for complete messages, seperated by newline
				if (eventBufferParts.length > 1) {
					boolean lastPartComplete = eventBuffer.endsWith("\n");
					
					int max = eventBufferParts.length - (lastPartComplete ? 0 : 1);
					
					for (int i = 0; i < max; i++) {
						// handle a complete message
						handleEvent(eventBufferParts[i]);
					}
					
					eventBuffer = lastPartComplete ? "" : eventBufferParts[eventBufferParts.length-1];
				}
			}
			
		} catch (IOException e) {
			System.out.println("IOException, Handler-run");
		} finally {
			if (!client.isClosed()) {
				try {
					client.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	void handleEvent(String entry) {
		Event e = Event.parseEvent(entry);
		
		if (e == null) {
			System.out.println("Could not parse event: " + entry);
			return;
		}
		
		recordEvent(e);
	}
}
