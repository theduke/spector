/**
 * 
 */
package at.theduke.spector.client;

import java.net.UnknownHostException;

import at.theduke.spector.Session;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

/**
 * @author thedukelong time = e.getWhen();
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
		session = eventRecorder.startSession(config);
		
		pusher = new Pusher(config);
		
		// engage shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                persistSession();
            }
        });
	}
	
	private void run() {
		swtApp = new Swt();
		swtApp.run(this);
	}
	
	public void doNotify() {
		if (!(notificationsEnabled && (config != null) && config.isValid())) return;
		
		// add code for fetching and displaying notifications
		
		Configuration.writeConfiguration(config);
	}
	
	public void persistSession() {
		System.out.println("Persisting session");
		
		if (config.persistLocally) {
			session.saveToFile(Configuration.getSessionDataPath(session.id));
		}
		
		pusher.persistSession(session);
		
		session.clearAggregateData();
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
