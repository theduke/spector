package at.theduke.spector.client;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.slf4j.Logger;

import at.theduke.spector.Event;
import at.theduke.spector.eventdata.MouseClickData;
import at.theduke.spector.eventdata.MouseMoveData;
import at.theduke.spector.eventwriter.Writer;
import at.theduke.spector.session.ClientSessionHandler;

import com.google.gson.Gson;

public class Session 
{
  static final Logger logger = Application.getLogger();
	
  public static final int IDLE_TIME_LIMIT = 120 * 1000;
  
  private String id;
  
  private String hostname;
  private String username;
	
  private long startTime;
  private long idleTime = 0;
  private long endTime;
  
  ClientSessionHandler sessionHandler;
  Writer eventWriter;
  
  private Dimension screenResolution;
  
  private boolean printToConsole = false;
  
  public Session(Writer eventWriter) {
	  this.eventWriter = eventWriter;
	  sessionHandler = new ClientSessionHandler(this);
  }
  
  public void start(Configuration config) {
	  hostname = config.getHostname();
	  username = config.getUsername();
	  
	  startTime = endTime = System.currentTimeMillis();
	  
	  // Set initial screen resolution.
	  // The systemEventWatcher will check if it changes and trigger screen
	  // resolution events.
	  screenResolution = Toolkit.getDefaultToolkit().getScreenSize();
	  
	  // calculate unique session id
	  id = hostname + "-" + username + "-" + startTime;
	  
	  eventWriter.onSessionStart(id);
	  
	  logEvent(Event.EVENT_SESSION_START, id);
  }
  
  public void stop() {
	  logEvent(Event.EVENT_SESSION_END, "");
	  
	  eventWriter.onSessionStop();
  }
  
  protected void recordEvent(Date date) {
	  long timeSpan = date.getTime() - endTime;
	  if (timeSpan >= IDLE_TIME_LIMIT) {
		  idleTime += timeSpan;
	  }

	  endTime = date.getTime();
  }
  
  public void logEvent(String type, String data) {
	  logEvent(type, data, new Date());
  }
  
  /**
   * 
   * @param type
   * @param data
   * @param time Time as ISO8601 string.
   */
  public void logEvent(String type, String data, Date time) {
	  Event event = new Event(type, data, time, this);
	  logEvent(event);
  }
  
  public synchronized void logEvent(Event event) {
	  recordEvent(event.getTime());
	  
	  if (event.getSource() == null) {
		  event.setSource(Application.SPECTOR_CLIENT_SOURCE);
	  }
	  
	  // If debugging is enabled, print to console.
	  // This should normally be achieved with the stdout writer.
	  if (printToConsole) logger.debug("Event: " + event.serialize());
	  
	  // First, the sessionHandler gets the chance to handle the event.
	  // Only if the sessionHandler does not deal with it,
	  // it is immediately forwarded to the regular writer.
	  // The sessionhandler will send session_update events itself
	  // in appropriate intervals.
	  
	  if (!sessionHandler.pushEvent(event)) {
		  eventWriter.pushEvent(event);
	  }
  }
  
  /**
   * Filesystem events.
   */
  
  /**
   *
   * @param type
   * @param path
   * @throws Exception
   */
  public void recordFileEvent(String type, String path) throws Exception {
	  if (!(type == Event.EVENT_FILE_CREATE || 
			  type == Event.EVENT_FILE_DELETE ||
			  type == Event.EVENT_FILE_MODIFY)) {
		  throw new Exception("Invalid event type.");
	  }
	  logEvent(type, path);
  }
  
  /**
   * 
   * @param type
   * @param path
   * @throws Exception
   */
  public void recordDirEvent(String type, String path) throws Exception {
	  if (!(type == Event.EVENT_DIR_CREATE || 
			  type == Event.EVENT_DIR_DELETE)) {
		  throw new Exception("Invalid event type.");
	  }
	  logEvent(type, path);
  }
  
  /**
   * Keyboard and mouse events.
   */
  
  public void recordKeyPress(String typed, Date time) {
	  logEvent(Event.EVENT_KEYPRESS, typed, time);
  }
  
  public void recordKeyDown(int keyCode, Date time) {
	  logEvent(Event.EVENT_KEYDOWN, Integer.toString(keyCode), time);
  }
  
  public void recordKeyUp(int keyCode, String description, Date time) {
	  logEvent(Event.EVENT_KEYUP, keyCode + "," + description, time);
  }
  
  public void recordMouseMove(int posX, int posY, Date time) {
	  String data = new Gson().toJson(new MouseMoveData(posX, posY));
	  logEvent(Event.EVENT_MOUSEMOVE, data, time);
  }
  
  public void recordMouseClick(int button, int posX, int posY, Date time) {
	  String data = new Gson().toJson(new MouseClickData(posX, posY, button));
	  logEvent(Event.EVENT_MOUSECLICK, data, time);
  }
  
  public void recordMouseDown(int button, int posX, int posY, Date time) {
	  String data = new Gson().toJson(new MouseClickData(posX, posY, button));
	  logEvent(Event.EVENT_MOUSEDOWN, data, time);
  }
  
  public void recordMouseUp(int button, int posX, int posY, Date time) {
	  String data = new Gson().toJson(new MouseClickData(posX, posY, button));
	  logEvent(Event.EVENT_MOUSEUP, data, time);
  }
  
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getHostname() {
		return hostname;
	}
	
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public long getStartTime() {
		return startTime;
	}
	
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	
	public long getIdleTime() {
		return idleTime;
	}
	
	public void setIdleTime(long idleTime) {
		this.idleTime = idleTime;
	}
	
	public long getEndTime() {
		return endTime;
	}
	
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public boolean isPrintToConsole() {
		return printToConsole;
	}
	
	public void setPrintToConsole(boolean printToConsole) {
		this.printToConsole = printToConsole;
	}

	public Dimension getScreenResolution() {
		return screenResolution;
	}

	public void setScreenResolution(Dimension screenResolution) {
		this.screenResolution = screenResolution;
	}
	
}
