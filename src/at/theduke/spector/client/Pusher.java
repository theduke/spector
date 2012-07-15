package at.theduke.ispy.client;

import org.bson.types.ObjectId;

import at.theduke.ispy.Session;

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

	public Pusher(ConfigData c) throws Exception 
	{
		System.out.println("Trying to establish mongo connection.");
		
		config = c;
		
		mongo = new Mongo(config.mongoHost, config.mongoPort);
		
		database = mongo.getDB(config.mongoDatabase);
		
		if (config.authRequired()) 
		{
			boolean flag = database.authenticate(config.mongoUser, config.mongoPassword.toCharArray());
			
			if (!flag) 
			{
				throw new Exception("Invalid username/password! Check settings");
			}
		}
		
		database.getCollectionNames();
		
		System.out.println("Database connection established successfully.");
	}
	
	public void persistSession(Session session)
	{
		
	}
}
