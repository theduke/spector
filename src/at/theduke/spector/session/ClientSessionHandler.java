package at.theduke.spector.session;

import java.util.HashMap;

import com.google.gson.Gson;

import at.theduke.spector.Event;
import at.theduke.spector.Utils;
import at.theduke.spector.eventdata.MouseClickData;
import at.theduke.spector.eventdata.MouseMoveData;
import at.theduke.spector.eventfilter.SessionFilter;
import at.theduke.spector.eventwriter.FileWriter;
import at.theduke.spector.eventwriter.Writer;

public class ClientSessionHandler implements Writer {
	
	private final int CHANGE_LIMIT = 100;
	
	SessionFilter filter = new SessionFilter();
	
	/**
	 * Writer used to send session_update events.
	 */
	at.theduke.spector.client.Session clientSession;
	
	HashMap<String, SessionData> sessions = new HashMap<String, SessionData>();
	
	public ClientSessionHandler(at.theduke.spector.client.Session session) {
		clientSession = session;
	}
	
	@Override
	public void onSessionStart(String id) {
	}
	
	private SessionData createSession(Event event) {
		SessionData session = new SessionData();
		session.setId(event.getSession());
		session.setStart(event.getIsoTime());
		session.setUser(event.getUser());
		session.setHost(event.getHost());
		
		sessions.put(event.getSession(), session);
		
		return session;
	}

	@Override
	public void onSessionStop() {
		// TODO Auto-generated method stub

	}
	
	private void updateSession(SessionData session) {
		Session baseSession = session;
		clientSession.logEvent(Event.EVENT_SESSION_UPDATE, baseSession.toJson());
		
		session.clearAggregateData();
		session.changeCounter = 0;
	}

	@Override
	public boolean pushEvent(Event event) {
		if (!filter.doFilter(event)) {
			return false;
		}
		
		SessionData session = null;
		
		if (event.getType() == Event.EVENT_SESSION_START) {
			session = createSession(event);
		}
		else if (!sessions.containsKey(event.getSession())) {
			session = createSession(event);
		}
		
		if (event.getType() == Event.EVENT_SESSION_END) {
			session = sessions.get(event.getSession());
			session.setEnd(event.getIsoTime());
			
			updateSession(session);
			sessions.remove(event.getSession());
		}
		else {
			// Handle non session end events.
			session = sessions.get(event.getSession());
			handleEvent(event, session);
			
			if (session.changeCounter >= CHANGE_LIMIT) {
				updateSession(session);
			}
		}
		
		return true;
	}
	
	private void handleEvent(Event event, SessionData session) {
		switch (event.getType()) {
		
			case Event.EVENT_SCREEN_RESOLUTION_SET:
				break;
			
			case Event.EVENT_KEYUP:
				String data = event.getData();
				String[] parts = data.split(",");
				
				session.addKeypress(Integer.parseInt(parts[0]));
				
				// Add certain important keys to the log.
				String description = parts[1];
				
				System.out.println(description);					
				if (description.equals("Enter") 
						|| description.equals("Tab")
						|| description.equals("Delete")
						|| description.equals("Backspace")) {
					session.appendTextLog("<" + description + ">");
				}

				++session.changeCounter;
				break;
				
			case Event.EVENT_KEYPRESS:
				session.appendTextLog(event.getData());
				++session.changeCounter;
				break;
				
			case Event.EVENT_MOUSEUP:
				MouseClickData clickData = new Gson().fromJson(event.getData(), MouseClickData.class);
				session.addMousepress(clickData.button);
				
				++session.changeCounter;
				break;
				
			case Event.EVENT_MOUSEMOVE:
				MouseMoveData data1 = new Gson().fromJson(event.getData(), MouseMoveData.class);
				
				if (session.lastMousePosX > 0) {
					int deltaX = Math.abs(session.lastMousePosX - data1.x);
					int deltaY = Math.abs(session.lastMousePosY - data1.y);
					
					session.incrementMouseDistance(deltaX + deltaY);
				}
				
				session.lastMousePosX = data1.x;
				session.lastMousePosY = data1.y;
				
				++session.changeCounter;
				break;
				
			case Event.EVENT_FILE_CREATE:
				session.incrementFilesCreated(1);
				++session.changeCounter;
				break;
			
			case Event.EVENT_FILE_MODIFY:
				session.incrementFilesModified(1);
				++session.changeCounter;
				break;
				
			case Event.EVENT_FILE_DELETE:
				session.incrementFilesDeleted(1);
				++session.changeCounter;
				break;
		}
	}

	@Override
	public void flushEvents() {
	}

}
