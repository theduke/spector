/**
 * 
 */
package at.theduke.spector.client;

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
	}
	
	public void doFetch() {
		
	}
}
