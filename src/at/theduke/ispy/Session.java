package at.theduke.ispy;

import java.awt.Dimension;
import java.util.ArrayList;

import org.jnativehook.mouse.NativeMouseEvent;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class Session 
{
  public String id;
	
  public long startTime;
  public long idleTime = 0;
  public long endTime;
  
  public String keyLog = "";
  
  public ArrayList<NativeMouseEvent> mouseClicks = new ArrayList<NativeMouseEvent>();
  public ArrayList<NativeMouseEvent> mouseMoves = new ArrayList<NativeMouseEvent>();
  
  public Dimension screenResolution;
  
  private BasicDBObject dbObject;
  
  public void clearAggregateData()
  {
	  mouseClicks.clear();
	  mouseMoves.clear();
	  
	  keyLog = "";
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
}
