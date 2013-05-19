/**
 * 
 */
package at.theduke.spector.client;

import at.theduke.spector.Session;
import at.theduke.spector.client.Pusher.FilePusher;
import at.theduke.spector.client.Pusher.SocketPusher;
import at.theduke.spector.client.events.EventRecorder;

/**
 * @author thedukelong time = e.getWhen();
 *
 */
public class Application {

	private Configuration config;
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
		config = new Configuration();
		config.load();
		
		session = new Session();
		
		// connect pushers
		if (config.isPushToFile()) {
			FilePusher pusher = new FilePusher(config.getDataPath());
			session.addPusher(pusher);
		}
		
		session.start(config);
		
		eventRecorder = new EventRecorder(session, config);
		
		// engage shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
            	session.stop();
                persistSession();
            }
        });
	}
	
	private void run() {
		
		if (config.isGuiEnabled()) {
			swtApp = new Swt();
			swtApp.setApplication(this);
			swtApp.start();
		}
		
		while (true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void doNotify() {
		if (!(notificationsEnabled && (config != null) && config.isValid())) return;
		
		// add code for fetching and displaying notifications
		config.save();
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
	public Configuration getConfig() {
		return config;
	}

	/**
	 * @param config the config to set
	 */
	public void setConfig(Configuration config) {
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
