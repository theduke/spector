package at.theduke.spector.client;

import org.bson.types.ObjectId;

import at.theduke.spector.Session;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;

public class Pusher 
{
	ConfigData config;
	
	Mongo mongo;
	DB database;
	DBCollection collection;

	public Pusher(ConfigData c) 
	{
		config = c;
		
		if (config.authRequired()) 
		{
			
		}
	}
	
	public void persistSession(Session session)
	{
		
	}
}
