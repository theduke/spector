/**
 * 
 */
package at.theduke.ispy.client;

import java.net.UnknownHostException;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * @author theduke
 *
 */
public class Fetcher {
	ConfigData config;
	
	Mongo mongo;
	DB database;
	DBCollection collection;

	public Fetcher(ConfigData c) throws Exception {
		System.out.println("Trying to establish mongo connection");
		
		config = c;
		
		mongo = new Mongo(config.mongoHost, config.mongoPort);
		
		database = mongo.getDB(config.mongoDatabase);
		
		if (config.authRequired()) {
			boolean flag = database.authenticate(config.mongoUser, config.mongoPassword.toCharArray());
			
			if (!flag) {
				throw new Exception("Invalid username/password! Check settings");
			}
		}
		
		database.getCollectionNames();
		
		collection = database.getCollection("log_entries");
		
		
		System.out.println("Database connection established successfully");
	}
	
	public DBCursor doFetch() {
		BasicDBObject query = buildQuery();
		 
		BasicDBObject sort = new BasicDBObject();
		sort.put("_id", -1);
		sort.put("severity", 1);
		
		DBCursor entries = collection.find(query)
			.sort(sort)
			.limit(100);
		
		System.out.println("Fetched " + entries.count() + " log entries");
		
		return entries;
	}
	
	protected BasicDBObject buildQuery() {
		BasicDBObject query = new BasicDBObject();
		
		if (config.lastNotificationId != null) {
			query.put("_id", new BasicDBObject("$gt", new ObjectId(config.lastNotificationId)));
		}
		
		return query;
	}
}
