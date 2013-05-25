package at.theduke.spector.server.http;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;

import at.theduke.spector.server.Application;
import at.theduke.spector.server.http.handler.EventSubmitHandler;

public class JettyServer {
	
	String host;
	int port;
	
	Application app;
	
	
	public JettyServer(int port, Application app) {
		this.port = port;
		this.app = app;
	}
	
	public void run() {
		
		// Set log level to WARN for jetty.
		
		final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger("org.eclipse.jetty");
        if (!(logger instanceof ch.qos.logback.classic.Logger)) {
            return;
        }
        ch.qos.logback.classic.Logger logbackLogger = (ch.qos.logback.classic.Logger) logger;
        logbackLogger.setLevel(ch.qos.logback.classic.Level.WARN);
        
		
		Server server = new Server(port);
	    
		ContextHandler context = new ContextHandler();
        context.setContextPath("/events/submit");
        context.setResourceBase(".");
        context.setClassLoader(Thread.currentThread().getContextClassLoader());
        server.setHandler(context);
        
        EventSubmitHandler submitHandler = new EventSubmitHandler();
        submitHandler.setApplication(app);
        
        context.setHandler(submitHandler);
	 
	    try {
			server.start();
			server.join();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
