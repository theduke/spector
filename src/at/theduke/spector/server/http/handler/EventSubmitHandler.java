package at.theduke.spector.server.http.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;

import at.theduke.spector.server.Application;
import at.theduke.spector.server.EventReceiver;

public class EventSubmitHandler extends AbstractHandler {
	
	EventReceiver eventReceiver;
	
	protected final Logger logger = Application.getLogger();

    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) 
        throws IOException, ServletException 
    {
    	String gzipped = request.getParameter("gzipped");
    	String events = request.getParameter("events");
    	
    	if (events == null) {
    		logger.info("REquest does not contain event data");
    		
    		response.setContentType("text/html;charset=utf-8");
    		response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
    		response.getWriter().println("No event data supplied");
    		baseRequest.setHandled(true);
    	}
    	else {
    		int counter = eventReceiver.receiveEvents(events, gzipped == "1" ? true : false);
    		
    		response.setContentType("text/html;charset=utf-8");
    		response.setStatus(HttpServletResponse.SC_OK);
    		response.getWriter().println("Events processed: " + Integer.toString(counter));
    		baseRequest.setHandled(true);
    	}
    }

	public EventReceiver getEventReceiver() {
		return eventReceiver;
	}

	public void setEventReceiver(EventReceiver eventReceiver) {
		this.eventReceiver = eventReceiver;
	}
    
    
}
