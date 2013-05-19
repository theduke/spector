package at.theduke.spector;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.slf4j.Logger;

import org.jnativehook.mouse.NativeMouseEvent;

import at.theduke.spector.client.Application;
import at.theduke.spector.client.Configuration;
import at.theduke.spector.client.Pusher.Pusher;

import com.mongodb.BasicDBObject;

public class Session 
{
	static final Logger logger = Application.getLogger();
	
  public static final int IDLE_TIME_LIMIT = 120 * 1000;
  public static final double MOUSE_MOVE_RECOGNITION_THRESHHOLD_PERCENT = 0.05;
  
  public String id;
  
  public String hostname;
  public String username;
	
  public long startTime;
  public long idleTime = 0;
  public long endTime;
  
  ArrayList<Pusher> pushers = new ArrayList<Pusher>();
  
  private final ReentrantReadWriteLock eventReadWriteLock = new ReentrantReadWriteLock();
  private final Lock eventWriteLock = eventReadWriteLock.writeLock();
  
  /*
   * Text log of all events that occur during a session.
   * Format:
   * EVENT-TYPE:DATA|TIME-SINCE-SESSION-START-IN-MS
   * 
   *  Examples:
   *  
   *  KeyPress, data = keycode    -> kp:16|500
   *  MouseClick, data = button,posX,posY   -> mc:1|1545
   *  MouseMove, data = posX,posY -> mm:10,30|2203
   */
  protected String log = "";
  
  public ArrayList<Integer> keyCodes = new ArrayList<Integer>();
  public ArrayList<NativeMouseEvent> mouseClicks = new ArrayList<NativeMouseEvent>();
  public ArrayList<NativeMouseEvent> mouseMoves = new ArrayList<NativeMouseEvent>();
  
  public Dimension screenResolution;
  
  private BasicDBObject dbObject;
  
  private boolean printToConsole = true;
  
  public void start(Configuration config) {
	  hostname = config.getHostname();
	  username = config.getUsername();
	  
	  startTime = endTime = System.currentTimeMillis();
	  screenResolution = Toolkit.getDefaultToolkit().getScreenSize();
	  
	  // calculate unique session id
	  id = hostname + "-" + username + "-" + startTime;
	  
	  for (Pusher pusher : pushers) {
		  pusher.onSessionStart(id);
	  }
	  
	  logEvent(Event.EVENT_SESSION_START, id);
	  
	  String resolution = screenResolution.width + "," + screenResolution.height;
	  logEvent(Event.EVENT_SCREEN_RESOLUTION_SET, resolution);
  }
  
  public void stop() {
	  logEvent(Event.EVENT_SESSION_END, "");
	  
	  for (Pusher pusher : pushers) {
		  pusher.onSessionStop();
	  }
  }
  
  protected void recordEvent(long time) {
	  long timeSpan = time - endTime;
	  if (timeSpan >= IDLE_TIME_LIMIT) {
		  idleTime += timeSpan;
	  }

	  endTime = time;
  }
  
  public void logEvent(String type, String data) {
	  logEvent(type, data, System.currentTimeMillis());
  }
  
  public void logEvent(String type, String data, long time) {
	  // normalize time
	  time = time - startTime;
	  
	  Event event = new Event(type, data, time);
	  logEvent(event);
  }
  
  public synchronized void logEvent(Event event) {
	  recordEvent(event.time);
	  
	  String entry = event.serialize();
	  
	  eventWriteLock.lock();        // writeLock anfordern. Blockiert solange, bis es verfügbar ist.
      try {
    	  log += entry;
    	  
    	  if (printToConsole) logger.debug("Event: " + entry);
    	  
    	  for (Pusher pusher : pushers) {
    		  pusher.pushEvent(event);
    	  }
      }
      finally {
    	  eventWriteLock.unlock();
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
  
  public void recordKeyPress(int keyCode, long time) {
	  logEvent(Event.EVENT_KEYPRESS, Integer.toString(keyCode), time);
  }
  
  public void recordMouseMove(int posX, int posY, long time) {
	  String data = posX + "," + posY;
	  logEvent(Event.EVENT_MOUSEMOVE, data, time);
  }
  
  public void recordMouseMove(NativeMouseEvent e) {
	boolean shouldLog = false;
	
	int posX = e.getX();
	int posY = e.getY();
	
	long time = e.getWhen();
	
	// ensure that idle time is reset, even if move is not logged
	recordEvent(time);
  	
  	// determine whether to log the move
  	int size = mouseMoves.size();
  	if (size > 0)
  	{
  		NativeMouseEvent lastEvent = mouseMoves.get(size - 1);
  		
  		int deltaX = Math.abs(posX - lastEvent.getX());
  		if ((float) deltaX / screenResolution.width > MOUSE_MOVE_RECOGNITION_THRESHHOLD_PERCENT)
  		{
  			shouldLog = true;
  		}
  		else
  		{
  			int deltaY = Math.abs(posY - lastEvent.getY());
  			if ((float) deltaY / screenResolution.height > MOUSE_MOVE_RECOGNITION_THRESHHOLD_PERCENT) shouldLog = true;
  		}
  	}
  	else
  	{
  		shouldLog = true;
  	}
  	
  	if (shouldLog) {
  		recordMouseMove(posX, posY, time);
  		mouseMoves.add(e);
  	}
  }
  
  public void recordMouseClick(int button, int posX, int posY, long time) {
	  String data = button + "," + posX + "," + posY;
	  logEvent(Event.EVENT_MOUSECLICK, data, time);
  }
  
  public void clearAggregateData()
  {
	  mouseClicks.clear();
	  mouseMoves.clear();
	  
	  log = "";
  }
  
  public BasicDBObject getDbObject()
  {
	  if (dbObject == null)
	  {
		  dbObject = new BasicDBObject();
		  
		  dbObject.put("startTime", startTime);
	  }
	  
	  dbObject.put("idleTime", idleTime);
	  dbObject.put("endTime", endTime);
	  
	  return dbObject;
  }
  
  public void addPusher(Pusher p) {
	  pushers.add(p);
  }

	public boolean isPrintToConsole() {
		return printToConsole;
	}
	
	public void setPrintToConsole(boolean printToConsole) {
		this.printToConsole = printToConsole;
	}
  
  
}
