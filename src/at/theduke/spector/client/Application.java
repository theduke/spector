/**
 * 
 */
package at.theduke.spector.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.db4o.Db4o;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;

import at.theduke.spector.client.events.EventRecorder;
import at.theduke.spector.eventwriter.FileWriter;
import at.theduke.spector.eventwriter.PipeWriter;
import at.theduke.spector.eventwriter.StdOutWriter;

/**
 * @author thedukelong time = e.getWhen();
 *
 */
public class Application {
	public static final String SPECTOR_CLIENT_SOURCE = "spector_client";
	
	static final Logger logger = getLogger();

	private Configuration config;
	private boolean notificationsEnabled = false;
	
	ObjectContainer db;
	
	private Session session;
	PipeWriter eventWriter = new PipeWriter();
	
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
		
		db = Db4oEmbedded.openFile(config.getDb4oPath());
		
		session = new Session(eventWriter);
		
		// connect pushers
		
		eventWriter.addWriter(new StdOutWriter());
		
		if (config.isPushToFile()) {
			FileWriter pusher = new FileWriter(config.getDataPath(), true, false);
			//eventWriter.addWriter(pusher);
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
