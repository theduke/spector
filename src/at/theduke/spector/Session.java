package at.theduke.spector;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.jnativehook.mouse.NativeMouseEvent;

import at.theduke.spector.client.ConfigData;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class Session 
{
  public String id;
  
  public String hostname;
  public String username;
	
  public long startTime;
  public long idleTime = 0;
  public long endTime;
  
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
  public String log = "";
  
  public ArrayList<Integer> keyCodes = new ArrayList<Integer>();
  public ArrayList<NativeMouseEvent> mouseClicks = new ArrayList<NativeMouseEvent>();
  public ArrayList<NativeMouseEvent> mouseMoves = new ArrayList<NativeMouseEvent>();
  
  public Dimension screenResolution;
  
  private BasicDBObject dbObject;
  
  public void start(ConfigData config) {
	  hostname = config.hostname;
	  username = config.username;
	  
	  startTime = endTime = System.currentTimeMillis();
	  screenResolution = Toolkit.getDefaultToolkit().getScreenSize();
	  
	  // calculate unique session id
	  id = hostname + "-" + username + "-" + startTime;
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
  
  public boolean saveToFile(String path) {
	  File file = new File(path);
	  
	  try {
		  BufferedWriter writer = new BufferedWriter(new FileWriter(file, file.exists()));

		  writer.write(log);

		  writer.close();
	  } catch (IOException e) {
		  return false;
	  }
	 
	  System.out.println("Saved session data to file");
	  
	  return true;
  }
}
