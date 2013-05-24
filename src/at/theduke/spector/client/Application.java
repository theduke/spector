/**
 * 
 */
package at.theduke.spector.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.theduke.spector.Session;
import at.theduke.spector.client.Pusher.FilePusher;
import at.theduke.spector.client.Pusher.StdOutPusher;
import at.theduke.spector.client.events.EventRecorder;

/**
 * @author thedukelong time = e.getWhen();
 *
 */
public class Application {
	
	static final Logger logger = getLogger();

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
	
	public static Logger getLogger() {
		Logger logger = LoggerFactory.getLogger("spector");
		return logger;
	}
	
	public Application() 
	{
		logger.info("Startup");
		logger.debug("Test");
		
		config = new Configuration();
		config.load();
		
		session = new Session();
		
		// connect pushers
		
		session.addPusher(new StdOutPusher());
		
		if (config.isPushToFile()) {
			FilePusher pusher = new FilePusher(config.getDataPath(), true, false);
			session.addPusher(pusher);
		}
		
		logger.debug("Starting session.");
		session.start(config);
		
		eventRecorder = new EventRecorder(session, config);
		eventRecorder.connect();
		
		// engage shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
            	logger.debug("Stopping session.");
            	session.stop();
                persistSession();
            }
        });
	}
	
	private void run() {
		logger.debug("Starting event loop");
		
		if (config.isGuiEnabled()) {
			swtApp = new Swt();
			swtApp.setApplication(this);
			swtApp.start();
		}
		
		while (true) {
			try {
				Thread.sleep(500);
				eventRecorder.pollEvents();
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
