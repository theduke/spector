/**
 * 
 */
package at.theduke.ispy.client;

import java.net.UnknownHostException;

import at.theduke.ispy.Session;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

/**
 * @author theduke
 *
 */
public class Application {

	private ConfigData config;
	private boolean notificationsEnabled = false;
	
	private Session session;
	
	private Fetcher fetcher;
	private Pusher pusher;
	
	private EventRecorder eventRecorder;
	
	
	private Swt swtApp;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		Application app = new Application();
		app.run();
	}
	
	public Application() 
	{
		config = Configuration.readConfiguration();
		
		eventRecorder = new EventRecorder();
		session = eventRecorder.startSession();
	}
	
	private void run() {
		swtApp = new Swt();
		swtApp.run(this);
	}
	
	public void initializeMongo(ConfigData config) {
		System.out.println("Initializing Mongo connection");
		
		if (config != null && config.isValid()) {
			System.out.println("Config validated successfully");
			
			try {
				fetcher = new Fetcher(config);
				notificationsEnabled = true;
				
				swtApp.showToolTip("Connected to database.");
			} catch (UnknownHostException e) {
				swtApp.showAlert("iSpy", "The specified Mongo Host could not be found. Check settings.");
			} catch (MongoException e) {
				swtApp.showAlert("iSpy", "Could not connect to the MongoDB server. Check settings and connection.");
			} catch (Exception e) {
				swtApp.showAlert("iSpy", e.getMessage());
			}
		} else {
			notificationsEnabled = false;
			
			System.out.println("Config is invalid.");
		}
	}
	
	public void doNotify() {
		if (!(notificationsEnabled && (config != null) && config.isValid())) return;
		
		DBCursor entries = fetcher.doFetch();
		
		boolean idSet = false;
		
		for (DBObject entry : entries) {
			if (!idSet) {
				config.lastNotificationId = entry.get("_id").toString();
				idSet = true;
			}
		}
		
		Configuration.writeConfiguration(config);
	}

	/**
	 * @return boolean the notificationsEnabled
	 */
	public boolean getNotificationsEnabled() {
		return notificationsEnabled;
	}

	/**
	 * @param boolean notificationsEnabled the notificationsEnabled to set
	 */
	public void setNotificationsEnabled(boolean notificationsEnabled) {
		this.notificationsEnabled = notificationsEnabled;
	}

	/**
	 * @return the config
	 */
	public ConfigData getConfig() {
		return config;
	}

	/**
	 * @param config the config to set
	 */
	public void setConfig(ConfigData config) {
		this.config = config;
	}

	/**
	 * @return the fetcher
	 */
	public Fetcher getFetcher() {
		return fetcher;
	}

	/**
	 * @param fetcher the fetcher to set
	 */
	public void setFetcher(Fetcher fetcher) {
		this.fetcher = fetcher;
	}
}
