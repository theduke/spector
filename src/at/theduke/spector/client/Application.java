/**
 * 
 */
package at.theduke.spector.client;

import at.theduke.spector.Session;
import at.theduke.spector.client.Pusher.FilePusher;
import at.theduke.spector.client.Pusher.SocketPusher;

/**
 * @author thedukelong time = e.getWhen();
 *
 */
public class Application {

	private ConfigData config;
	private boolean notificationsEnabled = false;
	
	private Session session;
	
	private Fetcher fetcher;
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
		
		session = new Session();
		
		// connect pushers
		if (config.pushToFile) {
			FilePusher pusher = new FilePusher(Configuration.getDataPath());
			session.addPusher(pusher);
		}
		
		if (config.pushToServer) {
			SocketPusher pusher = new SocketPusher(config);
			session.addPusher(pusher);
		}
		
		session.start(config);
		
		eventRecorder = new EventRecorder();
		eventRecorder.session = session;
		
		// engage shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
            	session.stop();
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
